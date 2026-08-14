/* Refuerza la navegación desde el historial y proporciona un acceso directo
   al inicio de la cuenta para pantallas pequeñas. */
(function () {
  var inicio = document.querySelector('.navegacion a[href$="/PanelUsuario.jsp"]');
  if (!inicio) return;

  window.addEventListener('pageshow', function (evento) {
    if (evento.persisted) window.location.reload();
  });

  if (!document.body.querySelector('.mobile-home')) {
    var enlace = document.createElement('a');
    enlace.className = 'mobile-home';
    enlace.href = inicio.href;
    enlace.setAttribute('aria-label', 'Volver al inicio');
    enlace.innerHTML = '&#8962;<span>Inicio</span>';
    document.body.appendChild(enlace);
  }
}());
