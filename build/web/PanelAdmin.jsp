<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrador - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
    <script src="${ctx}/Vista/JavaScript/menuAdmin.js"></script>
</head>
<body class="admin-body">
    <div class="admin-shell">
        <aside class="barrainicioadmin">
            <div class="logotitulosoftadmin">
                    <img src="${ctx}/Vista/Imagenes/logo.png" alt="logocorto">
                <h1>Panel administrador</h1>
                
                <c:if test="${not empty mensaje}">
                    <div class="mensaje-bienvenida">
                        <p>${mensaje}</p>
                    </div>
                </c:if>

            </div>
            
            <nav class="navegacionadmin">
                <ul>
                    <li><a href="${ctx}/PanelAdmin.jsp">Inicio</a></li>
                    <li><a href="${ctx}/ReservaAdmi">Reservas</a></li>
                    <li><a href="${ctx}/UsuarioAdmi">Usuarios</a></li>
                    <li><a href="${ctx}/Actividad">Actividades</a></li>
                    <li><a href="${ctx}/ProductosMenu">Menú</a></li>
                    <li><a href="${ctx}/CerrarSesion">Cerrar Sesion</a></li>
                </ul>
            </nav>
        </aside>

        <main class="admin-workspace">
            <section class="admin-panel-card">
                <header class="admin-header">
                    <div>
                        <p class="admin-eyebrow">Casa y Jardin</p>
                        <h2>Vivero - Café</h2>
                    </div>
                </header>

                <section class="burbuja-grid" aria-label="Opciones del panel administrador">
                    <div class="burbuja">
                        <h1>Reservas</h1>
                        <a class="accion-principal" href="${ctx}/ReservaAdmi">Ver reservas</a>
                        <div class="acciones-secundarias">
                            <a href="${ctx}/Disponibilidaad">Disponibilidad</a>
                            <a href="${ctx}/Horarios">Horario</a>
                        </div>
                    </div>
                    <div class="burbuja">
                        <h1>Menú</h1>
                        <a class="accion-principal" href="${ctx}/ProductosMenu">Gestionar productos</a>
                        <div class="acciones-secundarias"><a href="${ctx}/MenuPublico">Ver menú público</a></div>
                    </div>

                    <div class="burbuja">
                        <h1>Usuarios</h1>
                        <a class="accion-principal" href="${ctx}/UsuarioAdmi">Ver usuarios</a>
                        <div class="acciones-secundarias">
                            <a href="${ctx}/Tipodocumento">Tipo documento</a>
                            <a href="${ctx}/Roles">Roles</a>
                        </div>
                    </div>

                    <div class="burbuja">
                        <h1>Actividad</h1>
                        <a class="accion-principal" href="${ctx}/Actividad">Ver actividades</a>
                        <div class="acciones-secundarias">
                            <a href="${ctx}/TipoActividad">Tipo actividad</a>
                            <a href="${ctx}/Listaprecios">Lista precios</a>
                        </div>
                    </div>
                    <button class="boton-expandible onclick="window.location='${ctx}/index.jsp'">
                        <span class="icono">
                            <svg viewBox="0 0 512 512">
                                <path d="M377.9 105.9L500.7 228.7c7.2 7.2 11.3 17.1 11.3 27.3s-4.1 20.1-11.3 27.3L377.9 406.1c-6.4 6.4-15 9.9-24 9.9c-18.7 0-33.9-15.2-33.9-33.9l0-62.1-128 0c-17.7 0-32-14.3-32-32l0-64c0-17.7 14.3-32 32-32l128 0 0-62.1c0-18.7 15.2-33.9 33.9-33.9c9 0 17.6 3.6 24 9.9zM160 96L96 96c-17.7 0-32 14.3-32 32l0 256c0 17.7 14.3 32 32 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-64 0c-53 0-96-43-96-96L0 128C0 75 43 32 96 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32z"/>
                            </svg>
                        </span>
                        <span class="texto">Cerrar sesión</span>
                    </button>
                </section>
            </section>
        </main>
    </div>
</body>
</html>
