<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actividades | Casa y Jardín</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css?v=5">
     <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🌿</text></svg>">
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
                    <li><a href="${ctx}/PerfilUsuario">Mi Perfil</a></li>
                    <li><a href="${ctx}/CerrarSesion">Cerrar Sesión</a></li>
                </ul>
            </nav>
    </div>
    <main class="actividades-publicas"> 
        <h2>Explora nuestras actividades</h2> <br> <br>
        <section class="tarjetas-actividades" aria-label="Actividades disponibles">
            <c:forEach var="actividad" items="${listaActividades}" varStatus="estado">
                <article class="tarjeta-actividad">
                    <button class="tarjeta-actividad-boton" type="button" aria-pressed="false" aria-label="Ver información de la actividad ${actividad.descripcion_actividad}">
                        <span class="tarjeta-actividad-inner">
                            <span class="tarjeta-actividad-frente">
                                <c:choose>
                                    <c:when test="${not empty actividad.foto_actividad}">
                                        <img src="${ctx}/${actividad.foto_actividad}" alt="Imagen de la actividad ${actividad.descripcion_actividad}">
                                    </c:when>
                                </c:choose>
                                <span class="tarjeta-sombra"></span>
                                <span class="tarjeta-actividad-titulo">${actividad.descripcion_actividad}</span>
                                <span class="tarjeta-indicacion">Ver información ↻</span>
                            </span>
                            <span class="tarjeta-actividad-reverso">
                                <span class="tarjeta-actividad-informacion">
                                    <c:out value="${actividad.informacion}" default="Próximamente encontrarás más información sobre esta actividad."/>
                                </span>
                                <a class="tarjeta-agenda-btn" href="${ctx}/ReservaUsuario">Agenda tu actividad!</a>
                                <span class="tarjeta-indicacion">Volver ↻</span>
                            </span>
                        </span>
                    </button>
                </article>
            </c:forEach>
        </section>
        <c:if test="${empty listaActividades}"><p class="sin-actividades">Aún no hay actividades registradas. Registra cuatro actividades desde el panel de administración para verlas aquí.</p></c:if>
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
    <script src="${ctx}/Vista/JavaScript/actividades.js?v=20260731"></script>
</body>
</html>
