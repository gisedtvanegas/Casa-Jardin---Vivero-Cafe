
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
                    <li><a href="${ctx}/Vista/Registrarse.jsp">Registrarse</a></li>
                </ul>
           </nav>
    </div>
    <form action="${ctx}/Registrarse" method="post" onsubmit="return validarRegistro()">
        <div class="Formulario">
            <c:if test="${not empty resultado}">
                <p class="mensaje">${resultado}</p>
            </c:if>
            <label for="nombre">Ingrese su nombre:</label>
            <input type="text" name="nombrep" id="nombre"><br><br>
            <label for="apellido">Ingrese su apellido:</label>
            <input type="text" name="apellidoa" id="apellido"><br><br>
            <label for="tipodoc">Tipo de documento:</label>
            <select id="tipodoc" name="tipodocs">
                <c:forEach var="tipo" items="${tiposDoc}">
                    <option value="${tipo.idTipo_documento}">${tipo.descripcion_doc}</option>
                </c:forEach>
            </select><br><br>
            <label for="documento">Ingrese su número de documento:</label>
            <input type="text" name="documentoa" id="documento"><br><br>
            <label for="telefono">Ingrese su teléfono:</label>
            <input type="text" name="telefonoi" id="telefono"><br><br>
            <label for="correo">Ingrese su correo:</label>
            <input type="email" name="correoz" id="correo"><br><br>          
            <label for="rol">Rol:</label>
            <select id="rol" name="rola">
                <c:forEach var="rol" items="${roles}">
                    <option value="${rol.idRoles}">${rol.descripcion_rol}</option>
                </c:forEach>
            </select><br><br>
            <label for="clave">Ingrese su contraseña:</label>
            <input type="password" name="clavev" id="clave"><br><br>
            <label for="fecha_nac">Fecha de nacimiento:</label>
            <input type="date" name="fecha_nac" id="fecha_nac" required><br><br>

            <label for="checkbox">
            <input type="checkbox" name="checkbox" id="checkbox" required>
            Acepto el tratamiento de mis datos personales
            </label><br><br>
            <button type="submit">Registrarse</button>
            <button type="button" onclick="window.location='${ctx}/Iniciar'">Iniciar Sesión</button>
        </div>
    </form> 
    <script src="${ctx}/Vista/JavaScript/hamburguesa.js"></script>
</body>

</html>
