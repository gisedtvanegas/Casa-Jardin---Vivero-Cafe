const formularioReserva = document.getElementById("reservaForm");
const modalPago = document.getElementById("pagoModal");
const metodoPago = document.getElementById("metodo");
const titularPago = document.getElementById("titular");
const camposTarjeta = document.getElementById("tarjeta");
const camposBilletera = document.getElementById("billetera");
const entradasTarjeta = [...camposTarjeta.querySelectorAll("input")];
const entradasBilletera = [...camposBilletera.querySelectorAll("input")];

function actualizarMetodoPago() {
    const esTarjeta = ["credito", "debito"].includes(metodoPago.value);
    const esBilletera = ["nequi", "daviplata", "dale"].includes(metodoPago.value);
    camposTarjeta.classList.toggle("oculto", !esTarjeta);
    camposBilletera.classList.toggle("oculto", !esBilletera);
    entradasTarjeta.forEach((campo) => campo.required = esTarjeta);
    entradasBilletera.forEach((campo) => campo.required = esBilletera);
    document.getElementById("tipoTarjeta").textContent = metodoPago.value === "credito" ? "Tarjeta de crédito" : metodoPago.value === "debito" ? "Tarjeta débito" : "";
}

metodoPago.addEventListener("change", actualizarMetodoPago);
document.getElementById("abrirPago").addEventListener("click", () => {
    const camposReserva = [...formularioReserva.querySelectorAll("[required]")];
    if (camposReserva.every((campo) => campo.reportValidity())) {
        titularPago.required = true;
        metodoPago.required = true;
        modalPago.classList.add("activo");
    }
});
document.getElementById("cancelar").addEventListener("click", () => modalPago.classList.remove("activo"));

const modalExito = document.getElementById("exito");
if (modalExito.dataset.reservaRealizada === "true") {
    modalExito.classList.add("activo");
}
