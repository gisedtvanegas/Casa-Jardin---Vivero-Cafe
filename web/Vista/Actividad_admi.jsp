<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actividades - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
     <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🌿</text></svg>">
    <style>
        .foto-miniatura { width: 64px; height: 64px; object-fit: cover; border-radius: 8px; display: block; }
        .foto-miniatura-placeholder { width: 64px; height: 64px; border-radius: 8px; background: #e8f0eb; display: flex; align-items: center; justify-content: center; font-size: 1.6rem; }
        .input-foto-actual { display: none; }
    </style>
</head>
<body class="admin-crud-page">
    <main class="admin-crud-container">
        <header class="admin-crud-header">
            <div>
                <p class="admin-eyebrow">Actividad</p>
                <h2>Gestion de Actividades</h2>
            </div>
            <a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a>
        </header>

        <c:if test="${not empty mensaje}">
            <div class="mensaje-bienvenida"><p>${mensaje}</p></div>
        </c:if>

        <%-- Formulario de inserción: multipart/form-data para permitir subida de imagen --%>
        <form class="crud-form" action="${ctx}/Actividad" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="accion" value="insertar">
            <label>Descripcion<input type="text" name="descripcionAct" required></label>
            <label>Información de la tarjeta<textarea name="informacionAct" maxlength="1000" required placeholder="Describe qué incluye, duración, materiales y recomendaciones."></textarea></label>
            <label>Foto de la actividad
                <input type="file" name="fotoActividad" accept="image/*">
                <small style="color:#6b8e6b;font-size:.82rem;">Opcional · Formatos: JPG, PNG, WEBP (máx. 5 MB)</small>
            </label>
            <label>Tipo de actividad
                <select name="tipoActi" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="tipo" items="${listaTiposActividad}">
                        <option value="${tipo.idTipo_Actividad}">${tipo.nombre_activi}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Lista de precios
                <select name="listaPrecioAct" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="precio" items="${listaPrecios}">
                        <option value="${precio.idLista_Precio}">${precio.descrip_precio}</option>
                    </c:forEach>
                </select>
            </label>
            <div class="crud-actions"><button type="submit">Guardar actividad</button></div>
        </form>

        <table class="crud-table">
            <thead>
                <tr>
                    <th>Descripcion</th>
                    <th>Información de la tarjeta</th>
                    <th>Foto actual</th>
                    <th>Nueva foto</th>
                    <th>Tipo de actividad</th>
                    <th>Lista de precios</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="actividad" items="${listaActividades}">
                    <tr>
                        <%-- Formulario de actualización también con multipart --%>
                        <form action="${ctx}/Actividad" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="idActividad" value="${actividad.idActividad}">
                            <%-- Foto actual guardada en BD, se conserva si no se sube una nueva --%>
                            <input type="hidden" name="fotoActual" value="${actividad.foto_actividad}" class="input-foto-actual">
                            <td><input type="text" name="descripcionAct" value="${actividad.descripcion_actividad}" required></td>
                            <td><textarea name="informacionAct" maxlength="1000" required><c:out value="${actividad.informacion}"/></textarea></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty actividad.foto_actividad}">
                                        <img class="foto-miniatura" src="${ctx}/${actividad.foto_actividad}" alt="Foto de ${actividad.descripcion_actividad}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="foto-miniatura-placeholder">🌿</div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <input type="file" name="fotoActividad" accept="image/*">
                                <small style="color:#6b8e6b;font-size:.78rem;">Deja vacío para conservar la actual</small>
                            </td>
                            <td>
                                <select name="tipoActi" required>
                                    <c:forEach var="tipo" items="${listaTiposActividad}">
                                        <option value="${tipo.idTipo_Actividad}" ${tipo.idTipo_Actividad == actividad.tipo_Actividad_idTipo_Actividad ? 'selected' : ''}>${tipo.nombre_activi}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select name="listaPrecioAct" required>
                                    <c:forEach var="precio" items="${listaPrecios}">
                                        <option value="${precio.idLista_Precio}" ${precio.idLista_Precio == actividad.lista_Precios_idLista_Precios ? 'selected' : ''}>${precio.descrip_precio}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="row-actions">
                                 <button type="submit" name="accion" value="actualizar">Actualizar</button>
                                 <button type="button" name="accion" value="eliminar" onclick="showDeleteModal(event, this)">Eliminar</button>
                             </td>
                        </form>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>

    <!-- Modal de Confirmación de Eliminación -->
    <div id="deleteModal" class="custom-modal">
        <div class="custom-modal-content">
            <p>¿Estás seguro que quieres eliminar este registro?</p>
            <div class="custom-modal-buttons">
                <button type="button" id="confirmDeleteBtn" class="modal-btn-si">Sí</button>
                <button type="button" id="cancelDeleteBtn" class="modal-btn-no">No</button>
            </div>
        </div>
    </div>

    <script>
        let activeForm = null;
        
        function showDeleteModal(event, button) {
            event.preventDefault();
            activeForm = button.closest('form');
            document.getElementById('deleteModal').classList.add('show');
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('confirmDeleteBtn').addEventListener('click', function() {
                if (activeForm) {
                    const inputAccion = document.createElement('input');
                    inputAccion.type = 'hidden';
                    inputAccion.name = 'accion';
                    inputAccion.value = 'eliminar';
                    activeForm.appendChild(inputAccion);
                    activeForm.submit();
                }
            });
            
            document.getElementById('cancelDeleteBtn').addEventListener('click', function() {
                document.getElementById('deleteModal').classList.remove('show');
                activeForm = null;
            });
        });
    </script>
</body>
</html>
