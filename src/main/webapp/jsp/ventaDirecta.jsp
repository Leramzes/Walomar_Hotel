<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <title>Venta Directa</title>
</head>

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
        <input type="text" class="form-control" placeholder="Buscar producto">

        <select class="form-select">
          <option selected>Open this select menu</option>
          <option value="1">One</option>
          <option value="2">Two</option>
          <option value="3">Three</option>
        </select>

        <button class="btn btn-primary">Agregar</button>
      </div>

      <div class="col-3 text-end">
        <button class="btn btn-success" onclick="cargarPagina('jsp/habitacionesVenta.jsp')" >Terminar venta</button>
      </div>
    </div>
  </div>

  <div class="card-body">
    <div class="table-responsive">
      <table class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>Nombre</th>
          <th>Tipo</th>
          <th>Cantidad</th>
          <th>Precio Unit.</th>
          <th>Precio Total</th>
          <th>Eliminar</th>
        </tr>
        </thead>
        <tbody id="tablaVentaDirecta">
        <tr>
          <td>Agua</td>
          <td>Producto</td>
          <td><label>
            <input type="number" class="form-control" value="2">
          </label></td>
          <td>S/.15</td>
          <td>S/.30</td>
          <td class="text-center">
            <button class="btn btn-danger"><i class="fas fa-trash"></i></button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <p class="fw-bold">TOTAL: S/.30</p>

    <div class="form-check">
      <input class="form-check-input" type="radio" name="pago" id="pagarAhora">
      <label class="form-check-label" for="pagarAhora">Pagar Ahora</label>
    </div>

    <div class="form-check">
      <input class="form-check-input" type="radio" name="pago" id="pagarDespues" checked>
      <label class="form-check-label" for="pagarDespues">Pagar Después</label>
    </div>

  </div>
</div>

</body>