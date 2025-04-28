<%@ page import="development.team.hoteltransylvania.Business.GestionFloor" %>
<%@ page import="development.team.hoteltransylvania.Model.Floor" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Pisos / Niveles</title>
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
  List<Floor> listFloors = GestionFloor.getAllFloorsPaginated(pagina, pageSize);
  int totalFloors = GestionFloor.getTotalFloors();
  int totalPages = (int) Math.ceil((double) totalFloors / pageSize);
%>

<body>
<div class="d-flex justify-content-between align-items-center">
  <h4><i class="fa-solid fa-gears me-2"></i> Niveles / Pisos</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Configuración</a></li>
      <li class="breadcrumb-item active" aria-current="page">Pisos</li>
    </ol>
  </nav>
</div>

<!-- Sección de Niveles / Pisos -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-9 d-flex gap-2">
        <p>Catálogo de Niveles / Pisos</p>
      </div>
      <div class="col-3 d-flex justify-content-end align-items-center">
        <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
        <select id="estadoSelect" class="form-select  w-auto"
                onchange="Search('#nameTRSearch', '#estadoSelect','#tablaPisos','#sizeFloors','filterFloorServlet',1,10)">
          <option value="">Todos</option>
          <option value="Activo">Activos</option>
          <option value="Inactivo">Inactivos</option>
        </select>
      </div>
    </div>

    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarPiso">
      <i class="fas fa-plus"></i> Agregar nuevo
    </button>
  </div>

  <!-- Modal para agregar Piso -->
  <div class="modal fade" id="modalAgregarPiso" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarPiso">Agregar Piso</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <!--Form Agregar-->
          <form id="formPiso" action="floorcontroller" method="post">
            <input type="hidden" id="inputAgregarPiso">
            <input type="hidden" value="add" name="actionFloor">
            <div class="mb-3">
              <label for="nombre">Nombre</label>
              <input type="text" class="form-control" id="nombre" name="nombreFloor" required>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal para editar Piso -->
  <div class="modal fade" id="modalEditarPiso" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editarPiso">Editar Piso</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <!--Form Editar-->
          <form id="formEditarPiso" action="floorcontroller" method="post">
            <input type="hidden" name="idFloor" id="inputEditarPiso">
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
        <input type="number" min="1" max="999" id="sizeFloors" value="<%=listFloors.size()%>"
               class="form-control d-inline-block" style="width: 3rem;">registros
      </span>

      <div class="input-group" style="max-width: 250px;">
        <input type="text" class="form-control" id="nameTRSearch" placeholder="Buscar"
               onkeyup="Search('#nameTRSearch', '#estadoSelect','#tablaPisos','#sizeFloors','filterFloorServlet',1,10)">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
      </div>
    </div>

    <div class="table-responsive">
      <table id="tablaPisos" class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>N°</th>
          <th>Nombre</th>
          <th>Estatus</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody >
        <%int count=1; for(Floor floors : listFloors){%>
        <tr>
          <td><%=count%></td>
          <td><%=floors.getName()%></td>
          <td><%=floors.getStatus()%></td>
          <td class="d-flex justify-content-center gap-1">
            <button class="btn btn-warning btn-sm"
                    data-bs-toggle="modal"
                    data-bs-target="#modalEditarPiso"
                    onclick="editarFloor(<%=floors.getId()%>)">✏️</button>
            <% if (floors.getStatus().equals("Activo")) { %>
            <form action="floorcontroller" method="post">
              <input type="hidden" name="idFloor" value="<%=floors.getId()%>">
              <input type="hidden" name="actionFloor" value="inactivate">
              <button class="btn btn-danger btn-sm">❌</button>
            </form>
            <% } else { %>
            <form action="floorcontroller" method="post">
              <input type="hidden" name="idFloor" value="<%=floors.getId()%>">
              <input type="hidden" name="actionFloor" value="activate">
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
            <a class="page-link" href="menu.jsp?view=pisos&page=<%= pagina - 1 %>">Anterior</a>
          </li>

          <% for (int i = 1; i <= totalPages; i++) { %>
          <li class="page-item <% if (i == pagina) { %>active<% } %>">
            <a class="page-link" href="menu.jsp?view=pisos&page=<%= i %>"><%= i %></a>
          </li>
          <% } %>

          <li class="page-item <% if (pagina == totalPages) { %>disabled<% } %>">
            <a class="page-link" href="menu.jsp?view=pisos&page=<%= pagina + 1 %>">Siguiente</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>