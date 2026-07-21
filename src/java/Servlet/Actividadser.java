package Servlet;

import Modelo.Actividad;
import Controlador.ActividadDAO;
import Controlador.Lista_preciosDAO;
import Controlador.Tipo_ActividadDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Actividad", urlPatterns = {"/Actividad"})
public class Actividadser extends HttpServlet {

    private static final String VISTA_ACTIVIDAD = "/Vista/Actividad_admi.jsp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        ActividadDAO dao = new ActividadDAO();
        
        try {
            if ("insertar".equalsIgnoreCase(accion)) {
                String descripcion = request.getParameter("descripcionAct");
                int tipoActi = Integer.parseInt(request.getParameter("tipoActi"));
                int listaPrecio = Integer.parseInt(request.getParameter("listaPrecioAct"));
                
                Actividad actividad = new Actividad();
                actividad.setdescripcion_actividad(descripcion);
                actividad.setTipo_Actividad_idTipo_Actividad(tipoActi);
                actividad.setLista_Precios_idLista_Precios(listaPrecio);
                
                boolean ok = dao.insertarActividad(actividad);
                request.setAttribute("mensaje", ok ? "Actividad insertada correctamente." : "Error al insertar actividad.");
                
            } else if ("actualizar".equalsIgnoreCase(accion)) {
                int id = Integer.parseInt(request.getParameter("idActividad"));
                String descripcion = request.getParameter("descripcionAct");
                int tipoActi = Integer.parseInt(request.getParameter("tipoActi"));
                int listaPrecio = Integer.parseInt(request.getParameter("listaPrecioAct"));
                
                Actividad actividad = new Actividad();
                actividad.setidActividad(id);
                actividad.setdescripcion_actividad(descripcion);
                actividad.setTipo_Actividad_idTipo_Actividad(tipoActi);
                actividad.setLista_Precios_idLista_Precios(listaPrecio);
                
                boolean ok = dao.actualizarActividad(actividad);
                request.setAttribute("mensaje", ok ? "Actividad actualizada correctamente." : "Error al actualizar actividad.");
                
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                int id = Integer.parseInt(request.getParameter("idActividad"));
                boolean ok = dao.eliminarActividad(id);
                request.setAttribute("mensaje", ok ? "Actividad eliminada correctamente." : "Error al eliminar actividad.");
            }
            
            cargarDatosFormulario(request);
            
            request.getRequestDispatcher(VISTA_ACTIVIDAD).forward(request, response);
            
        } catch (SQLException e) {
            String msg = e.getMessage();
            if (msg.contains("foreign key") || msg.contains("a foreign key constraint fails") || msg.contains("CONSTRAINT")) {
                request.setAttribute("mensaje", "No se puede eliminar la actividad porque está asociada a una o más reservas.");
            } else {
                request.setAttribute("mensaje", "Error en la base de datos: " + msg);
            }
            cargarDatosFormulario(request);
            request.getRequestDispatcher(VISTA_ACTIVIDAD).forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Error: seleccione un tipo de actividad y una lista de precios validos.");
            cargarDatosFormulario(request);
            request.getRequestDispatcher(VISTA_ACTIVIDAD).forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarDatosFormulario(request);
        request.getRequestDispatcher(VISTA_ACTIVIDAD).forward(request, response);
    }

    private void cargarDatosFormulario(HttpServletRequest request) {
        ActividadDAO dao = new ActividadDAO();
        List<Actividad> lista = dao.Actividad();
        request.setAttribute("listaActividades", lista);
        request.setAttribute("listaTiposActividad", new Tipo_ActividadDAO().listarTipoActividad());
        request.setAttribute("listaPrecios", new Lista_preciosDAO().listarLista_precios());
    }
}
