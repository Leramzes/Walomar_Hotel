async function exportarClientesPDF() {
    // Accede al módulo jsPDF desde el global
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // Obtener fecha y hora actual
    const fecha = new Date().toLocaleDateString('es-PE');
    const hora = new Date().toLocaleTimeString('es-PE');

    // Título
    doc.setFontSize(14);
    doc.text("REPORTE DE CLIENTES", 15, 20);

    doc.setFontSize(10);
    doc.text(`Fecha: ${fecha}`, 15, 27);
    doc.text(`Hora: ${hora}`, 60, 27);

    // Obtener la tabla
    const tabla = document.querySelector("#tablaClients");
    if (!tabla) {
        Swal.fire("Error", "No se encontró la tabla", "error");
        return;
    }

    const filas = tabla.querySelectorAll("tbody tr");
    const datos = [];

    filas.forEach(tr => {
        // Ignora filas ocultas (filtros)
        if (tr.style.display === "none") return;

        const celdas = tr.querySelectorAll("td");
        if (celdas.length < 6) return;

        datos.push([
            celdas[0].innerText.trim(), // N°
            celdas[1].innerText.trim(), // Nombre
            celdas[2].innerText.trim(), // Tipo Documento
            celdas[3].innerText.trim(), // Número Documento
            celdas[4].innerText.trim(), // Correo
            celdas[5].innerText.trim(), // Teléfono
        ]);
    });

    if (datos.length === 0) {
        Swal.fire("Error", "No hay datos visibles para exportar", "error");
        return;
    }

    // Encabezado de columnas
    const columnas = ["N°", "Nombre", "Tipo Doc.", "N° Documento", "Correo", "Teléfono"];

    // Generar tabla en PDF
    doc.autoTable({
        head: [columnas],
        body: datos,
        startY: 35,
        theme: 'grid',
        styles: {
            fontSize: 9,
            halign: 'center',
            cellPadding: 3
        },
        headStyles: {
            fillColor: [255, 193, 7], // amarillo tipo Bootstrap warning
            textColor: 0,
            fontStyle: 'bold'
        },
        columnStyles: {
            0: { cellWidth: 10 },
            1: { halign: 'left', cellWidth: 50 },
            2: { cellWidth: 20 },
            3: { cellWidth: 30 },
            4: { cellWidth: 50 },
            5: { cellWidth: 30 }
        }
    });

    // Guardar el PDF
    const nombreArchivo = `Reporte_Clientes_${new Date().toISOString().replace(/[:.-]/g, '')}.pdf`;
    doc.save(nombreArchivo);

    Swal.fire("¡Éxito!", "Reporte exportado correctamente", "success");
}