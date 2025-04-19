<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Categoría de Habitaciones</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="d-flex justify-content-between align-items-center">
  <h4><i class="fa-solid fa-gears me-2"></i> Categorías</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Configuración</a></li>
      <li class="breadcrumb-item active" aria-current="page">Categorías</li>
    </ol>
  </nav>
</div>

<!-- Sección de Categorías -->
<div class="card mt-4">
  <div class="card-header">
    <div class="row align-items-center">
      <div class="col-9 d-flex gap-2">
        <p>Catálogo de Categorías de Habitaciones</p>
      </div>
      <div class="col-3 d-flex justify-content-end align-items-center">
        <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
        <select id="estadoSelect" class="form-select  w-auto">
          <option value="activos">Activos</option>
          <option value="inactivos">Inactivos</option>
        </select>
        <script>
          document.getElementById("estadoSelect").addEventListener("change", function() {
            console.log("Estado seleccionado:", this.value);
          });
        </script>
      </div>
    </div>

    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarCategoria">
      <i class="fas fa-plus"></i> Agregar nueva
    </button>
  </div>

  <!-- Modal para agregar Categoria -->
  <div class="modal fade" id="modalAgregarCategoria" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="agregarCategoria">Agregar Categoría</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formCategoria">
            <input type="hidden" id="inputAgregarCategoria">
            <div class="mb-3">
              <label for="nombre">Nombre</label>
              <input type="text" class="form-control" id="nombre" required>
            </div>
            <button type="button" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal para editar Categoria -->
  <div class="modal fade" id="modalEditarCategoria" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editarCategoria">Editar Categoría</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form id="formEditarCategoria">
            <input type="hidden" id="inputEditarCategoria">
            <div class="mb-3">
              <label for="nombreEditar">Nombre</label>
              <input type="text" class="form-control" id="nombreEditar" required>
            </div>
            <div class="mb-3">
              <label for="estatusEditar">Estatus</label>
              <select class="form-select" id="estatusEditar">
                <option value="Activo">Activo</option>
                <option value="Inactivo">Inactivo</option>
              </select>
            </div>
            <button type="button" class="btn btn-success">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <div class="card-body">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <span>Mostrando
        <input type="number" min="1" max="999" value="1" class="form-control d-inline-block" style="width: 3rem;"> registros
      </span>

      <div class="input-group" style="max-width: 250px;">
        <input type="text" class="form-control" id="nameSearch" placeholder="Buscar">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
          <th>N°</th>
          <th>Nombre</th>
          <th>Estatus</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody id="tablaHabitacionesCategorias">
        <tr>
          <td>1</td>
          <td>Presidencial</td>
          <td>Activo</td>
          <td class="d-flex justify-content-center gap-1">
            <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#modalEditarCategoria">✏️</button>
            <button class="btn btn-danger btn-sm">❌</button>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
