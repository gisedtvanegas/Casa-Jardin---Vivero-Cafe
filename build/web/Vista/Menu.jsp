<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Menú | Casa y Jardín</title>
        <link rel="stylesheet" href="${ctx}/Vista/Css/style.css?v=4">
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
                    <main class="menu-publico"> <br> <br>
                            <section class="tarjetas-menu" aria-label="Productos del menú">
                                <c:forEach var="producto" items="${productos}">
                                    <article class="tarjeta-menu">
                                        <c:choose>
                                            <c:when test="${producto.tieneImagen}"><img src="${ctx}/ProductoImagen?id=${producto.idProducto}" alt="${producto.nombre}"></c:when>
                                            <c:otherwise><img src="${ctx}/Vista/Imagenes/fotoprueba.jpg" alt="Imagen de muestra del producto"></c:otherwise>
                                        </c:choose><div class="tarjeta-menu-contenido">
                                            <h3><c:out value="${producto.nombre}"/></h3>
                                            <p><c:out value="${producto.descripcion}"/></p><strong>
                                                <fmt:formatNumber   value="${producto.precio}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                            </strong>
                                        </div>
                                    </article>
                                </c:forEach>
                            </section>
                            <c:if test="${empty productos}"><p class="sin-actividades">Pronto encontrarás productos deliciosos aquí.</p>
                            </c:if>
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
                    <img src="<%= request.getContextPath() %>/Vista/Imagenes/loguito.png" alt="Logo Casa y Jardín">
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
</body>
</html>
