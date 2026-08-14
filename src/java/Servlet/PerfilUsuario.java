package Servlet;

import Modelo.Usuarios;
import Controlador.UsuariosDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.SQLException;

@WebServlet("/PerfilUsuario")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize       = 5 * 1024 * 1024,  // 5 MB
    maxRequestSize    = 10 * 1024 * 1024  // 10 MB
)
public class PerfilUsuario extends HttpServlet {

    private static final String VISTA_PERFIL = "/Vista/PerfilUsuario.jsp";
    private static final String CARPETA_FOTOS = "Vista/Imagenes/perfiles";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!esUsuario(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        HttpSession sesion = request.getSession(false);
        int idUsuario = (Integer) sesion.getAttribute("idUsuario");

        UsuariosDAO dao = new UsuariosDAO();
        Usuarios usuario = dao.ConsultaUsuariosPorId(idUsuario);

        if (usuario != null) {
            request.setAttribute("usuario", usuario);
        } else {
            request.setAttribute("mensajeError", "No se encontró la información del usuario.");
        }

        request.getRequestDispatcher(VISTA_PERFIL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!esUsuario(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        HttpSession sesion = request.getSession(false);
        int idUsuario = (Integer) sesion.getAttribute("idUsuario");

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");

        UsuariosDAO dao = new UsuariosDAO();

        try {
            // Guardar o cambiar la foto si el usuario subió una nueva
            String rutaFoto = procesarFoto(request);
            if (rutaFoto == null) {
                // Conservar la foto actual
                rutaFoto = request.getParameter("fotoActual");
            }

            boolean ok = dao.actualizarPerfil(idUsuario, nombre, correo, telefono, rutaFoto);

            if (ok) {
                // Actualizar el nombre en la sesión por si cambió
                sesion.setAttribute("nombreUsuario", nombre);
                request.setAttribute("mensajeExito", "Perfil actualizado correctamente.");
            } else {
                request.setAttribute("mensajeError", "No fue posible actualizar el perfil.");
            }
        } catch (SQLException e) {
            request.setAttribute("mensajeError", "Error de base de datos: " + e.getMessage());
        }

        // Volver a cargar el usuario modificado para mostrar en la vista
        Usuarios usuario = dao.ConsultaUsuariosPorId(idUsuario);
        request.setAttribute("usuario", usuario);

        request.getRequestDispatcher(VISTA_PERFIL).forward(request, response);
    }

    private boolean esUsuario(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        return sesion != null && sesion.getAttribute("idUsuario") instanceof Integer
                && ((Integer) sesion.getAttribute("idUsuario")) > 0
                && !Integer.valueOf(1).equals(sesion.getAttribute("perfil"));
    }

    /**
     * Procesa la foto subida y la guarda en la carpeta CARPETA_FOTOS.
     * Retorna la ruta relativa para persistir en BD o null si no se seleccionó archivo.
     */
    private String procesarFoto(HttpServletRequest request) throws IOException, ServletException {
        try {
            Part fotoPart = request.getPart("foto_perfil");
            if (fotoPart == null) return null;

            String nombreArchivo = obtenerNombreArchivo(fotoPart);
            if (nombreArchivo == null || nombreArchivo.isEmpty()) return null;

            String dirRealPath = getServletContext().getRealPath("") + File.separator
                    + CARPETA_FOTOS.replace("/", File.separator);
            File dir = new File(dirRealPath);
            if (!dir.exists()) dir.mkdirs();

            String extension = "";
            int puntoIdx = nombreArchivo.lastIndexOf('.');
            if (puntoIdx >= 0) extension = nombreArchivo.substring(puntoIdx);
            String nombreUnico = System.currentTimeMillis() + extension;

            File archivo = new File(dir, nombreUnico);
            try (InputStream in = fotoPart.getInputStream();
                 OutputStream out = Files.newOutputStream(archivo.toPath())) {
                byte[] buf = new byte[8192];
                int bytesLeidos;
                while ((bytesLeidos = in.read(buf)) != -1) {
                    out.write(buf, 0, bytesLeidos);
                }
            }

            return CARPETA_FOTOS + "/" + nombreUnico;
        } catch (Exception e) {
            // Si la petición no es multipart o hay un problema leyendo la parte, simplemente retornamos null
            return null;
        }
    }

    private String obtenerNombreArchivo(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return null;
        for (String token : contentDisposition.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String nombre = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                int sep = nombre.lastIndexOf('/');
                if (sep < 0) sep = nombre.lastIndexOf('\\');
                if (sep >= 0) nombre = nombre.substring(sep + 1);
                return nombre;
            }
        }
        return null;
    }
}
