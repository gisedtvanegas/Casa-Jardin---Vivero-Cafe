package Servlet;
import Controlador.Tipo_documentoDAO;
import Controlador.UsuariosDAO;
import Controlador.RolesDAO;
import Modelo.Usuarios;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
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
        
        request.setCharacterEncoding("UTF-8");

        // El rol no se recibe del navegador: asi no se puede alterar el formulario
        // para crear una cuenta administradora.
        String nombre = request.getParameter("nombrep");
        String apellido = request.getParameter("apellidoa");
        String documento = request.getParameter("documentoa");
        String telefono = request.getParameter("telefonoi");
        String correo = request.getParameter("correoz");
        String clave = request.getParameter("clavev");
        String tipoDocumento = request.getParameter("tipodocs");

        if (esVacio(nombre) || esVacio(apellido) || esVacio(documento)
                || esVacio(telefono) || esVacio(correo) || esVacio(clave)
                || esVacio(tipoDocumento) || esVacio(request.getParameter("fecha_nac"))
                || request.getParameter("checkbox") == null) {
            mostrarFormulario(request, response, "Complete todos los campos obligatorios y acepte el tratamiento de datos.");
            return;
        }
        if (!documento.matches("\\d+") || !correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            mostrarFormulario(request, response, "Revise el documento y el correo electronico.");
            return;
        }
        if (clave.length() < 8) {
            mostrarFormulario(request, response, "La contrasena debe tener al menos 8 caracteres.");
            return;
        }

        int Tipo_documento_idTipo_documento;
        try {
            Tipo_documento_idTipo_documento = Integer.parseInt(tipoDocumento);
        } catch (NumberFormatException e) {
            mostrarFormulario(request, response, "El tipo de documento no es valido.");
            return;
        }

        
        // Crear objeto 
        Usuarios usuario = new Usuarios();
        usuario.setnombre(nombre);
        usuario.setapellido(apellido);
        usuario.setdocumento(documento);
        usuario.settelefono(telefono);
        usuario.setcorreo(correo);
        try {
            usuario.setfecha_nac(Date.valueOf(request.getParameter("fecha_nac")));
        } catch (IllegalArgumentException e) {
            mostrarFormulario(request, response, "La fecha de nacimiento no es valida.");
            return;
        }
        usuario.setclave(clave);
        usuario.setTipo_documento_idTipo_documento(Tipo_documento_idTipo_documento);
        
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
                // No se acepta ningun rol enviado por el navegador. Se usa un
                // rol publico existente o se crea uno para evitar fallos de FK.
                usuario.setRoles_idRoles(new RolesDAO().obtenerOCrearRolPublico());
                resultado = usuariosDao.insertarUsuarios(usuario);
                if (resultado) {
                    request.setAttribute("resultado", "Usuario registrado exitosamente");
                } else {
                    request.setAttribute("resultado", "Error al registrar usuario");
                }
            } catch (Exception e) {
                System.err.println("No fue posible registrar usuario: " + e.getMessage());
                // No se expone el detalle de la base de datos al navegador.
                request.setAttribute("resultado", "No fue posible completar el registro. Intente nuevamente.");
            }
        }
        
        // Carga combos y va al JSP
        mostrarFormulario(request, response, (String) request.getAttribute("resultado"));
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String resultado)
            throws ServletException, IOException {
        request.setAttribute("resultado", resultado);
        request.setAttribute("tiposDoc", new Tipo_documentoDAO().listarTipoDocumento());
        request.getRequestDispatcher("/Vista/Registrarse.jsp").forward(request, response);
    }
}
