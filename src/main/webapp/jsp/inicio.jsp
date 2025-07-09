<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page import="development.team.hoteltransylvania.DTO.TableReservationDTO" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionReservation" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <title>Inicio</title>
</head>
<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("usuario") == null) {
        response.sendRedirect("index.jsp"); //Mensaje: Inicia sesión primero
        return;
    }
    User usuario = (User) sessionObj.getAttribute("usuario");
    int rolUser = Integer.parseInt(usuario.getEmployee().getPosition());

    List<TableReservationDTO> reservasOcupadas = GestionReservation.getAllReservations().stream().
            filter(ocupada -> ocupada.getReservationStatus().equals("Ocupada")).collect(Collectors.toUnmodifiableList());
    List<Room> rooms = GestionRoom.getAllRooms();
    int quantityRooms = rooms.size();
    int quantityRoomsFree = rooms.stream().filter(stado -> stado.getStatusRoom().getValue() == 1).collect(Collectors.toUnmodifiableList()).size();
    int quantityRoomsBusy = rooms.stream().filter(stado -> stado.getStatusRoom().getValue() == 2).collect(Collectors.toUnmodifiableList()).size();
%>
<body>
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
    <h4><i class="fa-solid fa-house me-2"></i> Inicio</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="#" onclick="cargarPagina('jsp/inicio.jsp')">Inicio</a></li>
        </ol>
    </nav>
</div>

<!-- Sección de habitaciones -->
<div class="row custom-row-rooms">
    <div class="col-lg-4 col-md-4 col-sm-12">
        <div class="small-box bg-info">
            <div class="inner">
                <h3><%=quantityRooms%>
                </h3>
                <p>Total Habitaciones</p>
            </div>
            <div class="icon">
                <i class="fas fa-bed room-icon"></i>
            </div>
            <%if (rolUser == 1) {%>
            <a onclick="cargarPagina('jsp/habitaciones.jsp')" class="small-box-footer">Mas info <i
                    class="fas fa-arrow-circle-right"></i></a>
            <%}%>
        </div>
    </div>

    <div class="col-lg-4 col-md-4 col-sm-12">
        <div class="small-box bg-success">
            <div class="inner">
                <h3><%=quantityRoomsFree%>
                </h3>
                <p>Habitaciones Libres</p>
            </div>
            <div class="icon">
                <i class="fas fa-bed room-icon"></i>
            </div>
            <%if (rolUser == 1) {%>
            <a onclick="cargarPagina('jsp/habitaciones.jsp')" class="small-box-footer">Mas info <i
                    class="fas fa-arrow-circle-right"></i></a>
            <%}%>
        </div>
    </div>

    <div class="col-lg-4 col-md-4 col-sm-12">
        <div class="small-box bg-danger">
            <div class="inner">
                <h3><%=quantityRoomsBusy%>
                </h3>
                <p>Habitaciones Ocupadas</p>
            </div>
            <div class="icon">
                <i class="fas fa-bed room-icon"></i>
            </div>
            <%if (rolUser == 1) {%>
            <a onclick="cargarPagina('jsp/habitaciones.jsp')" class="small-box-footer">Mas info <i
                    class="fas fa-arrow-circle-right"></i></a>
            <%}%>
        </div>
    </div>

</div>

<!-- Sección de estadísticas y habitaciones ocupadas -->
<div class="row custom-row-statistics-rooms">

    <%
        String modClassAdmin1 = "col-lg-7 col-md-7 col-sm-12";
        String modClassAdmin2 = "col-lg-5 col-md-5 col-sm-12";
        if (rolUser == 2) {
            modClassAdmin1 = "d-none";
            modClassAdmin2 = "col-lg-12 col-md-12 col-sm-12";
        }
    %>
    <!-- Tarjeta de Estadísticas -->
    <div class="<%=modClassAdmin1%>">
        <div class="card mt-4">
            <div class="card-header text-white d-flex align-items-center">
                <p class="mb-0"><i class="fa-solid fa-chart-area me-2"></i> Estadísticas</p>
            </div>
            <div class="card-body">
                <!-- Filtro para seleccionar el rango de fechas por año-->
                <div class="mb-3">
                    <label for="filtroAnio" class="form-label">Seleccionar Año:</label>
                    <select id="filtroAnio" class="form-select">
                        <option value="2023">2023</option>
                        <option value="2024">2024</option>
                        <option value="2025" selected>2025</option>
                    </select>
                </div>

                <!-- Gráfica de barras -->
                <div style="height: auto;">
                    <canvas id="graficaIngresos"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Tarjeta de Lista de Habitaciones Ocupadas -->
    <div class="<%=modClassAdmin2%>">
        <div class="card mt-4">
            <div class="card-header text-white d-flex align-items-center" style="background: #0e2238;">
                <p class="mb-0"><i class="fa-regular fa-clock me-2"></i> Lista de Habitaciones Ocupadas</p>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered align-middle">
                        <thead class="table-warning">
                        <tr>
                            <th>N°</th>
                            <th>Cliente</th>
                            <th>Habitación</th>
                            <th>Tipo</th>
                            <th>Ingreso</th>
                        </tr>
                        </thead>
                        <tbody id="tablaHabitacionesOcupadas">
                        <%
                            int count = 1;
                            for (TableReservationDTO reservationDTO : reservasOcupadas) {%>
                        <tr>
                            <td><%=count%></td>
                            <td><%=reservationDTO.getClientName()%></td>
                            <td><%=reservationDTO.getNumberRoom()%></td>
                            <td><%=reservationDTO.getRoomType()%></td>
                            <td><%=reservationDTO.getFecha_ingreso()%></td>
                        </tr>
                        <%count++;}%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

</body>