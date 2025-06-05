<%@ page import="development.team.hoteltransylvania.Model.Room" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRoom" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.Model.Client" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionClient" %>
<%@ page import="development.team.hoteltransylvania.DTO.TableReservationDTO" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionRecepcion" %>
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

    int statusRoom = room.getStatusRoom().getValue();
    boolean camposBloqueados = false;
    boolean mostrar = false;
    TableReservationDTO reservaPendiente = null;

    switch (statusRoom) {
        case 4:
            //display del cliente se puede ver
            mostrar = true;
            //bloqueo de todos los campos
            camposBloqueados = true;
            //verificar en que reserva esta asignada
            reservaPendiente = GestionRecepcion.getReservationPendiente(room.getNumber());

            break;
        case 2:
            //display del cliente se puede ver
            mostrar = true;
            //bloqueo de todos los campos
            camposBloqueados = true;
            //verificar en que reserva esta asignada
            reservaPendiente = GestionRecepcion.getReservationOcupada(room.getNumber());
            // -  si en caso esta ocuapada, verificar en que reserva esta asignada
            //      * verificar en que momento vence la reserva (porsiacaso)
            //      * obntener la reserva asignada y sacar datos de ahi para mostrar
            break;
        case 3:
            //campos bloqueados
            camposBloqueados = true;
            break;
        default:
            //campos libres
            break;
    }

    //primero validar si la habitacion esta libre
    //mostrar simepre datos de la haiatcion en seccion informativa
    // - si en caso esta libre dejar libres los campos ()
    // -  si en caso esta ocuapada o pendoiente, verificar en que reserva esta asignada
    //      * verificar en que momento vence la reserva (porsiacaso)
    //      * obntener la reserva asignada y sacar datos de ahi para mostrar
    // - si en caso esta en mantenimiento los campos deben estar bloqueados hasta que se vuelva a habilitar

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

<form action="recepController" method="post">
    <!-- Sección de datos del cliente y hospedaje -->
    <div class="row mt-4">
        <!-- Sección de datos del cliente -->
        <div class="col-lg-6 col-md-6 col-sm-12">
            <div class="card">
                <div class="card-header bg-light"><strong>Datos del Cliente</strong></div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12 d-flex flex-column flex-sm-row align-items-center justify-content-between gap-2">
                            <label for="busquedaCliente" class="form-label mb-0"><strong>Documento:</strong></label>
                            <input type="text" class="form-control" id="busquedaCliente"
                                   placeholder="Buscar por Documento"
                                   onkeyup="buscarClienteRecepcion()" required
                                   autofocus <%= camposBloqueados ? "disabled" : "" %>>
                            <button class="btn btn-primary"
                                    onclick="cargarPagina('jsp/clientes.jsp')"
                                    <%= camposBloqueados ? "disabled" : "" %>>
                                <i class="fa-solid fa-user-plus"></i>
                            </button>
                        </div>

                        <div id="dataClientRecepcion" style="<%= mostrar ? "" : "display: none;" %>">
                            <input type="hidden" name="idClienteProcesar" id="idClienteProcesar">
                            <div class="row">
                                <div class="col-md-6 mt-2">
                                    <label for="tipoDocumento" class="form-label"><strong>Tipo de
                                        Documento:</strong></label>
                                    <input type="text" class="form-control" id="tipoDocumento"
                                           name="tipoDocumentoProcesar"
                                           value="<%= reservaPendiente != null ? reservaPendiente.getDocumentType() : "" %>"
                                           readonly>
                                </div>

                                <div class="col-md-6 mt-2">
                                    <label for="documento" class="form-label"><strong>Documento:</strong></label>
                                    <input type="text" class="form-control" id="documento" name="documentoProcesar"
                                           value="<%= reservaPendiente != null ? reservaPendiente.getDocumentNumber() : "" %>"
                                           readonly>
                                </div>
                            </div>

                            <div class="col-md-12 mt-2">
                                <label for="name" class="form-label"><strong>Nombre Completo:</strong></label>
                                <input type="text" class="form-control" id="name" name="nombreProcesar"
                                       value="<%= reservaPendiente != null ? reservaPendiente.getClientName() : "" %>"
                                       readonly>
                            </div>
                            <div class="col-md-12 mt-2">
                                <label for="correo" class="form-label"><strong>Correo:</strong></label>
                                <input type="email" class="form-control" id="correo" name="emailProcesar"
                                       value="<%= reservaPendiente != null ? reservaPendiente.getEmail() : "" %>"
                                       readonly>
                            </div>


                            <div class="col-md-12 mt-2">
                                <label for="telefono" class="form-label"><strong>Teléfono:</strong></label>
                                <input type="text" class="form-control" id="telefono" name="telefonoProcesar"
                                       value="<%= reservaPendiente != null ? reservaPendiente.getPhone() : "" %>"
                                       readonly>
                            </div>
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
                        <%
                            java.time.LocalDateTime now = java.time.LocalDateTime.now();
                            // Formateador para datetime-local
                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                            String fechaHoraActual = now.format(formatter);
                        %>
                        <!-- Columna Izquierda -->
                        <div class="col-md-6">
                            <div class="mt-2">
                                <label for="fechaEntrada" class="form-label"><strong>Fecha y Hora de
                                    Entrada Reservada:</strong></label>
                                <input type="datetime-local" class="form-control" id="fechaEntradaRecep"
                                       name="fechaEntradaRecep"
                                       value="<%= fechaHoraActual %>" readonly <%= camposBloqueados ? "disabled" : "" %>>
                            </div>

                            <div class="mt-2">
                                <label for="fechaEntrada" class="form-label"><strong>Fecha y Hora de
                                    Entrada de Ingreso:</strong></label>
                                <input type="datetime-local" class="form-control" id="fechaEntradaRecep"
                                       name="fechaEntradaRecep"
                                       readonly <%= camposBloqueados ? "disabled" : "" %>>
                            </div>
                        </div>

                        <!-- Columna Derecha -->
                        <div class="col-md-6">
                            <div class="mt-2">
                                <label for="fechaSalida" class="form-label"><strong>Fecha y Hora de Salida:</strong></label>
                                <input type="datetime-local" class="form-control" id="fechaSalidaRecep"
                                       name="fechaSalidaRecep"
                                       min="<%= fechaHoraActual %>" required <%= camposBloqueados ? "disabled" : "" %>
                                       value="<%= reservaPendiente != null ? reservaPendiente.getCheckOutDate() : "" %>">
                            </div>
                        </div>

                        <div style="border-bottom: 1px solid #ccc; margin: 20px auto; width: 94%;"></div>

                    <%--<div class="col-md-6 mt-2">
                            <label for="fechaEntrada" class="form-label"><strong>Fecha y Hora de
                                Entrada Reservada:</strong></label>
                            <input type="datetime-local" class="form-control" id="fechaEntradaRecep"
                                   name="fechaEntradaRecep"
                                   value="<%= fechaHoraActual %>" readonly <%= camposBloqueados ? "disabled" : "" %>>
                        </div>

                        <div class="col-md-6 mt-2">
                            <label for="fechaSalida" class="form-label"><strong>Fecha y Hora de Salida:</strong></label>
                            <input type="datetime-local" class="form-control" id="fechaSalidaRecep"
                                   name="fechaSalidaRecep"
                                   min="<%= fechaHoraActual %>" required <%= camposBloqueados ? "disabled" : "" %>
                                   value="<%= reservaPendiente != null ? reservaPendiente.getCheckOutDate() : "" %>">
                        </div>
                        <div class="col-md-6 mt-2">
                            <label for="fechaEntrada" class="form-label"><strong>Fecha y Hora de
                                Entrada de Ingreso:</strong></label>
                            <input type="datetime-local" class="form-control" id="fechaEntradaRecep"
                                   name="fechaEntradaRecep"
                                   readonly <%= camposBloqueados ? "disabled" : "" %>>
                        </div>--%>


                        <!--Solo para pasar el precio-->
                        <input type="hidden" data-precio="<%=room.getPrice()%>" name="habitacionRecep"
                               id="habitacionRecep">
                        <input type="hidden" name="roomSelect" value="<%=room.getId()%>">
                        <div class="col-md-6 mt-2">
                            <div class="d-flex justify-content-between align-items-center">
                                <label for="descuento" class="form-label m-0"><strong>Descuento:</strong></label>
                                <button type="button" id="toggleDescuento"
                                        class="btn btn-outline-secondary btn-sm rounded-circle"
                                        title="Activar descuento"
                                        onclick="actDscRecepcion()"
                                        <%= camposBloqueados ? "disabled" : "" %>>
                                    <i id="iconoDescuento" class="fas fa-lock text-danger"></i>
                                </button>
                            </div>
                            <div class="input-group mt-2">
                                <select class="form-select" id="descuentoRecep" name="descuentoRecep" required disabled>
                                    <option value="0">0</option>
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                </select>
                                <label for="descuento" class="input-group-text"><i
                                        class="fa-solid fa-percent"></i></label>
                            </div>
                        </div>


                        <div class="col-md-6 mt-2">
                            <label for="cobroExtraRecep" class="form-label"><strong>Cobro Extra:</strong></label>
                            <input type="number" class="form-control" id="cobroExtraRecep" name="cobroExtraRecep"
                                   min="0"
                                   value="0" required <%= camposBloqueados ? "disabled" : "" %>>
                        </div>

                        <div class="col-md-6 mt-2">
                            <label for="adelantoRecep" class="form-label"><strong>Adelanto:</strong></label>
                            <input type="number" class="form-control" id="adelantoRecep" name="adelantoRecep" min="0"
                                   value="0" required <%= camposBloqueados ? "disabled" : "" %>>
                        </div>

                        <div class="col-md-6 mt-2">
                            <label for="totalPagarRecep" class="form-label"><strong>Total a Pagar:</strong></label>
                            <input type="text" class="form-control" id="totalPagarRecep" name="totalPagarRecep"
                                   value="<%=room.getPrice()%>" readonly required
                                <%= camposBloqueados ? "disabled" : "" %>>
                        </div>

                        <div class="col-md-12 mt-2">
                            <label for="observaciones" class="form-label"><strong>Observaciones:</strong></label>
                            <textarea class="form-control" id="observaciones"
                                      rows="3" <%= camposBloqueados ? "disabled" : "" %>></textarea>
                        </div>

                        <div class="col-md-12 mt-2 d-flex justify-content-between">
                            <%
                                switch (room.getStatusRoom().getValue()) {
                                    case 2:
                            %>
                            <button type="submit" class="btn btn-danger">Finalizar</button>
                            <%
                                    break;
                                case 3:
                            %>
                            <button type="submit" name="accion" value="habilitar" class="btn btn-warning">Habilitar
                                Habitación
                            </button>
                            <%
                                    break;
                                case 4:
                            %>
                            <button type="submit" class="btn btn-danger">Cancelar Reserva</button>
                            <button type="submit" class="btn btn-success">Ocupar Habitación</button>
                        </div>
                        <%
                                break;
                            default:
                        %>
                        <button type="submit" class="btn btn-primary">Ocupar Habitación</button>
                        <%
                                    break;
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
        <%--<!-- Modal para agregar Cliente -->
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
        --%>
    </div>
</form>

</body>
</html>