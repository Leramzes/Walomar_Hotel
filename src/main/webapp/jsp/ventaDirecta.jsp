<%@ page import="development.team.hoteltransylvania.Model.Product" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionProduct" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="development.team.hoteltransylvania.Model.PaymentMethod" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionMetodosPago" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <title>Venta Directa</title>
</head>
<%
    List<Product> productsInList = GestionProduct.getAllProducts().
            stream().filter(p -> p.getStatus() == 1 && p.getQuantity()>0).collect(Collectors.toUnmodifiableList());
    List<PaymentMethod> paymentMethodsActive = GestionMetodosPago.getAllMethodPayments()
            .stream().filter(method -> method.getStatus() == 1).collect(Collectors.toUnmodifiableList());
%>
<body>
<div class="d-flex justify-content-between align-items-center">
    <h4><i class="fa-solid fa-basket-shopping me-2"></i> Proceso de Venta</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item active" aria-current="page">Procesar Venta Directa</li>
        </ol>
    </nav>
</div>

<!-- Sección de productos -->
<div class="card mt-4">
    <div class="card-header text-white">
        <div class="row align-items-center">
            <div class="col-9 d-flex gap-2">

                <select class="form-select" id="selectProducto">
                    <option selected>Open this select menu</option>
                    <%for (Product product : productsInList) {%>
                    <option value="<%=product.getId()%>">Nombre: <%=product.getName()%> |
                        Precio: <%=product.getPrice()%>
                    </option>
                    <%}%>
                </select>

                <button class="btn btn-primary" onclick="agregarProducto('#detalleVentaDirecta')">Agregar</button>
            </div>

        </div>
    </div>

    <form id="formVentaDirecta" action="ventacontroller" method="post">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered align-middle" id="detalleVentaDirecta">
                    <thead class="table-warning">
                    <tr>
                        <th>Nombre</th>
                        <th>Cantidad</th>
                        <th>Precio Unit.</th>
                        <th>Precio Total</th>
                        <th>Eliminar</th>
                    </tr>
                    </thead>
                    <tbody id="tablaVentaDirecta">
                    <tr>
                        <td colspan="5" class="text-center text-muted">Agrega productos</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div id="errorMaxStock" class="alert alert-danger mt-3" role="alert" style="display: none;">
            </div>

            <p id="totalGeneral" class="fw-bold mt-3 mt-sm-3">TOTAL: S/.0</p>

            <div class="my-3 my-md-0" style="max-width: 100%;">
                <div class="d-flex justify-content-between align-items-center flex-column flex-md-row gap-2">

                    <!-- Select de métodos de pago -->
                    <div class="input-group" style="max-width: 300px;">
                        <label class="input-group-text" for="metodoPago">
                            <i class="fa-solid fa-money-bill-wave"></i>
                        </label>
                        <select class="form-select" id="metodoPago" name="metodoPago" required>
                            <option value="" selected disabled>Método de Pago</option>
                            <% for (PaymentMethod paymentMethod : paymentMethodsActive) { %>
                            <option value="<%= paymentMethod.getId() %>">
                                <%= paymentMethod.getNameMethod() %>
                            </option>
                            <% } %>
                        </select>
                    </div>

                    <!-- Botón de terminar venta -->
                    <div>
                        <input type="hidden" name="actionVenta" value="directa">
                        <button class="btn btn-success" onclick="validacionVenta()">
                            Terminar venta
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/script.js"></script>
</body>