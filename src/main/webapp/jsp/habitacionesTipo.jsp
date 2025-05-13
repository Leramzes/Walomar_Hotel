<%@ page import="development.team.hoteltransylvania.Business.GestionTypeRoom" %>
<%@ page import="development.team.hoteltransylvania.Model.TypeRoom" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tipo de Habitaciones</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

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

  List<TypeRoom> listTypeRooms = GestionTypeRoom.getAllTypeRoomsPaginated(pagina, pageSize);
  int totalRooms = GestionTypeRoom.getTotalTypeRooms();
  int totalPages = (int) Math.ceil((double) totalRooms / pageSize);
%>

<body>
<div class="d-flex justify-content-between align-items-center">
  <h4><i class="fa-solid fa-gears me-2"></i> Tipo de Habitaciones</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Configuración</a></li>
      <li class="breadcrumb-item active" aria-current="page">Tipo de Habitaciones</li>
    </ol>
  </nav>
</div>

<!-- Sección de Tipo de Habitaciones -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-9 d-flex gap-2">
        <p>Catálogo de Tipo de Habitaciones</p>
      </div>
      <div class="col-3 d-flex justify-content-end align-items-center">
        <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
        <select id="estadoSelect" class="form-select  w-auto"
                onchange="Search('#nameTRSearch', '#estadoSelect','#tablaHabitacionesTipos','#sizeTypeRooms','filterTypeRoomServlet',1,10)">
          <option value="">Todos</option>
          <option value="Activo">Activos</option>
          <option value="Inactivo">Inactivos</option>
        </select>
      </div>
    </div>

    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarTipo">
      <i class="fas fa-plus"></i> Agregar nuevo
    </button>
  </div>

  <!-- Modal para agregar Tipo -->
  <div class="modal fade" id="modalAgregarTipo" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarTipo">Agregar Tipo de Habitacion</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <!--Form Agregar-->
          <form id="formTipo" action="typeroomcontroller" method="post">
            <input type="hidden" id="inputAgregarTipo">
            <input type="hidden" value="add" name="actionTypeRoom">
            <div class="mb-3">
              <label for="nombre">Nombre</label>
              <input type="text" class="form-control" id="nombre" name="nombreType" required>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal para editar Tipo -->
  <div class="modal fade" id="modalEditarTipo" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editarTipo">Editar Tipo de Habitacion</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <!--Form Editar-->
          <form id="formEditarTipo" action="typeroomcontroller" method="post">
            <input type="hidden" name="idTypeRoom" id="inputEditarTipoHabitacion">
            <div class="mb-3">
              <label for="nombreEditar">Nombre</label>
              <input type="text" class="form-control" id="nombreEditar" name="nombreEditar" required>
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
        <input type="number" min="1" max="999" id="sizeTypeRooms" value="<%=listTypeRooms.size()%>"
               class="form-control d-inline-block" style="width: 5rem;" readonly> registros
      </span>

      <div class="input-group" style="max-width: 250px;">
        <input type="text" class="form-control" id="nameTRSearch" placeholder="Buscar por nombre"
               onkeyup="Search('#nameTRSearch', '#estadoSelect','#tablaHabitacionesTipos','#sizeTypeRooms','filterTypeRoomServlet',1,10)">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
      </div>
    </div>

    <div class="table-responsive">
      <table id="tablaHabitacionesTipos" class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>N°</th>
          <th>Nombre</th>
          <th>Estatus</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%int count=1; for(TypeRoom typeRoom : listTypeRooms){%>
          <tr>
            <td><%=count%></td>
            <td><%=typeRoom.getName()%></td>
            <td><%=typeRoom.getStatus()%></td>
            <td class="d-flex justify-content-center gap-1">
              <button class="btn btn-warning btn-sm"
                      data-bs-toggle="modal"
                      data-bs-target="#modalEditarTipo"
                      onclick="editarTypeRoom(<%=typeRoom.getId()%>)">✏️</button>
              <% if (typeRoom.getStatus().equals("Activo")) { %>
              <form action="typeroomcontroller" method="post">
                <input type="hidden" name="idType" value="<%=typeRoom.getId()%>">
                <input type="hidden" name="actionTypeRoom" value="inactivate">
                <button class="btn btn-danger btn-sm">❌</button>
              </form>
              <% } else { %>
              <form action="typeroomcontroller" method="post">
                <input type="hidden" name="idType" value="<%=typeRoom.getId()%>">
                <input type="hidden" name="actionTypeRoom" value="activate">
                <button class="btn btn-success btn-sm">✅</button>
              </form>
              <% } %>
            </td>
          </tr>
        <%count++;}%>
        </tbody>
      </table>
    </div>

    <div class="d-flex justify-content-end align-items-center">
      <nav aria-label="Page navigation example">
        <ul class="pagination mb-0" id="pagination">
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
