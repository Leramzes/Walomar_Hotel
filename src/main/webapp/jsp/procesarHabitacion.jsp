<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.Client" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionClient" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Procesar Habitación</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<!-- Encabezado -->
<div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
    <h4><i class="fa-solid fa-bed me-2"></i> Procesar Habitación</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item"><a href="#" onclick="cargarPagina('jsp/recepcion.jsp')">Recepción</a></li>
            <li class="breadcrumb-item active" aria-current="page">Procesar Habitación</li>
        </ol>
    </nav>
</div>

<%
    int idParam = Integer.parseInt(request.getParameter("id"));
    Room room = GestionRoom.getRoomById(idParam);

    List<Client> clients = GestionClient.getAllClients();
%>

<!-- Sección de datos de la habitación -->
<div class="row mt-4">
    <div class="col-lg-12 col-md-12 col-sm-12">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos de la Habitación</strong></div>

            <div class="card-body">
                <div class="row">
                    <div class="col-md-6"><p><strong>Nombre:</strong> <%=room.getNumber()%>
                    </p></div>
                    <div class="col-md-6"><p><strong>Tarifa:</strong> 24hr</p></div>
                    <div class="col-md-6"><p><strong>Tipo:</strong> <%=room.getTypeRoom().getName().toUpperCase()%>
                    </p></div>
                    <div class="col-md-6"><p><strong>Detalles:</strong> ---- </p></div>
                    <div class="col-md-6"><p><strong>Costo:</strong> S/ <%=room.getPrice()%>
                    </p></div>
                    <%
                        String statusBd = room.getStatusRoom().getName();
                        String estado = statusBd.substring(0, 1).toUpperCase() + statusBd.substring(1).toLowerCase();%>
                    <div class="col-md-6"><p><strong>Estado:</strong> <%=estado%>
                    </p></div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Sección de datos del cliente y hospedaje -->
<div class="row mt-4">
    <!-- Sección de datos del cliente -->
    <div class="col-lg-6 col-md-6 col-sm-12">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos del Cliente</strong></div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-12 d-flex flex-column flex-sm-row align-items-start align-items-sm-center justify-content-between gap-2">
                        <label for="busquedaCliente" class="form-label mb-0"><strong>Nombre:</strong></label>
                        <select class="form-select w-100 w-sm-75" id="busquedaCliente"
                                aria-label="Default select example">
                            <option selected>Buscar Cliente</option>
                            <%for(Client client : clients){%>
                            <option value="<%=client.getId()%>"><%=client.getName() + " "+
                                    client.getApPaterno()+" "+client.getApMaterno()%></option>
                            <%}%>
                        </select>
                        <button class="btn btn-primary mt-2 mt-sm-0" <%--data-bs-toggle="modal"
                                data-bs-target="#modalAgregarCliente"--%>
                                onclick="cargarPagina('jsp/clientes.jsp')">
                            <i class="fa-solid fa-user-plus"></i>
                        </button>
                    </div>

                    <div class="col-md-6 mt-2">
                        <label for="tipoDocumento" class="form-label"><strong>Tipo de Documento:</strong></label>
                        <input type="text" class="form-control" id="tipoDocumento" readonly>
                    </div>

                    <div class="col-md-6 mt-2">
                        <label for="documento" class="form-label"><strong>Documento:</strong></label>
                        <input type="text" class="form-control" id="documento" readonly>
                    </div>

                    <div class="col-md-12 mt-2">
                        <label for="correo" class="form-label"><strong>Correo:</strong></label>
                        <input type="email" class="form-control" id="correo" readonly>
                    </div>

                    <div class="form-check d-flex justify-content-center mt-2">
                        <input class="form-check-input me-2" type="checkbox" value="" id="enviarCorreo">
                        <label class="form-check-label" for="enviarCorreo">
                            <strong>Enviar estado de cuenta por <span class="text-primary">correo</span>.</strong>
                        </label>
                    </div>

                    <div class="col-md-12 mt-2">
                        <label for="telefono" class="form-label"><strong>Teléfono:</strong></label>
                        <input type="text" class="form-control" id="telefono">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Sección de datos del hospedaje -->
    <div class="col-lg-6 col-md-6 col-sm-12 mt-4 mt-md-0">
        <div class="card">
            <div class="card-header bg-light"><strong>Datos del Alojamiento</strong></div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6 mt-2">
                        <label for="fechaEntrada" class="form-label"><strong>Fecha y Hora de Entrada:</strong></label>
                        <input type="datetime-local" class="form-control" id="fechaEntrada">
                    </div>

                    <div class="col-md-6 mt-2">
                        <label for="fechaSalida" class="form-label"><strong>Fecha y Hora de Salida:</strong></label>
                        <input type="datetime-local" class="form-control" id="fechaSalida">
                    </div>

                    <div class="col-md-6 mt-2">
                        <div class="d-flex justify-content-between align-items-center">
                            <label for="descuento" class="form-label m-0"><strong>Descuento:</strong></label>
                            <div class="d-flex">
                                <div class="form-check me-3">
                                    <input class="form-check-input" type="radio" name="descuentoTipo"
                                           id="descuentoPorcentual">
                                    <label class="form-check-label" for="descuentoPorcentual">%</label>
                                </div>

                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="descuentoTipo"
                                           id="descuentoMonetario">
                                    <label class="form-check-label" for="descuentoMonetario">S/.</label>
                                </div>
                            </div>
                        </div>

                        <div class="input-group mt-2">
                            <input type="text" id="descuento" name="descuento"
                                   class="form-control form-control-lg bg-light fs-6">
                            <label for="descuento" class="input-group-text"><i class="fa-solid fa-percent"></i></label>
                        </div>
                    </div>


                    <div class="col-md-6 mt-2">
                        <label for="cobroExtra" class="form-label"><strong>Cobro Extra:</strong></label>
                        <input type="text" class="form-control" id="cobroExtra">
                    </div>

                    <div class="col-md-6 mt-2">
                        <label for="adelanto" class="form-label"><strong>Adelanto:</strong></label>
                        <input type="text" class="form-control" id="adelanto">
                    </div>

                    <div class="col-md-6 mt-2">
                        <label for="totalPagar" class="form-label"><strong>Total a Pagar:</strong></label>
                        <input type="text" class="form-control" id="totalPagar">
                    </div>

                    <div class="col-md-12 mt-2">
                        <label for="observaciones" class="form-label"><strong>Observaciones:</strong></label>
                        <textarea class="form-control" id="observaciones" rows="3"></textarea>
                    </div>

                    <div class="col-md-12 mt-2 d-flex justify-content-between">
                        <button type="button" class="btn btn-danger">Regresar</button>
                        <button type="button" class="btn btn-success">Agregar Registro</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para agregar Cliente -->
    <div class="modal fade" id="modalAgregarCliente" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="agregarCliente">Agregar Cliente</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="#">
                        <input type="hidden" id="inputAgregarCliente">
                        <input type="hidden" value="add" name="#">
                        <div class="row">
                            <div class="col-md-6 mt-2">
                                <label for="tipoDocumentoModal" class="form-label"><strong>Tipo de
                                    Documento:</strong></label>
                                <select class="form-select" id="tipoDocumentoModal" aria-label="Default select example"
                                        required>
                                    <option selected>Buscar Cliente</option>
                                    <option value="#">DNI</option>
                                    <option value="#">Pasaporte</option>
                                    <option value="#">RUC</option>
                                </select>
                            </div>

                            <div class="col-md-6 mt-2">
                                <label for="documentoModal" class="form-label"><strong>Documento:</strong></label>
                                <input type="text" class="form-control" id="documentoModal" required>
                            </div>

                            <div class="col-md-12 mt-2">
                                <label for="nombre" class="form-label"><strong>Nombre:</strong> (Obligatorio)</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" required>
                            </div>

                            <div class="col-md-12 mt-2">
                                <label for="correoModal" class="form-label"><strong>Correo:</strong></label>
                                <input type="email" class="form-control" id="correoModal">
                            </div>

                            <div class="col-md-12 mt-2">
                                <label for="telefonoModal" class="form-label"><strong>Teléfono:</strong></label>
                                <input type="text" class="form-control" id="telefonoModal">
                            </div>

                            <div class="col-md-12 mt-2 text-end">
                                <button type="submit" class="btn btn-success">Agregar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>