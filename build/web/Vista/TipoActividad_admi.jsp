<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tipo de actividad - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
</head>
<body class="admin-crud-page">
    <main class="admin-crud-container">
        <header class="admin-crud-header">
            <div>
                <p class="admin-eyebrow">Actividad</p>
                <h2>Tipos de actividad</h2>
            </div>
            <a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a>
        </header>

        <c:if test="${not empty mensaje}">
            <div class="mensaje-bienvenida"><p>${mensaje}</p></div>
        </c:if>

        <form class="crud-form crud-form-compact" action="${ctx}/TipoActividad" method="POST">
            <input type="hidden" name="accion" value="insertar">
            <label>Nombre del tipo<input type="text" name="nombreActivi" required></label>
            <div class="crud-actions"><button type="submit">Guardar tipo</button></div>
        </form>

        <table class="crud-table">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tipo" items="${listaTiposActividad}">
                    <tr>
                        <form action="${ctx}/TipoActividad" method="POST">
                            <input type="hidden" name="idTipo_Actividad" value="${tipo.idTipo_Actividad}">
                            <td><input type="text" name="nombreActivi" value="${tipo.nombre_activi}" required></td>
                            <td class="row-actions">
                                <button type="submit" name="accion" value="actualizar">Actualizar</button>
                                <button type="submit" name="accion" value="eliminar">Eliminar</button>
                            </td>
                        </form>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</body>
</html>
