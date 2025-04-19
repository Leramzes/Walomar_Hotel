<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hotel Transylvania</title>
  <link rel="stylesheet" href="css/global.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
  <link rel="stylesheet" href="css/login.css">
</head>

<body>
<div class="container d-flex justify-content-center align-items-center min-vh-100">
  <div class="row border rounded-5 p-3 bg-white shadow box-area">
    <div class="col-md-6 rounded-4 d-flex justify-content-center align-items-center flex-column left-box p-3" style="background: #0e2238;">
      <div class="featured-image">
        <img src="img/hotelLogo.png" alt="Hotel Transylvania" class="img-fluid" style="max-width: 100%; height: auto;">
      </div>
    </div>

    <div class="col-md-6 right-box">
      <div class="row align-items-center">
        <div class="header-text mb-4">
          <h2>Hola de Nuevo</h2>
          <p>Nos alegra tenerte de vuelta.</p>
        </div>

        <form action="login" method="post">
          <div class="input-group mb-3">
            <label for="username" class="input-group-text"><i class="fas fa-user"></i></label>
            <input type="text" id="username" name="username" class="form-control form-control-lg bg-light fs-6" placeholder="Nombre Usuario">
          </div>

          <div class="input-group mb-3">
            <label for="password" class="input-group-text"><i class="fas fa-lock"></i></label>
            <input type="password" id="password" name="password" class="form-control form-control-lg bg-light fs-6" placeholder="Contraseña">
          </div>

          <div class="input-group mb-5 d-flex justify-content-between">
            <div class="form-check">
              <input type="checkbox" class="form-check-input" id="formCheck">
              <label for="formCheck" class="form-check-label text-secondary">
                <small>Recordar</small>
              </label>
            </div>
            <div class="forgot">
              <small><a href="#">¿Has olvidado tu contraseña?</a></small>
            </div>
          </div>
          <div class="input-group mb-3">
            <button type="submit" class="btn btn-lg w-100 fs-6 text-white" style="background: rgb(24,59,113);">
              Iniciar Sesión
            </button>
          </div>
        </form>
        <!--
        <div class="row">
          <small>¿No tienes cuenta? <a href="registro.jsp">Regístrate</a></small>
        </div>
        -->
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/login.js"></script>
</body>
</html>