<%@ page import="development.team.hoteltransylvania.DTO.TableReservationDTO" %>
<%@ page import="development.team.hoteltransylvania.Model.Product" %>
<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="development.team.hoteltransylvania.Model.Service" %>
<%@ page import="development.team.hoteltransylvania.Model.PaymentMethod" %>
<%@ page import="development.team.hoteltransylvania.Business.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <title>Vender Servicio</title>
</head>

<body>
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
    <h4><i class="fa-solid fa-basket-shopping me-2"></i> Proceso de Servicio</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item"><a href="#" onclick="cargarPagina('jsp/habitacionesServicio.jsp')">Habitaciones</a></li>
            <li class="breadcrumb-item active" aria-current="page">Procesar Servicio</li>
        </ol>
    </nav>
</div>

<%
    int idParam = Integer.parseInt(request.getParameter("id"));
    TableReservationDTO reservation = GestionReservation.getReservationById(idParam);
    Room room = GestionRoom.getRoomById(reservation.getIdRoom());

    List<Service> servicesInList = GestionService.getAllServices().
            stream().filter(s -> s.getStatus() == 1).collect(Collectors.toUnmodifiableList());
    List<PaymentMethod> paymentMethodsActive = GestionMetodosPago.getAllMethodPayments()
            .stream().filter(method -> method.getStatus() == 1).collect(Collectors.toUnmodifiableList());
%>

<!-- Sección de datos -->
<div class="row mt-4">
    <div class="col-lg-6 col-md-6 col-sm-12">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos de la Habitación</strong></div>
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <p><strong>Número:</strong> <%= room.getNumber() %></p>
                        <p><strong>Costo:</strong> <span class="text-primary">S/ <%= room.getPrice() %></span></p>
                        <p><strong>Tipo:</strong> <%= room.getTypeRoom().getName().toUpperCase() %></p>
                    </div>
                    <div class="col-6">
                        <p><strong>Estado:</strong> <%= room.getTypeRoom().getName() %></p>
                        <p><strong>Tarifa:</strong> 24Hr.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-12 mt-4 mt-md-0">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos del Cliente</strong></div>
            <div class="card-body">
                <p><strong>Nombre:</strong> <%=reservation.getClientName()+" "+reservation.getClientApellidos()%></p>
                <p><strong>Documento:</strong> <%=reservation.getDocumentNumber()%></p>
                <p><strong>Fecha entrada:</strong> <%=reservation.getFecha_ingreso()%></p>
            </div>
        </div>
    </div>
</div>

<!-- Sección de servicios -->
<div class="card mt-4">
    <div class="card-header text-white">
        <div class="row align-items-center">
            <div class="col-9 d-flex gap-2">
                <select class="form-select" id="selectServicio">
                    <option selected>Seleccione una opción</option>
                    <%for (Service service : servicesInList) {%>
                    <option value="<%=service.getId()%>">Nombre: <%=service.getName()%> | Precio: <%=service.getPrice()%></option>
                    <%}%>
                </select>
                <button class="btn btn-primary" onclick="agregarServicio('#detalleServicios')">Agregar</button>
            </div>
        </div>
    </div>
    <form id="formVentaServicio" action="ventacontroller" method="post">
        <div class="card-body">
            <div class="table-responsive">
                <table id="detalleServicios" class="table table-bordered align-middle">
                    <thead class="table-warning">
                    <tr>
                        <th>Nombre</th>
                        <th>Precio Total</th>
                        <th>Eliminar</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td colspan="5" class="text-center text-muted">Agrega servicios</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <p id="totalGeneral" class="fw-bold mt-3 mt-sm-3">TOTAL: S/.0</p>

            <div class="d-flex flex-column flex-md-row align-items-start align-items-md-center">
                <div class="form-check me-3 mb-2 mb-md-0">
                    <input class="form-check-input" type="radio" name="pago" id="pagarAhora" value="ahora" onclick="habilitarMetodoPago(true)">
                    <label class="form-check-label" for="pagarAhora">Pagar Ahora</label>
                </div>

                <div class="input-group my-3 my-md-0" id="metodoPagoGroup" style="max-width: 300px;">
                    <label class="input-group-text" for="metodoPago"><i class="fa-solid fa-money-bill-wave"></i></label>
                    <select class="form-select" id="metodoPago" name="metodoPago" required disabled>
                        <option value="" selected disabled>Método de Pago</option>
                        <%for (PaymentMethod paymentMethod : paymentMethodsActive) {%>
                        <option value="<%=paymentMethod.getId()%>"><%=paymentMethod.getNameMethod()%></option>
                        <%}%>
                    </select>
                </div>
            </div>

            <div class="form-check mt-2">
                <input class="form-check-input" type="radio" name="pago" id="pagarDespues" value="despues" onclick="habilitarMetodoPago(false)" checked>
                <label class="form-check-label" for="pagarDespues">Pagar Después</label>
            </div>
            <div class="d-flex justify-content-end mt-3">
                <input type="hidden" name="actionVenta" value="ventaServicio">
                <input type="hidden" name="reservaId" value="<%=reservation.getIdReservation()%>">
                <input type="hidden" name="roomId" value="<%=reservation.getIdRoom()%>">
                <button class="btn btn-success" onclick="validacionVentaServicio()">
                    Terminar venta
                </button>
            </div>
        </div>
    </form>

</div>
</body>