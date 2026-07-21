package Servlet;
import Controlador.RolesDAO;
import Controlador.Tipo_documentoDAO;
import Controlador.UsuariosDAO;
import Modelo.Roles;
import Modelo.Tipo_documento;
import Modelo.Usuarios;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Registrarse", urlPatterns = {"/Registrarse"})
public class Registrarse extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // formulario
        String nombre = request.getParameter("nombrep");
        String apellido = request.getParameter("apellidoa");
        String documento = request.getParameter("documentoa");
        System.out.println("Documento recibido: " + documento);
        String telefono = request.getParameter("telefonoi");
        String correo = request.getParameter("correoz");
        String clave = request.getParameter("clavev");
        int Tipo_documento_idTipo_documento = Integer.parseInt(request.getParameter("tipodocs"));
        int Roles_idRoles = Integer.parseInt(request.getParameter("rola"));
        
        
        // Crear objeto 
        Usuarios usuario = new Usuarios();
        usuario.setnombre(nombre);
        usuario.setapellido(apellido);
        usuario.setdocumento(documento);
        usuario.settelefono(telefono);
        usuario.setcorreo(correo);
        usuario.setfecha_nac(Date.valueOf(request.getParameter("fecha_nac")));
        usuario.setclave(clave);
        usuario.setTipo_documento_idTipo_documento(Tipo_documento_idTipo_documento);
        usuario.setRoles_idRoles(Roles_idRoles);
        
        LocalDate fechaCad = LocalDate.now().plusYears(1);
        usuario.setfecha_cad(Date.valueOf(fechaCad));
        
        usuario.setcheckbox(request.getParameter("checkbox") != null);
        
        // DAO
        UsuariosDAO usuariosDao = new UsuariosDAO();
        
        // Verifica si ya existe el documento
        if (usuariosDao.existeUsuario(documento)) {
            request.setAttribute("resultado", "El documento ya está registrado");
        } else {
            // Registra el usuario
            boolean resultado = false;
            try {
                resultado = usuariosDao.insertarUsuarios(usuario);
                if (resultado) {
                    request.setAttribute("resultado", "Usuario registrado exitosamente");
                } else {
                    request.setAttribute("resultado", "Error al registrar usuario");
                }
            } catch (Exception e) {
                request.setAttribute("resultado", "Error: " + e.getMessage());
            }
        }
        
        // Carga combos y va al JSP
        RolesDAO rolesDAO = new RolesDAO();
        Tipo_documentoDAO tipoDAO = new Tipo_documentoDAO();
        request.setAttribute("roles", rolesDAO.listarRoles());
        request.setAttribute("tiposDoc", tipoDAO.listarTipoDocumento());
        request.getRequestDispatcher("/Vista/Registrarse.jsp").forward(request, response);
    }
}
