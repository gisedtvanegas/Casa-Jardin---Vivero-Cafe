package Servlet;

import Modelo.Roles;
import Controlador.RolesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Roles", urlPatterns = {"/Roles"})
public class Roless extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        RolesDAO dao = new RolesDAO();
        
        try {
            if ("insertar".equalsIgnoreCase(accion)) {
                String descripcion = request.getParameter("descripcion_rol");
                
                Roles rol = new Roles();
                rol.setdescripcion_rol(descripcion);
                
                boolean ok = dao.insertarRol(rol);
                request.setAttribute("mensaje", ok ? "Rol insertado correctamente." : "Error al insertar rol.");
                
            } else if ("actualizar".equalsIgnoreCase(accion)) {
                Roles rol = new Roles();
                rol.setidRoles(Integer.parseInt(request.getParameter("idRoles")));
                rol.setdescripcion_rol(request.getParameter("descripcion_rol"));
                boolean ok = dao.actualizarRol(rol);
                request.setAttribute("mensaje", ok ? "Rol actualizado correctamente." : "Error al actualizar rol.");
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                int id = Integer.parseInt(request.getParameter("idRoles"));
                boolean ok = dao.eliminarRol(id);
                request.setAttribute("mensaje", ok ? "Rol eliminado correctamente." : "Error al eliminar rol.");
            }
            
            List<Roles> lista = dao.listarRoles();
            request.setAttribute("listaRoles", lista);
            
            request.getRequestDispatcher("/Vista/Roles.jsp").forward(request, response);
            
        } catch (SQLException e) {
            throw new ServletException("Error en operaciones de Roles: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RolesDAO dao = new RolesDAO();
        List<Roles> lista = dao.listarRoles();
        request.setAttribute("listaRoles", lista);
        request.getRequestDispatcher("/Vista/Roles.jsp").forward(request, response);
    }
}
