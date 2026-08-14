<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html><html lang="es"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>Gestionar menú | Casa y Jardín</title><link rel="stylesheet" href="${ctx}/Vista/Css/style.css?v=20260721-menu"></head>
<body class="admin-crud-page"><main class="admin-crud-container admin-crud-container-wide">
    <header class="admin-crud-header"><div><p class="admin-eyebrow">Menú</p><h2>Gestionar productos</h2></div><a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a></header>
    <c:if test="${not empty mensaje}"><div class="mensaje-bienvenida"><p><c:out value="${mensaje}"/></p></div></c:if>
    <form class="crud-form menu-form" action="${ctx}/ProductosMenu" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="accion" value="insertar">
        <label>Nombre<input type="text" name="nombre" maxlength="120" required></label>
        <label>Descripción<textarea name="descripcion" maxlength="500" required></textarea></label>
        <label>Precio<input type="number" name="precio" min="0" step="0.01" required></label>
        <label>Foto<input type="file" name="foto" accept="image/*"></label>
        <div class="crud-actions"><button type="submit">Agregar producto</button></div>
    </form>
    <section class="admin-productos-lista"><c:forEach var="producto" items="${productos}">
        <article class="admin-producto-item">
            <c:choose><c:when test="${producto.tieneImagen}"><img src="${ctx}/ProductoImagen?id=${producto.idProducto}" alt="${producto.nombre}"></c:when><c:otherwise><div class="producto-sin-imagen">Sin foto</div></c:otherwise></c:choose>
            <form action="${ctx}/ProductosMenu" method="POST" enctype="multipart/form-data" class="admin-producto-form">
                <input type="hidden" name="idProducto" value="${producto.idProducto}">
                <input type="text" name="nombre" value="${producto.nombre}" maxlength="120" required>
                <textarea name="descripcion" maxlength="500" required>${producto.descripcion}</textarea>
                <input type="number" name="precio" value="${producto.precio}" min="0" step="0.01" required>
                <label class="cambiar-foto">Cambiar foto<input type="file" name="foto" accept="image/*"></label>
                <div class="row-actions"><button type="submit" name="accion" value="actualizar">Guardar cambios</button><button type="button" name="accion" value="eliminar" onclick="showDeleteModal(event, this)">Eliminar</button></div>
            </form>
        </article>
    </c:forEach><c:if test="${empty productos}"><p class="sin-actividades">Aún no hay productos en el menú.</p></c:if></section>
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
</body></html>
