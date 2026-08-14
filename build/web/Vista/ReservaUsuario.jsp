<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserva</title>
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
                    <li><a href="${ctx}/PanelUsuario.jsp">Inicio</a></li>
                    <li><a href="${ctx}/ActividadesPublicas">Actividad</a></li>
                    <li><a href="${ctx}/ReservaUsuario">Reservas</a></li>
                    <li><a href="${ctx}/MenuUsuario">Menú</a></li>
                    <li><a href="${ctx}/CerrarSesion">Cerrar Sesión</a></li>
                </ul>
            </nav>
    </div>
    <main class="mis-reservas">
        <h2 class="titulo-form">Mis reservas</h2>
        <c:if test="${empty listaReservasUsuario}"><p class="mensaje">No hay ningún registro de reservas.</p></c:if>
        <c:if test="${not empty listaReservasUsuario}">
            <div class="table-scroll"><table class="crud-table"><thead><tr><th>Actividad</th><th>Personas</th><th>Fecha</th><th>Hora</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>
            <c:forEach var="r" items="${listaReservasUsuario}"><tr>
                <form action="${ctx}/ReservaUsuario" method="post"><input type="hidden" name="idReserva" value="${r.idReserva}">
                <td><c:out value="${r.descripcionActividad}"/></td>
                <td><c:choose><c:when test="${r.puedeEditar}"><input type="number" name="num_personas" min="1" max="10" value="${r.num_personas}" required></c:when><c:otherwise>${r.num_personas}</c:otherwise></c:choose></td>
                <td><c:choose><c:when test="${r.puedeEditar}"><input type="date" name="fecha" value="${r.fecha}" required></c:when><c:otherwise>${r.fecha}</c:otherwise></c:choose></td>
                <td><c:choose><c:when test="${r.puedeEditar}"><input type="time" name="hora" value="${r.hora}" required></c:when><c:otherwise>${r.hora}</c:otherwise></c:choose></td>
                <td><c:out value="${r.descripcionEstado}"/></td><td class="row-actions">
                    <c:if test="${r.puedeEditar}"><button type="submit" name="accion" value="actualizar">Actualizar</button></c:if>
                    <c:if test="${not r.cancelada}"><button type="submit" name="accion" value="cancelar">Cancelar</button></c:if>
                    <c:if test="${not r.puedeEditar and not r.cancelada}"><small>Solo es editable con tres días de antelación.</small></c:if>
                </td></form></tr></c:forEach>
            </tbody></table></div>
        </c:if>
    </main>
    <button type="button" class="nuevo-lapiz" id="abrirReserva" aria-label="Crear reserva">&#9998;</button>
    <main id="nuevaReservaModal" class="Formulario modal-nueva-reserva" aria-hidden="true">
        <button type="button" class="cerrar-nueva-reserva" id="cerrarReserva" aria-label="Cerrar">&times;</button>
        <h2 class="titulo-form">Reserva tu actividad</h2>
        <c:if test="${not empty mensaje}"><p class="mensaje"><c:out value="${mensaje}"/></p></c:if>
        <form id="reservaForm" action="${ctx}/ReservaUsuario" method="post">
            
            <section class="datos">
                <label>Nombre<input value="<c:out value='${usuarioReserva.nombre}'/>" readonly></label>
                <label>Apellido<input value="<c:out value='${usuarioReserva.apellido}'/>" readonly></label>
                <label>Tipo de documento<input value="<c:out value='${tipoDocumentoReserva.descripcion_doc}'/>" readonly></label>
                <label>Documento<input value="<c:out value='${usuarioReserva.documento}'/>" readonly></label>
                <label>Correo<input value="<c:out value='${usuarioReserva.correo}'/>" readonly></label>
            </section>
            
            <label>Personas<input type="number" name="num_personas" min="1" max="10" required></label>
            <label>Fecha<input type="date" name="fecha" required></label>
            <label>Hora<input type="time" name="hora" required></label>
            <label>Disponibilidad
                <select name="Disponibilidad_idDisponibilidad" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="d" items="${listaDisponibilidades}">
                        <option value="${d.idDisponibilidad}">${d.fecha} - cupos ${d.cupo_disponible}/${d.cupo_total}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Actividad
                <select name="Actividad_idActividad" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="a" items="${listaActividades}">
                        <option value="${a.idActividad}">${a.descripcion_actividad}</option>
                    </c:forEach>
                </select>
            </label>
            <button type="button" id="abrirPago">Continuar al pago</button>
        </form>
    </main>
    <div id="pagoModal" class="modal"><div class="caja">
            <h2>Pasarela de pago</h2>
            <label>Nombre del titular
                <input id="titular" form="reservaForm" name="titular"></label>
            <label>Método de pago
                <select id="metodo" form="reservaForm" name="metodoPago">
                    <option value="">Seleccione...</option>
                    <option value="credito">Tarjeta de crédito</option>
                    <option value="debito">Tarjeta débito</option>
                    <option value="nequi">Nequi</option>
                    <option value="daviplata">Daviplata</option>
                    <option value="dale">Dale!</option>
                </select>
            </label>
            <div id="tarjeta" class="oculto"><p id="tipoTarjeta"></p>
                <label>Correo electrónico
                    <input form="reservaForm" name="correoTitular" type="email"></label>
                <label>Número de tarjeta
                    <input form="reservaForm" name="numeroTarjeta" inputmode="numeric" minlength="12" maxlength="19">
                </label>
                <label>Fecha de expedición
                    <input form="reservaForm" name="fechaExpedicion" type="month"></label>
                <label>CVV
                    <input form="reservaForm" name="cvv" inputmode="numeric" minlength="3" maxlength="4">
                </label>
            </div>
            <div id="billetera" class="oculto">
                <label>Número de teléfono
                    <input form="reservaForm" name="telefonoBilletera" inputmode="numeric" minlength="10" maxlength="10"></label>
                <label>Clave
                    <input form="reservaForm" name="claveBilletera" type="password" minlength="4" maxlength="8">
                </label>
            </div>
            <div class="acciones">
                <button type="button" id="cancelar">Cancelar</button>
                <button type="submit" form="reservaForm" name="pago" value="confirmado">Realizar pago</button>
            </div>
        </div>
    </div>
    <div id="exito" class="modal" data-reserva-realizada="${reservaRealizada}">
        <div class="caja"><h2>Reserva realizada</h2>
            <p>El pago fue aprobado y la reserva fue efectuada.</p>
            <a href="${ctx}/PanelUsuario.jsp">Aceptar</a></div>
    </div>
    <script src="${ctx}/Vista/JavaScript/pagosfa.js"></script>
    <style>
        .mis-reservas { max-width: 1100px; margin: 150px auto 2rem; padding: 0 1rem; }
        .nuevo-lapiz { position: fixed; right: 1.5rem; bottom: 1.5rem; z-index: 20; width: 54px; height: 54px; border: 0; border-radius: 50%; background: #166534; color: #fff; font-size: 1.6rem; cursor: pointer; box-shadow: 0 4px 12px #0005; }
        .modal-nueva-reserva { display: none; position: fixed; inset: 0; z-index: 50; overflow: auto; margin: 0; padding: 4rem max(1rem, calc((100vw - 700px) / 2)); background: rgba(0,0,0,.55); }
        .modal-nueva-reserva.activo { display: block; }
        .modal-nueva-reserva > form, .modal-nueva-reserva > .titulo-form, .modal-nueva-reserva > section, .modal-nueva-reserva > label { background: #fff; }
        .cerrar-nueva-reserva { position: fixed; top: 1rem; right: 1.5rem; z-index: 51; border: 0; border-radius: 50%; width: 40px; height: 40px; font-size: 1.8rem; cursor: pointer; }
        #pagoModal, #exito { z-index: 3000; }
        @media (max-width: 700px) { .mis-reservas { margin-top: 180px; } .mis-reservas .crud-table { font-size: .85rem; } .modal-nueva-reserva { padding: 4.5rem 1rem 2rem; } }
    </style>
    <script>
        const modalReserva = document.getElementById('nuevaReservaModal');
        document.getElementById('abrirReserva').addEventListener('click', () => { modalReserva.classList.add('activo'); modalReserva.setAttribute('aria-hidden', 'false'); });
        document.getElementById('cerrarReserva').addEventListener('click', () => { modalReserva.classList.remove('activo'); modalReserva.setAttribute('aria-hidden', 'true'); });
    </script>
</body>
</html>
