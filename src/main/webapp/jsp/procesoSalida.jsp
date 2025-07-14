<%@ page import="development.team.hoteltransylvania.DTO.TableReservationDTO" %>
<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="development.team.hoteltransylvania.Model.PaymentMethod" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="development.team.hoteltransylvania.Model.Employee" %>
<%@ page import="development.team.hoteltransylvania.DTO.usersEmployeeDTO" %>
<%@ page import="development.team.hoteltransylvania.Business.*" %>
<%@ page import="development.team.hoteltransylvania.Model.Product" %>
<%@ page import="development.team.hoteltransylvania.DTO.AllInfoTableProdSalida" %>
<%@ page import="development.team.hoteltransylvania.DTO.AllInfoTableServSalida" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <title>Proceso Salida</title>
</head>

<body>
<!-- Encabezado -->
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
    <h4><i class="fa-solid fa-right-from-bracket me-2"></i> Proceso de Salida</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item"><a href="#"
                                           onclick="cargarPagina('jsp/VerificacionSalidas.jsp')">Habitaciones</a></li>
            <li class="breadcrumb-item active" aria-current="page">Proceso Salida</li>
        </ol>
    </nav>
</div>

<%
    int idParam = Integer.parseInt(request.getParameter("id"));
    TableReservationDTO reservation = GestionReservation.getReservationById(idParam);
    Room room = GestionRoom.getRoomById(reservation.getIdRoom());
    GestionEmployee employeeObject = new GestionEmployee();
    usersEmployeeDTO employee = employeeObject.getEmployeeById(reservation.getEmpleadoId());
    String fullName = employee.getName_employee();
    String[] parts = fullName.trim().split("\\s+"); // divide por espacios múltiples
    String onlyFirstName = parts[0];

    List<PaymentMethod> paymentMethodsActive = GestionMetodosPago.getAllMethodPayments()
            .stream().filter(method -> method.getStatus() == 1).collect(Collectors.toUnmodifiableList());

    //Lista de venta de productos (pendientes y pagadas) del huesped
    List<AllInfoTableProdSalida> ventasByReserva = GestionVentas.obtenerVentasProdPorReserva(reservation.getIdReservation());
    //Lista de venta de servicios (pendientes y pagadas) del huesped
    List<AllInfoTableServSalida> ventasSByReserva = GestionVentas.obtenerVentasServPorReserva(reservation.getIdReservation());
%>

<!-- Sección de Información -->
<div class="row mt-4">
    <div class="col-md-4 col-sm-12 mt-4 mt-md-0">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos de la Habitación</strong></div>
            <div class="card-body">
                <p><strong>Nombre:</strong> <%= room.getNumber() %>
                </p>
                <p><strong>Tipo:</strong> <%= room.getTypeRoom().getName().toUpperCase() %>
                </p>
                <p><strong>Costo:</strong> <span class="text-primary"><%= room.getPrice() %></span></p>
                <p><strong>Descuento:</strong> <span><%= reservation.getDsct() %></span></p>
            </div>
        </div>
    </div>
    <div class="col-md-4 col-sm-12 mt-4 mt-md-0">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos del Cliente</strong></div>
            <div class="card-body">
                <p><strong>Nombre:</strong> <%=reservation.getClientName() + " " + reservation.getClientApellidos()%>
                </p>
                <p><strong>Documento:</strong> <%=reservation.getDocumentNumber()%>
                </p>
                <p><strong>Teléfono:</strong> <%=reservation.getPhone()%>
                </p>
                <p><strong>Correo:</strong> <%=reservation.getEmail()%>
                </p>
            </div>
        </div>
    </div>
    <div class="col-md-4 col-sm-12 mt-4 mt-md-0">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos del Hospedaje</strong></div>
            <div class="card-body">
                <div class="row">
                    <!-- Columna izquierda -->
                    <div class="col-6">
                        <p><strong>Fecha de Entrada Programada:</strong> <%= reservation.getCheckInDate() %>
                        </p>
                        <p><strong>Fecha de Ingreso:</strong> <%= reservation.getFecha_ingreso() %>
                        </p>
                        <p><strong>Fecha de Salida:</strong> <%= reservation.getCheckOutDate() %>
                        </p>
                    </div>

                    <!-- Columna derecha -->
                    <div class="col-6">
                        <p><strong>Tiempo Estimado:</strong> <%= reservation.getTiempoEstimado() %>
                        </p>
                        <p><strong>Tiempo Rebasado:</strong> <span
                                class="text-danger"><%= reservation.getTiempoRebasado() %></span></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Sección de Proceso Salida -->
<div class="card mt-4">
    <div class="card-header" style="background: #0e2238;">
    </div>
    <div class="card-body">
        <h5>Costo de Alojamiento:</h5>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>Dinero Extra</th>
                    <th>Costo Calculado</th>
                    <th>Dinero Adelantado</th>
                    <th>Mora/Penalidad</th>
                    <th>Por Pagar</th>
                    <th>Responsable</th>
                </tr>
                </thead>
                <tbody id="tablaProcesoSalida">
                <tr>
                    <td><%= reservation.getCobro_extra() %>
                    </td>
                    <td><%= reservation.getPago_total() %>
                    </td>
                    <td><%= reservation.getAdelanto() %>
                    </td>
                    <td>
                        <input type="number" id="inputPenalidad" name="inputPenalidad" class="form-control" value="0" min="0"
                               oninput="actualizarTotalConPenalidad()">
                    </td>
                    <td><%= reservation.getPago_total() - reservation.getAdelanto()%>
                    </td>
                    <td><%= onlyFirstName %>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <hr>

        <h5>Productos Consumidos:</h5>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>Producto</th>
                    <th>Precio Unitario</th>
                    <th>Cantidad</th>
                    <th>Estado</th>
                    <th>Sub Total</th>
                </tr>
                </thead>
                <tbody id="tablaServicioCuarto">
                <%
                    double total = reservation.getPago_total() - reservation.getAdelanto();
                    for (AllInfoTableProdSalida venta : ventasByReserva) {
                        boolean esPendiente = venta.getEstadoProducto().equalsIgnoreCase("Pendiente");
                        String classEstado = esPendiente ? "text-danger" : "text-success";
                        String styleTotal = !esPendiente ? "text-decoration: line-through;" : "";

                        if (esPendiente) {
                            total += venta.getTotal();
                        }
                %>
                <tr>
                    <td><%= venta.getNombreProducto() %>
                    </td>
                    <td><%= venta.getPrecioUnitProducto() %>
                    </td>
                    <td><%= venta.getCantidad() %>
                    </td>
                    <td class="<%= classEstado %>"><%= venta.getEstadoProducto() %>
                    </td>
                    <td style="<%= styleTotal %>">S/. <%= venta.getTotal() %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <h5>Servicios Consumidos:</h5>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>Servicio</th>
                    <th>Precio Unitario</th>
                    <th>Cantidad</th>
                    <th>Estado</th>
                    <th>Sub Total</th>
                </tr>
                </thead>
                <tbody id="tablaServicioCuarto">
                <%
                    for (AllInfoTableServSalida venta : ventasSByReserva) {
                        boolean esPendiente = venta.getEstadoServicio().equalsIgnoreCase("Pendiente");
                        String classEstado = esPendiente ? "text-danger" : "text-success";
                        String styleTotal = !esPendiente ? "text-decoration: line-through;" : "";

                        if (esPendiente) {
                            total += venta.getTotal();
                        }
                %>
                <tr>
                    <td><%= venta.getNombreServicio() %>
                    </td>
                    <td>-</td>
                    <td>-</td>
                    <td class="<%= classEstado %>"><%= venta.getEstadoServicio() %>
                    </td>
                    <td style="<%= styleTotal %>">S/. <%= venta.getTotal() %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <p id="totalFinal" class="fw-bold mt-3 mt-sm-3" data-base-total="<%=total%>">TOTAL: S/. <%=total%>
        </p>

        <!-- Método de Pago en un Select-->
        <div class="input-group my-3" id="metodoPagoGroup" style="max-width: 320px;">
            <label class="input-group-text" for="metodoPago"><i class="fa-solid fa-money-bill-wave"></i></label>
            <select class="form-select" id="metodoPago" name="metodoPago" required>
                <option value="" selected disabled>Método de Pago</option>
                <%for (PaymentMethod paymentMethod : paymentMethodsActive) {%>
                <option value="<%=paymentMethod.getId()%>"><%=paymentMethod.getNameMethod()%>
                </option>
                <%}%>
            </select>
        </div>

        <!-- Tipo de Comprobante -->
        <div class="input-group my-3" id="tipoComprobanteGroup" style="max-width: 320px;">
            <label class="input-group-text" for="tipoComprobante">
                <i class="fa-solid fa-receipt"></i>
            </label>
            <select class="form-select" id="tipoComprobante" name="tipoComprobante" required>
                <option value="" selected disabled>Tipo de Comprobante</option>
                <option value="1">Boleta</option>
                <option value="2">Factura</option>
            </select>
        </div>

        <div class="centro">
            <label>
                <input type="checkbox" name="enviarCorreo" value="1" id="enviarCorreo">
                Enviar estado de cuenta por correo
            </label>
        </div>

        <!-- Botones -->
        <div class="d-flex flex-column flex-md-row justify-content-between mt-2">
            <div class="d-flex flex-column flex-md-row justify-content-start gap-1">
                <button class="btn btn-danger w-auto w-sm-100" onclick="cargarPagina('jsp/verificacionSalidas.jsp')">
                    Regresar
                </button>
                <%--<button class="btn btn-primary w-auto w-sm-100 mt-2 mt-md-0" onclick="cargarPagina('jsp/VerificacionSalidas.jsp')"> Realizar Limpieza Intermedia </button>--%>
            </div>
            <input type="hidden" value="<%=reservation.getIdReservation()%>" name="idReserva" id="idReserva">
            <input type="hidden" value="<%=reservation.getIdClient()%>" name="idClient" id="idClient">
            <button class="btn btn-success w-auto w-sm-100 mt-2 mt-md-0" onclick="culminarYGenerarComprobante()"> Culminar y Limpiar Habitación
            </button>
        </div>
    </div>
</div>

</body>