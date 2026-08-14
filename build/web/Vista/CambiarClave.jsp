<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html><html lang="es"><head><meta charset="UTF-8"><title>Cambiar contraseña</title><link rel="stylesheet" href="${ctx}/Vista/Css/style.css"></head>
<body><div class="Formulario recuperacion"><h2>Crear nueva contraseña</h2><p>Ingresa el código recibido por correo. Tiene una vigencia de 15 minutos.</p>
<form action="${ctx}/CambiarClave" method="post"><input type="text" name="codigo" placeholder="Código de 5 dígitos" pattern="[0-9]{5}" required>
<input type="password" name="clave" placeholder="Nueva contraseña" minlength="6" required><input type="password" name="confirmarClave" placeholder="Confirmar contraseña" minlength="6" required>
<c:if test="${not empty mensaje}"><p class="mensaje">${mensaje}</p></c:if><button type="submit">Actualizar contraseña</button></form></div></body></html>
