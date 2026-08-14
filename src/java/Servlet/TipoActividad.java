package Servlet;

import Controlador.Tipo_ActividadDAO;
import Modelo.Tipo_Actividad;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TipoActividad", urlPatterns = {"/TipoActividad"})
public class TipoActividad extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        Tipo_ActividadDAO dao = new Tipo_ActividadDAO();

        try {
            switch (accion) {
                case "insertar":
                    Tipo_Actividad nuevo = new Tipo_Actividad();
                    nuevo.setnombre_activi(request.getParameter("nombreActivi"));
                    dao.insertarTipo_Actividad(nuevo);
                    request.setAttribute("mensaje", "Tipo de actividad insertado correctamente.");
                    break;

                case "actualizar":
                    Tipo_Actividad actualizado = new Tipo_Actividad();
                    actualizado.setidTipo_Actividad(Integer.parseInt(request.getParameter("idTipo_Actividad")));
                    actualizado.setnombre_activi(request.getParameter("nombreActivi"));
                    dao.actualizarTipoActividad(actualizado);
                    request.setAttribute("mensaje", "Tipo de actividad actualizado correctamente.");
                    break;

                case "eliminar":
                    int idEliminar = Integer.parseInt(request.getParameter("idTipo_Actividad"));
                    dao.eliminarTipoActividad(idEliminar);
                    request.setAttribute("mensaje", "Tipo de actividad eliminado correctamente.");
                    break;
            }
        } catch (SQLException e) {
            request.setAttribute("mensaje", "Error en la operación: " + e.getMessage());
        }

        List<Tipo_Actividad> lista = dao.listarTipoActividad();
        request.setAttribute("listaTiposActividad", lista);

        request.getRequestDispatcher("Vista/TipoActividad_admi.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Tipo_ActividadDAO dao = new Tipo_ActividadDAO();
        List<Tipo_Actividad> lista = dao.listarTipoActividad();
        request.setAttribute("listaTiposActividad", lista);
        request.getRequestDispatcher("Vista/TipoActividad_admi.jsp").forward(request, response);
    }
}
