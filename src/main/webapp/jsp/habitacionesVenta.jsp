<%@ page import="development.team.hoteltransylvania.Model.Floor" %>
<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <title>Habitaciones para realizar ventas</title>
</head>
<%
    List<Room> rooms = GestionRoom.getAllRoomsOcupied();
    List<Floor> floors = GestionRoom.quantityFloorsEnabled().stream()
            .sorted(Comparator.comparing(Floor::getId))
            .toList();
%>
<body>
<div class="d-flex justify-content-between align-items-center">
    <h4><i class="fa-solid fa-basket-shopping me-2"></i> Habitaciones para realizar ventas</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="#" data-pagina="ventaDirecta"
                                           onclick="cargarPagina('jsp/ventaDirecta.jsp')">¿Venta Directa?</a></li>
        </ol>
    </nav>
</div>

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
    <!-- Aquí renderiza todo el código actual de las habitaciones y los pisos -->
    <div id="todos" class="tab-pane fade show active">
        <div class="row">
            <%
                for (Room room : rooms) {
                    String colorOfStatus;
                    switch (room.getStatusRoom().getValue()) {
                        case 2:
                            colorOfStatus = "occupied";
                            break;
                        case 3:
                            colorOfStatus = "warning";
                            break;
                        case 4:
                            colorOfStatus = "reserved";
                            break;
                        default:
                            colorOfStatus = "available";
                    }
                    // Convertir el nombre del estado en tipo oración
                    String statusName = room.getStatusRoom().getName().toLowerCase();
                    statusName = statusName.substring(0, 1).toUpperCase() + statusName.substring(1);
            %>
            <div class="col-md-3">
                <button class="room-card <%= colorOfStatus %>"
                        onclick="cargarPagina('jsp/venderProductos.jsp?id=<%= room.getId() %>')">
                    <h5><%= room.getNumber() %>
                    </h5>
                    <span><%= room.getTypeRoom().getName().toUpperCase() %></span>
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

    <% for (Floor floor : floors) {
        if (!floor.getStatus().equals("Activo")) continue; // Solo mostrar si está activo
    %>

    <div id="<%=floor.getId()%>-nivel" class="tab-pane fade show">
        <div class="row">
            <%
                List<Room> roomsFloor = rooms.stream()
                        .filter(room -> room.getFloor() == floor.getId())
                        .collect(Collectors.toList());
                for (Room room : roomsFloor) {
                    String colorOfStatus;
                    switch (room.getStatusRoom().getValue()) {
                        case 2:
                            colorOfStatus = "occupied";
                            break;
                        case 3:
                            colorOfStatus = "warning";
                            break;
                        case 4:
                            colorOfStatus = "reserved";
                            break;
                        default:
                            colorOfStatus = "available";
                    }
                    // Convertir el nombre del estado en tipo oración
                    String statusName = room.getStatusRoom().getName().toLowerCase();
                    statusName = statusName.substring(0, 1).toUpperCase() + statusName.substring(1);
            %>
            <div class="col-md-3">
                <button class="room-card <%= colorOfStatus %>"
                        onclick="cargarPagina('jsp/venderProductos.jsp?id=<%= room.getId() %>')">
                    <h5><%= room.getNumber() %>
                    </h5>
                    <span><%= room.getTypeRoom().getName().toUpperCase() %></span>
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
    <% } %>

    <% } else { %>
    <!-- Mensaje si no hay pisos activos -->
    <div class="alert alert-info text-center" role="alert">
        No hay habitaciones habilitadas. Agregue o habilite un piso desde la opción <strong>'Pisos'</strong>.
    </div>
    <% } %>

</div>

</body>