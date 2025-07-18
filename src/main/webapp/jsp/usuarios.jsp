<%@ page import="development.team.hoteltransylvania.Business.GestionEmployee" %>
<%@ page import="java.util.List" %>
<%@ page import="development.team.hoteltransylvania.DTO.usersEmployeeDTO" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZonedDateTime" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="d-flex justify-content-between align-items-center">
    <h4><i class="fa-solid fa-users-gear me-2"></i> Usuarios</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Inicio</a></li>
            <li class="breadcrumb-item active" aria-current="page">Usuarios</li>
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
    if (usuario.getEmployee().getPosition().equalsIgnoreCase("2")) {
        response.sendRedirect("inicio.jsp"); //Mensaje: No tienes privilegios
        return;
    }

    int pagina = 1;
    int pageSize = 10;

    String pageParam = request.getParameter("page");
    if (pageParam != null) {
        pagina = Integer.parseInt(pageParam);
    }

    List<usersEmployeeDTO> allEmplooyes = GestionEmployee.getAllEmployeesPaginated(pagina, pageSize);
    int totalEmployee = GestionEmployee.getAllEmployees().size();
    int totalPages = (int) Math.ceil((double) totalEmployee / pageSize);
%>

<!-- Sección de usuarios -->
<div class="card mt-4">
    <div class="card-header">
        <div class="row align-items-center">
            <div class="col-9 d-flex gap-2">
                <p>Catálogo de Usuarios</p>
            </div>
            <div class="col-3 d-flex justify-content-end align-items-center">
                <label for="estadoSelect" class="form-label m-0 me-2">Estado:</label>
                <select id="estadoSelect" class="form-select  w-auto"
                        onchange="Search('#nameUserSearch', '#estadoSelect','#tablaUsuarios','#sizeUsers','filterUserServlet', 1, 10)">
                    <option value="">Todos</option>
                    <option value="Activo">Activos</option>
                    <option value="Inactivo">Inactivos</option>
                </select>
            </div>
        </div>
        <div class="d-flex justify-content-between align-items-start gap-2 mt-2 mb-3">
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarUsuario">
                <i class="fas fa-plus"></i> Agregar nuevo
            </button>
            <div class="d-flex gap-2">
                <button class="btn btn-success" onclick="exportarData('usuarios')">
                    <i class="fa-solid fa-file-export"></i> Exportar Usuarios
                </button>
                <button class="btn btn-success" onclick="exportarTablaPDF({
            tablaId: 'tablaUsuarios',
            tituloReporte: 'REPORTE DE USUARIOS',
            nombreArchivo: 'Reporte_Usuarios',
            columnas: ['N°', 'Nombre', 'Usuario', 'Correo', 'Tipo', 'Fecha Caducidad', 'Estatus']
        })">
                    <i class="fa-solid fa-file-export"></i> Exportar Vista Actual a PDF
                </button>
            </div>
        </div>
    </div>

    <!-- Modal para agregar usuario -->
    <div class="modal fade" id="modalAgregarUsuario" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="agregarUsuario">Agregar Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">

                    <form>
                        <div class="mb-3">
                            <label>DNI</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                <input type="hidden" name="tipoDocumento" id="tipoDocumento" value="DNI-user">
                                <input type="text" class="form-control" id="documento" name="document"
                                       required maxlength="8" inputmode="numeric" placeholder="Ingrese el DNI"
                                       pattern="\d{8}" oninput="this.value = this.value.replace(/\D/g, '').slice(0,8);">
                                <button type="button" class="btn btn-success" id="btnBuscar" onclick="buscarDNI()">
                                    Buscar
                                </button>
                            </div>
                        </div>
                    </form>


                    <form id="formUsuario" action="user" method="post">
                        <input type="hidden" id="inputAgregarUsuario">
                        <input type="hidden" name="accion" value="Registrar">
                        <input type="hidden" id="numberDocumentoHidden" name="numberdocumentHidden" required>

                        <div class="mb-3">
                            <label for="nombre">Nombre</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                                <input type="text" class="form-control" id="nombre" name="nombre"
                                       placeholder="Nombre Completo" required readonly>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="correo">Correo</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                <input type="text" class="form-control" id="correo" name="email"
                                       placeholder="ejemplo@dominio.com"
                                       autocomplete="off"
                                       oninput="validarCorreoLive(this)">
                                <small id="mensajeCorreo" class="form-text"></small>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="rol">Rol</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-user-tag"></i></span>
                                <select class="form-select" id="rol" name="rol" required>
                                    <option selected disabled>Seleccionar Rol</option>
                                    <option value="1">Administrador</option>
                                    <option value="2">Recepcionista</option>
                                </select>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="username">Usuario</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                                <input type="text" id="username" name="username" class="form-control"
                                       placeholder="Nombre Usuario" required readonly>
                            </div>
                        </div>
                        <%
                            ZoneId zonaPeru = ZoneId.of("America/Lima");
                            LocalDate hoy = LocalDate.now(zonaPeru);
                            LocalDate fechaEnUnMes = hoy.plusMonths(1);

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String fechaMinima = hoy.format(formatter); // Hoy
                            String fechaPorDefecto = fechaEnUnMes.format(formatter); // Hoy + 1 mes
                        %>
                        <div class="mb-3">
                            <label for="username">Fecha Caducidad</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-calendar-alt"></i></span>
                                <input type="date"
                                       class="form-control"
                                       id="fechCaducidad"
                                       name="fechCaducidad"
                                       min="<%= fechaMinima %>"
                                       value="<%= fechaPorDefecto %>"
                                       required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-success">
                                Guardar
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para editar usuario -->
    <div class="modal fade" id="modalEditarUsuario" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editarUsuario">Editar Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <form id="formEditarUsuario" action="user" method="post">
                        <input type="hidden" name="accion" value="update">
                        <input type="hidden" name="idemployee" id="inputEditarUsuario">
                        <div class="mb-3">
                            <label for="nombreEditar">Nombre</label>
                            <input type="text" class="form-control" id="nombreEditar" name="nombreEdit" required
                                   readonly>
                        </div>
                        <div class="mb-3">
                            <label for="correoEditar">Correo</label>
                            <input type="text" class="form-control" id="correoEditar" name="correoEdit"
                                   placeholder="ejemplo@dominio.com"
                                   autocomplete="off"
                                   oninput="validarCorreoLive(this)" required>
                            <small id="mensajeCorreo" class="form-text"></small>
                            <%--<input type="email" class="form-control" id="correoEditar" name="correoEdit" required>--%>
                        </div>
                        <div class="mb-3">
                            <label for="usuarioEditar">Usuario</label>
                            <input type="text" class="form-control" id="usuarioEditar" name="userEdit" required>
                        </div>
                        <div class="mb-3">
                            <label for="rolEditar">Rol</label>
                            <select class="form-select" id="rolEditar" name="rolEditar" required>
                                <option value="1">Administrador</option>
                                <option value="2">Recepcionista</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="estatusEditar">Estatus</label>
                            <select class="form-select" id="estatusEditar" name="estatusEdit" required>
                                <option value="Activo">Activo</option>
                                <option value="Inactivo">Inactivo</option>
                            </select>
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
                <input type="number" id="sizeUsers" min="1" max="999" value="<%=allEmplooyes.size()%>"
                       class="form-control d-inline-block" style="width: 5rem;" readonly> registros
            </span>

            <div class="input-group" style="max-width: 250px;">
                <input type="text" class="form-control" id="nameUserSearch" placeholder="Buscar por Nombre"
                       onkeyup="Search('#nameUserSearch', '#estadoSelect','#tablaUsuarios','#sizeUsers','filterUserServlet', 1, 10)">
                <span class="input-group-text"><i class="fas fa-search"></i></span>
            </div>
        </div>

        <div class="table-responsive">
            <table id="tablaUsuarios" class="table table-bordered align-middle">
                <thead class="table-warning">
                <tr>
                    <th>N°</th>
                    <th>Nombre</th>
                    <th>Usuario</th>
                    <th>Correo</th>
                    <th>Tipo</th>
                    <th>Fecha Caducidad</th>
                    <th>Estatus</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <% int count = 1; // Definir count antes del bucle %>
                <%for (usersEmployeeDTO employee : allEmplooyes) { %>
                <tr>
                    <td><%=count%>
                    </td>
                    <td><%=employee.getName_employee()%>
                    </td>
                    <td><%=employee.getName_user()%>
                    </td>
                    <td><%=employee.getEmail_user()%>
                    </td>
                    <td><%=employee.getTipo_user()%>
                    </td>
                    <td><%=employee.getFecha_caducidad()%>
                    </td>
                    <td><%=employee.getEstado_user()%>
                    </td>
                    <td class="d-flex justify-content-center align-items-center gap-1">
                        <button class="btn btn-warning btn-sm" id="btn-editar"
                                data-bs-toggle="modal"
                                data-bs-target="#modalEditarUsuario" title="Editar Usuario"
                                onclick="editarUser(<%=employee.getId_employee()%>)">✏️
                        </button>
                        <form class="form-restart" action="user" method="post">
                            <input type="hidden" name="idUser" value="<%=employee.getId_user()%>">
                            <input type="hidden" name="accion" value="restartPass">
                            <button type="submit" title="Resetear Clave" class="btn btn-secondary btn-sm">🔑</button>
                        </form>
                        <% if (employee.getEstado_user().equals("Activo")) { %>
                        <form class="form-delete" action="user" method="post">
                            <input type="hidden" name="idUser" value="<%=employee.getId_user()%>">
                            <input type="hidden" name="accion" value="delete">
                            <button type="submit" title="Desactivar Usuario" class="btn btn-danger btn-sm">❌</button>
                        </form>
                        <% } else { %>
                        <form class="form-activate" action="user" method="post">
                            <input type="hidden" name="idUser" value="<%=employee.getId_user()%>">
                            <input type="hidden" name="accion" value="activate">
                            <button type="submit" title="Activar Usuario" class="btn btn-success btn-sm">✅</button>
                        </form>
                        <% } %>
                    </td>
                </tr>
                <% count++;
                }%>

                </tbody>
            </table>
        </div>

        <div class="d-flex flex-column align-items-end">
            <div id="no-data" class="w-100"></div>
            <nav aria-label="Page navigation">
                <ul class="pagination mb-0" id="pagination">
                    <li class="page-item <% if (pagina == 1) { %>disabled<% } %>">
                        <a class="page-link" aria-label="Anterior" href="menu.jsp?view=usuarios&page=<%= pagina - 1 %>">Anterior</a>
                    </li>

                    <% for (int i = 1; i <= totalPages; i++) { %>
                    <li class="page-item <% if (i == pagina) { %>active<% } %>">
                        <a class="page-link" aria-label="Actual" href="menu.jsp?view=usuarios&page=<%= i %>"><%= i %>
                        </a>
                    </li>
                    <% } %>

                    <li class="page-item <% if (pagina == totalPages) { %>disabled<% } %>">
                        <a class="page-link" aria-label="Siguiente"
                           href="menu.jsp?view=usuarios&page=<%= pagina + 1 %>">Siguiente</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
