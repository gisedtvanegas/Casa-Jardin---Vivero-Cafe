<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Recuperar contraseña - Casa y Jardín</title>
        <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
         <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🌿</text></svg>">
    </head>
<body>
    <div class="Formulario recuperacion">
        <h2>Recuperar contraseña</h2>
        <p>Escribe el correo con el que te registraste y te enviaremos un código.</p>
        
<form action="${ctx}/RecuperarClave" method="post">
    <input type="email" name="correo" placeholder="Correo electrónico" required>
<c:if test="${not empty mensaje}"><p class="mensaje">${mensaje}</p></c:if><button type="submit">Enviar código</button></form>
<p><a href="${ctx}/Vista/InicioSesion.jsp">Volver a iniciar sesión</a></p></div></body></html>
