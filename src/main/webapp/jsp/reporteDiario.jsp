<%@ page import="development.team.hoteltransylvania.Business.GestionEmployee" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.Employee" %>
<%@ page import="development.team.hoteltransylvania.DTO.usersEmployeeDTO" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionVentas" %>
<%@ page import="development.team.hoteltransylvania.DTO.AllInfoVentasDirecta" %>
<%@ page import="development.team.hoteltransylvania.DTO.AllInfoReporteVenta" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionReportes" %>
<%@ page import="development.team.hoteltransylvania.DTO.AllInfoReporteAlquiler" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte Diario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

    <%
    //1era pestaña
    List<AllInfoReporteAlquiler> alquileres = GestionReportes.getReporteReservation();
    double totalReservation = alquileres.stream().filter(a -> "Reservación".equalsIgnoreCase(a.getTipoAlquiler()))
                                .mapToDouble(AllInfoReporteAlquiler::getPago_total_reserva)
                                .sum();
    double totalRecepcion = alquileres.stream().filter(a -> "Recepción".equalsIgnoreCase(a.getTipoAlquiler()))
                                .mapToDouble(AllInfoReporteAlquiler::getPago_total_reserva)
                                .sum();
    double recaudacionCancelada = alquileres.stream().filter(a -> "Cancelada *".equalsIgnoreCase(a.getTipoAlquiler()))
                                .mapToDouble(AllInfoReporteAlquiler::getAdelanto)
                                .sum();

    //2da pestaña
    List<AllInfoVentasDirecta> reporteVentasDirecta = GestionVentas.getAllVentasDirecta();
    List<AllInfoReporteVenta> reporteVentaProducHabiatcion = GestionReportes.getAllReporteVentaProductoHabitacion();
    double totalProduct = reporteVentaProducHabiatcion.stream().mapToDouble(AllInfoReporteVenta::getPrecioTotalArticulo).sum();
    List<AllInfoReporteVenta> reporteVentaServcHabiatcion = GestionReportes.getAllReporteVentaServicioHabitacion();
    double totalService = reporteVentaServcHabiatcion.stream().mapToDouble(AllInfoReporteVenta::getPrecioTotalArticulo).sum();
    double totalPSrecepcion = totalProduct + totalService;

    //3ra pestaña

    List<usersEmployeeDTO> employees = GestionEmployee.getAllEmployees();
    double totalVentaDirecta = GestionVentas.getAmuntTotalVentaDirecta();
    List<AllInfoVentasDirecta> allInfoVentasDirectas = GestionVentas.getAllVentasDirecta();
%>
<body>
<div class="d-flex justify-content-between align-items-center">
    <h4><i class="fa-solid fa-sheet-plastic me-2"></i> Reporte Diario</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item active" aria-current="page">Reporte Diario</li>
        </ol>
    </nav>
</div>

<!-- Filtros -->
<div class="row my-3">
    <%
        java.time.LocalDate hoy = java.time.LocalDate.now();
    %>
    <div class="col-md-2">
        <label for="fecha" class="form-label"><strong>Fecha</strong></label>
        <input type="date" id="fecha" class="form-control" value="<%= hoy %>"
               onchange="SearchReporte('#fecha', '#responsable', '#nameSearch', 'filterReportes', 1, 10)">
    </div>
    <div class="col-md-2">
        <label for="responsable" class="form-label"><strong>Responsable</strong></label>
        <select id="responsable" class="form-select"
                onchange="SearchReporte('#fecha', '#responsable', '#nameSearch','filterReportes', 1, 10)">
            <option value="todos">Todos</option>
            <%for (usersEmployeeDTO employeeDTO : employees) {%>
            <option value="<%=employeeDTO.getId_employee()%>"><%=employeeDTO.getName_employee()%>
            </option>
            <%}%>
        </select>
    </div>
</div>

<!-- Pestañas -->
<ul class="nav nav-tabs mt-4">
    <li class="nav-item">
        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#alquiler"
                type="button"
                onclick="SearchReporte('#fecha', '#responsable', '#registros', 'filterReportes', 1, 10)">
            Tabla alquiler
        </button>
    </li>
    <li class="nav-item">
        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#habitacion-venta"
                type="button"
                onclick="SearchReporte('#fecha', '#responsable', '#registros', 'filterReportes', 1, 10)">
            Servicio a la habitación y venta
        </button>
    </li>
    <li class="nav-item">
        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#habitacion-venta-directa"
                type="button"
                onclick="SearchReporte('#fecha', '#responsable', '#registros', 'filterReportes', 1, 10)">
            Ventas directa
        </button>
    </li>
    <li class="nav-item ms-auto">
        <button class="btn btn-success">
            <i class="fa-solid fa-file-export"></i> Exportar
        </button>
    </li>
</ul>

<div class="tab-content mt-3">
    <div class="tab-pane fade show active" id="alquiler">
        <!-- Resumen de recepción -->
        <div class="d-flex justify-content-between text-center px-5 mt-3">
            <div>
                <h5>S/. <%=totalRecepcion%></h5>
                <h5>TOTAL RECEPCIÓN</h5>
            </div>
            <div>
                <h5>S/. <%=totalReservation%></h5>
                <h5>TOTAL RESERVACION</h5>
            </div>
            <div>
                <h5>S/. <%=recaudacionCancelada%></h5>
                <h5>REC. CANCELADA</h5>
            </div>
            <div>
                <h5>S/.<%=totalRecepcion+totalReservation+recaudacionCancelada%></h5>
                <h5>TOTAL</h5>
            </div>
        </div>

        <div class="d-flex justify-content-between align-items-center mb-3 mt-4">
            <label for="registros">Mostrando
                <input id="registrosAlquiler" type="number" min="1" max="999" value="1"
                       class="form-control d-inline-block text-center ms-1 me-1" style="width: 4rem;">
                registros
            </label>

            <div class="input-group" style="max-width: 250px;">
                <input type="text" class="form-control" id="roomSearch" placeholder="Buscar por Habitación o Tipo"
                onkeyup="SearchReporte('#fecha', '#responsable', '#roomSearch', 'filterReportes', 1, 10)">
                <span class="input-group-text"><i class="fas fa-search"></i></span>
            </div>
        </div>
        <div class="table-responsive mt-4">
            <table id="reporteAlquiler" class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>N°</th>
                    <th>Identificador</th>
                    <th>Tipo</th>
                    <th>Habitación</th>
                    <th>Descuento</th>
                    <th>Extra</th>
                    <th>Dinero Adelantado</th>
                    <th>Servicios</th>
                    <th>Penalidad</th>
                    <th>Total Alquiler</th>
                    <th>Tiempo Rebasado</th>
                    <th>Detalles</th>
                </tr>
                </thead>
                <tbody>
                <%
                    int countAl = 1;
                    for (AllInfoReporteAlquiler alquiler : alquileres) {
                        boolean esCancelada = "Cancelada *".equals(alquiler.getTipoAlquiler());

                        // Color para pago total
                        String clasePagoTotal = esCancelada ? "" : "text-success-emphasis fw-bold";
                        String pagoTotalTexto = esCancelada
                                ? alquiler.getPago_total_reserva() + " *"
                                : String.valueOf(alquiler.getPago_total_reserva());

                        // Color para adelanto
                        String claseAdelanto = (esCancelada && alquiler.getAdelanto() > 0)
                                ? "text-success-emphasis fw-bold"
                                : "";
                %>
                <tr>
                    <td><%= countAl %></td>
                    <td><%= alquiler.getIdReservation() %></td>
                    <td><%= alquiler.getTipoAlquiler() %></td>
                    <td><%= alquiler.getNumberRoom() %>-<%= alquiler.getRoomType() %></td>
                    <td><%= alquiler.getDsct() %></td>
                    <td><%= alquiler.getCobro_extra() %></td>
                    <td class="<%= claseAdelanto %>"><%= alquiler.getAdelanto() %></td>
                    <td><%= alquiler.getTotal_consumo_productos() + alquiler.getTotal_consumo_servicios() %></td>
                    <td><%= alquiler.getTotal_penalidad() %></td>
                    <td class="<%= clasePagoTotal %>"><%= pagoTotalTexto %></td>
                    <td class="text-danger">Sí</td>
                    <td class="d-flex justify-content-center gap-1">
                        <button class="btn btn-info btn-sm" data-bs-toggle="modal" data-bs-target="#modalVerDetalle">
                            👁️
                        </button>
                    </td>
                </tr>
                <%
                        countAl++;
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end align-items-center">
            <nav aria-label="Page navigation example">
                <ul class="pagination mb-0">
                    <li class="page-item"><a class="page-link" href="#">Anterior</a></li>
                    <li class="page-item"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">Siguiente</a></li>
                </ul>
            </nav>
        </div>
    </div>


    <div class="tab-pane fade" id="habitacion-venta">
        <!-- Resumen de recepción -->
        <div class="d-flex justify-content-between text-center px-5 mt-3">
            <div>
                <h5>S/.<%=totalVentaDirecta%></h5>
                <h5>TOTAL VENTA DIRECTA</h5>
            </div>
            <div>
                <h5>S/.<%=totalPSrecepcion%></h5>
                <h5>TOTAL RECEPCION</h5>
            </div>
            <div>
                <h5>S/.<%=totalVentaDirecta + totalPSrecepcion%></h5>
                <h5>TOTAL</h5>
            </div>
        </div>

        <div class="d-flex justify-content-between align-items-center mb-3 mt-4">
            <label for="registros">Mostrando
                <input id="registrosVentaHab" type="number" min="1" max="999" value="1"
                       class="form-control d-inline-block text-center ms-1 me-1" style="width: 5rem;">
                registros
            </label>

            <div class="input-group" style="max-width: 250px;">
                <input type="text" class="form-control" id="articuloSearch" placeholder="Buscar"
                       onkeyup="SearchReporte('#fecha', '#responsable', '#articuloSearch', 'filterReportes', 1, 10)">
                <span class="input-group-text"><i class="fas fa-search"></i></span>
            </div>
        </div>
        <div class="table-responsive mt-4">
            <table id="tablaServiciosHab" class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>N°</th>
                    <th>Tipo</th>
                    <th>Habitación</th>
                    <th>Articulo</th>
                    <th>Cantidad</th>
                    <th>Precio Unitario</th>
                    <th>Total</th>
                    <th>Fecha - Hora</th>
                    <th>Responsable</th>
                </tr>
                </thead>
                <tbody>
                <%
                    int count1=1;
                    for(AllInfoVentasDirecta reportevd : reporteVentasDirecta){%>
                <tr>
                    <td><%=count1%></td>
                    <td>Venta Directa</td>
                    <td>--</td>
                    <td><%=reportevd.getProducto()%></td>
                    <td><%=reportevd.getCantidad()%></td>
                    <td><%=reportevd.getPrecio_unitario()%></td>
                    <td><%=reportevd.getPrecio_total()%></td>
                    <td><%=reportevd.getFecha_hora()%></td>
                    <td><%=reportevd.getEmpleado()%></td>
                </tr>
                <%count1++;}%>
                <%
                    for(AllInfoReporteVenta reporteph : reporteVentaProducHabiatcion){%>
                <tr>
                    <td><%=count1%></td>
                    <td>Recepción</td>
                    <td><%=reporteph.getNumeroHabitacion()%></td>
                    <td><%=reporteph.getNombreArticulo()%></td>
                    <td><%=reporteph.getCantArticulo()%></td>
                    <td><%=reporteph.getPrecioUnitArticulo()%></td>
                    <td><%=reporteph.getPrecioTotalArticulo()%></td>
                    <td><%=reporteph.getFecha_hora_compra()%></td>
                    <td><%=reporteph.getNombreEmpleado()%></td>
                </tr>
                <%count1++;}%>
                <%
                    for(AllInfoReporteVenta reportesh : reporteVentaServcHabiatcion){%>
                <tr>
                    <td><%=count1%></td>
                    <td>Recepción</td>
                    <td><%=reportesh.getNumeroHabitacion()%></td>
                    <td><%=reportesh.getNombreArticulo()%></td>
                    <td>--</td>
                    <td>--</td>
                    <td><%=reportesh.getPrecioTotalArticulo()%></td>
                    <td><%=reportesh.getFecha_hora_compra()%></td>
                    <td><%=reportesh.getNombreEmpleado()%></td>
                </tr>
                <%count1++;}%>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end align-items-center">
            <nav aria-label="Page navigation example">
                <ul class="pagination mb-0">
                    <li class="page-item"><a class="page-link" href="#">Anterior</a></li>
                    <li class="page-item"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">Siguiente</a></li>
                </ul>
            </nav>
        </div>
    </div>


    <div class="tab-pane fade" id="habitacion-venta-directa">
        <!-- Resumen de recepción -->
        <div class="container mt-4">
            <div class="d-flex justify-content-center text-center gap-5">
                <div>
                    <h5>S/.<%=totalVentaDirecta%>
                    </h5>
                    <h5>TOTAL VENTA DIRECTA</h5>
                </div>
                <div>
                    <h5 id="totalVentaEmpleadoVD">S/.<%=totalVentaDirecta%></h5>
                    <h5>TOTAL POR EMPLEADO</h5>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-between align-items-center mb-3 mt-4">
            <label for="registros">Mostrando
                <input id="registrosVentaDirecta" type="number" min="1" max="999" value="<%=allInfoVentasDirectas.size()%>"
                       class="form-control d-inline-block text-center ms-1 me-1" style="width: 5rem;" readonly>
                registros
            </label>

            <div class="input-group" style="max-width: 250px;">
                <input type="text" class="form-control" id="productSearch" placeholder="Buscar por producto"
                onkeyup="SearchReporte('#fecha', '#responsable', '#productSearch', 'filterReportes', 1, 10)">
                <span class="input-group-text"><i class="fas fa-search"></i></span>
            </div>
        </div>
        <div class="table-responsive mt-4">
            <table id="tablaReportesVD" class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>N°</th>
                    <th>Producto</th>
                    <th>Cantidad</th>
                    <th>Precio Unitario</th>
                    <th>Total</th>
                    <th>Fecha-Hora</th>
                    <th>Responsable</th>
                </tr>
                </thead>
                <tbody>
                <%
                    int count = 1;
                    for (AllInfoVentasDirecta directa : allInfoVentasDirectas) {
                %>
                    <tr>
                        <td><%=count%></td>
                        <td><%=directa.getProducto()%></td>
                        <td><%=directa.getCantidad()%></td>
                        <td><%=directa.getPrecio_unitario()%></td>
                        <td><%=directa.getPrecio_total()%></td>
                        <td><%=directa.getFecha_hora()%></td>
                        <td><%=directa.getEmpleado()%></td>
                    </tr>
                <%count++;}%>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end align-items-center">
            <nav aria-label="Page navigation example">
                <ul class="pagination mb-0">
                    <li class="page-item"><a class="page-link" href="#">Anterior</a></li>
                    <li class="page-item"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">Siguiente</a></li>
                </ul>
            </nav>
        </div>
    </div>
</div>

<!-- Modal para ver Detalle -->
<div class="modal fade" id="modalVerDetalle" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="detalleAlquiler">Detalles del Alquiler</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <h5>Datos del Cliente:</h5>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre Completo</label>
                    <input type="text" class="form-control" id="nombre" readonly>
                </div>
                <div class="mb-3">
                    <label for="tipoDocumento" class="form-label">Tipo de Documento</label>
                    <select class="form-select" id="tipoDocumento" disabled>
                        <option value="DNI">DNI</option>
                        <option value="PASAPORTE">PASAPORTE</option>
                        <option value="RUC">RUC</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="documento" class="form-label">Documento</label>
                    <input type="text" class="form-control" id="documento" readonly>
                </div>
                <div class="mb-3">
                    <label for="nit" class="form-label">NIT</label>
                    <input type="text" class="form-control" id="nit" readonly>
                </div>
                <div class="mb-3">
                    <label for="nombreFacturar" class="form-label">Nombre a Facturar</label>
                    <input type="text" class="form-control" id="nombreFacturar" readonly>
                </div>
                <div class="mb-3">
                    <label for="correo" class="form-label">Correo</label>
                    <input type="email" class="form-control" id="correo" readonly>
                </div>
                <div class="mb-3">
                    <label for="telefono" class="form-label">Teléfono</label>
                    <input type="tel" class="form-control" id="telefono" readonly>
                </div>

                <hr>

                <h5>Datos del Alojamiento:</h5>
                <div class="mb-3">
                    <label for="tipoAtencion" class="form-label">Tipo de Atención</label>
                    <select class="form-select" id="tipoAtencion" disabled>
                        <option value="recepción">Recepción</option>
                        <option value="reservación">Reservación</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="tipoHabitacion" class="form-label">Tipo de Habitación</label>
                    <select class="form-select" id="tipoHabitacion" disabled>
                        <option value="simple">Simple</option>
                        <option value="doble">Doble</option>
                        <option value="presidencial">Presidencial</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="habitacion" class="form-label">Habitación</label>
                    <select class="form-select" id="habitacion" disabled>
                        <option value="696">696</option>
                        <option value="600">600</option>
                        <option value="100">100</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="tarifa" class="form-label">Tarifa</label>
                    <select class="form-select" id="tarifa" disabled>
                        <option value="12hr">12hr</option>
                        <option value="24hr">24hr</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="descuento" class="form-label">Descuento</label>
                    <input type="text" class="form-control" id="descuento" readonly>
                </div>
                <div class="mb-3">
                    <label for="precioPagar" class="form-label">Precio a Pagar</label>
                    <input type="text" class="form-control" id="precioPagar" readonly>
                </div>
                <div class="mb-3">
                    <label for="creacion" class="form-label">Creación</label>
                    <input type="date" class="form-control" id="creacion" readonly>
                </div>
                <div class="mb-3">
                    <label for="ingreso" class="form-label">Ingreso</label>
                    <input type="date" class="form-control" id="ingreso" readonly>
                </div>
                <div class="mb-3">
                    <label for="salidaCalculada" class="form-label">Salida Calculada</label>
                    <input type="date" class="form-control" id="salidaCalculada" readonly>
                </div>
                <div class="mb-3">
                    <label for="salida" class="form-label">Salida</label>
                    <input type="date" class="form-control" id="salida" readonly>
                </div>
                <div class="mb-3">
                    <label for="tiempoRebasado" class="form-label">Tiempo Rebasado</label>
                    <input type="text" class="form-control" id="tiempoRebasado" readonly>
                </div>
                <div class="mb-3">
                    <label for="observaciones" class="form-label">Observaciones</label>
                    <input type="text" class="form-control" id="observaciones" readonly>
                </div>

                <hr>

                <h5>Costos:</h5>
                <div class="mb-3">
                    <label for="cobroExtra" class="form-label">Cobro extra</label>
                    <input type="number" class="form-control" id="cobroExtra" readonly>
                </div>
                <div class="mb-3">
                    <label for="costoAlquilerCalculado" class="form-label">Costo Alquiler Calculado</label>
                    <input type="number" class="form-control" id="costoAlquilerCalculado" readonly>
                </div>
                <div class="mb-3">
                    <label for="dineroAdelantado" class="form-label">Dinero Adelantado</label>
                    <input type="number" class="form-control" id="dineroAdelantado" readonly>
                </div>
                <div class="mb-3">
                    <label for="servicio" class="form-label">Servicio</label>
                    <input type="number" class="form-control" id="servicio" readonly>
                </div>
                <div class="mb-3">
                    <label for="penalidad" class="form-label">Penalidad</label>
                    <input type="number" class="form-control" id="penalidad" readonly>
                </div>
                <div class="mb-3">
                    <label for="totalPagar" class="form-label">Total a Pagar</label>
                    <input type="number" class="form-control" id="totalPagar" readonly>
                </div>

                <hr>

                <h5>Métodos de Pago:</h5>
                <div class="mb-3">
                    <label for="dineroAdelantadoPago" class="form-label">Dinero Adelantado</label>
                    <select class="form-select" id="dineroAdelantadoPago" disabled>
                        <option value="efectivo">Efectivo</option>
                        <option value="tarjeta">Tarjeta</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="penalidadAlquilerCalculado" class="form-label">Penalidad y Resto del Alquiler
                        Calculado</label>
                    <select class="form-select" id="penalidadAlquilerCalculado" disabled>
                        <option value="efectivo">Efectivo</option>
                        <option value="tarjeta">Tarjeta</option>
                    </select>
                </div>

                <hr>

                <h5>Responsables:</h5>
                <div class="mb-3">
                    <label for="usuarioCreacion" class="form-label">Usuario de Creación</label>
                    <select class="form-select" id="usuarioCreacion" disabled>
                        <option value="usuario1">Usuario 1</option>
                        <option value="usuario2">Usuario 2</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="usuarioModificacion" class="form-label">Usuario de Modificación</label>
                    <select class="form-select" id="usuarioModificacion" disabled>
                        <option value="usuario1">Usuario 1</option>
                        <option value="usuario2">Usuario 2</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="usuarioFinalizacion" class="form-label">Usuario de Finalización</label>
                    <select class="form-select" id="usuarioFinalizacion" disabled>
                        <option value="usuario1">Usuario 1</option>
                        <option value="usuario2">Usuario 2</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>


</body>