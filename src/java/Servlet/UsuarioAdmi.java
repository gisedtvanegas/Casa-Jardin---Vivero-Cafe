package Servlet;

import Controlador.UsuariosDAO;
import Controlador.RolesDAO;
import Controlador.Tipo_documentoDAO;
import Modelo.Usuarios;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UsuarioAdmi", urlPatterns = {"/UsuarioAdmi"})
public class UsuarioAdmi extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarDatos(request);
        request.getRequestDispatcher("/Vista/UsuariosAdmi.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        UsuariosDAO usuariosDao = new UsuariosDAO();
        
        try {
            if ("insertar".equalsIgnoreCase(accion)) {
                Usuarios usuario = crearUsuarioDesdeRequest(request, false);
                if (usuariosDao.existeUsuario(usuario.getdocumento())) {
                    request.setAttribute("resultado", "El documento ya esta registrado");
                } else {
                    boolean resultado = usuariosDao.insertarUsuarios(usuario);
                    request.setAttribute("resultado", resultado ? "Usuario registrado correctamente" : "Error al registrar usuario");
                }
            } else if ("actualizar".equalsIgnoreCase(accion) || "modificar".equalsIgnoreCase(accion)) {
                Usuarios usuario = crearUsuarioDesdeRequest(request, true);
                boolean resultado = usuariosDao.actualizarUsuario(usuario);
                request.setAttribute("resultado", resultado ? "Usuario actualizado correctamente" : "Error al actualizar usuario");
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                int id = Integer.parseInt(request.getParameter("idUsuarios"));
                boolean resultado = usuariosDao.eliminarUsuario(id);
                request.setAttribute("resultado", resultado ? "Usuario eliminado correctamente" : "Error al eliminar usuario");
            } else {
                request.setAttribute("resultado", "Accion no reconocida");
            }
        } catch (Exception e) {
            request.setAttribute("resultado", "Error: " + e.getMessage());
        }
        
        cargarDatos(request);
        request.getRequestDispatcher("/Vista/UsuariosAdmi.jsp").forward(request, response);
    }

    private Usuarios crearUsuarioDesdeRequest(HttpServletRequest request, boolean requiereId) {
        Usuarios usuario = new Usuarios();
        if (requiereId) {
            usuario.setidUsuarios(Integer.parseInt(request.getParameter("idUsuarios")));
        }
        usuario.setnombre(request.getParameter("nombre"));
        usuario.setapellido(request.getParameter("apellido"));
        usuario.setdocumento(request.getParameter("documento"));
        usuario.settelefono(request.getParameter("telefono"));
        usuario.setcorreo(request.getParameter("correo"));
        usuario.setclave(request.getParameter("clave"));
        usuario.setfecha_nac(Date.valueOf(request.getParameter("fecha_nac")));

        String fechaCad = request.getParameter("fecha_cad");
        if (fechaCad == null || fechaCad.trim().isEmpty()) {
            usuario.setfecha_cad(Date.valueOf(LocalDate.now().plusYears(1)));
        } else {
            usuario.setfecha_cad(Date.valueOf(fechaCad));
        }

        usuario.setcheckbox("1".equals(request.getParameter("checkbox")) || "on".equalsIgnoreCase(request.getParameter("checkbox")));
        usuario.setTipo_documento_idTipo_documento(Integer.parseInt(request.getParameter("Tipo_documento_idTipo_documento")));
        usuario.setRoles_idRoles(Integer.parseInt(request.getParameter("Roles_idRoles")));
        return usuario;
    }

    private void cargarDatos(HttpServletRequest request) {
        request.setAttribute("listaUsuarios", new UsuariosDAO().listarUsuarios());
        request.setAttribute("listaTiposDocumento", new Tipo_documentoDAO().listarTipoDocumento());
        request.setAttribute("listaRoles", new RolesDAO().listarRoles());
    }
}
