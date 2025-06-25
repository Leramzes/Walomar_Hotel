<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionTypeRoom" %>
<%@ page import="development.team.hoteltransylvania.Model.*" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionMetodosPago" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Métodos de Pago</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>

<body>
<div class="d-flex justify-content-between align-items-center">
  <h4><i class="fa-solid fa-credit-card me-2"></i> Métodos de pago</h4>

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


  List<PaymentMethod> paymentMethods = GestionMetodosPago.getAllMethodPayments();
  int totalMethods = paymentMethods.size();
%>


<!-- Sección de metodos de pago -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-9 d-flex gap-2">
        <p>Métodos de Pago</p>
      </div>
      <div class="col-3 d-flex justify-content-end align-items-center">
        <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
        <select id="estadoSelect" class="form-select w-auto"
                onchange="Search('#nameSearch','#estadoSelect','#tablaMetodos','#sizeMetodos','filterMetodoServlet', 1, 10)">
          <option value="">Todos</option>
          <option value="1">Activo</option>
          <option value="0">Inactivo</option>
        </select>
      </div>
    </div>

    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarMetodo">
      <i class="fas fa-plus"></i> Agregar nueva
    </button>
  </div>

  <!-- Modal para agregar método -->
  <div class="modal fade" id="modalAgregarMetodo" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarMetodo">Agregar Método de Pago</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formMetodo" action="metodoController" method="post">
            <input type="hidden" id="inputAgregarMetodo">
            <input type="hidden" name="actionMetodo" value="add">

            <div class="mb-3">
              <label for="metodo">Método de Pago</label>
                <input type="text" class="form-control" id="metodo" name="metodo" required>
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
        <input type="number" id="sizeMetodos" min="1" max="999" value="<%=totalMethods%>" class="form-control d-inline-block" style="width: 5rem;" readonly> registros
      </span>

      <div class="input-group" style="max-width: 250px;">
        <input type="text" class="form-control" id="nameSearch" placeholder="Buscar"
               onkeyup="Search('#nameSearch','#estadoSelect','#tablaMetodos','#sizeMetodos','filterMetodoServlet', 1, 10)">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
      </div>
    </div>

    <div class="table-responsive">
      <table id="tablaMetodos" class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>N°</th>
          <th>Método de Pago</th>
          <th>Estatus</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <% int count=1; for (PaymentMethod paymentMethod : paymentMethods) { %>
        <tr>
          <td><%= count %></td>
          <td><%= paymentMethod.getNameMethod() %></td>
          <td><%= paymentMethod.getStatus()==1?"Activo":"Inactivo" %></td>
          <td class="d-flex justify-content-center gap-1">
            <input type="hidden" id="idroom" name="idroom" value="<%= paymentMethod.getId() %>">
            <form action="metodoController" method="post" id="methodInactive">
              <input type="hidden" name="idMethod" value="<%= paymentMethod.getId() %>">
              <input type="hidden" name="availability" value="<%=paymentMethod.getStatus()%>">
              <input type="hidden" name="actionMetodo" value="disponible">
              <%
                boolean disponible = paymentMethod.getStatus() == 1;
                String btnClass = disponible ? "btn-danger" : "btn-success";
                String btnText = disponible ? "❌" : "✅";
              %>

              <button class="btn <%= btnClass %> btn-sm"><%= btnText %>
              </button>
            </form>
          </td>
        </tr>
        <% count++;} %>
        </tbody>
      </table>
    </div>

  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>