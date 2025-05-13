<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="org.mindrot.jbcrypt.BCrypt" %>
<%@ page import="development.team.hoteltransylvania.Model.InformationHotel" %>
<%@ page import="development.team.hoteltransylvania.Business.GestionInformationHotel" %>
<%
    InformationHotel hotelInfo = GestionInformationHotel.getInformationHotel();

    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("usuario") == null) {
        response.sendRedirect("index.jsp"); //Mensaje: Inicia sesión primero
        return;
    }
    User usuario = (User) sessionObj.getAttribute("usuario");
    boolean mostrarModal = BCrypt.checkpw(usuario.getUsername(), usuario.getPassword());
    int rolUser = Integer.parseInt(usuario.getEmployee().getPosition());
    String fullName = usuario.getEmployee().getName();
    String[] parts = fullName.trim().split("\\s+"); // divide por espacios múltiples
    String firstTwoWords = parts[0];

%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Transylvania</title>
    <link rel="stylesheet" href="css/global.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <!-- Estilo de inicio.jsp -->
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/grafica.css">
    <!-- Estilo de reserva.jsp -->
    <link rel="stylesheet" href="css/reserva.css">
    <!-- Estilo de recepcion.jsp y habitacionVenta.jsp y verificacionSalidas.jsp -->
    <link rel="stylesheet" href="css/habitaciones.css">
    <!-- Estilo de ventaDirecta.jsp y venderProductos.jsp -->
    <link rel="stylesheet" href="css/venderProductos.css">
    <!-- FullCalendar CSS -->
    <link href='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.css' rel='stylesheet'/>
</head>

<body>
<!-- Modal para cambiar contraseña -->
<div class="modal fade" id="modalCambiarPassword" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true"
     data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cambiar Contraseña</h5>
            </div>
            <div class="modal-body">
                <p>Tu contraseña actual es la predeterminada. Por seguridad, cámbiala ahora.</p>
                <form id="formCambiarPassword" action="user" method="post">
                    <input type="hidden" name="accion" value="updatePassword">
                    <div class="mb-3">
                        <label for="nuevaPassword">Nueva Contraseña</label>
                        <input type="password" class="form-control" name="newPassword" id="nuevaPassword" required>
                    </div>
                    <div class="mb-3">
                        <label for="confirmarPassword">Confirmar Contraseña</label>
                        <input type="password" class="form-control" id="confirmarPassword" required>
                    </div>
                    <div id="errorMensaje" class="alert alert-danger mt-3" role="alert" style="display: none;">
                        Las contraseñas no coinciden.
                    </div>
                    <div id="errorSeguridad" class="alert alert-danger mt-3" role="alert" style="display: none;">
                        <strong>Por favor, crea una contraseña segura que cumpla con los siguientes requisitos:</strong>
                        <ul class="mt-2 mb-0">
                            <li>Mínimo 8 caracteres</li>
                            <li>Al menos una letra mayúscula</li>
                            <li>Al menos una letra minúscula</li>
                            <li>Al menos un número</li>
                            <li>Al menos un carácter especial (por ejemplo: @, #, $, !, etc.)</li>
                        </ul>
                    </div>
                    <button type="submit" class="btn btn-success">Actualizar Contraseña</button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="wrapper">
    <!-- Sidebar -->
    <aside id="sidebar">
        <div class="d-flex">
            <!-- Botón para contraer/expandir el sidebar -->
            <button class="toggle-btn" type="button">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div class="sidebar-logo text-white">
                <i class="fa-solid fa-hotel "></i>
                <span><%=hotelInfo.getName()%></span>
            </div>
        </div>
        <hr>

        <div class="sidebar-header">
            <a href="#" class="sidebar-link">
                <i class="fa-solid fa-user me-2"></i>
                <span><%=firstTwoWords%></span>
            </a>
        </div>

        <hr>
        <!-- Menú de opciones -->
        <ul class="sidebar-nav">
            <li class="sidebar-item">
                <a id="btnInicio" href="#" class="sidebar-link" data-pagina="inicio"
                   onclick="cargarPagina('jsp/inicio.jsp')">
                    <i class="fa-solid fa-house me-2"></i>
                    <span>Inicio</span>
                </a>
            </li>
            <li class="sidebar-item">
                <a href="#" class="sidebar-link" data-pagina="reserva" onclick="cargarPagina('jsp/reserva.jsp')">
                    <i class="fa-solid fa-calendar-days me-2"></i>
                    <span>Reserva</span>
                </a>
            </li>
            <li class="sidebar-item">
                <a href="#" class="sidebar-link" data-pagina="recepcion" onclick="cargarPagina('jsp/recepcion.jsp')">
                    <i class="fa-solid fa-right-to-bracket me-2"></i>
                    <span>Recepción</span>
                </a>
            </li>
            <li class="sidebar-item">
                <a href="#" class="sidebar-link collapsed has-dropdown" data-bs-toggle="collapse"
                   data-bs-target="#puntoVenta" aria-expanded="false" aria-controls="auth">
                    <i class="fa-solid fa-basket-shopping me-2"></i>
                    <span>Punto de Venta</span>
                </a>
                <ul id="puntoVenta" class="sidebar-dropdown list-unstyled collapse" data-bs-parent="#sidebar">
                    <li class="sidebar-item">
                        <a href="#" class="sidebar-link" data-pagina="habitacionesVenta"
                           onclick="cargarPagina('jsp/habitacionesVenta.jsp')">
                            <i class="fa-solid fa-basket-shopping me-2"></i>
                            Vender Productos
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a href="#" class="sidebar-link" data-pagina="catalagoProductos"
                           onclick="cargarPagina('jsp/catalagoProductos.jsp', 'catalogoProductos')">
                            <i class="fa-solid fa-basket-shopping me-2"></i>
                            Catálogo de Productos
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a href="#" class="sidebar-link" data-pagina="habitacionesServicio"
                           onclick="cargarPagina('jsp/habitacionesServicio.jsp')">
                            <i class="fa-solid fa-basket-shopping me-2"></i>
                            Vender Servicios
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a href="#" class="sidebar-link" data-pagina="catalagoServicios"
                           onclick="cargarPagina('jsp/catalagoServicios.jsp', 'catalogoServicios')">
                            <i class="fa-solid fa-basket-shopping me-2"></i>
                            Catálogo de Servicios
                        </a>
                    </li>
                </ul>
            </li>
            <li class="sidebar-item">
                <a href="#" class="sidebar-link" data-pagina="verificacionSalidas"
                   onclick="cargarPagina('jsp/verificacionSalidas.jsp')">
                    <i class="fa-solid fa-right-from-bracket me-2"></i>
                    <span>Verificación de Salidas</span>
                </a>
            </li>
            <li class="sidebar-item">
                <a href="#" class="sidebar-link disabled" data-pagina="clientes"
                   onclick="cargarPagina('jsp/clientes.jsp')">
                    <i class="fa-solid fa-users me-2"></i>
                    <span>Clientes</span>
                </a>
            </li>
            <li class="sidebar-item">
                <a href="#" class="sidebar-link collapsed has-dropdown" data-bs-toggle="collapse"
                   data-bs-target="#reportes" aria-expanded="false" aria-controls="auth">
                    <i class="fa-solid fa-sheet-plastic me-2"></i>
                    <span>Reportes</span>
                </a>
                <ul id="reportes" class="sidebar-dropdown list-unstyled collapse" data-bs-parent="#sidebar">
                    <li class="sidebar-item">
                        <a href="#" class="sidebar-link" data-pagina="reporteDiario"
                           onclick="cargarPagina('jsp/reporteDiario.jsp')">
                            <i class="fa-solid fa-sheet-plastic me-2"></i>
                            Reporte Diario
                        </a>
                    </li>
                    <li class="sidebar-item">
                        <a href="#" class="sidebar-link" data-pagina="reporteMensual"
                           onclick="cargarPagina('jsp/reporteMensual.jsp')">
                            <i class="fa-solid fa-sheet-plastic me-2"></i>
                            Reporte Mensual
                        </a>
                    </li>
                </ul>
            </li>

            <%
                String mod = "";
                if (rolUser == 2) {
                    mod = "d-none";
                }
            %>
            <li class="sidebar-item <%=mod%>">
                <a href="#" class="sidebar-link" data-pagina="usuarios" onclick="cargarPagina('jsp/usuarios.jsp')">
                    <i class="fa-solid fa-users-gear me-2"></i>
                    <span>Usuarios</span>
                </a>
            </li>
            <li class="sidebar-item <%=mod%>">
                <a href="#" class="sidebar-link collapsed has-dropdown" data-bs-toggle="collapse"
                   data-bs-target="#configuracion" aria-expanded="false" aria-controls="auth">
                    <i class="fa-solid fa-gears me-2"></i>
                    <span>Configuración</span>
                </a>
                <ul id="configuracion" class="sidebar-dropdown list-unstyled collapse" data-bs-parent="#sidebar">
                    <li class="sidebar-item <%=mod%>">
                        <a href="#" class="sidebar-link" data-pagina="informacionHotelera"
                           onclick="cargarPagina('jsp/informacionHotelera.jsp')">
                            <i class="fa-solid fa-gears me-2"></i>
                            Información Hotelera
                        </a>
                    </li>
                    <li class="sidebar-item <%=mod%>">
                        <a href="#" class="sidebar-link" data-pagina="habitaciones"
                           onclick="cargarPagina('jsp/habitaciones.jsp')">
                            <i class="fa-solid fa-gears me-2"></i>
                            Habitaciones
                        </a>
                    </li>
                    <li class="sidebar-item <%=mod%>">
                        <a href="#" class="sidebar-link" data-pagina="habitacionesTipo"
                           onclick="cargarPagina('jsp/habitacionesTipo.jsp')">
                            <i class="fa-solid fa-gears me-2"></i>
                            Tipos de Habitación
                        </a>
                    </li>
                    <li class="sidebar-item <%=mod%>">
                        <a href="#" class="sidebar-link" data-pagina="pisos" onclick="cargarPagina('jsp/pisos.jsp')">
                            <i class="fa-solid fa-gears me-2"></i>
                            Pisos / Niveles
                        </a>
                    </li>
                </ul>
            </li>


        </ul>

        <div class="sidebar-footer">
            <a href="#" class="sidebar-link" onclick="logout();">
                <i class="fa-solid fa-door-open me-2"></i>
                <span>Cerrar Sesión</span>
            </a>
        </div>

        <!-- Script para cerrar sesión -->
        <script>
            function logout() {
                fetch('logout', {method: 'POST'})
                    .then(response => {
                        if (response.redirected) {
                            window.location.href = response.url;
                        }
                    })
                    .catch(error => console.error('Error al cerrar sesión:', error));
            }
        </script>
    </aside>

    <!-- Contenido principal dinámicamente cargado -->
    <main class="main p-4">
        <div class="container-fluid" id="contenido">
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="js/script.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js'></script>

<!-- Script para validar que ambas contraseñas coincidan antes de enviar el formulario -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        // Mostrar modal de cambio de contraseña si es necesario
        <% if (mostrarModal) { %>
        const modalPasswordElement = document.getElementById("modalCambiarPassword");
        const modalPassword = new bootstrap.Modal(modalPasswordElement, {
            backdrop: 'static', // No cerrar al hacer clic fuera
            keyboard: false     // No cerrar con la tecla ESC
        });
        modalPassword.show();
        <% } %>

        // Validar que ambas contraseñas coincidan antes de enviar el formulario
        document.getElementById("formCambiarPassword").addEventListener("submit", function (event) {
            const nuevaPassword = document.getElementById("nuevaPassword").value;
            const confirmarPassword = document.getElementById("confirmarPassword").value;
            const errorMensaje = document.getElementById("errorMensaje");
            const errorSeguridad = document.getElementById("errorSeguridad");
            const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;


            if (nuevaPassword !== confirmarPassword) {
                event.preventDefault();
                errorMensaje.style.display = "block";
                errorSeguridad.style.display = "none"; // Oculta el otro error si estaba activo
            } else {
                errorMensaje.style.display = "none";
                errorSeguridad.style.display = "none";
                event.preventDefault(); // Evitar envío inmediato

                Swal.fire({
                    title: 'Contraseña Actualizada',
                    text: 'Tu contraseña ha sido actualizada exitosamente.',
                    icon: 'success',
                    confirmButtonText: 'OK',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById("formCambiarPassword").submit();
                    }
                });
            }
        });
    });

    // Evitar problemas con múltiples modales abiertos
    document.addEventListener("hidden.bs.modal", function () {
        document.body.classList.remove("modal-open");
        document.querySelectorAll(".modal-backdrop").forEach(el => el.remove());
    });
</script>

<!-- Script para resaltar el enlace activo en el menú -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const sidebarLinks = document.querySelectorAll(".sidebar-link");

        sidebarLinks.forEach(link => {
            link.addEventListener("click", function () {
                // Remover clase 'active' de todos los enlaces
                sidebarLinks.forEach(item => item.classList.remove("active"));

                // Agregar la clase 'active' al elemento seleccionado
                this.classList.add("active");

                // Cerrar todos los dropdowns
                document.querySelectorAll(".sidebar-dropdown").forEach(dropdown => {
                    dropdown.classList.remove("show");
                });

                // Si el enlace pertenece a un dropdown, abrir el menú y resaltar la opción
                let parentDropdown = this.closest(".sidebar-dropdown");
                if (parentDropdown) {
                    parentDropdown.classList.add("show");
                    let parentItem = parentDropdown.closest(".sidebar-item");
                    if (parentItem) {
                        parentItem.querySelector(".has-dropdown").classList.add("active");
                    }
                }
            });
        });

        // Verificar la URL para marcar como activo el enlace correspondiente al cargar la página
        let currentPage = window.location.pathname.split("/").pop();
        sidebarLinks.forEach(link => {
            if (link.getAttribute("onclick")?.includes(currentPage)) {
                link.classList.add("active");

                // Si está dentro de un dropdown, abrirlo
                let parentDropdown = link.closest(".sidebar-dropdown");
                if (parentDropdown) {
                    parentDropdown.classList.add("show");
                    let parentItem = parentDropdown.closest(".sidebar-item");
                    if (parentItem) {
                        parentItem.querySelector(".has-dropdown").classList.add("active");
                    }
                }
            }
        });
    });
</script>

<!-- Script para mostrar alertas >-->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Alertas de los botones de reiniciar, eliminar y activar usuario
        document.getElementById('contenido').addEventListener('submit', function (e) {
            if (e.target.classList.contains('form-restart')) {
                e.preventDefault();
                Swal.fire({
                    title: '¿Estás seguro?',
                    text: "Se reiniciará la contraseña del usuario a su username",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#0d6efd',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, reiniciar',
                    cancelButtonText: 'Cancelar',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        e.target.submit();
                    }
                });
            } else if (e.target.classList.contains('form-delete')) {
                e.preventDefault();
                Swal.fire({
                    title: "¿Desactivar al usuario?",
                    text: "El usuario será desactivado y no podrá acceder al sistema.",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#dc3545',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, desactivar',
                    cancelButtonText: 'Cancelar',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        e.target.submit();
                    }
                });
            } else if (e.target.classList.contains('form-activate')) {
                e.preventDefault();
                Swal.fire({
                    title: "¿Activar usuario?",
                    text: "El usuario podrá volver a acceder al sistema.",
                    icon: "info",
                    showCancelButton: true,
                    confirmButtonColor: '#198754',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, activar',
                    cancelButtonText: 'Cancelar',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        e.target.submit();
                    }
                });
            }
        });

        // Alerta para registrar usuario
        document.getElementById('contenido').addEventListener('submit', function (e) {
            if (e.target.id === 'formUsuario') {
                e.preventDefault();

                const nombre = document.getElementById("nombre").value.trim();
                const rol = document.getElementById("rol").value;
                const dni = document.getElementById("numberDocumentoHidden").value.trim();

                // Validación del nombre
                if (nombre === "Error al consultar documento" || nombre === "") {
                    Swal.fire({
                        icon: 'error',
                        title: 'Documento inválido',
                        text: 'Debe buscar un número de documento válido antes de registrar.',
                    });
                    return; // cortar aquí para que no continúe
                }
                // Validación del rol
                if (rol === "Seleccionar Rol" || rol === "") {
                    Swal.fire({
                        icon: 'error',
                        title: 'Rol no seleccionado',
                        text: 'Debe seleccionar un rol válido.',
                    });
                    return;
                }
                // Validación del dni
                if (!dni || dni.trim() === "") {
                    Swal.fire({
                        icon: 'error',
                        title: 'DNI inválido',
                        text: 'Debe ingresar un DNI válido.',
                    });
                    return;
                }

                Swal.fire({
                    title: '¿Registrar usuario?',
                    text: "Se agregará un nuevo usuario al sistema.",
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#198754',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, registrar',
                    cancelButtonText: 'Cancelar',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        e.target.submit();
                    }
                });
            }
        });

        /*// Alerta para registrar cliente
        document.getElementById('contenido').addEventListener('submit', function (e) {
            if (e.target.id === 'formCliente') {
                e.preventDefault();
                Swal.fire({
                    title: '¿Registrar cliente?',
                    text: "Se agregará un nuevo cliente al sistema.",
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#198754',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, registrar',
                    cancelButtonText: 'Cancelar',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        e.target.submit();
                    }
                });
            }
        });*/

        // Alerta para editar usuario
        document.getElementById('contenido').addEventListener('submit', function (e) {
            if (e.target.id === 'formEditarUsuario') {
                e.preventDefault();
                Swal.fire({
                    title: '¿Guardar cambios?',
                    text: "Se actualizará la información del usuario.",
                    icon: 'info',
                    showCancelButton: true,
                    confirmButtonColor: '#198754',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, guardar',
                    cancelButtonText: 'Cancelar',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        e.target.submit();
                    }
                });
            }
        });

        // Alerta para registrar pisos, tipos de habitacion y habitaciones
        document.getElementById('contenido').addEventListener('submit', function (e) {
            if (e.target.id === 'formPiso') {
                e.preventDefault();

                const nombre = document.getElementById("nombre").value.trim();

                const params = new URLSearchParams(window.location.search);
                if (params.get("error") === "pisoexistente") {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'El nombre del piso ya existe.',
                    });
                } else {
                    // Validación del nombre
                    if (nombre === "") {
                        Swal.fire({
                            icon: 'Error',
                            title: 'Debe ingresar un nombre para el piso.',
                            text: 'Debe ingresar un nombre para el piso.',
                        });
                        return; // cortar aquí para que no continúe
                    }
                    // Validación del rol
                    if (nombre.length < 6) {
                        Swal.fire({
                            icon: 'Error',
                            title: 'El nombre del piso es muy corto',
                            text: 'El nombre del piso debe tener al menos 6 caracteres.',
                        });
                        return;
                    }

                    Swal.fire({
                        title: '¿Registrar Nuevo Piso?',
                        text: "Se agregará un nuevo piso al sistema.",
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonColor: '#198754',
                        cancelButtonColor: '#6c757d',
                        confirmButtonText: 'Sí, registrar',
                        cancelButtonText: 'Cancelar',
                        allowOutsideClick: false,
                        allowEscapeKey: false
                    }).then((result) => {
                        if (result.isConfirmed) {
                            e.target.submit();
                        }
                    });
                }
            }

        });
        document.getElementById('contenido').addEventListener('submit', function (e) {
            if (e.target.id === 'formTipo') {
                e.preventDefault();

                const nombre = document.getElementById("nombre").value.trim();

                const params = new URLSearchParams(window.location.search);
                if (params.get("error2") === "tiporoomexistente") {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'El tipo de habitacion ya existe.',
                    });
                } else {
                    // Validación del nombre
                    if (nombre === "") {
                        Swal.fire({
                            icon: 'Error',
                            title: 'Debe ingresar un nombre para el Tipo de Habitación.',
                            text: 'Debe ingresar un nombre para el Tipo de Habitación.',
                        });
                        return; // cortar aquí para que no continúe
                    }

                    Swal.fire({
                        title: '¿Registrar Nuevo Tipo de Habitación?',
                        text: "Se agregará un nuevo tipo de habitación al sistema.",
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonColor: '#198754',
                        cancelButtonColor: '#6c757d',
                        confirmButtonText: 'Sí, registrar',
                        cancelButtonText: 'Cancelar',
                        allowOutsideClick: false,
                        allowEscapeKey: false
                    }).then((result) => {
                        if (result.isConfirmed) {
                            e.target.submit();
                        }
                    });
                }
            }

        });
    });

</script>

<!-- Script para cargar el calendario -->
<script>
    function iniciarCalendario() {
        const fecha = new Date();
        const opcionesFecha = {year: 'numeric', month: 'long', day: 'numeric'};
        const fechaTexto = fecha.toLocaleDateString('es-ES', opcionesFecha);
        document.getElementById('currentDate').textContent = fecha.toLocaleDateString('es-ES', opcionesFecha);


        const calendarEl = document.getElementById('calendar');
        const calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            selectable: true,
            locale: 'es',
            timeZone: 'America/Lima',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
            },
            buttonText: {
                today: 'Hoy',
                month: 'Mes',
                week: 'Semana',
                day: 'Día',
                list: 'Lista'
            },
            select: function (info) {
                // Formatear fechas para los inputs datetime-local
                const entrada = new Date(info.start);
                const salida = new Date(info.end);
                const formatoISO = (fecha) => fecha.toISOString().slice(0, 16); // yyyy-mm-ddThh:mm

                // Autocompletar los campos de fecha y hora del modal
                document.getElementById('fechaEntradaRango').value = formatoISO(entrada);
                document.getElementById('fechaSalidaRango').value = formatoISO(salida);

                // Mostrar el modal de rango
                const rangoModal = new bootstrap.Modal(document.getElementById('rangoReservaModal'));
                rangoModal.show();

                // Asignar la acción de submit al formulario de rango
                document.getElementById('formRango').onsubmit = function (e) {
                    e.preventDefault();
                    const nombreCliente = document.getElementById('nombreRango').value.trim();
                    const habitacion = document.getElementById('habitacionRango').value;
                    const start = document.getElementById('fechaEntradaRango').value;
                    const end = document.getElementById('fechaSalidaRango').value;

                    if (nombreCliente && habitacion && start && end) {
                        calendar.addEvent({
                            title: `Reserva - ${nombreCliente}`,
                            room: `Habitación - ${habitacion}`,
                            start: start,
                            end: end,
                            allDay: false
                        });
                        rangoModal.hide();
                        e.target.reset();
                        alert("✅ ¡Reserva registrada exitosamente!");
                        console.log("Reserva registrada:", nombreCliente, habitacion, start, end);
                    } else {
                        alert("⚠️ Por favor, completa todos los campos para registrar la reserva.");
                    }
                };
            }
        });

        calendar.render();

        // Guardar la reserva desde el modal principal "Agregar Reserva"
        document.querySelector('#modalAgregarReserva .btn-success').addEventListener('click', function (e) {
            e.preventDefault();
            const nombreCliente = document.getElementById('nombre').value.trim();
            const habitacion = document.getElementById('habitacion').value;
            const start = document.getElementById('fechaEntrada').value;
            const end = document.getElementById('fechaSalida').value;

            if (nombreCliente && habitacion && start && end) {
                calendar.addEvent({
                    title: `Reserva - ${nombreCliente}`,
                    room: `Habitación - ${habitacion}`,
                    start: start,
                    end: end,
                    allDay: false
                });

                const modal = bootstrap.Modal.getInstance(document.getElementById('modalAgregarReserva'));
                modal.hide();
                document.getElementById('formReserva').reset();
                alert("✅ ¡Reserva registrada exitosamente!");
                console.log("Reserva registrada:", nombreCliente, habitacion, start, end);
            } else {
                alert("⚠️ Por favor, completa todos los campos para registrar la reserva.");
            }
        });
    }
</script>

<script>
    //validacion correo
    document.addEventListener('submit', function (e) {
        const form = e.target;

        // Verifica si el formulario que se está enviando es uno de los que quieres validar
        if (form && (form.id === 'formCliente' || form.id === 'formEditarCliente' || form.id === 'formUsuario'
            || form.id === 'formEditarUsuario')) {
            let correoInput;

            // Selecciona el campo de correo correspondiente según el formulario
            if (form.id === 'formCliente') {
                correoInput = form.querySelector('#correo'); // Para el formulario de 'formCliente'
            } else if (form.id === 'formEditarCliente') {
                correoInput = form.querySelector('#correoEditar'); // Para el formulario de 'formEditarCliente'
            } else if (form.id === 'formUsuario') {
                correoInput = form.querySelector('#correo'); // Para el formulario de 'formUsuario'
            } else if (form.id === 'formEditarUsuario') {
                correoInput = form.querySelector('#correoEditar'); // Para el formulario de 'formEditarUsuario'
            }

            if (correoInput) {
                const correo = correoInput.value.trim();
                const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(?:\.[a-zA-Z]{2,})?$/;

                if (!regexCorreo.test(correo)) {
                    e.preventDefault(); // Detiene el envío del formulario

                    Swal.fire({
                        icon: 'warning',
                        title: 'Correo inválido',
                        text: 'Ejemplo válido: ejemplo@dominio.com',
                        timer: 3000,
                        showConfirmButton: false,
                        position: 'center',
                        toast: true
                    });
                }
            }
        }
    });
</script>
<script>
    const params = new URLSearchParams(window.location.search);
    if (params.get("error") === "pisoexistente") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El nombre del piso ya existe.',
        });
    }
    if (params.get("error2") === "tiporoomexistente") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El tipo de habitacion ya existe.',
        });
    }
    if (params.get("error") === "nombrepisoexistente") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El nombre del piso ya existe.',
        });
    }
</script>
</body>
</html>