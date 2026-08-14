
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Casa y Jardin - Vivero Café</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css?v=4">
    <script src="${ctx}/Vista/JavaScript/validarReg.js"></script>
</head>
<body>   
    <div class="barrainicio">
            <div class="logotitulosof">
                <div class="logocorto">
                    <img src="${ctx}/Vista/Imagenes/logo.png" alt="logocorto">
                </div>
                <h1>Casa y Jardin</h1>
            </div>
            <button class="nav-toggle" id="nav-toggle" aria-label="Abrir menú" aria-expanded="false">
                <span class="bar"></span>
                <span class="bar"></span>
                <span class="bar"></span>
            </button>
            <nav class="navegacion" id="nav-menu">
                <ul>
                    <li><a href="${ctx}/index.jsp">Inicio</a></li>
                    <li><a href="${ctx}/ActividadesPublicas">Actividad</a></li> 
                    <li><a href="${ctx}/MenuPublico">Menú</a></li>
                    <li><a href="${ctx}/Iniciar">Iniciar Sesión</a></li>
                    <li><a href="${ctx}/CargarRegistro">Registrarse</a></li>
                </ul>
           </nav>
    </div>
    <form action="${ctx}/Registrarse" method="post" onsubmit="return validarRegistro()">
        <div class="Formulario">
            <c:if test="${not empty resultado}">
                <p class="mensaje">${resultado}</p>
            </c:if>
            <label for="nombre">Ingrese su nombre:</label>
            <input type="text" name="nombrep" id="nombre" required><br><br>
            <label for="apellido">Ingrese su apellido:</label>
            <input type="text" name="apellidoa" id="apellido" required><br><br>
            <label for="tipodoc">Tipo de documento:</label>
            <select id="tipodoc" name="tipodocs" required>
                <c:forEach var="tipo" items="${tiposDoc}">
                    <option value="${tipo.idTipo_documento}">${tipo.descripcion_doc}</option>
                </c:forEach>
            </select><br><br>
            <label for="documento">Ingrese su número de documento:</label>
            <input type="text" name="documentoa" id="documento" inputmode="numeric" pattern="[0-9]+" required><br><br>
            <label for="telefono">Ingrese su teléfono:</label>
            <input type="text" name="telefonoi" id="telefono" required><br><br>
            <label for="correo">Ingrese su correo:</label>
            <input type="email" name="correoz" id="correo" required><br><br>
            <p>Rol asignado: Usuario</p>
            <label for="clave">Ingrese su contraseña:</label>
            <input type="password" name="clavev" id="clave" minlength="8" required>
            <small>Minimo 8 caracteres.</small><br><br>
            <label for="fecha_nac">Fecha de nacimiento:</label>
            <input type="date" name="fecha_nac" id="fecha_nac" required><br><br>

            <label for="checkbox">
            <input type="checkbox" name="checkbox" id="checkbox" required>
            Acepto los
            </label>
            <button type="button" class="enlace-terminos" id="abrir-terminos">terminos y condiciones</button>.
            <span>Tambien autorizo el tratamiento de mis datos personales.</span><br><br>
            <button type="submit">Registrarse</button>
            <button type="button" onclick="window.location='${ctx}/Iniciar'">Iniciar Sesión</button>
        </div>
    </form>
    <div class="modal-terminos" id="modal-terminos" role="dialog" aria-modal="true" aria-labelledby="titulo-terminos" hidden>
        <div class="modal-terminos-contenido">
            <button type="button" class="cerrar-terminos" id="cerrar-terminos" aria-label="Cerrar">&times;</button>
            <h2 id="titulo-terminos">Terminos y condiciones</h2>
            <p><strong>Documento de ejemplo.</strong> Estos terminos son informativos para este aplicativo.</p>
            <p>Al registrarte autorizas a Casa y Jardin a usar los datos que proporcionas para crear y administrar tu cuenta, atender reservas y enviarte comunicaciones relacionadas con el servicio.</p>
            <p>Tu informacion se conserva mientras la cuenta este activa o sea necesaria para cumplir obligaciones del servicio. Puedes solicitar la actualizacion o eliminacion de tus datos mediante los canales de contacto del establecimiento.</p>
            <p>No compartas tu contrasena. Eres responsable de mantenerla privada y de informar cualquier uso no autorizado de tu cuenta.</p>
            <button type="button" id="aceptar-terminos">Entendido</button>
        </div>
    </div>
    <style>
        .enlace-terminos { border: 0; padding: 0; background: transparent; color: #166534; text-decoration: underline; cursor: pointer; font: inherit; }
        .modal-terminos { position: fixed; inset: 0; z-index: 1000; display: grid; place-items: center; padding: 1rem; background: rgba(0, 0, 0, .55); }
        .modal-terminos[hidden] { display: none !important; }
        .modal-terminos-contenido { position: relative; width: min(560px, 100%); max-height: 80vh; overflow: auto; padding: 2rem; border-radius: 12px; background: #fff; color: #1f2937; }
        .cerrar-terminos { position: absolute; top: .5rem; right: .75rem; border: 0; background: transparent; font-size: 2rem; cursor: pointer; }
    </style>
    <script>
        (function () {
            const modal = document.getElementById('modal-terminos');
            const abrir = document.getElementById('abrir-terminos');
            const cerrar = document.getElementById('cerrar-terminos');
            const aceptar = document.getElementById('aceptar-terminos');
            const ocultar = () => { modal.hidden = true; abrir.focus(); };
            abrir.addEventListener('click', () => { modal.hidden = false; cerrar.focus(); });
            cerrar.addEventListener('click', ocultar);
            aceptar.addEventListener('click', ocultar);
            modal.addEventListener('click', (event) => { if (event.target === modal) ocultar(); });
            document.addEventListener('keydown', (event) => { if (event.key === 'Escape' && !modal.hidden) ocultar(); });
        }());
    </script>
    <script src="${ctx}/Vista/JavaScript/hamburguesa.js"></script>
</body>

</html>
