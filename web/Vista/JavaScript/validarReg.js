function validarRegistro() {
    const nombre = document.getElementById("nombre").value.trim();
    const apellido = document.getElementById("apellido").value.trim();
    const documento = document.getElementById("documento").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const correo = document.getElementById("correo").value.trim();
    const clave = document.getElementById("clave").value.trim();

    if (nombre === "") {
        alert("El nombre es obligatorio");
        return false;
    }
    if (apellido === "") {
        alert("El apellido es obligatorio");
        return false;
    }
    if (documento === "") {
        alert("El documento es obligatorio");
        return false;
    }
    if (isNaN(documento)) {
        alert("El documento debe ser numérico");
        return false;
    }
    if (telefono === "") {
        alert("El teléfono es obligatorio");
        return false;
    }
    if (correo === "") {
        alert("El correo es obligatorio");
        return false;
    }
    if (clave === "") {
        alert("La contraseña es obligatoria");
        return false;
    }
    return true;
}
function validarLogin() {
    const usuario = document.getElementById("usuario").value.trim();
    const pass = document.getElementById("pass").value.trim();

    if (usuario === "") {
        alert("El documento es obligatorio");
        return false;
    }
    if (isNaN(usuario)) {
        alert("El documento debe ser numérico");
        return false;
    }
    if (pass === "") {
        alert("La clave es obligatoria");
        return false;
    }
    return true;
}