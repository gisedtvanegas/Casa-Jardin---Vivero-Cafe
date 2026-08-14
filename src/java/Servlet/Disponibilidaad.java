package Servlet;

import Controlador.DisponibilidadDAO;
import Controlador.HorariosDAO;
import Modelo.Disponibilidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Disponibilidaad", urlPatterns = {"/Disponibilidaad"})
public class Disponibilidaad extends HttpServlet {

    private static final String VISTA_DISPONIBILIDAD = "/Vista/Disponibilidad_admi.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarDisponibilidades(request);
        request.getRequestDispatcher(VISTA_DISPONIBILIDAD).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        DisponibilidadDAO disponibilidadDao = new DisponibilidadDAO();

        try {
            if ("insertar".equalsIgnoreCase(accion) || accion == null) {
                Disponibilidad disponibilidad = crearDisponibilidadDesdeRequest(request, false);
                boolean resultado = disponibilidadDao.insertarDisponibilidad(disponibilidad);
                request.setAttribute("resultado", resultado ? "Disponibilidad registrada exitosamente" : "Error al registrar disponibilidad");
            } else if ("actualizar".equalsIgnoreCase(accion)) {
                Disponibilidad disponibilidad = crearDisponibilidadDesdeRequest(request, true);
                boolean resultado = disponibilidadDao.actualizarDisponibilidad(disponibilidad);
                request.setAttribute("resultado", resultado ? "Disponibilidad actualizada exitosamente" : "Error al actualizar disponibilidad");
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                int id = Integer.parseInt(request.getParameter("idDisponibilidad"));
                boolean resultado = disponibilidadDao.eliminarDisponibilidad(id);
                request.setAttribute("resultado", resultado ? "Disponibilidad eliminada exitosamente" : "Error al eliminar disponibilidad");
            }
        } catch (SQLException e) {
            request.setAttribute("resultado", "Error de base de datos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            request.setAttribute("resultado", "Error: revise los datos ingresados.");
        }

        cargarDisponibilidades(request);
        request.getRequestDispatcher(VISTA_DISPONIBILIDAD).forward(request, response);
    }

    private Disponibilidad crearDisponibilidadDesdeRequest(HttpServletRequest request, boolean requiereId) {
        Disponibilidad disponibilidad = new Disponibilidad();
        String id = request.getParameter("idDisponibilidad");
        if (requiereId || (id != null && !id.trim().isEmpty())) {
            disponibilidad.setidDisponibilidad(Integer.parseInt(id));
        }

        disponibilidad.setfecha(java.sql.Date.valueOf(request.getParameter("fechaDisp")));
        disponibilidad.setcupo_total(Integer.parseInt(request.getParameter("cupoTotalDisp")));
        disponibilidad.setcupo_disponible(Integer.parseInt(request.getParameter("cupoDisponibleDisp")));
        disponibilidad.setHorarios_idHorarios(Integer.parseInt(request.getParameter("horarioIdDisp")));
        return disponibilidad;
    }

    private void cargarDisponibilidades(HttpServletRequest request) {
        DisponibilidadDAO dao = new DisponibilidadDAO();
        List<Disponibilidad> lista = dao.Disponibilidad();
        request.setAttribute("listaDisponibilidades", lista);
        request.setAttribute("listaHorarios", new HorariosDAO().listarHorarios());
    }
}
