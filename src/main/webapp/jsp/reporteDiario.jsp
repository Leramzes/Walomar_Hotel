<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte Diario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="d-flex justify-content-between align-items-center">
    <h4><i class="fa-solid fa-sheet-plastic me-2"></i> Reporte Diario</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item active" aria-current="page">Reporte Diario</li>
        </ol>
    </nav>
</div>

<!-- Filtros -->
<div class="row my-3">
    <div class="col-md-2">
        <label for="fecha" class="form-label"><strong>Fecha</strong></label>
        <input type="date" id="fecha" class="form-control">
    </div>
    <div class="col-md-2">
        <label for="responsable" class="form-label"><strong>Responsable</strong></label>
        <select id="responsable" class="form-select">
            <option value="todos">Todos</option>
            <option value="1">Responsable 1</option>
            <option value="2">Responsable 2</option>
        </select>
    </div>
</div>

<!-- Pestañas -->
<ul class="nav nav-tabs mt-4">
    <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab" data-bs-target="#alquiler">Tabla alquiler</button></li>
    <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#habitacion-venta">Tabla servicio a la habitación y venta directa</button></li>
    <li class="nav-item ms-auto">
        <button class="btn btn-success">
            <i class="fa-solid fa-file-export"></i> Exportar
        </button>
    </li>
</ul>

<!-- Resumen de recepción -->
<div class="d-flex justify-content-between text-center px-5 mt-4">
    <div>
        <h5>S/.500</h5>
        <h5>TOTAL RECEPCIÓN</h5>
    </div>
    <div>
        <h5>S/.500</h5>
        <h5>TOTAL RECEPCIÓN</h5>
    </div>
    <div>
        <h5>S/.500</h5>
        <h5>TOTAL RECEPCIÓN</h5>
    </div>
</div>

<div class="d-flex justify-content-between align-items-center mb-3 mt-4">
    <label for="registros">Mostrando
        <input id="registros" type="number" min="1" max="999" value="1" class="form-control d-inline-block text-center ms-1 me-1" style="width: 4rem;">
        registros
    </label>

    <div class="input-group" style="max-width: 250px;">
        <input type="text" class="form-control" id="nameSearch" placeholder="Buscar">
        <span class="input-group-text"><i class="fas fa-search"></i></span>
    </div>
</div>


<!-- Modal para ver Detalle -->
<div class="modal fade" id="modalVerDetalle" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="detalleAlquiler">Detalles del Alquiler</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <h5>Datos del Cliente:</h5>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre Completo</label>
                    <input type="text" class="form-control" id="nombre" readonly>
                </div>
                <div class="mb-3">
                    <label for="tipoDocumento" class="form-label">Tipo de Documento</label>
                    <select class="form-select" id="tipoDocumento" disabled>
                        <option value="DNI">DNI</option>
                        <option value="PASAPORTE">PASAPORTE</option>
                        <option value="RUC">RUC</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="documento" class="form-label">Documento</label>
                    <input type="text" class="form-control" id="documento" readonly>
                </div>
                <div class="mb-3">
                    <label for="nit" class="form-label">NIT</label>
                    <input type="text" class="form-control" id="nit" readonly>
                </div>
                <div class="mb-3">
                    <label for="nombreFacturar" class="form-label">Nombre a Facturar</label>
                    <input type="text" class="form-control" id="nombreFacturar" readonly>
                </div>
                <div class="mb-3">
                    <label for="correo" class="form-label">Correo</label>
                    <input type="email" class="form-control" id="correo" readonly>
                </div>
                <div class="mb-3">
                    <label for="telefono" class="form-label">Teléfono</label>
                    <input type="tel" class="form-control" id="telefono" readonly>
                </div>

                <hr>

                <h5>Datos del Alojamiento:</h5>
                <div class="mb-3">
                    <label for="tipoAtencion" class="form-label">Tipo de Atención</label>
                    <select class="form-select" id="tipoAtencion" disabled>
                        <option value="recepción">Recepción</option>
                        <option value="reservación">Reservación</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="tipoHabitacion" class="form-label">Tipo de Habitación</label>
                    <select class="form-select" id="tipoHabitacion" disabled>
                        <option value="simple">Simple</option>
                        <option value="doble">Doble</option>
                        <option value="presidencial">Presidencial</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="habitacion" class="form-label">Habitación</label>
                    <select class="form-select" id="habitacion" disabled>
                        <option value="696">696</option>
                        <option value="600">600</option>
                        <option value="100">100</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="tarifa" class="form-label">Tarifa</label>
                    <select class="form-select" id="tarifa" disabled>
                        <option value="12hr">12hr</option>
                        <option value="24hr">24hr</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="descuento" class="form-label">Descuento</label>
                    <input type="text" class="form-control" id="descuento" readonly>
                </div>
                <div class="mb-3">
                    <label for="precioPagar" class="form-label">Precio a Pagar</label>
                    <input type="text" class="form-control" id="precioPagar" readonly>
                </div>
                <div class="mb-3">
                    <label for="creacion" class="form-label">Creación</label>
                    <input type="date" class="form-control" id="creacion" readonly>
                </div>
                <div class="mb-3">
                    <label for="ingreso" class="form-label">Ingreso</label>
                    <input type="date" class="form-control" id="ingreso" readonly>
                </div>
                <div class="mb-3">
                    <label for="salidaCalculada" class="form-label">Salida Calculada</label>
                    <input type="date" class="form-control" id="salidaCalculada" readonly>
                </div>
                <div class="mb-3">
                    <label for="salida" class="form-label">Salida</label>
                    <input type="date" class="form-control" id="salida" readonly>
                </div>
                <div class="mb-3">
                    <label for="tiempoRebasado" class="form-label">Tiempo Rebasado</label>
                    <input type="text" class="form-control" id="tiempoRebasado" readonly>
                </div>
                <div class="mb-3">
                    <label for="observaciones" class="form-label">Observaciones</label>
                    <input type="text" class="form-control" id="observaciones" readonly>
                </div>

                <hr>

                <h5>Costos:</h5>
                <div class="mb-3">
                    <label for="cobroExtra" class="form-label">Cobro extra</label>
                    <input type="number" class="form-control" id="cobroExtra" readonly>
                </div>
                <div class="mb-3">
                    <label for="costoAlquilerCalculado" class="form-label">Costo Alquiler Calculado</label>
                    <input type="number" class="form-control" id="costoAlquilerCalculado" readonly>
                </div>
                <div class="mb-3">
                    <label for="dineroAdelantado" class="form-label">Dinero Adelantado</label>
                    <input type="number" class="form-control" id="dineroAdelantado" readonly>
                </div>
                <div class="mb-3">
                    <label for="servicio" class="form-label">Servicio</label>
                    <input type="number" class="form-control" id="servicio" readonly>
                </div>
                <div class="mb-3">
                    <label for="penalidad" class="form-label">Penalidad</label>
                    <input type="number" class="form-control" id="penalidad" readonly>
                </div>
                <div class="mb-3">
                    <label for="totalPagar" class="form-label">Total a Pagar</label>
                    <input type="number" class="form-control" id="totalPagar" readonly>
                </div>

                <hr>

                <h5>Métodos de Pago:</h5>
                <div class="mb-3">
                    <label for="dineroAdelantadoPago" class="form-label">Dinero Adelantado</label>
                    <select class="form-select" id="dineroAdelantadoPago" disabled>
                        <option value="efectivo">Efectivo</option>
                        <option value="tarjeta">Tarjeta</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="penalidadAlquilerCalculado" class="form-label">Penalidad y Resto del Alquiler Calculado</label>
                    <select class="form-select" id="penalidadAlquilerCalculado" disabled>
                        <option value="efectivo">Efectivo</option>
                        <option value="tarjeta">Tarjeta</option>
                    </select>
                </div>

                <hr>

                <h5>Responsables:</h5>
                <div class="mb-3">
                    <label for="usuarioCreacion" class="form-label">Usuario de Creación</label>
                    <select class="form-select" id="usuarioCreacion" disabled>
                        <option value="usuario1">Usuario 1</option>
                        <option value="usuario2">Usuario 2</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="usuarioModificacion" class="form-label">Usuario de Modificación</label>
                    <select class="form-select" id="usuarioModificacion" disabled>
                        <option value="usuario1">Usuario 1</option>
                        <option value="usuario2">Usuario 2</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="usuarioFinalizacion" class="form-label">Usuario de Finalización</label>
                    <select class="form-select" id="usuarioFinalizacion" disabled>
                        <option value="usuario1">Usuario 1</option>
                        <option value="usuario2">Usuario 2</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<div class="table-responsive mt-4">
    <table class="table table-bordered align-middle">
        <thead class="table-warning">
        <tr>
            <th>N°</th>
            <th>DNI</th>
            <th>Tipo</th>
            <th>Habitación</th>
            <th>Descuento</th>
            <th>Extra</th>
            <th>Dinero Adelantado</th>
            <th>Servicio</th>
            <th>Penalidad</th>
            <th>Total</th>
            <th>Tiempo Rebasado</th>
            <th>Detalles</th>
        </tr>
        </thead>
        <tbody id="tablaReporteDiario">
        <tr>
            <td>1</td>
            <td>12345678</td>
            <td>Recepción</td>
            <td>696 - 24hr</td>
            <td>S/.0</td>
            <td>S/.0</td>
            <td>S/.0</td>
            <td>S/.0</td>
            <td>S/.0</td>
            <td>S/.0</td>
            <td class="text-danger">Si</td>
            <td class="d-flex justify-content-center gap-1">
                <button class="btn btn-info btn-sm" data-bs-toggle="modal" data-bs-target="#modalVerDetalle">👁️</button>
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
</body>