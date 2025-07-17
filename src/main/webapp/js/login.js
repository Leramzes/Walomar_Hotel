document.querySelector("form").addEventListener("submit", function (e) {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const captcha = grecaptcha.getResponse();
    const alerta = document.getElementById("captcha-alert");

    // Verifica si campos están vacíos
    if (username === "" || password === "") {
        e.preventDefault();
        mostrarAlerta("⚠️ Por favor, completa todos los campos.");
        return;
    }

    // Verifica si captcha fue resuelto
    if (captcha.length === 0) {
        e.preventDefault();
        mostrarAlerta("⚠️ Verifica el captcha antes de continuar.");
        return;
    }
});

// Función para mostrar alerta personalizada
function mostrarAlerta(mensaje) {
    const alerta = document.getElementById("captcha-alert");
    alerta.textContent = mensaje;
    alerta.classList.add("show");
    alerta.classList.remove("hide");

    setTimeout(() => {
        alerta.classList.add("hide");
        alerta.classList.remove("show");
    }, 4000);
}