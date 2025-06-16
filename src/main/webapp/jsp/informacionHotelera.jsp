<%@ page import="development.team.hoteltransylvania.Business.GestionInformationHotel" %>
<%@ page import="development.team.hoteltransylvania.Model.InformationHotel" %>
<%@ page import="development.team.hoteltransylvania.Model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Información del Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        .img-preview {
            display: none;
            max-width: 100%;
            max-height: 200px;
            margin-top: 10px;
            margin-left: auto;
            margin-right: auto;
            display: block;
            border: none;
            object-fit: contain;
        }
    </style>
</head>

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

    InformationHotel hotelInfo = GestionInformationHotel.getInformationHotel();
%>

<body>
<div class="d-flex justify-content-between align-items-center">
    <h4><i class="fa-solid fa-gears me-2"></i> Información del Hotel</h4>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Configuración</a></li>
            <li class="breadcrumb-item active" aria-current="page">Información del Hotel</li>
        </ol>
    </nav>
</div>

<div class="modal fade" id="modalCambiarImg" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cambiar Imagen</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <form id="formImgHotel" action="hotelcontrol" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="accion" value="2">

                    <div class="mb-3">
                        <label for="nuevaImagen">Nueva Imagen</label>
                        <input type="file" id="nuevaImagen" name="nuevaImagen" accept="image/*" class="form-control" required>
                        <img id="preview" src="" alt="Vista previa" class="img-preview">
                    </div>

                    <button type="submit" class="btn btn-success">Guardar</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Sección de Información del Hotel -->
<div class="card mt-4">
    <div class="card-header" style="background: #0e2238;">
    </div>
    <div class="card-body">
        <!-- Imagen actual del hotel -->
        <div class="text-center mb-3">
            <img id="previewLogo" src="${pageContext.request.contextPath}/img/imagenWalomar.jpg" class="img-fluid"
                 style="max-width: 50%; height: auto;" alt="Hotel Transylvania" style="max-width: 100%; height: auto;">
        </div>

        <%--<div class="text-center mb-3">
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalCambiarImg">
                <i class="fas fa-plus"></i> Cambiar Imagen
            </button>
        </div>--%>

        <form id="formHotel" action="hotelcontrol" method="post">
            <input type="hidden" id="editIndex">
            <input type="hidden" name="accion" value="1">
            <div class="mb-3">
                <label for="nombreHotel">Nombre del Hotel</label>
                <input type="text" class="form-control" id="nombreHotel" name="nombreHotel"
                       value="<%=hotelInfo.getName()%>" required>
            </div>
            <div class="mb-3">
                <label for="telefonoHotel">Teléfono</label>
                <input type="text" class="form-control" id="telefonoHotel" name="telefonoHotel"
                       value="<%=hotelInfo.getPhone()%>"
                       pattern="\d{9}"
                       oninput="this.value = this.value.replace(/\D/g, '').slice(0,9); this.setCustomValidity('')"
                       oninvalid="this.setCustomValidity('Debe ingresar exactamente 9 dígitos numéricos')"
                       required>
            </div>
            <div class="mb-3">
                <label for="correoHotel">Correo</label>
                <input type="email" class="form-control" id="correoHotel" name="correoHotel"
                       value="<%=hotelInfo.getEmail()%>"
                       pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
                       title="Debe ingresar un correo válido como ejemplo@dominio.com"
                       oninvalid="this.setCustomValidity('Ingrese un correo electrónico válido. Ejemplo: ejemplo@dominio.com')"
                       oninput="this.setCustomValidity('')"
                       required>
            </div>
            <div class="mb-3">
                <label for="ubicacionHotel">Ubicación</label>
                <input type="text" class="form-control" value="<%=hotelInfo.getAddress()%>" id="ubicacionHotel"
                       name="ubicacionHotel" required>
            </div>
            <button type="submit" class="btn btn-success">Guardar</button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/script.js"></script>
</body>
</html>