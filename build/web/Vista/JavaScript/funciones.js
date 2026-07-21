const contenedor = document.getElementById("carrusel");
const btnIzq = document.getElementById("left");
const btnDer = document.getElementById("right");

let index = 0;
const total = contenedor.children.length;

function moverCarrusel() {
  const imgWidth = contenedor.children[0].clientWidth;
  contenedor.style.transform = `translateX(-${index * imgWidth}px)`;
}

btnDer.addEventListener("click", () => {
  index = (index + 1) % total; // si llega al final, vuelve a 0
  moverCarrusel();
});

btnIzq.addEventListener("click", () => {
  index = (index - 1 + total) % total; // si llega al inicio, vuelve al último
  moverCarrusel();
});


/* Menú hamburguesa */
(function () {
  const toggle = document.getElementById('nav-toggle');
  const nav    = document.getElementById('nav-menu');
  if (!toggle || !nav) return;

  toggle.addEventListener('click', function () {
    nav.classList.toggle('active');
    toggle.classList.toggle('active');
    toggle.setAttribute('aria-expanded', nav.classList.contains('active'));
  });

  /* Cerrar el menú al hacer click en un enlace */
  nav.querySelectorAll('a').forEach(function (link) {
    link.addEventListener('click', function () {
      nav.classList.remove('active');
      toggle.classList.remove('active');
      toggle.setAttribute('aria-expanded', 'false');
    });
  });
}());
