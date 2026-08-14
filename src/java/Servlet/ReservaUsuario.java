package Servlet;

import Controlador.ActividadDAO;
import Controlador.ReservaDAO;
import Controlador.Tipo_documentoDAO;
import Controlador.UsuariosDAO;
import Modelo.Reserva;
import Modelo.Usuarios;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReservaUsuario", urlPatterns = {"/ReservaUsuario"})
public class ReservaUsuario extends HttpServlet {
    private static final String VISTA_RESERVA = "/Vista/ReservaUsuario.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!esUsuario(request)) { response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp"); return; }
        cargarFormulario(request);
        request.getRequestDispatcher(VISTA_RESERVA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!esUsuario(request)) { response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp"); return; }
        try {
            HttpSession sesion = request.getSession(false);
            int idUsuario = (Integer) sesion.getAttribute("idUsuario");
            String accion = request.getParameter("accion");
            ReservaDAO reservaDao = new ReservaDAO();
            if ("actualizar".equals(accion)) {
                Date fechaNueva = Date.valueOf(request.getParameter("fecha"));
                if (!fechaNueva.toLocalDate().isAfter(LocalDate.now().plusDays(3))) {
                    throw new IllegalArgumentException("La nueva fecha debe tener mas de 3 dias de anticipacion.");
                }
                boolean actualizada = reservaDao.actualizarReservaUsuario(Integer.parseInt(request.getParameter("idReserva")), idUsuario,
                        enteroPositivo(request.getParameter("num_personas")), fechaNueva,
                        Time.valueOf(normalizarHora(request.getParameter("hora"))));
                request.setAttribute("mensaje", actualizada ? "Reserva actualizada correctamente." : "La reserva no puede modificarse: solo se permite con mas de 3 dias de anticipacion.");
            } else if ("cancelar".equals(accion)) {
                boolean cancelada = reservaDao.cancelarReservaUsuario(Integer.parseInt(request.getParameter("idReserva")), idUsuario);
                request.setAttribute("mensaje", cancelada ? "Reserva cancelada correctamente." : "No fue posible cancelar la reserva.");
            } else {
            if (!"confirmado".equals(request.getParameter("pago"))) throw new IllegalArgumentException();
            Reserva reserva = new Reserva();
            reserva.setNum_personas(enteroPositivo(request.getParameter("num_personas")));
            reserva.setFecha(Date.valueOf(request.getParameter("fecha")));
            reserva.setHora(Time.valueOf(normalizarHora(request.getParameter("hora"))));
            reserva.setUsuarios_idUsuarios(idUsuario);
            reserva.setActividad_idActividad(enteroPositivo(request.getParameter("Actividad_idActividad")));
            boolean insertado = reservaDao.insertarReservaPagada(reserva);
            request.setAttribute("mensaje", insertado ? "Reserva realizada con éxito." : "No fue posible registrar la reserva.");
            request.setAttribute("reservaRealizada", insertado);
            }
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("CUPO_NO_DISPONIBLE")) {
                request.setAttribute("mensaje", "Lo lamentamos, no contamos con la disponibilidad de espacio para el día que elegiste la reserva. Intenta nuevamente con una fecha distinta");
            } else {
                request.setAttribute("mensaje", "No fue posible registrar la reserva: " + e.getMessage());
            }
        }
        cargarFormulario(request);
        request.getRequestDispatcher(VISTA_RESERVA).forward(request, response);
    }

    private boolean esUsuario(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        return sesion != null && sesion.getAttribute("idUsuario") instanceof Integer
                && ((Integer) sesion.getAttribute("idUsuario")) > 0
                && !Integer.valueOf(1).equals(sesion.getAttribute("perfil"));
    }

    private void cargarFormulario(HttpServletRequest request) {
        request.setAttribute("listaActividades", new ActividadDAO().Actividad());
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("idUsuario") instanceof Integer) {
            request.setAttribute("listaReservasUsuario", new ReservaDAO().listarReservasPorUsuario((Integer) sesion.getAttribute("idUsuario")));
            Usuarios usuario = new UsuariosDAO().ConsultaUsuariosPorId((Integer) sesion.getAttribute("idUsuario"));
            request.setAttribute("usuarioReserva", usuario);
            if (usuario != null) try { request.setAttribute("tipoDocumentoReserva", new Tipo_documentoDAO().ConsultarTipo_documento(usuario.getTipo_documento_idTipo_documento())); } catch (Exception e) { }
        }
    }
    private int enteroPositivo(String valor) {
        int numero = Integer.parseInt(valor);
        if (numero <= 0 || numero > 10) {
            throw new IllegalArgumentException("El numero de personas debe estar entre 1 y 10.");
        }
        return numero;
    }
    private String normalizarHora(String hora) { return hora != null && hora.length() == 5 ? hora + ":00" : hora; }
}
