document.addEventListener("DOMContentLoaded", function () {
    // Girar tarjeta al hacer clic en el botón contenedor,
    // pero NO cuando el clic viene del enlace "Agenda tu actividad"
    document.querySelectorAll(".tarjeta-actividad-boton").forEach(function (tarjeta) {
        tarjeta.addEventListener("click", function (e) {
            // Si el clic proviene del enlace de agenda, dejar que navegue sin girar
            if (e.target.closest(".tarjeta-agenda-btn")) return;

            const girada = tarjeta.classList.toggle("esta-girada");
            tarjeta.setAttribute("aria-pressed", girada ? "true" : "false");
        });
    });

    // Evitar que el clic en el enlace propague hacia el botón padre
    document.querySelectorAll(".tarjeta-agenda-btn").forEach(function (enlace) {
        enlace.addEventListener("click", function (e) {
            e.stopPropagation();
            // Navegar manualmente por si el browser bloquea <a> dentro de <button>
            window.location.href = enlace.getAttribute("href");
        });
    });
});
