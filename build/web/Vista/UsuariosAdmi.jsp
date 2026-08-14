<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Usuarios - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
</head>
<body class="admin-crud-page">
    <main class="admin-crud-container">
        <header class="admin-crud-header">
            <div>
                <p class="admin-eyebrow">Usuarios</p>
                <h2>Gestion de Usuarios</h2>
            </div>
            <a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a>
        </header>

        <c:if test="${not empty resultado}">
            <div class="mensaje-bienvenida"><p>${resultado}</p></div>
        </c:if>

        <form class="crud-form" action="${ctx}/UsuarioAdmi" method="POST">
            <input type="hidden" name="accion" value="insertar">
            <label>Nombre<input type="text" name="nombre" required></label>
            <label>Apellido<input type="text" name="apellido" required></label>
            <label>Documento<input type="text" name="documento" required></label>
            <label>Telefono<input type="text" name="telefono" required></label>
            <label>Correo<input type="email" name="correo" required></label>
            <label>Clave<input type="password" name="clave" required></label>
            <label>Fecha nacimiento<input type="date" name="fecha_nac" required></label>
            <label>Fecha caducidad<input type="date" name="fecha_cad"></label>
            <label>Tipo documento
                <select name="Tipo_documento_idTipo_documento" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="tipo" items="${listaTiposDocumento}">
                        <option value="${tipo.idTipo_documento}">${tipo.descripcion_doc}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Rol
                <select name="Roles_idRoles" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="rol" items="${listaRoles}">
                        <option value="${rol.idRoles}">${rol.descripcion_rol}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Estado
                <select name="checkbox">
                    <option value="1">Activo</option>
                    <option value="0">Inactivo</option>
                </select>
            </label>
            <div class="crud-actions">
                <button type="submit">Guardar usuario</button>
            </div>
        </form>

        <div class="table-scroll">
            <table class="crud-table">
                <thead>
                    <tr>
                        <th>Documento</th>
                        <th>Nombre</th>
                        <th>Contacto</th>
                        <th>Fechas</th>
                        <th>Tipo</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${listaUsuarios}">
                        <tr>
                            <form action="${ctx}/UsuarioAdmi" method="POST">
                                <input type="hidden" name="idUsuarios" value="${u.idUsuarios}">
                                <td><input type="text" name="documento" value="${u.documento}" required></td>
                                <td>
                                    <input type="text" name="nombre" value="${u.nombre}" required>
                                    <input type="text" name="apellido" value="${u.apellido}" required>
                                </td>
                                <td>
                                    <input type="text" name="telefono" value="${u.telefono}" required>
                                    <input type="email" name="correo" value="${u.correo}" required>
                                    <input type="password" name="clave" value="${u.clave}" required>
                                </td>
                                <td>
                                    <input type="date" name="fecha_nac" value="${u.fecha_nac}" required>
                                    <input type="date" name="fecha_cad" value="${u.fecha_cad}" required>
                                </td>
                                <td>
                                    <select name="Tipo_documento_idTipo_documento" required>
                                        <c:forEach var="tipo" items="${listaTiposDocumento}">
                                            <option value="${tipo.idTipo_documento}" ${tipo.idTipo_documento == u.tipo_documento_idTipo_documento ? 'selected' : ''}>${tipo.descripcion_doc}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="Roles_idRoles" required>
                                        <c:forEach var="rol" items="${listaRoles}">
                                            <option value="${rol.idRoles}" ${rol.idRoles == u.roles_idRoles ? 'selected' : ''}>${rol.descripcion_rol}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="checkbox">
                                        <option value="1" ${u.checkbox ? 'selected' : ''}>Activo</option>
                                        <option value="0" ${!u.checkbox ? 'selected' : ''}>Inactivo</option>
                                    </select>
                                </td>
                                <td class="row-actions">
                                    <button type="submit" name="accion" value="actualizar">Actualizar</button>
                                    <button type="submit" name="accion" value="eliminar">Eliminar</button>
                                </td>
                            </form>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>
