<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Roles - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
</head>
<body class="admin-crud-page">
    <main class="admin-crud-container">
        <header class="admin-crud-header">
            <div>
                <p class="admin-eyebrow">Usuarios</p>
                <h2>Roles</h2>
            </div>
            <a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a>
        </header>

        <c:if test="${not empty mensaje}">
            <div class="mensaje-bienvenida"><p>${mensaje}</p></div>
        </c:if>

        <form class="crud-form crud-form-compact" action="${ctx}/Roles" method="POST">
            <input type="hidden" name="accion" value="insertar">
            <label>Descripcion del rol<input type="text" name="descripcion_rol" required></label>
            <div class="crud-actions"><button type="submit">Guardar rol</button></div>
        </form>

        <table class="crud-table">
            <thead>
                <tr>
                    <th>Descripcion</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="rol" items="${listaRoles}">
                    <tr>
                        <form action="${ctx}/Roles" method="POST">
                            <input type="hidden" name="idRoles" value="${rol.idRoles}">
                            <td><input type="text" name="descripcion_rol" value="${rol.descripcion_rol}" required></td>
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
