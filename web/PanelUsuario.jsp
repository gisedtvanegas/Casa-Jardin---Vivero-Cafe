<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Casa y Jardin - Vivero Café</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
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
            <nav class="navegacion">
                <ul>
                    <li><a href="${ctx}/index.jsp">Inicio</a></li>
                    <li><a href="${ctx}/ActividadesPublicas">Actividad</a></li>
                    <li><a href="${ctx}/ReservaUsuario">Reservas</a></li>
                    <li><a href="${ctx}/Vista/MenuUsuario.jsp">Menú</a></li>
                    <li><a href="${ctx}/CerrarSesion">Cerrar Sesión</a></li>
                </ul>
            </nav>
    </div>
        <div class="carrusel">
            <div class="imagen-contenedor" id="carrusel">
                <img src="${ctx}/Vista/Imagenes/prueba.jpg" alt="Fachada">
                <img src="${ctx}/Vista/Imagenes/foto.jpg" alt="Entrada">
                <img src="${ctx}/Vista/Imagenes/fotoprueba.jpg" alt="Sillas">
                <img src="${ctx}/Vista/Imagenes/prueba.prueba.jpg" alt="Mesas">
            </div>
            <div class="buttons">
                <button type="button" class="btizquierdo" id="left">&#10094;</button>
                <button type="button" class="btderecho" id="right">&#10095;</button>
            </div>
        </div>
        <div class="bienvenida">
            <p>
                <c:if test="${not empty sessionScope.nombreUsuario}">
                    <a style="cursor:default; pointer-events:none;">Hola, ${sessionScope.nombreUsuario}</a>
                </c:if> <br>
                Agenda tu </p>
            <h3>
                <a href="${ctx}/ReservaUsuario" class="actividad">Reserva</a>
            </h3>
        </div>
            <div class="reservain">
            <h2>¿Cómo puedes agendar reserva con nosotros?</h2>
            
            <div class="pasos-contenedor">
                <div class="paso-card">
                    <span class="paso-numero">Paso 1</span>
                    <p class="paso-texto">Inicia Sesión, si no tienes cuenta, crea una.</p>
                </div>
                <div class="paso-card">
                    <span class="paso-numero">Paso 2</span>
                    <p class="paso-texto">Llena los datos para poder realizar la reserva.</p>
                </div>
                <div class="paso-card">
                    <span class="paso-numero">Paso 3</span>
                    <p class="paso-texto">Selecciona la actividad que desees.</p>
                </div>
                <div class="paso-card">
                    <span class="paso-numero">Paso 4</span>
                    <p class="paso-texto">Continua con el pago del abono.</p>
                </div>
                <div class="paso-card">
                    <span class="paso-numero">Paso 5</span>
                    <p class="paso-texto">Felicidades!! Ya tendrás realizada tu reserva!!</p>
                </div>
            </div>
            <a href="${ctx}/Vista/ReservaUsuario.jsp" class="agendare">Agenda tu Reserva!</a>
        <div class="historia">
            <h2>Historia</h2>
            <p>
                Crecimos con la idea de poder enseñarle a las personas un poco de arte, 
                pero también teniendo un rato agradable con familiares y amigos. 
                Inicialmente trabajamos con kokedamas, realizando pequeños talleres para 
                personas interesadas, luego decidimos implementar nuevos talleres por 
                recomendación de nuestros clientes. A partir de ahí tenemos cerámicas 
                para pintar de forma libre, tote bags, materas, terrarios con una zona 
                especial para que cada cliente tenga una experiencia única.
            </p>
        </div>
    </form>
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

    <script src="${ctx}/Vista/JavaScript/funciones.js"></script>
</body>
</html> 
