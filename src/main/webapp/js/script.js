// Función para expandir o contraer el sidebar
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('expand');

    // Guardar el estado en localStorage
    localStorage.setItem('sidebarExpanded', sidebar.classList.contains('expand'));
}

// Asignar la función al botón de toggle
document.querySelector('.toggle-btn').addEventListener('click', toggleSidebar);

// Restaurar el estado del sidebar al cargar la página
document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.getElementById('sidebar');
    const isExpanded = localStorage.getItem('sidebarExpanded') === 'true';

    if (isExpanded) {
        sidebar.classList.add('expand');
    } else {
        sidebar.classList.remove('expand');
    }
});

// Función para cargar el contenido sin actualizar el sidebar
function cargarPagina(pagina, view = "", limpiarView = false) {
    // Actualizar la URL con el nuevo 'view'
    if (!view) {
        // Si 'view' no se pasa, obtenerlo del enlace clickeado
        const link = document.querySelector(`[onclick="cargarPagina('${pagina}')"]`);
        view = link ? link.getAttribute("data-pagina") : "";
    }

    if (view) {
        // Actualizar la URL con el nuevo 'view'
        history.replaceState({}, document.title, `${window.location.pathname}?view=${view}`);
    }

    fetch(pagina)  // Agrega la extensión .jsp a la página solicitada
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar la página: ' + pagina);
            }
            return response.text();
        })
        .then(html => {
            document.getElementById('contenido').innerHTML = html;

            // Esperar a que el DOM del nuevo contenido esté disponible
            setTimeout(() => {
                if (pagina === "jsp/inicio.jsp") {
                    iniciarGrafica(); // Ejecutar solo si es la página de inicio
                } else if (pagina === "jsp/reservaCalendario.jsp") {
                    // Ejecuta la función de inicialización del calendario si es la vista del calendario
                    if (typeof iniciarCalendario === 'function') {
                        iniciarCalendario();
                    }
                }
            }, 100);
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('contenido').innerHTML = '<div class="alert alert-danger">No se pudo cargar el contenido.</div>';
        });
}

function iniciarGrafica() {
    const canvas = document.getElementById("graficaIngresos");
    if (!canvas) {
        console.error("No se encontró el elemento canvas con id 'graficaIngresos'");
        return;
    }

    const ctx = canvas.getContext("2d");

    // Datos de ingresos por año
    const datosIngresos = {
        "2023": [4500, 3200, 5000, 4800, 5200, 6000, 6300, 6100, 5800, 6200, 6400, 7000],
        "2024": [6500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        "2025": [7000, 7300, 7500, 7800, 8000, 8200, 8500, 8700, 8900, 9100, 9400, 9600]
    };

    // Obtener el valor del select
    const filtroAnio = document.getElementById("filtroAnio");

    // Crear la gráfica
    let chart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
            datasets: [{
                label: "Ingresos",
                data: datosIngresos[filtroAnio.value], // Cargar los datos según el año seleccionado
                backgroundColor: "rgba(54, 162, 235, 0.5)",
                borderColor: "rgba(54, 162, 235, 1)",
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            scales: {
                y: {beginAtZero: true}
            }
        }
    });

    // Evento para cambiar los datos cuando se seleccione otro año
    filtroAnio.addEventListener("change", function () {
        const nuevoAnio = filtroAnio.value;
        chart.data.datasets[0].data = datosIngresos[nuevoAnio]; // Actualizar datos
        chart.update(); // Refrescar la gráfica
    });
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".btnEditarProducto").forEach(button => {
        button.addEventListener("click", function () {
            let id = this.getAttribute("data-id");
            let name = this.getAttribute("data-name");
            let price = this.getAttribute("data-price");
            console.log(id)

            document.getElementById("inputEditarIdProducto").value = id;
            document.getElementById("nombreEditar").value = name;
            document.getElementById("precioVentaEditar").value = price;
        });
    });
});

function abrirModalEditar(id) {
    document.getElementById("inputEditarIdProducto").value = id;
    fetch("productcontrol?action=get&idproduct=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            document.getElementById("nombreEditar").value = data.name;
            document.getElementById("precioVentaEditar").value = data.price;
            document.getElementById("cantidadEditar").value = data.quantity;
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function abrirModalEditarServicio(id) {
    document.getElementById("inputEditarIdServicio").value = id;
    fetch("serviciocontrol?action=get&idservice=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            document.getElementById("nombreEditar").value = data.name;
            document.getElementById("precioServicioEditar").value = data.price;
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function editarClient(id) {
    document.getElementById("inputEditarCliente").value = id;

    fetch("clientcontrol?action=get&idclient=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            if (data.name === "-") {
                document.getElementById("nombreEditar").value = data.razonSocial;
            } else {
                document.getElementById("nombreEditar").value = data.name + " " + data.apPaterno + " " + data.apMaterno;
            }
            document.getElementById("tipoDocumentoEditar").value = data.typeDocument;
            document.getElementById("documentoEditar").value = data.numberDocument;
            document.getElementById("correoEditar").value = data.email;
            document.getElementById("telefonoEditar").value = data.telephone;
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function detalleReserva(id) {

    fetch("reservatioController?action=get&idreserva=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            document.getElementById("nombreDetalle").value = data.clientName;
            document.getElementById("tipoDocumentoDetalle").value = data.documentType;
            document.getElementById("documentoDetalle").value = data.documentNumber;
            document.getElementById("correoDetalle").value = data.email;
            document.getElementById("telefonoDetalle").value = data.phone;
            document.getElementById("tipoHabitacionDetalle").value = data.roomType;
            document.getElementById("habitacionDetalle").value = data.numberRoom;
            document.getElementById("fechaEntradaDetalle").value = data.checkInDate;
            document.getElementById("fechaSalidaDetalle").value = data.checkOutDate;
            document.getElementById("descuentoDetalle").value = data.dsct;
            document.getElementById("cobroExtraDetalle").value = data.cobro_extra;
            document.getElementById("adelantoDetalle").value = data.adelanto;
            document.getElementById("totalPagarDetalle").value = data.pago_total;

            const restante = data.pago_total - data.adelanto;

            const inputRestante = document.getElementById("restanteDetalle");
            const inputTotalPagar = document.getElementById("totalPagarDetalle");
            const inputEstadoPago = document.getElementById("estadoPagoDetalle");
            const fechaIngreso = document.getElementById("fechaIngresoDetalle");
            const fechaDesalojo = document.getElementById("fechaDesalojoDetalle");

            // Controlar visibilidad de fechas según estado
            if (data.reservationStatus === "Finalizada") {
                fechaIngreso.parentElement.style.display = "block";
                fechaIngreso.value = data.fecha_ingreso || ""; // debes asegurarte de que esto venga del backend
                fechaDesalojo.parentElement.style.display = "block";
                fechaDesalojo.value = data.fecha_desalojo || "";

                inputRestante.parentElement.style.display = "none";
                inputTotalPagar.style.border = "1px solid #ced4da";
                inputEstadoPago.value = "Pago completo";
                inputEstadoPago.parentElement.style.display = "block";
            } else if (data.reservationStatus === "Ocupada") {
                fechaIngreso.parentElement.style.display = "block";
                fechaIngreso.value = data.fecha_ingreso || "";
                fechaDesalojo.parentElement.style.display = "none";

                inputRestante.parentElement.style.display = "block";
                inputRestante.value = restante.toFixed(2);
                inputTotalPagar.style.border = "2px solid green";
                inputTotalPagar.style.borderRadius = "5px";
                inputEstadoPago.value = "En proceso";
                inputEstadoPago.parentElement.style.display = "block";
            } else if(data.reservationStatus === "Pendiente") {
                // Pendiente
                fechaIngreso.parentElement.style.display = "none";
                fechaDesalojo.parentElement.style.display = "none";

                inputRestante.parentElement.style.display = "block";
                inputRestante.value = restante.toFixed(2);
                inputTotalPagar.style.border = "2px solid green";
                inputTotalPagar.style.borderRadius = "5px";
                inputEstadoPago.value = "En proceso";
                inputEstadoPago.parentElement.style.display = "block";
            } else {
                fechaIngreso.parentElement.style.display = "none";
                fechaDesalojo.parentElement.style.display = "none";
                inputEstadoPago.parentElement.style.display = "none";
                inputTotalPagar.style.border = "2px solid";
                inputRestante.parentElement.style.display = "none";
            }
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function editarReserva(id) {
    fetch("reservatioController?action=get&idreserva=" + id)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            document.getElementById("habitacionEditar").value = data.roomType + ' - ' + data.numberRoom;
            document.getElementById("fechaEntradaEditarView").value = formatearFecha(data.checkInDate);
            document.getElementById("fechaSalidaEditarView").value = formatearFecha(data.checkOutDate);
            document.getElementById("idReservaEdit").value = data.idReservation;
            document.getElementById("habitacionIdEdit").value = data.idRoom;

            // Asignar al input oculto
            document.getElementById("fechaEntradaEditar").value = formatearFecha(data.checkInDate);

            const estado = data.reservationStatus; // Suponiendo que viene como 'pendiente' o 'ocupada'
            const checkInDate = new Date(data.checkInDate);
            const ahora = new Date();

            let minDate;

            if (estado === "Ocupada" && ahora > checkInDate) {
                // Ya ingresó, mínimo 1 día después de la fecha de entrada
                minDate = new Date(checkInDate);
                minDate.setDate(minDate.getDate() + 1);
            } else {
                // Aún no ha llegado (pendiente), mínimo 1 día después de check-in
                minDate = new Date(checkInDate);
                minDate.setDate(minDate.getDate() + 1);
            }

            const year = minDate.getFullYear();
            const month = String(minDate.getMonth() + 1).padStart(2, '0');
            const day = String(minDate.getDate()).padStart(2, '0');
            const hour = String(minDate.getHours()).padStart(2, '0');
            const minute = String(minDate.getMinutes()).padStart(2, '0');

            const minSalida = `${year}-${month}-${day}T${hour}:${minute}`;
            document.getElementById("fechaSalidaEditar").setAttribute("min", minSalida);
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function formatearFecha(fecha) {
    if (!fecha) return "";

    const d = new Date(fecha);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hour = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day}T${hour}:${minutes}`;
}

function editarRoom(id) {
    document.getElementById("inputEditarHabitacion").value = id;

    fetch(`checkreservations?id=${id}`)
        .then(res => res.json())
        .then(data => {
            if (data.hasReservations) {
                Swal.fire({
                    icon: 'error',
                    title: 'No se puede Editar',
                    text: 'Esta habitación no se puede editar debido a que tiene reservas próximas/pendientes.'
                });
            } else {
                fetch("roomcontroller?action=get&idroom=" + id)
                    .then(response => response.json())  // Convertimos la respuesta a JSON
                    .then(data => {
                        // Llenar los campos del formulario con los datos obtenidos
                        document.getElementById("nombreEditar").value = data.number;
                        document.getElementById("tipoeditar").value = data.typeRoom.id;
                        document.getElementById("precioEditar").value = data.price;
                        document.getElementById("estatusEditar").value = data.statusRoom;
                        // ✅ Ahora que todo está listo, abre el modal manualmente
                        const modal = new bootstrap.Modal(document.getElementById("modalEditarHabitacion"));
                        modal.show();
                    })
                    .catch(error => console.error("Error al obtener datos:", error));
            }
        }).catch(error => {
        console.error("Error en fetch:", error);
        Swal.fire({
            icon: 'error',
            title: 'Error al conectar',
            text: 'No se pudo verificar la habitación. Revisa la ruta del servlet.'
        });
    });


}

function editarTypeRoom(id) {
    document.getElementById("inputEditarTipoHabitacion").value = id;

    fetch("typeroomcontroller?action=get&idtyperoom=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            document.getElementById("nombreEditar").value = data.name;
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function editarFloor(id) {
    document.getElementById("inputEditarPiso").value = id;

    fetch("floorcontroller?action=get&idfloor=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            document.getElementById("nombreEditar").value = data.name;
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function editarUser(id) {
    document.getElementById("inputEditarUsuario").value = id;

    fetch("user?action=get&idEmployee=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            document.getElementById("nombreEditar").value = data.name_employee;
            document.getElementById("correoEditar").value = data.email_user;
            document.getElementById("usuarioEditar").value = data.name_user;
            document.getElementById("estatusEditar").value = data.estado_user;
            // Obtener el select y recorrer sus opciones
            // Mapeo del texto del rol a su valor en el <select>
            let rolMap = {
                "Administrador": "1",
                "Recepcionista": "2"
            };
            document.getElementById("rolEditar").value = rolMap[data.tipo_user] || ""; // Si no encuentra coincidencia, deja vacío
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function buscar() {
    var nameFilter = $("#nameSearch").val();
    $.ajax({
        url: "filterProducServlet",
        data: {filter: nameFilter},
        success: function (result) {
            // Insertar la tabla filtrada
            $("#tablaCatalagoProductos").html(result);

            // Extraer la cantidad de productos desde el comentario oculto
            var match = result.match(/<!--COUNT:(\d+)-->/);
            var cantidad = match ? match[1] : 0;

            // Actualizar el input con la cantidad de registros
            $("#sizeProducts").val(cantidad);
        }
    });
}

function buscarCliente() {
    var nameFilter = $("#numberDocument").val();
    $.ajax({
        url: "filterClientUniq",
        data: {filter: nameFilter},
        success: function (result) {
            // Insertar la tabla filtrada
            $("#datosCliente").html(result);
        }
    });
}

function buscarClienteRecepcion() {
    var numberFilter = $("#busquedaCliente").val();
    $.ajax({
        url: "filterClientRecp",
        data: {filter: numberFilter},
        success: function (result) {
            // Mostrar el contenedor si estaba oculto
            $("#dataClientRecepcion").css("display", "block");
            // Insertar la tabla filtrada
            $("#dataClientRecepcion").html(result);
        }
    });
}

window.SearchReporte = function (wordKey, stateKey, quantitySearch, controller, page = 1, size = 10) {
    var fecha = $(wordKey).val();
    var empleadoId = $(stateKey).val();
    var pestañaActivaId = $(".tab-pane.active").attr("id");
    var pst = 1;
    if (pestañaActivaId === "alquiler") pst = 1;
    else if (pestañaActivaId === "habitacion-venta") pst = 2;
    else if (pestañaActivaId === "habitacion-venta-directa") pst = 3;

    $.ajax({
        url: controller,
        data: {
            fecha: fecha,
            empleadoId: empleadoId,
            page: page,
            size: size,
            pst: pst
        },
        success: function (result) {
            let tbodyId = "";

            // Determina a qué tabla se va a insertar según la pestaña
            if (pst === 1) tbodyId = "#reporteAlquiler";
            else if (pst === 2) tbodyId = "#tablaServiciosHab";
            else if (pst === 3) tbodyId = "#tablaReportesVD";

            $(tbodyId).find("tbody").html(result);

            // Leer el comentario con el número total de registros del servidor
            var match = result.match(/<!--COUNT:(\d+)-->/);
            var totalRecords = match ? parseInt(match[1]) : 0;

            // ✅ Aquí se actualiza el valor del input "registros"
            $(quantitySearch).val($(tbodyId).find("tbody tr").length);

            if (pst === 3) {
                let totalMatch = result.match(/<!--TOTAL_EMPLEADO:(\d+(\.\d{1,2})?)-->/);
                if (totalMatch) {
                    $("#totalVentaEmpleadoVD").text("S/." + totalMatch[1]);
                } else {
                    $("#totalVentaEmpleadoVD").text("S/.0.00");
                }
            }

            // Actualizar la paginación
            updatePagination(totalRecords, page, size, wordKey, stateKey, tableSearch, quantitySearch, controller);
        },
        error: function () {
            console.error("Error al obtener los datos filtrados.");
        }
    });
}

window.Search = function (wordKey, stateKey, tableSearch, quantitySearch, controller, page = 1, size = 10) {
    console.log($(wordKey));
    var nameFilter = $(wordKey).val().trim();
    var stateFilter = $(stateKey).val().trim();
    console.log("Filtros enviados:", {filter: nameFilter, estate: stateFilter, page, size});

    $.ajax({
        url: controller,
        data: {
            filter: nameFilter,
            estate: stateFilter,
            page: page,
            size: size
        },
        success: function (result) {
            $(tableSearch).find("tbody").html(result);

            // Extraer la cantidad de registros desde el comentario oculto
            var match = result.match(/<!--COUNT:(\d+)-->/);
            var totalRecords = match ? parseInt(match[1]) : 0;

            // Actualizar el input con la cantidad de registros
            $(quantitySearch).val($(tableSearch).find("tbody tr").length);

            // Actualizar la paginación
            updatePagination(totalRecords, page, size, wordKey, stateKey, tableSearch, quantitySearch, controller);
        },
        error: function () {
            console.error("Error al obtener los datos filtrados.");
        }
    });
}

function updatePagination(totalRecords, currentPage, size, wordKey, stateKey, tableSearch, quantitySearch, controller) {
    const totalPages = Math.ceil(totalRecords / size);
    const paginationContainer = $("#pagination");
    paginationContainer.empty();

    // Contenedor donde quieres mostrar el mensaje
    const noDataContainer = $("#no-data");
    noDataContainer.empty();

    if (totalPages === 0) {
        noDataContainer.html('<div class="alert alert-info w-100 text-center">No hay datos para mostrar</div>');
        return;
    }

    noDataContainer.empty(); // Limpiamos el mensaje si había uno

    const prevPage = currentPage > 1 ? currentPage - 1 : 1;
    const nextPage = currentPage < totalPages ? currentPage + 1 : totalPages;

    // Botón "Anterior"
    paginationContainer.append(
        `<li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="if(${currentPage === 1}) return false; Search('${wordKey}', '${stateKey}', '${tableSearch}', '${quantitySearch}', '${controller}', ${prevPage}, ${size})">Anterior</a>
        </li>`
    );

    // Números de página
    for (let i = 1; i <= totalPages; i++) {
        paginationContainer.append(
            `<li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="Search('${wordKey}', '${stateKey}', '${tableSearch}', '${quantitySearch}', '${controller}', ${i}, ${size})">${i}</a>
            </li>`
        );
    }

    // Botón "Siguiente"
    paginationContainer.append(
        `<li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="if(${currentPage === totalPages}) return false; Search('${wordKey}', '${stateKey}', '${tableSearch}', '${quantitySearch}', '${controller}', ${nextPage}, ${size})">Siguiente</a>
        </li>`
    );
}

window.filtrarTablaReserva = function (wordKey1, wordKey2, wordKey3, wordKey4, wordKey5, tableSearch, quantitySearch, controller,
                                       page = 1, size = 10) {
    var clientFilter = $(wordKey1).val().trim();
    var docFilter = $(wordKey2).val().trim();
    var fecDesdeFilter = $(wordKey3).val().trim();
    var fecHastaFilter = $(wordKey4).val().trim();
    var statusFilter = $(wordKey5).val().trim();

    $.ajax({
        url: controller,
        data: {
            clientFilter: clientFilter,
            docFilter: docFilter,
            fecDesdeFilter: fecDesdeFilter,
            fecHastaFilter: fecHastaFilter,
            statusFilter: statusFilter,
            page: page,
            size: size
        },
        success: function (result) {
            console.log("Respuesta:", result);
            $(tableSearch).find("tbody").html(result);

            // Extraer la cantidad de registros desde el comentario oculto
            var match = result.match(/<!--COUNT:(\d+)-->/);
            var totalRecords = match ? parseInt(match[1]) : 0;

            // Actualizar el input con la cantidad de registros
            $(quantitySearch).val($(tableSearch).find("tbody tr").length);

            // Actualizar la paginación
            updatePaginationReserva(totalRecords, page, size, wordKey1, wordKey2, wordKey3, wordKey4, wordKey5, tableSearch, quantitySearch, controller);
        },
        error: function () {
            console.error("Error al obtener los datos filtrados.");
        }
    });
}

function updatePaginationReserva(totalRecords, currentPage, size, wordKey1, wordKey2, wordKey3, wordKey4, wordKey5, tableSearch, quantitySearch, controller) {
    const totalPages = Math.ceil(totalRecords / size);
    const paginationContainer = $("#pagination");
    paginationContainer.empty();

    // Contenedor donde quieres mostrar el mensaje
    const noDataContainer = $("#no-data");
    noDataContainer.empty();

    if (totalPages === 0) {
        noDataContainer.html('<div class="alert alert-info w-100 text-center">No hay datos para mostrar</div>');
        return;
    }

    noDataContainer.empty(); // Limpiamos el mensaje si había uno

    const prevPage = currentPage > 1 ? currentPage - 1 : 1;
    const nextPage = currentPage < totalPages ? currentPage + 1 : totalPages;

    // Botón "Anterior"
    paginationContainer.append(
        `<li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="if(${currentPage === 1}) return false; 
    filtrarTablaReserva('${wordKey1}', '${wordKey2}', '${wordKey3}', '${wordKey4}', '${wordKey5}', '${tableSearch}', '${quantitySearch}', '${controller}', 
    ${prevPage}, ${size})">Anterior</a>
        </li>`
    );

    // Números de página
    for (let i = 1; i <= totalPages; i++) {
        paginationContainer.append(
            `<li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" 
                onclick="filtrarTablaReserva('${wordKey1}', '${wordKey2}', '${wordKey3}', '${wordKey4}', '${wordKey5}',
                 '${tableSearch}', '${quantitySearch}', '${controller}', ${i}, ${size})">${i}</a>
            </li>`
        );
    }

    // Botón "Siguiente"
    paginationContainer.append(
        `<li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="if(${currentPage === totalPages}) return false; 
    filtrarTablaReserva('${wordKey1}', '${wordKey2}', '${wordKey3}', '${wordKey4}', '${wordKey5}', '${tableSearch}', '${quantitySearch}', '${controller}', 
    ${nextPage}, ${size})">Siguiente</a>
        </li>`
    );
}

document.addEventListener("DOMContentLoaded", function () {
    // Obtener parámetros de la URL
    const params = new URLSearchParams(window.location.search);
    let view = params.get("view") || "inicio";
    let page = params.get("page") || 1; // Obtener el número de la página

    const paginas = {
        inicio: "jsp/inicio.jsp",
        reserva: "jsp/reserva.jsp",
        recepcion: "jsp/recepcion.jsp",
        venderProducto: "jsp/venderProducto.jsp",
        catalogoProductos: "jsp/catalagoProductos.jsp",
        verificacionSalidas: "jsp/verificacionSalidas.jsp",
        clientes: "jsp/clientes.jsp",
        reporteDiario: "jsp/reporteDiario.jsp",
        reporteMensual: "jsp/reporteMensual.jsp",
        usuarios: "jsp/usuarios.jsp",
        informacionHotelera: "jsp/informacionHotelera.jsp",
        habitaciones: "jsp/habitaciones.jsp",
        habitacionesTipo: "jsp/habitacionesTipo.jsp",
        pisos: "jsp/pisos.jsp",
        habitacionesVenta: "jsp/habitacionesVenta.jsp",
        procesoSalida: "jsp/procesoSalida.jsp",
        ventaDirecta: "jsp/ventaDirecta.jsp",
        procesarHabitacion: "jsp/procesarHabitacion.jsp",
        venderServicios: "jsp/venderServicios.jsp",
        catalogoServicios: "jsp/catalagoServicios.jsp",
        habitacionesServicio: "jsp/habitacionesServicio.jsp",
        reservaCalendario: "jsp/reservaCalendario.jsp",
        metodosPago: "jsp/metodosPago.jsp"

    };

    if (paginas[view]) {
        let url = paginas[view];
        if (page && page !== 1) { // Solo agrega page si no es la página 1
            url += `?page=${page}`;
        }
        cargarPagina(url, view);
    }

    document.querySelectorAll(".pagination .page-link").forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault(); // Evita la recarga de la página

            let url = new URL(this.href, window.location.origin);
            let newPage = url.searchParams.get("page");

            if (newPage) {
                params.set("page", newPage);
                window.location.href = `${window.location.pathname}?${params.toString()}`;
            }
        });
    });

    const expandirMenu = {
        venderProductos: "puntoVenta",
        catalogoProductos: "puntoVenta",
        venderServicio: "puntoVenta",
        catalogoServicios: "puntoVenta",
        reporteDiario: "reportes",
        reporteMensual: "reportes",
        informacionHotelera: "configuracion",
        habitaciones: "configuracion",
        habitacionesTipo: "configuracion",
        pisos: "configuracion",
        metodosPago: "configuracion"
    };

    if (expandirMenu[view]) {
        const menu = document.getElementById(expandirMenu[view]);
        if (menu) {
            menu.classList.add("show");
            const link = document.querySelector(`[data-bs-target='#${expandirMenu[view]}']`);
            if (link) link.setAttribute("aria-expanded", "true");
        }
    }

    // Marcar como "active" la opción correcta en el sidebar
    document.querySelectorAll(".sidebar-link").forEach(item => {
        let paginaSeleccionada = item.getAttribute("data-pagina");

        if (paginaSeleccionada === view) {
            // Remover la clase "active" de todos antes de asignarla a la correcta
            document.querySelectorAll(".sidebar-link").forEach(link => link.classList.remove("active"));
            item.classList.add("active");
        }

        // Evento para cambiar de vista sin recargar la página
        item.addEventListener("click", function (e) {
            e.preventDefault(); // Evita la recarga de la página

            if (paginas[paginaSeleccionada]) {
                cargarPagina(paginas[paginaSeleccionada], paginaSeleccionada);
            }
        });
    });
});

// Define la función updateTotal primero
window.updateTotal = function () {
    let precio = parseFloat($("#habitacion option:selected").attr("data-precio")) || 0;
    let status = parseInt($("#habitacion option:selected").attr("data-status"));
    const msj = $("#habitacion option:selected").attr("data-msj") || "";
    let descuento = parseFloat($("#descuento").val()) || 0;
    let cobroExtra = parseFloat($("#cobroExtra").val()) || 0;
    let adelanto = parseFloat($("#adelanto").val()) || 0;

    let dias = calcularDias(); // Obtener cantidad de días (mínimo 1)
    let subtotal = precio * dias; // Precio total por los días

    let total = subtotal - (subtotal * (descuento / 100)) + cobroExtra - adelanto;
    total = total < 0 ? 0 : total;

    document.querySelector("#totalPagar").value = total.toFixed(2);
    document.querySelector("#msjRoom").innerHTML = msj.replace(/\\n/g, "<br>");

    if (status === 2) {
        document.querySelector("#msjStatus").innerHTML = "Habitación actualmente ocupada.";
        document.querySelector("#msjRoom").innerHTML = "";
    } else if (status === 3) {
        document.querySelector("#msjStatus").innerHTML = "Habitación actualmente en mantenimiento.";
        document.querySelector("#btnGuardar").disabled = true;
    } else {
        document.querySelector("#msjStatus").innerHTML = ""; // limpiar o poner otro mensaje si deseas
    }
};

// Luego, cuando el DOM esté listo, asigna los eventos //ESTA ES LA FUNCION CLAVE
$(document).on("change keyup", "#descuento, #cobroExtra, #adelanto, #fechaEntrada, #fechaSalida", function () {
    updateTotal();
});


// Define la función updateTotal primero
window.updateTotalRecepcion = function () {
    let precio = parseFloat($("#habitacionRecep").attr("data-precio")) || 0;
    let descuento = parseFloat($("#descuentoRecep").val()) || 0;
    let cobroExtra = parseFloat($("#cobroExtraRecep").val()) || 0;
    let adelanto = parseFloat($("#adelantoRecep").val()) || 0;

    let dias = calcularDiasRecep(); // Obtener cantidad de días (mínimo 1)
    let subtotal = precio * dias; // Precio total por los días

    let total = subtotal - (subtotal * (descuento / 100)) + cobroExtra - adelanto;
    total = total < 0 ? 0 : total;

    document.querySelector("#totalPagarRecep").value = total.toFixed(2);

};

// Luego, cuando el DOM esté listo, asigna los eventos //ESTA ES LA FUNCION CLAVE
$(document).on("change keyup", "#descuentoRecep, #cobroExtraRecep, #adelantoRecep, #fechaEntradaRecep, #fechaSalidaRecep", function () {
    updateTotalRecepcion();
});


// Función para obtener habitaciones según el tipo seleccionado
window.getRoomsByType = function (tipoHabitacion) {
    var tipoHabitacionId = $(tipoHabitacion).val().trim();

    $.ajax({
        url: "getRooms",
        data: {
            filter: tipoHabitacionId
        },
        success: function (result) {
            $("#combRooms").html(result);

            setTimeout(() => {
                updateTotal();
            }, 100); // Esperamos 100ms para asegurarnos de que el DOM se haya actualizado

            // Reasignamos evento para que funcione siempre
            $("#habitacion").off("change").on("change", updateTotal);
        }
    });
};

function calcularDiasRecep() {
    let fechaEntrada = new Date(document.getElementById("fechaEntradaRecep").value);
    let fechaSalida = new Date(document.getElementById("fechaSalidaRecep").value);

    if (!isNaN(fechaEntrada) && !isNaN(fechaSalida)) {
        let diferencia = Math.ceil((fechaSalida - fechaEntrada) / (1000 * 60 * 60 * 24)); // Diferencia en días
        return diferencia >= 1 ? diferencia : 1; // Siempre al menos 1 día
    }
    return 1; // Valor por defecto
}

function calcularDias() {
    let fechaEntrada = new Date(document.getElementById("fechaEntrada").value);
    let fechaSalida = new Date(document.getElementById("fechaSalida").value);

    if (!isNaN(fechaEntrada) && !isNaN(fechaSalida)) {
        let diferencia = Math.ceil((fechaSalida - fechaEntrada) / (1000 * 60 * 60 * 24)); // Diferencia en días
        return diferencia >= 1 ? diferencia : 1; // Siempre al menos 1 día
    }
    return 1; // Valor por defecto
}


document.getElementById('documento').addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault(); // Previene que el formulario se envíe automáticamente

        const dni = this.value.trim();

        if (dni.length === 8) {
            fetch(`https://api.apis.net.pe/v1/dni?numero=${dni}`, {
                headers: {
                    "Authorization": "apis-token-14450.89dFFK5wtOjb14SJuyXGFU65rWPJHkal" // Reemplaza con tu token real
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.nombres) {
                        const nombreCompleto = `${data.nombres} ${data.apellidoPaterno} ${data.apellidoMaterno}`;
                        document.getElementById('nombre').value = nombreCompleto;
                    } else {
                        alert('DNI no encontrado');
                    }
                })
                .catch(error => {
                    console.error('Error al consultar el DNI:', error);
                });
        } else {
            alert('Ingrese un DNI válido de 8 dígitos.');
        }
    }
});

function buscarDNI() {
    var documento = $("#documento").val();
    var ap_pater = $("#ap_pater").val();
    var ap_mater = $("#ap_mater").val();
    var tipo_documento = $("#tipoDocumento").val();
    var documento = $("#documento").val();
    $.ajax({
        url: "apireniec",
        data: {
            numDoc: documento,
            typeDoc: tipo_documento
        },
        success: function (result) {
            let data = JSON.parse(result);
            if (tipo_documento === "DNI-user") {
                if (data.nombres) {
                    let nombreCompleto = data.nombres + " " + data.apellidoPaterno + " " +
                        data.apellidoMaterno;
                    $("#nombre").val(nombreCompleto);
                    $("#numberDocumentoHidden").val(documento);
                    $("#username").val(documento);
                } else {
                    $("#nombre").val("No encontrado");
                    $("#username").val("-");
                }
            } else if (tipo_documento === "DNI") {
                if (data.nombres) {
                    $("#nombre").val(data.nombres);
                    $("#ap_pater").val(data.apellidoPaterno);
                    $("#ap_mater").val(data.apellidoMaterno);
                    $("#numberDocumentoHidden").val(documento);
                    $("#tipoDocumentoHidden").val(tipo_documento);
                } else {
                    $("#nombre").val("No encontrado");
                    $("#ap_pater").val("-");
                    $("#ap_mater").val("-");
                }
            } else if (tipo_documento === "RUC") {
                if (data.razonSocial) {
                    $("#raz_social").val(data.razonSocial);
                    $("#direccion").val(data.direccion);
                    $("#numberDocumentoHidden").val(documento);
                    $("#tipoDocumentoHidden").val(tipo_documento);
                } else {
                    $("#raz_social").val("No encontrado");
                    $("#direccion").val("-");
                }
            } else {
                $("#raz_social").val("No encontrado");
            }
        },
        error: function (xhr) {
            let errorMsg = "Error al consultar documento";
            if (xhr.responseJSON && xhr.responseJSON.error) {
                errorMsg = xhr.responseJSON.error;
            }
            $("#nombre").val(errorMsg);
            $("#username").val("-");
            $("#ap_pater").val("-");
            $("#ap_mater").val("-");
            $("#raz_social").val(errorMsg);
            $("#direccion").val("-");
            $("#numberDocumentoHidden").val("");
            $("#tipoDocumentoHidden").val("");
        }
    });
}

function mostrarOcultarBoton() {
    const tipo = document.getElementById("tipoDocumento").value;
    const boton = document.getElementById("btnBuscar");
    const inputNombre = document.getElementById("div_nombre");
    const inputApPatern = document.getElementById("div_ap_pater");
    const inputApMatern = document.getElementById("div_ap_mater");
    const inputRazon = document.getElementById("div_raz_social");
    const inputDireccion = document.getElementById("div_direccion");
    const inputNacion = document.getElementById("div_nacionalidad");
    const documentPrincipal = document.getElementById("documentPrincipal");
    const div_docPas = document.getElementById("div_docPas");
    let tipoDocumento = $("#tipoDocumento").val();

    if (tipo === "Pasaporte") {
        boton.style.display = "none";
        inputDireccion.style.display = "block";
        document.getElementById("nombre").removeAttribute("readonly");
        document.getElementById("ap_pater").removeAttribute("readonly");
        document.getElementById("ap_mater").removeAttribute("readonly");
        document.getElementById("documentoPas").required = true;
        document.getElementById("documento").required = false;
        inputNombre.classList.remove("d-none")
        documentPrincipal.classList.add("d-none");
        div_docPas.classList.remove("d-none");
        inputApPatern.classList.remove("d-none")
        inputApMatern.classList.remove("d-none")
        inputNacion.classList.remove("d-none")
        inputRazon.classList.add("d-none");
        $("#tipoDocumentoHidden").val(tipoDocumento);
        document.getElementById("direccion").removeAttribute("readonly");
    } else if (tipo === "DNI") {
        const documentoInput = document.getElementById("documento");
        // Configurar input para RUC
        documentoInput.setAttribute("maxlength", "8");
        documentoInput.setAttribute("pattern", "\\d{8}");
        documentoInput.setAttribute("oninput", "this.value = this.value.replace(/\\D/g, '').slice(0,8)");

        document.getElementById("documentoPas").required = false;
        document.getElementById("documento").required = true;
        boton.style.display = "inline-block";
        document.getElementById("nombre").setAttribute("readonly", true);
        document.getElementById("ap_pater").setAttribute("readonly", true);
        document.getElementById("ap_mater").setAttribute("readonly", true);
        inputNombre.classList.remove("d-none")
        inputApPatern.classList.remove("d-none")
        inputApMatern.classList.remove("d-none")
        inputRazon.classList.add("d-none");
        inputNacion.classList.add("d-none");
        documentPrincipal.classList.remove("d-none");
        div_docPas.classList.add("d-none");
        document.getElementById("direccion").removeAttribute("readonly");
    } else if (tipo === "RUC") {
        const documentoInput = document.getElementById("documento");
        // Configurar input para DNI
        documentoInput.setAttribute("maxlength", "11");
        documentoInput.setAttribute("pattern", "\\d{11}");
        documentoInput.setAttribute("oninput", "this.value = this.value.replace(/\\D/g, '').slice(0,11)");

        document.getElementById("documentoPas").required = false;
        document.getElementById("documento").required = true;
        boton.style.display = "inline-block";
        inputNombre.classList.add("d-none")
        inputApPatern.classList.add("d-none");
        inputApMatern.classList.add("d-none");
        inputRazon.classList.remove("d-none")
        inputNacion.classList.add("d-none");
        document.getElementById("direccion").setAttribute("readonly", true);
        documentPrincipal.classList.remove("d-none");
        div_docPas.classList.add("d-none");
    }
}

// Ejecutar cuando el modal se abre
document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("modalAgregarCliente");
    modal.addEventListener("shown.bs.modal", function () {
        mostrarOcultarBoton();
    });
});

<!-- Script para manejar la lógica de habilitar/deshabilitar el input -->
function toggleRadioInput() {
    const isLastSelected = document.getElementById("downloadLast").checked;
    document.getElementById("numLast").disabled = !isLastSelected;
}

function actDscRecepcion() {
    Swal.fire({
        title: "Ingrese clave de administrador",
        input: "password",
        inputLabel: "Clave",
        inputAttributes: {
            autocapitalize: "off"
        },
        showCancelButton: true,
        confirmButtonText: "Validar",
        preConfirm: (clave) => {
            if (clave !== "1234") {
                Swal.showValidationMessage("Clave incorrecta");
                return false;
            }
            return true;
        }
    }).then((result) => {
        const campoDescuento = document.getElementById("descuentoRecep");
        const iconoDescuento = document.getElementById("iconoDescuento");

        if (result.isConfirmed && result.value === true) {
            campoDescuento.disabled = false;
            iconoDescuento.className = "fas fa-unlock text-success"; // Candado abierto verde
            updateTotalRecepcion();
        } else {
            campoDescuento.disabled = true;
            iconoDescuento.className = "fas fa-lock text-danger"; // Candado cerrado rojo
            updateTotalRecepcion();
        }
    });
}

function abrirModalClave() {
    const modalReservaElement = document.getElementById('modalAgregarReserva');
    const modalReservaExistente = bootstrap.Modal.getInstance(modalReservaElement);
    if (modalReservaExistente) {
        modalReservaExistente.hide();
    }

    Swal.fire({
        title: "Validación de administrador",
        html:
            '<input id="swal-usuario" class="swal2-input" placeholder="Usuario">' +
            '<input id="swal-clave" type="password" class="swal2-input" placeholder="Clave">',
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: "Validar",
        preConfirm: () => {
            const usuario = document.getElementById("swal-usuario").value.trim();
            const clave = document.getElementById("swal-clave").value.trim();

            if (!usuario || !clave) {
                Swal.showValidationMessage("Ingrese ambos campos");
                return false;
            }

            // Retornar los datos para usarlos en el .then
            return {usuario, clave};
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            validarCredenciales(result.value.usuario, result.value.clave);
        }
    });
}

function validarCredenciales(usuario, clave) {
    $.ajax({
        url: "validarAdmin", // Servlet
        method: "POST",
        data: {
            usuario: usuario,
            clave: clave
        },
        success: function (respuesta) {
            if (respuesta === "OK") {
                // ✅ Usuario válido
                document.getElementById("descuento").disabled = false;
                document.getElementById("iconoDescuento").className = "fas fa-unlock text-success";
                bootstrap.Modal.getOrCreateInstance(document.getElementById("modalAgregarReserva")).show();
            } else {
                Swal.fire("Error", "Credenciales inválidas", "error");
            }
        },
        error: function () {
            Swal.fire("Error", "Ocurrió un error en el servidor", "error");
        }
    });
}

function agregarProducto(idTabla) {
    var productId = $("#selectProducto").val();

    // Si no se seleccionó un producto válido, simplemente no hace nada
    if (!productId || productId.trim() === "") {
        return; // Salir sin hacer nada
    }

    // Buscar si ya existe en la tabla
    var filaExistente = $(idTabla).find("tbody").find("tr[data-id='" + productId + "']");

    if (filaExistente.length > 0) {
        var inputCantidad = filaExistente.find("input.cantidad-producto");
        var cantidadActual = parseInt(inputCantidad.val()) || 0;
        inputCantidad.val(cantidadActual + 1);

        var precioUnit = parseFloat(filaExistente.find("td:nth-child(3)").text().replace("S/.", "").trim()) || 0;
        filaExistente.find("td:nth-child(4)").text("S/. " + ((cantidadActual + 1) * precioUnit).toFixed(2));

        // Recalcular total
        recalcularTotalProducto(idTabla);
    } else {
        $.ajax({
            url: "addTableProduct",
            data: {filter: productId},
            success: function (result) {
                const tbody = $(idTabla).find("tbody");
                tbody.find("td.text-muted").parent().remove();

                tbody.append(result);
                recalcularTotalProducto(idTabla);
            }
        });
    }
}

function recalcularTotalProducto(idTabla) {
    let total = 0;

    $(idTabla + " tbody tr").each(function () {
        const fila = $(this);
        const inputCantidad = fila.find("input.cantidad-producto");
        const cantidad = parseInt(inputCantidad.val()) || 0;
        const precioUnitario = parseFloat(inputCantidad.data("precio")) || 0;
        const subtotal = cantidad * precioUnitario;

        // Actualizar visual
        fila.find("td.precio-total").text("S/. " + subtotal.toFixed(2));

        // Actualizar el input hidden
        fila.find("input[name='precioTotalProduct[]']").val(subtotal.toFixed(2));

        total += subtotal;
    });

    $("#totalGeneral").text("TOTAL: S/. " + total.toFixed(2));
}

function agregarServicio(idTabla) {
    var serviceId = $("#selectServicio").val();

    // Si no se seleccionó un servicio válido, simplemente no hace nada
    if (!serviceId || serviceId.trim() === "") {
        return; // Salir sin hacer nada
    }

    // Verificar si ya está en la tabla
    var filaExistente = $(idTabla).find("tbody").find("tr[data-id='" + serviceId + "']");

    if (filaExistente.length > 0) {
        // Si ya está, no hacer nada (o mostrar un aviso si quieres)
        console.log("Servicio ya agregado.");
        return;
    }

    // Si no está, hacer petición AJAX y agregar la fila
    $.ajax({
        url: "addTableService",
        data: {filter: serviceId},
        success: function (result) {
            const tbody = $(idTabla).find("tbody");

            // Quitar mensaje de "agrega productos/servicios"
            tbody.find("td.text-muted").parent().remove();

            // Añadir nueva fila
            tbody.append(result);

            // Recalcular total
            recalcularTotalServicio(idTabla);
        }
    });
}

function recalcularTotalServicio(idTabla) {
    let total = 0;

    $(idTabla + " tbody tr").each(function () {
        const totalTexto = $(this).find(".precio-total").text().replace("S/.", "").trim();
        if (totalTexto) {
            total += parseFloat(totalTexto);
        }
    });

    $("#totalGeneral").text("TOTAL: S/. " + total.toFixed(2));
}

function validacionVenta() {
    const formVenta = document.getElementById("formVentaDirecta");

    formVenta.addEventListener("submit", function (event) {
        event.preventDefault();

        if (!validarTablaTieneItems(
            "#detalleProductos",
            "Agrega productos",
            "No hay productos agregados",
            "Por favor, agrega al menos un producto antes de continuar."
        )) return;

        const errorBox = document.getElementById("errorMaxStock");
        errorBox.style.display = "none";
        let stockInvalido = false;
        let mensaje = "";

        document.querySelectorAll("input[name='cantProduct[]']").forEach(input => {
            const cantidad = parseInt(input.value);
            const stock = parseInt(input.dataset.stock);
            const nombre = input.dataset.nombre;

            if (cantidad > stock) {
                stockInvalido = true;
                mensaje += `• ${nombre} (Cantidad: ${cantidad}, Stock: ${stock})\n`;
            }
        });

        if (stockInvalido) {
            errorBox.style.display = "block";
            errorBox.innerHTML = `
                <strong>Stock insuficiente:</strong>
                <pre style="margin: 0; white-space: pre-wrap;">${mensaje}</pre>
            `;
            // Ocultar el div después de 5 segundos (5000 milisegundos)
            setTimeout(() => {
                $("#errorMaxStock").fadeOut("slow", function () {
                    $(this).html(""); // Limpia el contenido al terminar el fadeOut
                });
            }, 5000);
            return;
        }

        Swal.fire({
            title: '¿Confirmar venta?',
            text: '¿Deseas registrar esta venta directa?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sí, confirmar',
            cancelButtonText: 'Cancelar',
            confirmButtonColor: '#198754',
            cancelButtonColor: '#6c757d',
        }).then((result) => {
            if (result.isConfirmed) {
                formVenta.submit();
            }
        });
    });
}

function validacionVentaDirecta() {
    const formVenta = document.getElementById("formVentaDirecta");

    formVenta.addEventListener("submit", function (event) {
        event.preventDefault();

        if (!validarTablaTieneItems(
            "#detalleVentaDirecta",
            "Agrega productos",
            "No hay productos agregados",
            "Por favor, agrega al menos un producto antes de continuar."
        )) return;

        const errorBox = document.getElementById("errorMaxStock");
        errorBox.style.display = "none";
        let stockInvalido = false;
        let mensaje = "";

        document.querySelectorAll("input[name='cantProduct[]']").forEach(input => {
            const cantidad = parseInt(input.value);
            const stock = parseInt(input.dataset.stock);
            const nombre = input.dataset.nombre;

            if (cantidad > stock) {
                stockInvalido = true;
                mensaje += `• ${nombre} (Cantidad: ${cantidad}, Stock: ${stock})\n`;
            }
        });

        if (stockInvalido) {
            errorBox.style.display = "block";
            errorBox.innerHTML = `
                <strong>Stock insuficiente:</strong>
                <pre style="margin: 0; white-space: pre-wrap;">${mensaje}</pre>
            `;
            // Ocultar el div después de 5 segundos (5000 milisegundos)
            setTimeout(() => {
                $("#errorMaxStock").fadeOut("slow", function () {
                    $(this).html(""); // Limpia el contenido al terminar el fadeOut
                });
            }, 5000);
            return;
        }

        Swal.fire({
            title: '¿Confirmar venta?',
            text: '¿Deseas registrar esta venta directa?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sí, confirmar',
            cancelButtonText: 'Cancelar',
            confirmButtonColor: '#198754',
            cancelButtonColor: '#6c757d',
        }).then((result) => {
            if (result.isConfirmed) {
                formVenta.submit();
            }
        });
    });
}

function validacionVentaServicio() {
    const formVentaS = document.getElementById("formVentaServicio");

    formVentaS.addEventListener("submit", function (event) {
        event.preventDefault();

        if (!validarTablaTieneItems(
            "#detalleServicios",
            "Agrega servicios",
            "No hay servicios agregados",
            "Por favor, agrega al menos un servicio antes de continuar."
        )) return;

        Swal.fire({
            title: '¿Confirmar venta?',
            text: '¿Deseas registrar esta venta de servicio?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sí, confirmar',
            cancelButtonText: 'Cancelar',
            confirmButtonColor: '#198754',
            cancelButtonColor: '#6c757d',
        }).then((result) => {
            if (result.isConfirmed) {
                formVentaS.submit();
            }
        });
    });
}

function validarTablaTieneItems(idTabla, textoPlaceholder, tituloAlerta, mensajeAlerta) {
    const tbody = document.querySelector(`${idTabla} tbody`);
    const filas = tbody.querySelectorAll("tr");

    if (filas.length === 1 && filas[0].textContent.includes(textoPlaceholder)) {
        Swal.fire({
            icon: 'warning',
            title: tituloAlerta,
            text: mensajeAlerta
        });
        return false;
    }
    return true;
}

function validarCancelacionReserva() {
    const formCancelarReserva = document.getElementById("formCancelarReserva");
    Swal.fire({
        title: '¿Confirmar Cancelación?',
        text: '¿Deseas cancelar esta reserva?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Sí, confirmar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#198754',
        cancelButtonColor: '#6c757d',
    }).then((result) => {
        if (result.isConfirmed) {
            formCancelarReserva.submit();
        }
    });
}

function actualizarTotalConPenalidad() {
    const input = document.getElementById("inputPenalidad");
    const totalElement = document.getElementById("totalFinal");

    if (!input || !totalElement) return;

    const penalidad = parseFloat(input.value) || 0;
    const baseTotal = parseFloat(totalElement.getAttribute("data-base-total")) || 0;

    const nuevoTotal = baseTotal + penalidad;

    totalElement.textContent = "TOTAL: S/. " + nuevoTotal.toFixed(2);
}

async function exportarTablaPDF({
                                    tablaId,
                                    tituloReporte = "REPORTE",
                                    nombreArchivo = "Reporte",
                                    columnas,
                                    nombreEmpresa = "Walomar Hotel",
                                    ruc = "10428703575",
                                    direccion = "Calle Diego Ferre N°102",
                                    telefono = "948036274",
                                    logoPath = 'img/imagenWalomar.jpg'
                                }) {
    const {jsPDF} = window.jspdf;
    const doc = new jsPDF();

    // 1. Mostrar alerta de carga
    Swal.fire({
        title: 'Generando PDF...',
        html: 'Por favor espera unos segundos.',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    const logo = new Image();
    logo.crossOrigin = "anonymous";
    logo.src = logoPath;

    logo.onload = () => {
        const fecha = new Date();
        const fechaTexto = fecha.toLocaleDateString('es-PE');
        const horaTexto = fecha.toLocaleTimeString('es-PE');

        // --- Header ---
        doc.addImage(logo, 'JPG', 15, 10, 30, 30);
        doc.setFontSize(16);
        doc.setFont('helvetica', 'bold');
        doc.text(nombreEmpresa, 50, 18);
        doc.setFontSize(10);
        doc.setFont('helvetica', 'normal');
        doc.text(`RUC: ${ruc}`, 50, 24);
        doc.text(direccion, 50, 30);
        doc.text(`Tel: ${telefono}`, 50, 36);
        doc.setFontSize(9);
        doc.text(`Generado: ${fechaTexto} ${horaTexto}`, 150, 20);

        // --- Título del Reporte ---
        doc.setFontSize(13);
        doc.setFont('helvetica', 'bold');
        doc.text(tituloReporte, 105, 50, {align: "center"});

        // --- Extraer datos de la tabla ---
        const tabla = document.querySelector(`#${tablaId}`);
        if (!tabla) {
            Swal.close();
            Swal.fire("Error", "No se encontró la tabla", "error");
            return;
        }

        const filas = tabla.querySelectorAll("tbody tr");
        const datos = [];

        filas.forEach(tr => {
            if (tr.style.display === "none") return;

            const celdas = tr.querySelectorAll("td");
            if (celdas.length === 0) return;

            const fila = [];
            for (let i = 0; i < columnas.length; i++) {
                fila.push(celdas[i]?.innerText.trim() || '');
            }
            datos.push(fila);
        });

        if (datos.length === 0) {
            Swal.close();
            Swal.fire("Sin datos", "No hay filas visibles para exportar", "info");
            return;
        }

        // --- Generar tabla ---
        doc.autoTable({
            startY: 60,
            head: [columnas],
            body: datos,
            theme: 'grid',
            styles: {
                fontSize: 9,
                cellPadding: 3,
                halign: 'left'
            },
            headStyles: {
                fillColor: [52, 58, 64],
                textColor: [255, 255, 255],
                fontStyle: 'bold',
                halign: 'center'
            },
            alternateRowStyles: {
                fillColor: [245, 245, 245]
            },
            margin: {top: 60, bottom: 30}
        });

        // --- Pie de página ---
        const totalPages = doc.internal.getNumberOfPages();
        for (let i = 1; i <= totalPages; i++) {
            doc.setPage(i);
            doc.setFontSize(8);
            doc.setTextColor(100);
            doc.text(`Página ${i} de ${totalPages}`, 195, 290, {align: "right"});
            doc.text(`Reporte generado por el sistema - ${nombreEmpresa}`, 15, 290);
        }

        // --- Descargar archivo ---
        const timestamp = new Date().toISOString().replace(/[-T:.]/g, "").slice(0, 14);
        doc.save(`${nombreArchivo}_${timestamp}.pdf`);

        Swal.close();
        Swal.fire("¡Éxito!", "PDF generado correctamente", "success");
    };

    logo.onerror = () => {
        Swal.close();
        Swal.fire("Error", "No se pudo cargar el logo", "error");
    };
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function cargarImagen(src) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.crossOrigin = "anonymous";
        img.onload = () => resolve(img);
        img.onerror = reject;
        img.src = src;
    });
}

async function exportarData(tipo) {
    const exportConfigs = {
        clientes: {
            titulo: 'REPORTE DE CLIENTES',
            nombreArchivo: 'Reporte_Clientes',
            columnas: ['#', 'Nombre Completo', 'Tipo Doc.', 'N° Documento', 'Correo', 'Teléfono'],
            campos: [
                (c, i) => i + 1,
                c => c.name === '-' ? c.razonSocial : `${c.name} ${c.apPaterno} ${c.apMaterno}`,
                'typeDocument',
                'numberDocument',
                'email',
                'telephone'
            ]
        },
        usuarios: {
            titulo: 'REPORTE DE USUARIOS',
            nombreArchivo: 'Reporte_Usuarios',
            columnas: ['#', 'Empleado', 'Usuario', 'Correo', 'Rol', 'Fecha Caducidad', 'Estado'],
            campos: [
                (u, i) => i + 1,
                u => u.employee?.name || '',
                u => u.username,
                u => u.employee?.email || '',
                u => u.employee?.position === '1' ? 'Administrador' : u.employee?.position === '2' ? 'Recepcionista' : 'Otro',
                u => u.caducidad,
                u => u.statusUser
            ]
        }
    };

    const config = exportConfigs[tipo];
    if (!config) {
        Swal.fire("Error", "No hay configuración para este tipo", "error");
        return;
    }

    Swal.fire({
        title: 'Generando PDF...',
        html: 'Por favor espera unos segundos.',
        allowOutsideClick: false,
        didOpen: () => Swal.showLoading()
    });

    try {
        await sleep(300); // ❗️espera breve para asegurar que se muestre Swal antes de cargar
        const response = await fetch(`export-data?tipo=${tipo}`);

        const result = await response.json();

        const {empresa, datos} = result;
        const {nombre, ruc, direccion, contacto, logo} = empresa;

        if (!Array.isArray(datos) || datos.length === 0) {
            Swal.close();
            Swal.fire("Sin datos", "No hay información para exportar", "info");
            return;
        }

        const {jsPDF} = window.jspdf;
        const doc = new jsPDF();
        const img = await cargarImagen(logo);

        const fecha = new Date();
        const fechaTexto = fecha.toLocaleDateString('es-PE');
        const horaTexto = fecha.toLocaleTimeString('es-PE');

        doc.addImage(img, 'JPG', 15, 10, 30, 30);
        doc.setFontSize(16);
        doc.setFont('helvetica', 'bold');
        doc.text(nombre, 50, 18);
        doc.setFontSize(10);
        doc.setFont('helvetica', 'normal');
        doc.text(ruc, 50, 24);
        doc.text(direccion, 50, 30);
        doc.text(contacto, 50, 36);
        doc.setFontSize(9);
        doc.text(`Generado: ${fechaTexto} ${horaTexto}`, 150, 20);

        doc.setFontSize(13);
        doc.setFont('helvetica', 'bold');
        doc.text(config.titulo, 105, 50, {align: "center"});

        const tablaDatos = datos.map((item, index) =>
            config.campos.map(c => typeof c === 'function' ? c(item, index) : item[c])
        );

        doc.autoTable({
            startY: 60,
            head: [config.columnas],
            body: tablaDatos,
            theme: 'grid',
            styles: {fontSize: 9, cellPadding: 3},
            headStyles: {
                fillColor: [52, 58, 64],
                textColor: [255, 255, 255],
                fontStyle: 'bold',
                halign: 'center'
            },
            alternateRowStyles: {fillColor: [245, 245, 245]},
            margin: {top: 60, bottom: 30}
        });

        const totalPages = doc.internal.getNumberOfPages();
        for (let i = 1; i <= totalPages; i++) {
            doc.setPage(i);
            doc.setFontSize(8);
            doc.setTextColor(100);
            doc.text(`Página ${i} de ${totalPages}`, 195, 290, {align: "right"});
            doc.text(`Reporte generado por el sistema - ${nombre}`, 15, 290);
        }

        const timestamp = new Date().toISOString().replace(/[-T:.]/g, "").slice(0, 14);
        doc.save(`${config.nombreArchivo}_${timestamp}.pdf`);

        Swal.close();
        Swal.fire("¡Éxito!", "PDF generado correctamente", "success");
    } catch (e) {
        Swal.close();
        console.error("Error al exportar:", e);
        Swal.fire("Error", "Ocurrió un error al generar el PDF", "error");
    }
}

async function generarComprobantePDF(idReserva, idClient) {
    const {jsPDF} = window.jspdf;
    const doc = new jsPDF();

    Swal.fire({
        title: `Generando BOLETA...`,
        html: 'Por favor espera unos segundos.',
        allowOutsideClick: false,
        didOpen: () => Swal.showLoading()
    });

    try {
        // 1. Cargar logo
        const logo = new Image();
        logo.src = 'img/imagenWalomar.jpg';
        await new Promise((resolve, reject) => {
            logo.onload = resolve;
            logo.onerror = reject;
        });

        // 2. Obtener datos desde el backend
        const response = await fetch(`proccheckout?idReserva=${idReserva}&idClient=${idClient}`);
        if (!response.ok) throw new Error("Error al obtener datos del servidor.");
        const data = await response.json();

        const cliente = data.cliente;
        const reserva = data.reserva;
        const productos = data.ventasProd;
        const servicios = data.ventasServ;
        const hotel = data.hotel;

        const now = new Date();
        const fechaTexto = now.toLocaleString('es-PE');

        // --- Marca de agua ---
        const watermarkLogo = new Image();
        watermarkLogo.src = logo.src;
        await new Promise((resolve, reject) => {
            watermarkLogo.onload = resolve;
            watermarkLogo.onerror = reject;
        });
        doc.setGState(new doc.GState({opacity: 0.07}));
        doc.addImage(watermarkLogo, 'JPG', 35, 80, 140, 140);
        doc.setGState(new doc.GState({opacity: 1}));

        // --- Cabecera ---
        doc.addImage(logo, 'JPG', 15, 10, 30, 30);
        doc.setFont('helvetica', 'bold');
        doc.setFontSize(20);
        doc.text("BOLETA", 105, 20, {align: 'center'});

        doc.setFont('helvetica', 'normal');
        doc.setFontSize(10);
        doc.text(`Huésped: ${cliente.name} ` + `${cliente.apPaterno} ` + `${cliente.apMaterno}`, 15, 44);
        doc.text(`Correo: ${cliente.email}`, 15, 50);
        doc.text(`${fechaTexto}`, 195, 53, {align: "right"});

        // --- Tabla Habitación ---
        doc.autoTable({
            startY: 58,
            head: [["Habitación", "Tarifa/Tipo", "F.Entrada", "F.Ingreso", "F.Salida", "F.Desalojo", "Descuento(%)", "Precio"]],
            body: [[
                reserva.numberRoom,
                `24H / ${reserva.roomType}`,
                reserva.checkInDate,
                reserva.fecha_ingreso,
                reserva.checkOutDate,
                reserva.fecha_desalojo,
                `${reserva.dsct}`,
                `S/. ${reserva.pago_total}`
            ]],
            theme: 'grid',
            styles: {fontSize: 9, cellPadding: 3, halign: 'left'},
            headStyles: {fillColor: [52, 58, 64], textColor: [255, 255, 255], fontStyle: 'bold', halign: 'center'},
            alternateRowStyles: {fillColor: [245, 245, 245]},
            margin: {top: 60, bottom: 30}
        });

        // --- Tabla Productos ---
        doc.autoTable({
            startY: doc.lastAutoTable.finalY + 5,
            head: [["Producto", "Precio Unitario", "Cantidad", "Estado", "Subtotal"]],
            body: productos.length > 0
                ? productos.map(p => [
                    p.nombreProducto,
                    `S/. ${p.precioUnitProducto}`,
                    p.cantidad,
                    p.estadoProducto === "Pagado" ? "Pagado" : "Pagado al salir",
                    `S/. ${p.total}`
                ])
                : [[{
                    content: "Sin productos consumidos", colSpan: 5,
                    styles: {halign: 'center', fontStyle: 'italic'}
                }]],
            theme: 'grid',
            styles: {fontSize: 9, cellPadding: 3, halign: 'left'},
            headStyles: {fillColor: [52, 58, 64], textColor: [255, 255, 255], fontStyle: 'bold', halign: 'center'},
            alternateRowStyles: {fillColor: [245, 245, 245]},
            margin: {top: 60, bottom: 30}
        });

        // --- Tabla Servicios ---
        doc.autoTable({
            startY: doc.lastAutoTable.finalY + 5,
            head: [["Servicio", "Estado", "Subtotal"]],
            body: servicios.length > 0
                ? servicios.map(s => [
                    s.nombreServicio,
                    s.estadoServicio === "Pagado" ? "Pagado" : "Pagado al salir",
                    `S/. ${s.total}`
                ])
                : [[{
                    content: "Sin servicios consumidos", colSpan: 3, styles:
                        {halign: 'center', fontStyle: 'italic'}
                }]],
            theme: 'grid',
            styles: {fontSize: 9, cellPadding: 3, halign: 'left'},
            headStyles: {fillColor: [52, 58, 64], textColor: [255, 255, 255], fontStyle: 'bold', halign: 'center'},
            alternateRowStyles: {fillColor: [245, 245, 245]},
            margin: {top: 60, bottom: 30}
        });

        // --- Resumen Final ---
        let resumenY = doc.lastAutoTable.finalY + 8;
        if (resumenY + 70 > 290) {
            doc.addPage();
            resumenY = 20;
        }
        doc.setFontSize(10);
        doc.setTextColor(0);

        // Sumar total productos y servicios (con seguridad)
        const totalProductos = productos
            .filter(p => p.estadoProducto === "Pagado al salir")
            .reduce((acc, p) => acc + (parseFloat(p.total) || 0), 0);
        const totalServicios = servicios
            .filter(s => s.estadoServicio === "Pagado al salir")
            .reduce((acc, s) => acc + (parseFloat(s.total) || 0), 0);
        const totalHabitacion = reserva.pago_total || 0;
        const totalExtra = reserva.cobro_extra || 0;
        const totalAdelanto = reserva.adelanto || 0;
        const totalFinal = totalProductos + totalServicios + totalHabitacion + totalExtra - totalAdelanto;

        doc.text(`Costo Extra:   S/. ${reserva.cobro_extra}`, 15, resumenY);
        doc.text(`Costo Alquiler:   S/. ${reserva.pago_total}`, 15, resumenY + 6);
        doc.text("Dinero Adelantado:   S/. " + totalAdelanto, 15, resumenY + 12);
        doc.text("Servicio a la habitación:   S/. " + (totalProductos + totalServicios), 15, resumenY + 18);
        doc.text("Penalidad:   S/. 0.0", 15, resumenY + 24);

        doc.setFont('helvetica', 'bold');
        doc.setFontSize(11);
        doc.text("Total pagado al salir:   S/. " + totalFinal.toFixed(2), 15, resumenY + 32);

        // --- Footer ---
        const footerY = resumenY + 60;
        doc.setFont('helvetica', 'normal');
        doc.setFontSize(10);
        doc.text(hotel.address, 105, footerY, {align: 'center'});
        doc.text("Tel: +51 " + hotel.phone, 105, footerY + 5, {align: 'center'});
        doc.text("Correo: " + hotel.email, 105, footerY + 10, {align: 'center'});
        doc.text("Gracias por su preferencia, ¡Vuelva pronto!", 105, footerY + 18, {align: 'center'});

        // --- Guardar PDF ---
        const filename = `BOLETA_HAB_${reserva.numberRoom}_${new Date().toISOString().slice(0, 10)}.pdf`;
        doc.save(filename);
        Swal.close();
        Swal.fire({
            title: "¡Éxito!",
            text: "BOLETA generada correctamente",
            icon: "success",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        }).then(() => {
            window.location.href = "menu.jsp?view=reserva";
        });

    } catch (error) {
        console.error("Error en la generación del PDF:", error);
        Swal.close();
        Swal.fire("Error", "Ocurrió un problema al generar la boleta", "error");
    }
}

function generarComprobanteDesdeInputs() {
    const idReserva = document.getElementById("idReserva").value;
    const idClient = document.getElementById("idClient").value;

    generarComprobantePDF(idReserva, idClient); // ← aquí llamas tu función original con los valores correctos
}

async function generarFacturaPDF(idReserva, idClient) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    Swal.fire({
        title: `Generando FACTURA...`,
        html: 'Por favor espera unos segundos.',
        allowOutsideClick: false,
        didOpen: () => Swal.showLoading()
    });

    try {
        // 1. Obtener datos desde backend
        const response = await fetch(`proccheckout?idReserva=${idReserva}&idClient=${idClient}`);
        if (!response.ok) throw new Error("Error al obtener datos del servidor.");
        const data = await response.json();

        const cliente = data.cliente;
        const reserva = data.reserva;
        const productos = data.ventasProd;
        const servicios = data.ventasServ;
        const hotel = data.hotel;

        const logo = new Image();
        logo.src = 'img/imagenWalomar.jpg';


        logo.onload = () => {
            const now = new Date();
            const fechaTexto = now.toLocaleString('es-PE');

            // CABECERA
            doc.addImage(logo, 'JPG', 15, 10, 35, 35);
            doc.setFont('helvetica', 'bold');
            doc.setFontSize(20);
            doc.text("FACTURA", 95, 28);

            // DATOS HOTEL
            doc.setFont('helvetica', 'normal');
            doc.setFontSize(10);
            doc.text(`Empresa: ${hotel.name}`, 15, 50);
            doc.text(`RUC: ${hotel.ruc}`, 15, 56);
            doc.text(`Dirección: ${hotel.address}`, 15, 62);
            doc.text(`Correo: ${hotel.email}`, 15, 68);

            // DATOS CLIENTE
            const boxX = 130, boxY = 44, boxWidth = 65;
            const fullName = `Cliente: ${cliente.name} ${cliente.apPaterno} ${cliente.apMaterno}`;
            const maxWidth = boxWidth - 4;
            const lines = doc.splitTextToSize(fullName, maxWidth);
            const linesCount = lines.length;
            // Altura dinámica: líneas de nombre + líneas fijas para RUC y correo (cada una con 5 de alto)
            const boxHeight = 6 * linesCount + 14;
            doc.setFillColor(245, 245, 245);
            doc.setDrawColor(200);
            doc.rect(boxX, boxY, boxWidth, boxHeight, 'FD');
            doc.setFontSize(10);
            doc.setTextColor(0);
            // Imprimir nombre (multilínea)
            doc.text(lines, boxX + 2, boxY + 6);
            // Imprimir RUC y Correo con buen espaciado
            const offsetY = boxY + 6 + linesCount * 6;
            doc.text(`RUC/DNI: ${cliente.numberDocument}`, boxX + 2, offsetY);
            doc.text(`Correo: ${cliente.email}`, boxX + 2, offsetY + 6);


            doc.text(`Fecha: ${fechaTexto}`, 195, boxY + boxHeight + 7, { align: 'right' });

            // TABLA HABITACIÓN
            const tablaStartY = boxY + boxHeight + 10;
            doc.autoTable({
                startY: tablaStartY,
                head: [["Habitación", "Tarifa/Tipo", "F.Entrada", "F.Ingreso", "F.Salida", "F.Desalojo", "Descuento(%)", "Precio"]],
                body: [[
                    reserva.numberRoom,
                    `24H / ${reserva.roomType}`,
                    reserva.checkInDate,
                    reserva.fecha_ingreso,
                    reserva.checkOutDate,
                    reserva.fecha_desalojo,
                    `${reserva.dsct}`,
                    `S/. ${reserva.pago_total}`
                ]],
                theme: 'grid',
                styles: { fontSize: 9, cellPadding: 3 },
                headStyles: {
                    fillColor: [52, 58, 64],
                    textColor: [255, 255, 255],
                    fontStyle: 'bold',
                    halign: 'center'
                },
                alternateRowStyles: { fillColor: [245, 245, 245] }
            });

            // TABLA PRODUCTOS
            doc.autoTable({
                startY: doc.lastAutoTable.finalY + 5,
                head: [["Producto", "Precio Unitario", "Cantidad", "Estado", "Subtotal"]],
                body: productos.length > 0
                    ? productos.map(p => [
                        p.nombreProducto,
                        `S/. ${p.precioUnitProducto}`,
                        p.cantidad,
                        p.estadoProducto === "Pagado" ? "Pagado" : "Pagado al salir",
                        `S/. ${p.total}`
                    ])
                    : [[{
                        content: "Sin productos consumidos", colSpan: 5,
                        styles: {halign: 'center', fontStyle: 'italic'}
                    }]],
                theme: 'grid',
                styles: { fontSize: 9, cellPadding: 3 },
                headStyles: {
                    fillColor: [52, 58, 64],
                    textColor: [255, 255, 255],
                    fontStyle: 'bold',
                    halign: 'center'
                },
                alternateRowStyles: { fillColor: [245, 245, 245] }
            });

            // TABLA SERVICIOS
            doc.autoTable({
                startY: doc.lastAutoTable.finalY + 5,
                head: [["Servicio", "Estado", "Subtotal"]],
                body: servicios.length > 0
                    ? servicios.map(s => [
                        s.nombreServicio,
                        s.estadoServicio === "Pagado" ? "Pagado" : "Pagado al salir",
                        `S/. ${s.total}`
                    ])
                    : [[{
                        content: "Sin servicios consumidos", colSpan: 3, styles:
                            {halign: 'center', fontStyle: 'italic'}
                    }]],
                theme: 'grid',
                styles: { fontSize: 9, cellPadding: 3 },
                headStyles: {
                    fillColor: [52, 58, 64],
                    textColor: [255, 255, 255],
                    fontStyle: 'bold',
                    halign: 'center'
                },
                alternateRowStyles: { fillColor: [245, 245, 245] }
            });

            // RESUMEN FINAL
            let resumenY = doc.lastAutoTable.finalY + 8;
            if (resumenY + 70 > 290) {
                doc.addPage();
                resumenY = 20;
            }

            doc.setFontSize(10);
            doc.setTextColor(0);

            // Sumar total productos y servicios (con seguridad)
            const totalProductos = productos
                .filter(p => p.estadoProducto === "Pagado al salir")
                .reduce((acc, p) => acc + (parseFloat(p.total) || 0), 0);
            // Solo sumar servicios con estado "pagado al salir"
            const totalServicios = servicios
                .filter(s => s.estadoServicio === "Pagado al salir")
                .reduce((acc, s) => acc + (parseFloat(s.total) || 0), 0);
            const totalHabitacion = reserva.pago_total || 0;
            const totalExtra = reserva.cobro_extra || 0;
            const totalAdelanto = reserva.adelanto || 0;
            const totalFinal = totalProductos + totalServicios + totalHabitacion + totalExtra - totalAdelanto;

            doc.text(`Costo Extra:   S/. ${reserva.cobro_extra}`, 15, resumenY);
            doc.text(`Costo Alquiler:   S/. ${reserva.pago_total}`, 15, resumenY + 6);
            doc.text("Dinero Adelantado:   S/. " + totalAdelanto, 15, resumenY + 12);
            doc.text("Servicio a la habitación:   S/. " + (totalProductos + totalServicios), 15, resumenY + 18);
            doc.text("Penalidad:   S/. 0.0", 15, resumenY + 24);

            doc.setFont('helvetica', 'bold');
            doc.setFontSize(11);
            doc.text(`Total pagado al salir:   S/. ${totalFinal.toFixed(2)}`, 15, resumenY + 32);

            // FOOTER
            const footerY = resumenY + 60;
            doc.setFont('helvetica', 'normal');
            doc.setFontSize(10);
            doc.text(hotel.address, 105, footerY, { align: 'center' });
            doc.text("Tel: +51 " + hotel.phone, 105, footerY + 5, { align: 'center' });
            doc.text("Correo: " + hotel.email, 105, footerY + 10, { align: 'center' });
            doc.text("Gracias por su preferencia, ¡Vuelva pronto a pasarla muy bien!", 105, footerY + 18, { align: 'center' });

            // GUARDAR
            const filename = `FACTURA_HAB_${reserva.numberRoom}_${new Date().toISOString().slice(0, 10)}.pdf`;
            doc.save(filename);
            Swal.close();
            Swal.fire({
                title: "¡Éxito!",
                text: "FACTURA generada correctamente",
                icon: "success",
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                allowOutsideClick: false,
                allowEscapeKey: false,
                allowEnterKey: false
            }).then(() => {
                window.location.href = "menu.jsp?view=reserva";
            });
        };

        logo.onerror = () => {
            Swal.close();
            Swal.fire("Error", "No se pudo cargar el logo", "error");
        };

    } catch (error) {
        console.error("Error en la generación del PDF:", error);
        Swal.close();
        Swal.fire("Error", "Ocurrió un problema al generar la factura", "error");
    }
}

async function culminarYGenerarComprobante() {
    const idReserva = document.getElementById("idReserva").value;
    const idClient = document.getElementById("idClient").value;
    const tipoComprobante = document.getElementById("tipoComprobante").value;
    const enviarCorreo = document.getElementById("enviarCorreo").checked ? "1" : "0";
    let inputPenalidad = document.getElementById("inputPenalidad").value;
    const metodoPago = document.getElementById("metodoPago").value;

    // Validación manual
    if (!tipoComprobante) {
        Swal.fire("Atención", "Debe seleccionar un tipo de comprobante.", "warning");
        return;
    }
    if (!metodoPago) {
        Swal.fire("Atención", "Debe seleccionar un método de pago.", "warning");
        return;
    }
    if (inputPenalidad.trim() === "" || isNaN(inputPenalidad)) {
        inputPenalidad = "0";
    }

    try {
        // 1. Enviar al servlet para registrar salida
        const response = await fetch("registroSalida", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: `idReserva=${encodeURIComponent(idReserva)}&idClient=${encodeURIComponent(idClient)}&tipoComprobante=${encodeURIComponent(tipoComprobante)}&enviarCorreo=${encodeURIComponent(enviarCorreo)}&inputPenalidad=${encodeURIComponent(inputPenalidad)}&metodoPago=${encodeURIComponent(metodoPago)}`
        });

        if (!response.ok) throw new Error("Error en el registro de salida.");

        if (tipoComprobante === "1") {
            await generarComprobantePDF(idReserva, idClient);
        } else if (tipoComprobante === "2") {
            await generarFacturaPDF(idReserva, idClient);
        }

    } catch (error) {
        console.error("Error al registrar salida o generar boleta:", error);
        Swal.fire("Error", "No se pudo completar el proceso", "error");
    }

}