<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil | Casa y Jardín</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css?v=6">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🌿</text></svg>">
    <style>
        .foto-perfil-contenedor {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 25px;
        }
        .foto-perfil-preview {
            width: 130px;
            height: 130px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #2e6846;
            box-shadow: 0 6px 15px rgba(46, 104, 70, 0.25);
            margin-bottom: 12px;
            background-color: #fff;
        }
        .foto-perfil-placeholder {
            width: 130px;
            height: 130px;
            border-radius: 50%;
            background-color: #e8f0eb;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3.5rem;
            color: #2e6846;
            border: 4px dashed #2e6846;
            margin-bottom: 12px;
            box-shadow: 0 6px 15px rgba(0,0,0,0.1);
        }
        .input-foto-perfil {
            font-size: 0.85rem;
            color: #555;
            margin-top: 5px;
        }
        .perfil-form-container {
            margin-top: 150px !important;
            margin-bottom: 80px !important;
        }
        .mensaje-alert {
            padding: 12px 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-weight: 500;
            text-align: center;
            font-size: 0.95rem;
        }
        .mensaje-exito {
            background-color: #e2f0d9;
            color: #385723;
            border: 1px solid #c5e1a5;
        }
        .mensaje-error {
            background-color: #fce4d6;
            color: #c65911;
            border: 1px solid #f8cbad;
        }
        .btn-perfil-submit {
            background: linear-gradient(135deg, #2e6846, #91bd72) !important;
            color: #fff !important;
            border: none !important;
            border-radius: 12px !important;
            padding: 12px 24px !important;
            font-weight: bold !important;
            cursor: pointer !important;
            width: 100% !important;
            box-sizing: border-box !important;
            margin: 15px 0 0 0 !important;
            transition: transform 0.2s ease, opacity 0.3s ease !important;
            box-shadow: 0 4px 10px rgba(46, 104, 70, 0.2) !important;
        }
        .btn-perfil-submit:hover {
            transform: scale(1.02) !important;
            opacity: 0.95 !important;
        }
    </style>
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
                <li><a href="${ctx}/PanelUsuario.jsp">Inicio</a></li>
                <li><a href="${ctx}/ActividadesUsuario">Actividad</a></li>
                <li><a href="${ctx}/ReservaUsuario">Reservas</a></li>
                <li><a href="${ctx}/MenuUsuario">Menú</a></li>
                <li><a href="${ctx}/PerfilUsuario" class="active">Mi Perfil</a></li>
                <li><a href="${ctx}/CerrarSesion">Cerrar Sesión</a></li>
            </ul>
        </nav>
    </div>

    <main class="Formulario perfil-form-container">
        <h2>Mi Perfil</h2>
        
        <c:if test="${not empty mensajeExito}">
            <div class="mensaje-alert mensaje-exito">
                <c:out value="${mensajeExito}"/>
            </div>
        </c:if>
        <c:if test="${not empty mensajeError}">
            <div class="mensaje-alert mensaje-error">
                <c:out value="${mensajeError}"/>
            </div>
        </c:if>

        <form action="${ctx}/PerfilUsuario" method="post" enctype="multipart/form-data">
            <input type="hidden" name="fotoActual" value="${usuario.foto_perfil}">

            <div class="foto-perfil-contenedor">
                <c:choose>
                    <c:when test="${not empty usuario.foto_perfil}">
                        <img src="${ctx}/${usuario.foto_perfil}" alt="Foto de perfil" class="foto-perfil-preview">
                    </c:when>
                    <c:otherwise>
                        <div class="foto-perfil-placeholder">👤</div>
                    </c:otherwise>
                </c:choose>
                <input type="file" name="foto_perfil" class="input-foto-perfil" accept="image/*">
                <small style="color: #6b8e6b; font-size: 0.8rem; margin-top: 5px;">Formatos: JPG, PNG, WEBP (Opcional)</small>
            </div>

            <label>Nombre
                <input type="text" name="nombre" value="<c:out value='${usuario.nombre}'/>" placeholder="Tu nombre" required>
            </label>

            <label>Correo Electrónico
                <input type="email" name="correo" value="<c:out value='${usuario.correo}'/>" placeholder="ejemplo@correo.com" required>
            </label>

            <label>Número de Teléfono
                <input type="text" name="telefono" value="<c:out value='${usuario.telefono}'/>" placeholder="Número de contacto" required>
            </label>

            <button type="submit" class="btn-perfil-submit">Guardar Cambios</button>
        </form>
    </main>

    <footer class="footer">
        <div class="footer-contenedor">
            <div class="footer-info">
                <h3>Casa y Jardín - Vivero Café</h3>
                <p>Dirección: Calle 123 #45-67, Bogotá</p>
                <p>Teléfono: +57 300 123 4567</p>
                <p>Email: contacto@casayjardin.com</p>
            </div>

            <div class="logo-footer">
                <img src="${ctx}/Vista/Imagenes/loguito.png" alt="Logo Casa y Jardín">
            </div>

            <div class="footer-redes">
                <h3>Síguenos</h3>
                <a href="https://www.facebook.com/casayjardincll53/">Facebook</a> |
                <a href="https://www.instagram.com/casayjardinviverocafe/">Instagram</a> |
                <a href="#">WhatsApp</a>
            </div>
        </div>

        <div class="footer-copy">
            <p>&copy; 2025 Casa y Jardín - Vivero Café. Todos los derechos reservados.</p>
        </div>
    </footer>

    <script src="${ctx}/Vista/JavaScript/hamburguesa.js"></script>
    <script src="${ctx}/Vista/JavaScript/navegacion-sesion.js"></script>
</body>
</html>
