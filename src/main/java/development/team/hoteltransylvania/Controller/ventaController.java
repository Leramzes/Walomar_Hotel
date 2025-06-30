package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionMetodosPago;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Business.GestionUser;
import development.team.hoteltransylvania.Business.GestionVentas;
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

        int reservaId = Integer.parseInt(req.getParameter("reservaId"));
        int roomId = Integer.parseInt(req.getParameter("roomId"));


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

                    try {
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

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    System.out.println("No se recibieron productos o método de pago.");
                }
                break;
            case "ventaProducto":

                String opcionPago = req.getParameter("pago"); // "ahora" o "despues"

                String metodoPagoStr = req.getParameter("metodoPago"); // puede ser null si eligió "despues"
                Integer metodoPago = null;

                if ("ahora".equals(opcionPago) && metodoPagoStr != null) {
                    metodoPago = Integer.parseInt(metodoPagoStr);
                }

                for (int i = 0; i < idsProductos.length; i++) {
                    ConsumeProduct cp = new ConsumeProduct();
                    cp.setProduct(GestionProduct.getProductById(Integer.parseInt(idsProductos[i])));
                    cp.setQuantity(Integer.parseInt(cantProducts[i]));
                    cp.setPriceUnit(Float.parseFloat(preciosUnit[i]));
                    cp.setPriceTotal(Float.parseFloat(preciosTotal[i]));

                    String estadoPago = (metodoPago == null) ? "Pendiente" : "Pagado";
                    cp.setEstado_pago(estadoPago);

                    boolean val = GestionVentas.registrarVenta(reservaId, roomId, cp);
                    if (val) System.out.println("se registro");

                }
                break;

        }

    }
}
