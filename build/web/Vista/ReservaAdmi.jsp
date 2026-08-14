<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservas - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
</head>
<body class="admin-crud-page">
    <main class="admin-crud-container admin-crud-container-wide">
        <header class="admin-crud-header">
            <div>
                <p class="admin-eyebrow">Reservas</p>
                <h2>Gestion de Reservas</h2>
            </div>
            <a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a>
        </header>

        <c:if test="${not empty resultado}">
            <div class="mensaje-bienvenida"><p>${resultado}</p></div>
        </c:if>

        <form class="crud-form" action="${ctx}/ReservaAdmi" method="POST">
            <input type="hidden" name="accion" value="insertar">
            <label>Personas<input type="number" name="num_personas" min="1" required></label>
            <label>Fecha<input type="date" name="fecha" required></label>
            <label>Hora<input type="time" name="hora" required></label>
            <label>Usuario
                <select name="Usuarios_idUsuarios" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="u" items="${listaUsuarios}">
                        <option value="${u.idUsuarios}">${u.nombre} ${u.apellido} - ${u.documento}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Disponibilidad
                <select name="Disponibilidad_idDisponibilidad" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="d" items="${listaDisponibilidades}">
                        <option value="${d.idDisponibilidad}">${d.fecha} - cupos ${d.cupo_disponible}/${d.cupo_total}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Estado reserva
                <select name="Estado_reserva_idEstado_reserva" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="estado" items="${listaEstadosReserva}">
                        <option value="${estado.idEstado_reserva}">${estado.descripcion_esta}</option>
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
            <label>Estado de pago
                <select name="Pagos_idPagos" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="p" items="${listaPagos}">
                        <option value="${p.idPagos}">${empty p.estado_pago ? 'Pendiente' : p.estado_pago}</option>
                    </c:forEach>
                </select>
            </label>
            <div class="crud-actions">
                <button type="submit">Guardar reserva</button>
            </div>
        </form>

        <div class="table-scroll">
            <table class="crud-table">
                <thead>
                    <tr>
                        <th>Personas</th>
                        <th>Fecha</th>
                        <th>Hora</th>
                        <th>Usuario</th>
                        <th>Disponibilidad</th>
                        <th>Estado</th>
                        <th>Actividad</th>
                        <th>Pago</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${listaReservas}">
                        <tr>
                            <form action="${ctx}/ReservaAdmi" method="POST">
                                <input type="hidden" name="idReserva" value="${r.idReserva}">
                                <td><input type="number" name="num_personas" value="${r.num_personas}" min="1" required></td>
                                <td><input type="date" name="fecha" value="${r.fecha}" required></td>
                                <td><input type="time" name="hora" value="${r.hora}" required></td>
                                <td>
                                    <select name="Usuarios_idUsuarios" required>
                                        <c:forEach var="u" items="${listaUsuarios}">
                                            <option value="${u.idUsuarios}" ${u.idUsuarios == r.usuarios_idUsuarios ? 'selected' : ''}>${u.nombre} ${u.apellido}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="Disponibilidad_idDisponibilidad" required>
                                        <c:forEach var="d" items="${listaDisponibilidades}">
                                            <option value="${d.idDisponibilidad}" ${d.idDisponibilidad == r.disponibilidad_idDisponibilidad ? 'selected' : ''}>${d.fecha} - ${d.cupo_disponible} cupos</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="Estado_reserva_idEstado_reserva" required>
                                        <c:forEach var="estado" items="${listaEstadosReserva}">
                                            <option value="${estado.idEstado_reserva}" ${estado.idEstado_reserva == r.estado_reserva_idEstado_reserva ? 'selected' : ''}>${estado.descripcion_esta}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="Actividad_idActividad" required>
                                        <c:forEach var="a" items="${listaActividades}">
                                            <option value="${a.idActividad}" ${a.idActividad == r.actividad_idActividad ? 'selected' : ''}>${a.descripcion_actividad}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <select name="Pagos_idPagos" required>
                                        <c:forEach var="p" items="${listaPagos}">
                                            <option value="${p.idPagos}" ${p.idPagos == r.pagos_idPagos ? 'selected' : ''}>${empty p.estado_pago ? 'Pendiente' : p.estado_pago}</option>
                                        </c:forEach>
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
