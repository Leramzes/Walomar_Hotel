<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <title>Habitaciones para realizar servicio</title>
</head>

<body>
<div class="d-flex justify-content-between align-items-center">
  <h4><i class="fa-solid fa-basket-shopping me-2"></i> Habitaciones para realizar servicio</h4>

  <nav aria-label="breadcrumb">
    <ol class="breadcrumb mb-0">
      <li class="breadcrumb-item"><a href="#" data-pagina="ventaDirecta" onclick="cargarPagina('jsp/ventaDirecta.jsp')">¿Venta Directa?</a></li>
    </ol>
  </nav>
</div>

<ul class="nav nav-tabs mt-4">
  <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab" data-bs-target="#todos">Todos</button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#primer-nivel">Primer Nivel</button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#segundo-nivel">Segundo Nivel</button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#tercer-nivel">Tercer Nivel</button></li>
</ul>

<div class="tab-content mt-3">
  <div id="todos" class="tab-pane fade show active">
    <div class="row">
      <div class="col-md-3">
        <button class="room-card occupied" onclick="cargarPagina('jsp/venderServicios.jsp')">
          <h5>696</h5>
          <span>PRESIDENCIAL</span>
          <i class="fas fa-bed room-icon"></i>
          <div class="room-status">
            <span>Ocupada</span>
            <i class="fas fa-arrow-right"></i>
          </div>
        </button>
      </div>
      <div class="col-md-3">
        <button class="room-card occupied" onclick="handleClick(105)">
          <h5>105</h5>
          <span>TRIPLE</span>
          <i class="fas fa-bed room-icon"></i>
          <div class="room-status">
            <span>Ocupada</span>
            <i class="fas fa-arrow-right"></i>
          </div>
        </button>
      </div>
    </div>
  </div>
</div>
</body>