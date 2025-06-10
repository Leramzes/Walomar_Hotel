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
            document.getElementById("restanteDetalle").value = data.pago_total - data.adelanto;
            document.getElementById("proximo").innerText = "Próximamente se validarán pagos";
        })
        .catch(error => console.error("Error al obtener datos:", error));
}
function editarReserva(id) {

    fetch("reservatioController?action=get&idreserva=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {

            document.getElementById("nombreEditar").value = data.clientName;
            document.getElementById("tipoDocumentoEditar").value = data.documentType;
            document.getElementById("documentoEditar").value = data.documentNumber;
            document.getElementById("correoEditar").value = data.email;
            document.getElementById("telefonoEditar").value = data.phone;
            /*document.getElementById("tipoHabitacionDetalle").value = data.roomType;
            document.getElementById("habitacionDetalle").value = data.numberRoom;
            document.getElementById("fechaEntradaDetalle").value = data.checkInDate;
            document.getElementById("fechaSalidaDetalle").value = data.checkOutDate;
            document.getElementById("descuentoDetalle").value = data.dsct;
            document.getElementById("cobroExtraDetalle").value = data.cobro_extra;
            document.getElementById("adelantoDetalle").value = data.adelanto;
            document.getElementById("totalPagarDetalle").value = data.pago_total;
            document.getElementById("restanteDetalle").value = data.pago_total - data.adelanto;*/
            document.getElementById("proximo").innerText = "Próximamente se validarán pagos";
        })
        .catch(error => console.error("Error al obtener datos:", error));
}

function editarRoom(id) {
    document.getElementById("inputEditarHabitacion").value = id;

    fetch("roomcontroller?action=get&idroom=" + id)
        .then(response => response.json())  // Convertimos la respuesta a JSON
        .then(data => {
            // Llenar los campos del formulario con los datos obtenidos
            document.getElementById("nombreEditar").value = data.number;
            document.getElementById("tipoeditar").value = data.typeRoom.id;
            document.getElementById("precioEditar").value = data.price;
            document.getElementById("estatusEditar").value = data.statusRoom;
        })
        .catch(error => console.error("Error al obtener datos:", error));
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

/*function Search(wordKey, tableSearch, quantitySearch, controller) {
    var nameFilter = $(wordKey).val().trim();

    $.ajax({
        url: controller,
        data: { filter: nameFilter },
        success: function (result) {
            $(tableSearch).find("tbody").html(result);

            // Extraer la cantidad de registros desde el comentario oculto
            var match = result.match(/<!--COUNT:(\d+)-->/);
            var cantidad = match ? parseInt(match[1]) : 0;

            // Actualizar el input con la cantidad de registros
            $(quantitySearch).val(cantidad);
        },
        error: function () {
            console.error("Error al obtener los datos filtrados.");
        }
    });
}*/
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
        catalogoServicios: "jsp/catalogoServicios.jsp",
        habitacionesServicio: "jsp/habitacionesServicio.jsp",
        reservaCalendario: "jsp/reservaCalendario.jsp"

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
        pisos: "configuracion"
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
    const  msj = $("#habitacion option:selected").attr("data-msj") || "";
    let descuento = parseFloat($("#descuento").val()) || 0;
    let cobroExtra = parseFloat($("#cobroExtra").val()) || 0;
    let adelanto = parseFloat($("#adelanto").val()) || 0;

    let dias = calcularDias(); // Obtener cantidad de días (mínimo 1)
    let subtotal = precio * dias; // Precio total por los días

    let total = subtotal - (subtotal * (descuento / 100)) + cobroExtra - adelanto;
    total = total < 0 ? 0 : total;

    document.querySelector("#totalPagar").value = total.toFixed(2);
    document.querySelector("#msjRoom").innerHTML = msj.replace(/\\n/g, "<br>");

    if(status === 2){
        document.querySelector("#msjStatus").innerHTML = "Habitación actualmente ocupada.";
        document.querySelector("#msjRoom").innerHTML = "";
    }else if(status === 3){
        document.querySelector("#msjStatus").innerHTML = "Habitación actualmente en mantenimiento.";
        document.querySelector("#btnGuardar").disabled = true;
    }else {
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
function actDscRecepcion(){
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
        const nuevoModalReserva = new bootstrap.Modal(modalReservaElement);
        nuevoModalReserva.show();

        const campoDescuento = document.getElementById("descuento");
        const iconoDescuento = document.getElementById("iconoDescuento");

        if (result.isConfirmed && result.value === true) {
            campoDescuento.disabled = false;
            iconoDescuento.className = "fas fa-unlock text-success"; // Candado abierto verde
        } else {
            campoDescuento.disabled = true;
            iconoDescuento.className = "fas fa-lock text-danger"; // Candado cerrado rojo
        }
    });
}

