package Servlet;

import Controlador.ReservaDAO;
import Controlador.ActividadDAO;
import Controlador.DisponibilidadDAO;
import Controlador.Estado_reservaDAO;
import Controlador.HorariosDAO;
import Controlador.PagosDAO;
import Controlador.UsuariosDAO;
import Modelo.Reserva;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ReservaAdmi", urlPatterns = {"/ReservaAdmi"})
public class ReservaAdmi extends HttpServlet {

    private static final String VISTA_RESERVA = "/Vista/ReservaAdmi.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarReservas(request);
        request.getRequestDispatcher(VISTA_RESERVA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        ReservaDAO reservaDao = new ReservaDAO();

        try {
            if ("insertar".equalsIgnoreCase(accion)) {
                Reserva reserva = crearReservaDesdeRequest(request, false);
                boolean resultado = reservaDao.insertarReserva(reserva);
                request.setAttribute("resultado", resultado ? "Reserva registrada exitosamente" : "Error al registrar la reserva");
            } else if ("actualizar".equalsIgnoreCase(accion) || "modificar".equalsIgnoreCase(accion)) {
                Reserva reserva = crearReservaDesdeRequest(request, true);
                boolean resultado = reservaDao.actualizarReserva(reserva);
                request.setAttribute("resultado", resultado ? "Reserva modificada exitosamente" : "Error al modificar la reserva");
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                int idReserva = Integer.parseInt(request.getParameter("idReserva"));
                boolean resultado = reservaDao.eliminarReserva(idReserva);
                request.setAttribute("resultado", resultado ? "Reserva eliminada exitosamente" : "Error al eliminar la reserva");
            } else {
                request.setAttribute("resultado", "Accion no reconocida");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("resultado", "Error: revise que los campos numericos esten completos.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("resultado", "Error: la fecha u hora no tiene un formato valido.");
        } catch (SQLException e) {
            request.setAttribute("resultado", "Error de base de datos: " + e.getMessage());
        }

        cargarReservas(request);
        request.getRequestDispatcher(VISTA_RESERVA).forward(request, response);
    }

    private Reserva crearReservaDesdeRequest(HttpServletRequest request, boolean requiereId) {
        Reserva reserva = new Reserva();
        String idReserva = request.getParameter("idReserva");
        if (requiereId || (idReserva != null && !idReserva.trim().isEmpty())) {
            reserva.setidReserva(Integer.parseInt(idReserva));
        }

        reserva.setNum_personas(Integer.parseInt(request.getParameter("num_personas")));
        reserva.setHora(Time.valueOf(normalizarHora(request.getParameter("hora"))));
        reserva.setFecha(Date.valueOf(request.getParameter("fecha")));
        reserva.setUsuarios_idUsuarios(Integer.parseInt(request.getParameter("Usuarios_idUsuarios")));
        reserva.setDisponibilidad_idDisponibilidad(Integer.parseInt(request.getParameter("Disponibilidad_idDisponibilidad")));
        reserva.setEstado_reserva_idEstado_reserva(Integer.parseInt(request.getParameter("Estado_reserva_idEstado_reserva")));
        reserva.setActividad_idActividad(Integer.parseInt(request.getParameter("Actividad_idActividad")));
        reserva.setPagos_idPagos(Integer.parseInt(request.getParameter("Pagos_idPagos")));
        return reserva;
    }

    private String normalizarHora(String hora) {
        if (hora != null && hora.length() == 5) {
            return hora + ":00";
        }
        return hora;
    }

    private void cargarReservas(HttpServletRequest request) {
        ReservaDAO reservaDao = new ReservaDAO();
        request.setAttribute("listaReservas", reservaDao.listarReserva());
        request.setAttribute("listaUsuarios", new UsuariosDAO().listarUsuarios());
        request.setAttribute("listaDisponibilidades", new DisponibilidadDAO().Disponibilidad());
        request.setAttribute("listaEstadosReserva", new Estado_reservaDAO().Estado_reserva());
        request.setAttribute("listaActividades", new ActividadDAO().Actividad());
        request.setAttribute("listaPagos", new PagosDAO().listarPagos());
        request.setAttribute("listaHorarios", new HorariosDAO().listarHorarios());
    }
}
