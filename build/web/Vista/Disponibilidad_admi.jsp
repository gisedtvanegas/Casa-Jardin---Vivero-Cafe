<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Disponibilidad - Casa y Jardin</title>
    <link rel="stylesheet" href="${ctx}/Vista/Css/style.css">
</head>
<body class="admin-crud-page">
    <main class="admin-crud-container">
        <header class="admin-crud-header">
            <div>
                <p class="admin-eyebrow">Reservas</p>
                <h2>Disponibilidad</h2>
            </div>
            <a class="admin-back" href="${ctx}/PanelAdmin.jsp">Volver al panel</a>
        </header>

        <c:if test="${not empty resultado}">
            <div class="mensaje-bienvenida"><p>${resultado}</p></div>
        </c:if>

        <form class="crud-form" action="${ctx}/Disponibilidaad" method="POST">
            <input type="hidden" name="accion" value="insertar">
            <label>Fecha<input type="date" name="fechaDisp" required></label>
            <label>Cupo total<input type="number" name="cupoTotalDisp" min="0" required></label>
            <label>Cupo disponible<input type="number" name="cupoDisponibleDisp" min="0" required></label>
            <label>Horario
                <select name="horarioIdDisp" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="h" items="${listaHorarios}">
                        <option value="${h.idHorarios}">${h.hora_ini} - ${h.hora_fin}</option>
                    </c:forEach>
                </select>
            </label>
            <div class="crud-actions"><button type="submit">Guardar disponibilidad</button></div>
        </form>

        <table class="crud-table">
            <thead>
                <tr>
                    <th>Fecha</th>
                    <th>Cupo total</th>
                    <th>Cupo disponible</th>
                    <th>Horario</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="d" items="${listaDisponibilidades}">
                    <tr>
                        <form action="${ctx}/Disponibilidaad" method="POST">
                            <input type="hidden" name="idDisponibilidad" value="${d.idDisponibilidad}">
                            <td><input type="date" name="fechaDisp" value="${d.fecha}" required></td>
                            <td><input type="number" name="cupoTotalDisp" value="${d.cupo_total}" min="0" required></td>
                            <td><input type="number" name="cupoDisponibleDisp" value="${d.cupo_disponible}" min="0" required></td>
                            <td>
                                <select name="horarioIdDisp" required>
                                    <c:forEach var="h" items="${listaHorarios}">
                                        <option value="${h.idHorarios}" ${h.idHorarios == d.horarios_idHorarios ? 'selected' : ''}>${h.hora_ini} - ${h.hora_fin}</option>
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
    </main>
</body>
</html>
