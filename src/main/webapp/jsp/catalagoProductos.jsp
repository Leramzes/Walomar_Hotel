<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.Product" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionProduct" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <title>Catálogo de Productos</title>

</head>

<%
  HttpSession sessionObj = request.getSession(false);
  if (sessionObj == null || sessionObj.getAttribute("usuario") == null) {
    response.sendRedirect("index.jsp"); //Mensaje: Inicia sesión primero
    return;
  }
  User usuario = (User) sessionObj.getAttribute("usuario");
  if (usuario.getEmployee().getPosition().equalsIgnoreCase("2")) {
    response.sendRedirect("inicio.jsp"); //Mensaje: No tienes privilegios
    return;
  }

  int pagina = 1;
  int pageSize = 10;

  String pageParam = request.getParameter("page");
  if (pageParam != null) {
    pagina = Integer.parseInt(pageParam);
  }

  List<Product> productsInCatalogo = GestionProduct.getAllProductsPaginated(pagina, pageSize);
  int totalProduct = GestionProduct.getAllProducts().size();
  int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
%>

<body>
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
  <h4><i class="fa-solid fa-basket-shopping me-2"></i> Catálogo de Productos</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
      <li class="breadcrumb-item"><a href="#" onclick="cargarPagina('jsp/habitaciones.jsp')">Habitaciones</a></li>
      <li class="breadcrumb-item active" aria-current="page">Catálogo de Productos</li>
    </ol>
  </nav>
</div>

<!-- Sección de Catálogo de Productos -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-lg-9 col-md-9 col-sm-12 d-flex gap-2 text-white">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarCatalogoProducto">
          <i class="fas fa-plus"></i> Agregar Producto
        </button>
      </div>
      <div class="col-3 d-flex justify-content-end align-items-center">
        <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
        <select id="estadoSelect" class="form-select  w-auto"
                onchange="Search('#nameSearch', '#estadoSelect','#tablaCatalagoProductos','#sizeProducts','filterProducServlet', 1, 10)">
          <option value="">Todos</option>
          <option value="1">Activos</option>
          <option value="0">Inactivos</option>
        </select>
      </div>
    </div>
  </div>

  <!-- Modal para agregar Catálogo de Producto -->
  <div class="modal fade" id="modalAgregarCatalogoProducto" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarCatalagoProducto">Agregar Producto al Catálogo</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formCatalogoProducto" action="productcontrol" method="post">
            <input type="hidden" id="inputAgregarCatalagoProducto">
            <input type="hidden" name="actionproduct" value="add">
            <div class="mb-3">
              <label for="nombre">Nombre</label>
              <input type="text" class="form-control" name="nameproduct" id="nombre" required>
            </div>
            <div class="mb-3">
              <label for="precioVenta">Precio Venta (S/)</label>
              <input type="number" class="form-control" name="priceproduct" id="precioVenta" min="2" step="0.01" required>
            </div>
            <div class="mb-3">
              <label for="cantidad">Cantidad (Unit.)</label>
              <input type="number" class="form-control" name="cantidadproduct" id="cantidad" min="1" step="1" required>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal para editar catalogo -->
  <div class="modal fade" id="modalEditarCatalogoProducto" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Editar Catálogo de Producto</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formEditarCatalogoProducto" action="productcontrol" method="post">
            <input type="hidden" name="actionproduct" value="update">
            <input type="hidden" name="idproduct" id="inputEditarIdProducto">
            <div class="mb-3">
              <label for="nombreEditar">Nombre</label>
              <input type="text" class="form-control" name="nameproduct" id="nombreEditar" required>
            </div>
            <div class="mb-3">
              <label for="precioVentaEditar">Precio Venta (S/)</label>
              <input type="number" class="form-control" name="priceproduct" id="precioVentaEditar"
                     min="" step="0.01" required>
            </div>
            <div class="mb-3">
              <label for="cantidad">Cantidad (Unit.)</label>
              <input type="number" class="form-control" name="cantidadproduct" id="cantidadEditar" min="1" step="1" required>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <div class="card-body mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <span class="d-none d-md-inline">Mostrando
        <input type="number" id="sizeProducts" min="1" max="999" value="<%=productsInCatalogo.size()%>"
               class="form-control d-inline-block" style="width: 5rem;" readonly> registros
      </span>
      <form>
        <div class="input-group ms-auto" style="max-width: 250px;">
          <input type="text" class="form-control" id="nameSearch" placeholder="Buscar por nombre"
                 onkeyup="Search('#nameSearch', '#estadoSelect','#tablaCatalagoProductos','#sizeProducts','filterProducServlet', 1, 10)">
          <span class="input-group-text"><i class="fas fa-search"></i></span>
        </div>
      </form>
    </div>

    <div class="table-responsive">
      <table id="tablaCatalagoProductos" class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>N°</th>
          <th>Nombre</th>
          <th>Precio Venta</th>
          <th>Cantidad en Stock</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>

          <%int count=1; for(Product product : productsInCatalogo){%>
            <tr>
              <td><%=count%></td>
              <td><%=product.getName()%></td>
              <td>S/. <%=product.getPrice()%></td>
              <td><%=product.getQuantity()%></td>
              <td class="align-middle text-center">
                <div class="d-flex justify-content-center align-items-center gap-1">
                  <button class="btn btn-warning btn-sm" id="btn-editar"
                          data-bs-toggle="modal"
                          data-bs-target="#modalEditarCatalogoProducto"
                          onclick="abrirModalEditar(<%=product.getId()%>)">
                    ✏️
                  </button>
                  <form action="productcontrol" method="post">
                    <input type="hidden" name="idproduct" value="<%=product.getId()%>">
                    <input type="hidden" name="actionproduct" value="inactive">
                    <input type="hidden" name="availability" value="<%=product.getStatus()%>">
                    <%
                      boolean disponible = product.getStatus() == 1;
                      String btnClass = disponible ? "btn-danger" : "btn-success";
                      String btnText = disponible ? "❌" : "✅";
                    %>
                    <button class="btn <%= btnClass %> btn-sm"><%= btnText %></button>
                  </form>
                </div>
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
            <a class="page-link" aria-label="Anterior" href="menu.jsp?view=catalogoProductos&page=<%= pagina - 1 %>">Anterior</a>
          </li>

          <% for (int i = 1; i <= totalPages; i++) { %>
          <li class="page-item <% if (i == pagina) { %>active<% } %>">
            <a class="page-link" aria-label="Actual" href="menu.jsp?view=catalogoProductos&page=<%= i %>"><%= i %>
            </a>
          </li>
          <% } %>

          <li class="page-item <% if (pagina == totalPages) { %>disabled<% } %>">
            <a class="page-link" aria-label="Siguiente"
               href="menu.jsp?view=catalogoProductos&page=<%= pagina + 1 %>">Siguiente</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</div>
</body>