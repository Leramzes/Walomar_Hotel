package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.*;
import development.team.hoteltransylvania.Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ventacontroller", urlPatterns = {"/ventacontroller"})
public class ventaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionVenta");

        HttpSession sessionObj = req.getSession(false);
        User usuario = (User) sessionObj.getAttribute("usuario");
        Employee employee = GestionUser.getEmployeeIdByUserId(usuario.getId());

        String[] cantProducts = req.getParameterValues("cantProduct[]");
        String[] preciosUnit = req.getParameterValues("precioUnitProduct[]");
        String[] preciosTotal = req.getParameterValues("precioTotalProduct[]");
        String[] idsProductos = req.getParameterValues("idProduct[]");

        String[] idsServicios = req.getParameterValues("serviceId[]");
        String[] preciosTotalService = req.getParameterValues("precioTotal[]");

        int reservaId = Integer.parseInt(req.getParameter("reservaId"));
        int roomId = Integer.parseInt(req.getParameter("roomId"));

        String opcionPago = req.getParameter("pago");


        switch (action) {
            case "directa":
                String metodoPagoId = req.getParameter("metodoPago");
                if (idsProductos != null && metodoPagoId != null) {
                    // Crear el voucher
                    Voucher voucher = new Voucher();
                    voucher.setTypeVoucher(new TypeVoucher(3, "Directo"));
                    voucher.setPaymentMethod(GestionMetodosPago.getMethodPaymentById(Integer.parseInt(metodoPagoId)));

                    float subtotalProductos = 0;
                    for (String total : preciosTotal) {
                        subtotalProductos += Float.parseFloat(total);
                    }

                    voucher.setSubtotalProducts(subtotalProductos);
                    voucher.setSubtotalServices(0);
                    voucher.setSubtotalPenalidad(0);
                    voucher.setTotalAmount(subtotalProductos);

                    int comprobanteId = GestionVentas.registrarComprobante(1000000000, voucher);
                    boolean ventaHecha = true;

                    for (int i = 0; i < idsProductos.length; i++) {
                        ConsumeProduct cp = new ConsumeProduct();
                        cp.setProduct(GestionProduct.getProductById(Integer.parseInt(idsProductos[i])));
                        cp.setQuantity(Integer.parseInt(cantProducts[i]));
                        cp.setPriceUnit(Float.parseFloat(preciosUnit[i]));
                        cp.setPriceTotal(Float.parseFloat(preciosTotal[i]));

                        boolean exito = GestionVentas.registrarLineaVentaDirecta(comprobanteId, employee.getId(), cp);
                        if (!exito) {
                            ventaHecha = false;
                            break;
                        }
                    }

                    if (ventaHecha) {
                        resp.sendRedirect("menu.jsp?view=ventaDirecta&succes=ventadirectarealizada");
                    }


                } else {
                    System.out.println("No se recibieron productos o método de pago.");
                }
                break;

            case "ventaProducto":

                int comprobanteIdGenerate = -1;
                boolean ventasOk = true;

                for (int i = 0; i < idsProductos.length; i++) {
                    ConsumeProduct cp = new ConsumeProduct();
                    cp.setProduct(GestionProduct.getProductById(Integer.parseInt(idsProductos[i])));
                    cp.setQuantity(Integer.parseInt(cantProducts[i]));
                    cp.setPriceUnit(Float.parseFloat(preciosUnit[i]));
                    cp.setPriceTotal(Float.parseFloat(preciosTotal[i]));

                    String estadoPago = opcionPago.equalsIgnoreCase("ahora") ? "Pagado" : "Pendiente";
                    cp.setEstado_pago(estadoPago);

                    boolean ventaExitosa = GestionVentas.registrarVenta(reservaId, roomId, cp);
                    if (!ventaExitosa) {
                        ventasOk = false;
                        break;
                    }
                }

                if (ventasOk && "ahora".equalsIgnoreCase(opcionPago)) {
                    String metodoPagoStr = req.getParameter("metodoPago");
                    if (metodoPagoStr == null || metodoPagoStr.isEmpty()) {
                        resp.sendRedirect("menu.jsp?view=habitacionesVenta&error=faltamodopago");
                        return;
                    }

                    float subtotalProductos = 0;
                    for (String total : preciosTotal) {
                        subtotalProductos += Float.parseFloat(total);
                    }

                    Voucher voucher = new Voucher();
                    voucher.setTypeVoucher(new TypeVoucher(1, "Boleta"));
                    voucher.setPaymentMethod(GestionMetodosPago.getMethodPaymentById(Integer.parseInt(metodoPagoStr)));
                    voucher.setSubtotalProducts(subtotalProductos);
                    voucher.setSubtotalServices(0);
                    voucher.setSubtotalPenalidad(0);
                    voucher.setTotalAmount(subtotalProductos);

                    comprobanteIdGenerate = GestionVentas.registrarComprobante(reservaId, voucher);
                }

                if (ventasOk && ("despues".equalsIgnoreCase(opcionPago) || comprobanteIdGenerate > 0)) {
                    resp.sendRedirect("menu.jsp?view=habitacionesVenta&succes=ventadirectarealizada");
                } else {
                    resp.sendRedirect("menu.jsp?view=habitacionesVenta&error=falloregistro");
                }

                break;
            case "ventaServicio":

                int comprobanteServiceIdGenerate = -1;
                boolean ventaServiceOk = true;

                for (int i = 0; i < idsServicios.length; i++) {
                    ConsumeService cs = new ConsumeService();
                    cs.setService(GestionService.getserviceById(Integer.parseInt(idsServicios[i])));
                    cs.setTotalPrice(Float.parseFloat(preciosTotalService[i]));

                    String estadoPago = opcionPago.equalsIgnoreCase("ahora") ? "Pagado" : "Pendiente";
                    cs.setEstado_pago(estadoPago);

                    boolean ventaExitosa = GestionVentas.registrarVentaService(reservaId, roomId, cs);
                    if (!ventaExitosa) {
                        ventaServiceOk = false;
                        break;
                    }
                }

                if (ventaServiceOk && "ahora".equalsIgnoreCase(opcionPago)) {
                    String metodoPagoStr = req.getParameter("metodoPago");
                    if (metodoPagoStr == null || metodoPagoStr.isEmpty()) {
                        resp.sendRedirect("menu.jsp?view=habitacionesServicio&error=faltamodopago");
                        return;
                    }

                    float subtotalServicios = 0;
                    for (String total : preciosTotalService) {
                        subtotalServicios += Float.parseFloat(total);
                    }

                    Voucher voucher = new Voucher();
                    voucher.setTypeVoucher(new TypeVoucher(1, "Boleta"));
                    voucher.setPaymentMethod(GestionMetodosPago.getMethodPaymentById(Integer.parseInt(metodoPagoStr)));
                    voucher.setSubtotalProducts(0);
                    voucher.setSubtotalServices(subtotalServicios);
                    voucher.setSubtotalPenalidad(0);
                    voucher.setTotalAmount(subtotalServicios);

                    comprobanteServiceIdGenerate = GestionVentas.registrarComprobante(reservaId, voucher);
                }

                if (ventaServiceOk && ("despues".equalsIgnoreCase(opcionPago) || comprobanteServiceIdGenerate > 0)) {
                    resp.sendRedirect("menu.jsp?view=habitacionesServicio&succes=ventaserviciorealizada");
                } else {
                    resp.sendRedirect("menu.jsp?view=habitacionesServicio&error=falloregistro");
                }
                break;
        }

    }
}
