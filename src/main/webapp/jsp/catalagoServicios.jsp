<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <title>Catálogo de Servicios</title>

</head>

<body>
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
  <h4><i class="fa-solid fa-basket-shopping me-2"></i> Catálogo de Servicios</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
      <li class="breadcrumb-item"><a href="#" onclick="cargarPagina('jsp/habitaciones.jsp')">Habitaciones</a></li>
      <li class="breadcrumb-item active" aria-current="page">Catálogo de Servicios</li>
    </ol>
  </nav>
</div>

<!-- Sección de Catálogo de Servicios -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-lg-9 col-md-9 col-sm-12 d-flex gap-2 text-white">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarCatalogoServicio">
          <i class="fas fa-plus"></i> Agregar Servicio
        </button>
      </div>
    </div>
  </div>

  <!-- Modal para agregar Catálogo de Servicios -->
  <div class="modal fade" id="modalAgregarCatalogoServicio" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarCatalagoServicio">Agregar Catálogo de Servicio</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formCatalogoProducto" action="" method="post">
            <input type="hidden" id="inputAgregarCatalagoProducto">
            <input type="hidden" name="" value="add">
            <div class="mb-3">
              <label for="tipo">Tipo</label>
              <select class="form-select" id="tipo" required>
                <option value="#">Producto</option>
                <option value="#">Servicio</option>
                <option value="#">Otro</option>
              </select>
            </div>
            <div class="mb-3">
              <label for="nombre">Nombre</label>
              <input type="text" class="form-control" name="" id="nombre" required>
            </div>
            <div class="mb-3">
              <label for="precioServicio">Precio Servicio</label>
              <input type="number" class="form-control" name="" id="precioServicio" min="2" step="0.01" required>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal para editar catalogo -->
  <div class="modal fade" id="modalEditarCatalogoServicio" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Editar Catálogo de <Ser></Ser></h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formEditarCatalogoServicio" action="" method="post">
            <input type="hidden" name="" value="update">
            <input type="hidden" name="" id="inputEditarIdServicio">

            <div class="mb-3">
              <label for="tipoEditar">Tipo</label>
              <select class="form-select" name="" id="tipoEditar" required>
                <option value="Producto">Producto</option>
                <option value="Servicio">Servicio</option>
                <option value="Otro">Otro</option>
              </select>
            </div>
            <div class="mb-3">
              <label for="nombreEditar">Nombre</label>
              <input type="text" class="form-control" name="" id="nombreEditar" required>
            </div>
            <div class="mb-3">
              <label for="precioServicioEditar">Precio <Ser></Ser></label>
              <input type="number" class="form-control" name="" id="precioServicioEditar" min="2" step="0.01" required>
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
        <input type="number" id="sizeProducts" min="1" max="999" value="" class="form-control d-inline-block" style="width: 4rem;"> registros
      </span>
      <form>
        <div class="input-group ms-auto" style="max-width: 250px;">
          <input type="text" class="form-control" id="nameSearch" placeholder="Buscar" onkeyup="buscar()">
          <span class="input-group-text"><i class="fas fa-search"></i></span>
        </div>
      </form>
    </div>

    <div class="table-responsive">
      <table class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>N°</th>
          <th>Tipo</th>
          <th>Nombre</th>
          <th>Precio Venta</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody id="tablaCatalagoServicio">

        <tr>
          <td>1</td>
          <td>Servicio</td>
          <td>Lavandería</td>
          <td>S/. 5</td>
          <td class="align-middle text-center">
            <div class="d-flex justify-content-center align-items-center gap-1">
              <button class="btn btn-warning btn-sm" id="btn-editar"
                      data-bs-toggle="modal"
                      data-bs-target="#modalEditarCatalogoProducto"
                      onclick="abrirModalEditar()">
                ✏️
              </button>
              <form action="" method="post">
                <input type="hidden" name="" value="">
                <input type="hidden" name="" value="delete">
                <button class="btn btn-danger btn-sm">❌</button>
              </form>
            </div>
          </td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="d-flex justify-content-end align-items-center">
      <nav aria-label="Page navigation example">
        <ul class="pagination mb-0">
          <li class="page-item"><a class="page-link" href="#">Anterior</a></li>
          <li class="page-item"><a class="page-link" href="#">1</a></li>
          <li class="page-item"><a class="page-link" href="#">Siguiente</a></li>
        </ul>
      </nav>
    </div>
  </div>
</div>
</body>