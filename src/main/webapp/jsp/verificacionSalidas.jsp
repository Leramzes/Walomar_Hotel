<%@ page import="development.team.hoteltransylvania.DTO.TableReservationDTO" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionReservation" %>
<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="development.team.hoteltransylvania.Model.Product" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionProduct" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.PaymentMethod" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionMetodosPago" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="development.team.hoteltransylvania.DTO.AllInfoRoom" %>
<%@ page import="development.team.hoteltransylvania.Model.Floor" %>
<%@ page import="java.util.Comparator" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verificación de Salidas</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<%
    List<AllInfoRoom> allInfoRooms = GestionRoom.getAllInfoFromRoomsOcupied();
    List<Floor> floors = GestionRoom.quantityFloorsEnabled().stream()
            .sorted(Comparator.comparing(Floor::getId))
            .toList();
%>
<body>
<!-- Encabezado -->
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
    <h4><i class="fa-solid fa-right-from-bracket me-2"></i> Verificación de Salida</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item active" aria-current="page">Verificar Salida</li>
        </ol>
    </nav>
</div>


<% if (allInfoRooms.isEmpty()) { %>
<div class="col-12">
    <div class="tab-content mt-3">
        <div class="alert alert-danger text-center">
            No hay habitaciones ocupadas por mostrar.
        </div>
    </div>
</div>
<% } else {%>

<ul class="nav nav-tabs mt-4">
    <li class="nav-item">
        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#todos">Todos</button>
    </li>
    <%for (Floor floor : floors) {%>
    <li class="nav-item">
        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#<%=floor.getId()%>-nivel">
            <%=floor.getName()%>
        </button>
    </li>
    <%}%>
</ul>

<%
    // Verificamos si hay al menos un piso activo
    boolean hayPisoActivo = false;
    for (Floor floor : floors) {
        if (floor.getStatus().equals("Activo")) {
            hayPisoActivo = true;
            break;
        }
    }
%>

<div class="tab-content mt-3">

    <% if (hayPisoActivo) { %>

    <!-- TAB: Mostrar todas las habitaciones -->
    <div id="todos" class="tab-pane fade show active">
        <div class="row">
            <% for (AllInfoRoom room : allInfoRooms) {
                String colorOfStatus;
                switch (room.getIdRoomStatus()) {
                    case 2: colorOfStatus = "occupied"; break;
                    case 3: colorOfStatus = "warning"; break;
                    case 4: colorOfStatus = "reserved"; break;
                    default: colorOfStatus = "available";
                }
                String statusName = room.getStatusRoom().toLowerCase();
                statusName = statusName.substring(0, 1).toUpperCase() + statusName.substring(1);
            %>
            <div class="col-md-3">
                <button class="room-card <%= colorOfStatus %>"
                        onclick="cargarPagina('jsp/procesoSalida.jsp?id=<%= room.getIdReservation() %>')">
                    <h5><%= room.getNumberRoom() %></h5>
                    <span><%= room.getTypeRoom().toUpperCase() %></span>
                    <i class="fas fa-bed room-icon"></i>
                    <div class="room-status">
                        <span><%= statusName %></span>
                        <i class="fas fa-arrow-right"></i>
                    </div>
                </button>
            </div>
            <% } %>
        </div>
    </div>

    <!-- TAB: Por cada piso activo -->
    <% for (Floor floor : floors) {
        if (!floor.getStatus().equals("Activo")) continue;
    %>
    <div id="<%=floor.getId()%>-nivel" class="tab-pane fade show">
        <div class="row">
            <%
                List<AllInfoRoom> roomsFloor = allInfoRooms.stream()
                        .filter(room -> room.getIdFloor() == floor.getId())
                        .collect(Collectors.toList());

                if (roomsFloor.isEmpty()) {
            %>
            <div class="col-12">
                <div class="alert alert-danger text-center">
                    No hay habitaciones ocupadas en este piso.
                </div>
            </div>
            <% } else {
                for (AllInfoRoom room : roomsFloor) {
                    String colorOfStatus;
                    switch (room.getIdRoomStatus()) {
                        case 2: colorOfStatus = "occupied"; break;
                        case 3: colorOfStatus = "warning"; break;
                        case 4: colorOfStatus = "reserved"; break;
                        default: colorOfStatus = "available";
                    }
                    String statusName = room.getStatusRoom().toLowerCase();
                    statusName = statusName.substring(0, 1).toUpperCase() + statusName.substring(1);
            %>
            <div class="col-md-3">
                <button class="room-card <%= colorOfStatus %>"
                        onclick="cargarPagina('jsp/procesoSalida.jsp?id=<%= room.getIdReservation() %>')">
                    <h5><%= room.getNumberRoom() %></h5>
                    <span><%= room.getTypeRoom().toUpperCase() %></span>
                    <i class="fas fa-bed room-icon"></i>
                    <div class="room-status">
                        <span><%= statusName %></span>
                        <i class="fas fa-arrow-right"></i>
                    </div>
                </button>
            </div>
            <% } } %>
        </div>
    </div>
    <% } %>

    <% } else { %>
    <!-- Mensaje si no hay pisos activos -->
    <div class="alert alert-info text-center" role="alert">
        No hay habitaciones habilitadas. Agregue o habilite un piso desde la opción <strong>'Pisos'</strong>.
    </div>
    <% } %>

</div>
<%}%>
</body>