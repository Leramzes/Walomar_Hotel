<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <title>Vender Productos</title>
</head>

<body>
<!-- Encabezado -->
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
    <h4><i class="fa-solid fa-basket-shopping me-2"></i> Proceso de Venta</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item"><a href="#" onclick="cargarPagina('jsp/habitacionesVenta.jsp')">Habitaciones</a>
            </li>
            <li class="breadcrumb-item active" aria-current="page">Procesar Venta</li>
        </ol>
    </nav>
</div>

<!-- Sección de datos -->
<div class="row mt-4">
    <div class="col-lg-6 col-md-6 col-sm-12">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos de la Habitación</strong></div>
            <div class="card-body">
                <p><strong>Nombre:</strong> 696</p>
                <p><strong>Tipo:</strong> PRESIDENCIAL</p>
                <p><strong>Costo:</strong> <span class="text-primary">S/.696</span></p>
            </div>
        </div>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-12 mt-4 mt-md-0">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos del Cliente</strong></div>
            <div class="card-body">
                <p><strong>Nombre:</strong> Gonashi 🤑</p>
                <p><strong>Documento:</strong> 69696969</p>
                <p><strong>Fecha entrada:</strong> 14-02-2025</p>
            </div>
        </div>
    </div>
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
                <button class="btn btn-success" onclick="cargarPagina('jsp/habitacionesVenta.jsp')">Terminar venta
                </button>
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
                <tbody id="tablaVenderProductos">
                <tr>
                    <td>Agua</td>
                    <td>Producto</td>
                    <td><label>
                        <input type="number" class="form-control" value="2">
                    </label></td>
                    <td>S/.15</td>
                    <td>S/.30</td>
                    <td class="align-middle text-center">
                        <div class="d-flex justify-content-center align-items-center gap-1">
                            <button class="btn btn-danger"><i class="fas fa-trash"></i></button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <p class="fw-bold mt-3 mt-sm-3">TOTAL: S/.30</p>

        <div class="d-flex flex-column flex-md-row align-items-start align-items-md-center">
            <div class="form-check me-3 mb-2 mb-md-0">
                <input class="form-check-input" type="radio" name="pago" id="pagarAhora">
                <label class="form-check-label" for="pagarAhora">Pagar Ahora</label>
            </div>

            <div class="input-group my-3 my-md-0" id="metodoPagoGroup" style="max-width: 300px;">
                <label class="input-group-text" for="metodoPago"><i class="fa-solid fa-money-bill-wave"></i></label>
                <select class="form-select" id="metodoPago" name="metodoPago" required>
                    <option value="" selected disabled>Método de Pago</option>
                    <option value="Efectivo">Efectivo</option>
                    <option value="Transferencia">Transferencia</option>
                    <option value="Tarjeta">Tarjeta</option>
                    <option value="Yape / Plin">Yape / Plin</option>
                </select>
            </div>
        </div>

        <div class="form-check mt-2">
            <input class="form-check-input" type="radio" name="pago" id="pagarDespues">
            <label class="form-check-label" for="pagarDespues">Pagar Después</label>
        </div>
    </div>
</div>
</body>