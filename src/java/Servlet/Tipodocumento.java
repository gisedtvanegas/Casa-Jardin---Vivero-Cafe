package Servlet;

import Modelo.Tipo_documento;
import Controlador.Tipo_documentoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Tipodocumento", urlPatterns = {"/Tipodocumento"})
public class Tipodocumento extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        Tipo_documentoDAO dao = new Tipo_documentoDAO();
        
        try {
            switch (accion) {
                case "insertar":
                Tipo_documento doc = new Tipo_documento();
                doc.setdescripcion_doc(request.getParameter("descripcion_doc"));
                dao.insertarTipo_documento(doc);
                request.setAttribute("mensaje", "Documento insertado correctamente.");
                break;
                case "actualizar":
                Tipo_documento actualizado = new Tipo_documento();
                actualizado.setidTipo_documento(Integer.parseInt(request.getParameter("idTipo_documento")));
                actualizado.setdescripcion_doc(request.getParameter("descripcion_doc"));
                dao.actualizarTipoDocumento(actualizado);
                request.setAttribute("mensaje", "Documento actualizado correctamente.");
                break;
                case "eliminar":
                dao.eliminarTipoDocumento(Integer.parseInt(request.getParameter("idTipo_documento")));
                request.setAttribute("mensaje", "Documento eliminado correctamente.");
                break;
            }
        } catch (SQLException e) {
            request.setAttribute("mensaje","Error en operaciones de TipoDocumento: " + e.getMessage());
        }
    
        List<Tipo_documento> lista = dao.listarTipoDocumento();
        request.setAttribute("listaTiposDocumento", lista);
        
        request.getRequestDispatcher("/Vista/Tipodocumento_admin.jsp").forward(request, response);  
    }        
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Tipo_documentoDAO dao = new Tipo_documentoDAO();
        List<Tipo_documento> lista = dao.listarTipoDocumento();
        request.setAttribute("listaTiposDocumento", lista);
        request.getRequestDispatcher("Vista/Tipodocumento_admin.jsp").forward(request, response);
    }
}
