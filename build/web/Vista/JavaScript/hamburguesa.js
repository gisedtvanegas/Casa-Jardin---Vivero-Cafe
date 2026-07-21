/* hamburguesa.js — Toggle del menú responsive
   Se puede incluir en cualquier página que tenga .barrainicio */
(function () {
  var toggle = document.getElementById('nav-toggle');
  var nav    = document.getElementById('nav-menu');
  if (!toggle || !nav) return;

  toggle.addEventListener('click', function () {
    nav.classList.toggle('active');
    toggle.classList.toggle('active');
    toggle.setAttribute('aria-expanded', nav.classList.contains('active'));
  });

  nav.querySelectorAll('a').forEach(function (link) {
    link.addEventListener('click', function () {
      nav.classList.remove('active');
      toggle.classList.remove('active');
      toggle.setAttribute('aria-expanded', 'false');
    });
  });
}());
