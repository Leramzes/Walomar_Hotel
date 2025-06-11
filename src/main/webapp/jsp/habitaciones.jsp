
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page import="development.team.hoteltransylvania.Model.Floor" %>
<%@ page import="development.team.hoteltransylvania.Model.TypeRoom" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionTypeRoom" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Catálogo de Habitaciones</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>

<body>
<div class="d-flex justify-content-between align-items-center">
  <h4><i class="fa-solid fa-gears me-2"></i> Habitaciones</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Configuración</a></li>
      <li class="breadcrumb-item active" aria-current="page">Habitaciones</li>
    </ol>
  </nav>
</div>
<%
  HttpSession sessionObj = request.getSession(false);
  if (sessionObj == null || sessionObj.getAttribute("usuario") == null) {
    response.sendRedirect("index.jsp"); //Mensaje: Inicia sesión primero
    return;
  }
  User usuario = (User) sessionObj.getAttribute("usuario");
  if(usuario.getEmployee().getPosition().equalsIgnoreCase("2")){
    response.sendRedirect("inicio.jsp"); //Mensaje: No tienes privilegios
    return;
  }

  int pagina = 1;
  int pageSize = 10;

  String pageParam = request.getParameter("page");
  if (pageParam != null) {
    pagina = Integer.parseInt(pageParam);
  }

  List<Room> rooms = GestionRoom.getRoomsPaginated(pagina, pageSize);
  int totalRooms = GestionRoom.getTotalRooms();
  int totalPages = (int) Math.ceil((double) totalRooms / pageSize);
  List<Floor> floors = GestionRoom.quantityFloorsEnabled();
  List<TypeRoom> allTypes = GestionTypeRoom.getAllTypeRoomsActive();
%>


<!-- Sección de habitaciones -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-9 d-flex gap-2">
        <p>Catálogo de Habitaciones</p>
      </div>
      <div class="col-3 d-flex justify-content-end align-items-center">
        <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
        <select id="estadoSelect" class="form-select w-auto"
        onchange="Search('#nameSearch','#estadoSelect','#tablaHabitaciones','#sizeRooms','filterRoomServlet', 1, 10)">
          <option value="">Todos</option>
          <option value="libre">Libre</option>
          <option value="ocupada">Ocupada</option>
          <option value="en mantenimiento">En Mantenimiento</option>
        </select>
      </div>
    </div>

    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarHabitación">
      <i class="fas fa-plus"></i> Agregar nueva
    </button>
  </div>

  <!-- Modal para agregar habitación -->
  <div class="modal fade" id="modalAgregarHabitación" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarHabitacion">Agregar Habitación</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formHabitacion" action="roomcontroller" method="post">
            <input type="hidden" id="inputAgregarHabitacion">
            <input type="hidden" name="actionRoom" value="add">

            <div class="mb-3">
              <label for="piso">Nivel/Piso</label>
              <select class="form-select" id="piso" name="piso" required>
                <%for(Floor floor : floors){%>
                <option value="<%=floor.getId()%>"><%=floor.getName()%></option>
                <%}%>
              </select>
            </div>

            <div class="mb-3">
              <label for="tipo">Tipo de Habitación</label>
              <select class="form-select" id="tipo" name="tipo" required>
                <%for (TypeRoom typeRoom : allTypes){%>
                  <option value="<%=typeRoom.getId()%>"><%=typeRoom.getName()%></option>
                <%}%>
              </select>
            </div>

            <div class="mb-3">
              <label for="nombre">Nombre/Número de Habitación</label>
              <input type="text" class="form-control" id="nombre" name="nombre" required>
            </div>

            <div class="mb-3">
              <label for="precio">Precio</label>
              <input type="number" class="form-control" id="precio" name="precio" required>
            </div>

            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal para editar habitación -->
  <div class="modal fade" id="modalEditarHabitacion" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editarHabitacion">Editar Habitación</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formEditarHabitacion" action="roomcontroller" method="post">
            <input type="hidden" name="idroom" id="inputEditarHabitacion">
            <input type="hidden" name="actionRoom" value="update">
            <div class="mb-3">
              <label for="nombreEditar">Nombre/Número de Habitación</label>
              <input type="text" class="form-control" id="nombreEditar" name="nombredit">
            </div>
            <div class="mb-3">
              <label for="tipoeditar">Tipo de Habitación</label>
              <select class="form-select" id="tipoeditar" name="tipo" required>
                <option value="3">Simple</option>
                <option value="4">Doble</option>
                <option value="5">Matrimonial</option>
                <option value="6">Suite</option>
                <option value="7">Presidencial</option>
              </select>
            </div>
            <div class="mb-3">
              <label for="precioEditar">Precio</label>
              <input type="number" class="form-control" id="precioEditar" name="precioedit" required>
            </div>
            <div class="mb-3">
              <label for="estatusEditar">Estatus</label>
              <select class="form-select" id="estatusEditar" name="statusedit">
                <option value="libre">Libre</option>
                <option value="ocupada">Ocupada</option>
                <option value="en_mantenimiento">En Mantenimiento</option>
              </select>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <div class="card-body">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <span>Mostrando
        <input type="number" id="sizeRooms" min="1" max="999" value="<%=rooms.size()%>" class="form-control d-inline-block" style="width: 5rem;" readonly> registros
      </span>

      <div class="input-group" style="max-width: 250px;">
        <input type="text" class="form-control" id="nameSearch" placeholder="Buscar"
               onkeyup="Search('#nameSearch','#estadoSelect','#tablaHabitaciones','#sizeRooms','filterRoomServlet', 1, 10)">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
      </div>
    </div>

    <div class="table-responsive">
      <table id="tablaHabitaciones" class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>Número de Habitación</th>
          <th>Nivel / Piso</th>
          <th>Tipo</th>
          <th>Precio S/</th>
          <th>Tarifa</th>
          <th>Estatus</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <% for (Room room : rooms) { %>
        <tr>
          <td><%= room.getNumber() %></td>
          <td>Nivel <%= room.getFloor() %></td>
          <td><%= room.getTypeRoom().getName() %></td>
          <td><%= room.getPrice() %></td>
          <td>24hr.</td>
          <td><%= room.getStatusRoom().getName() %></td>
          <td class="d-flex justify-content-center gap-1">
            <input type="hidden" id="idroom" name="idroom" value="<%= room.getId() %>">
            <button class="btn btn-warning btn-sm"
                    onclick="editarRoom(<%= room.getId() %>)">
              ✏️
            </button>
            <form action="roomcontroller" method="post" id="roomInactive">
              <input type="hidden" name="idroom" value="<%= room.getId() %>">
              <input type="hidden" name="availability" value="<%=room.getAvailable()%>">
              <input type="hidden" name="actionRoom" value="disponible">
              <%
                boolean disponible = room.getAvailable() == 1;
                String btnClass = disponible ? "btn-danger" : "btn-success";
                String btnText = disponible ? "❌" : "✅";
              %>

              <button class="btn <%= btnClass %> btn-sm"><%= btnText %>
              </button>
            </form>
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>

    <div class="d-flex justify-content-end align-items-center">
      <nav aria-label="Page navigation">
        <ul class="pagination" id="pagination">
          <li class="page-item <% if (pagina == 1) { %>disabled<% } %>">
            <a class="page-link" href="menu.jsp?view=habitaciones&page=<%= pagina - 1 %>">Anterior</a>
          </li>

          <% for (int i = 1; i <= totalPages; i++) { %>
          <li class="page-item <% if (i == pagina) { %>active<% } %>">
            <a class="page-link" href="menu.jsp?view=habitaciones&page=<%= i %>"><%= i %></a>
          </li>
          <% } %>

          <li class="page-item <% if (pagina == totalPages) { %>disabled<% } %>">
            <a class="page-link" href="menu.jsp?view=habitaciones&page=<%= pagina + 1 %>">Siguiente</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>