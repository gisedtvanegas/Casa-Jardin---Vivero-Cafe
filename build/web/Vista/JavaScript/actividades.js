document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".tarjeta-actividad-boton").forEach(function (tarjeta) {
        tarjeta.addEventListener("click", function () {
            const girada = tarjeta.classList.toggle("esta-girada");
            tarjeta.setAttribute("aria-pressed", girada ? "true" : "false");
        });
    });
});
