function toggleSidebar() {
  document.querySelector(".barrainicioadmin").classList.toggle("open");
}

function toggleSubmenu(event) {
  event.preventDefault();
  const parent = event.target.closest(".submenu");
  parent.classList.toggle("open");
}
