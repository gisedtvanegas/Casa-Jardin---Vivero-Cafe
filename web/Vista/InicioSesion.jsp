<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Casa y Jardin - Vivero Café</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css?v=4">
    <script src="${ctx}/Vista/JavaScript/validarReg.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon" />
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
    <div class="Formulario">
        <h2>Iniciar Sesion</h2>
        <form action="${ctx}/Iniciar" method="post" onsubmit="return validarLogin()">
            <label for="documento"><span class="material-symbols-outlined">account_circle</span></label>
            <input type="text" name="usuario" id="usuario" placeholder="Documento">
            <label for="clave"><span class="material-symbols-outlined">lock</span></label>
            <input type="password" name="pass" id="pass" placeholder="clave">
            <p class="enlace-recuperacion">¿Te olvidaste de la contraseña? <a href="${ctx}/RecuperarClave">Recuperar</a></p>
            <c:if test="${not empty mensaje}">
            <p class="mensaje">${mensaje}</p>
        </c:if>
            <c:if test="${not empty param.mensaje}">
            <p class="mensaje">${param.mensaje}</p>
        </c:if>
            <button type="submit">Iniciar Sesión</button>
            <button type="button" onclick="window.location='${ctx}/CargarRegistro'">Registrarse</button>
        </form>
    </div>
    <script src="${ctx}/Vista/JavaScript/hamburguesa.js"></script>
</body>
</html>
