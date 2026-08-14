package Servlet;

import Modelo.Actividad;
import Controlador.ActividadDAO;
import Controlador.Lista_preciosDAO;
import Controlador.Tipo_ActividadDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Actividad", urlPatterns = {"/Actividad"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize       = 5 * 1024 * 1024,  // 5 MB
    maxRequestSize    = 10 * 1024 * 1024  // 10 MB
)
public class Actividadser extends HttpServlet {

    private static final String VISTA_ACTIVIDAD = "/Vista/Actividad_admi.jsp";
    /** Ruta relativa (desde la raíz del contexto) donde se guardan las fotos. */
    private static final String CARPETA_FOTOS = "Vista/Imagenes/actividades";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        ActividadDAO dao = new ActividadDAO();

        try {
            if ("insertar".equalsIgnoreCase(accion)) {
                String descripcion = request.getParameter("descripcionAct");
                String informacion = request.getParameter("informacionAct");
                int tipoActi = Integer.parseInt(request.getParameter("tipoActi"));
                int listaPrecio = Integer.parseInt(request.getParameter("listaPrecioAct"));

                Actividad actividad = new Actividad();
                actividad.setdescripcion_actividad(descripcion);
                actividad.setinformacion(informacion);
                actividad.setTipo_Actividad_idTipo_Actividad(tipoActi);
                actividad.setLista_Precios_idLista_Precios(listaPrecio);

                // Procesar foto si se subió
                String rutaFoto = procesarFoto(request);
                actividad.setfoto_actividad(rutaFoto);

                boolean ok = dao.insertarActividad(actividad);
                request.setAttribute("mensaje", ok ? "Actividad insertada correctamente." : "Error al insertar actividad.");

            } else if ("actualizar".equalsIgnoreCase(accion)) {
                int id = Integer.parseInt(request.getParameter("idActividad"));
                String descripcion = request.getParameter("descripcionAct");
                String informacion = request.getParameter("informacionAct");
                int tipoActi = Integer.parseInt(request.getParameter("tipoActi"));
                int listaPrecio = Integer.parseInt(request.getParameter("listaPrecioAct"));

                Actividad actividad = new Actividad();
                actividad.setidActividad(id);
                actividad.setdescripcion_actividad(descripcion);
                actividad.setinformacion(informacion);
                actividad.setTipo_Actividad_idTipo_Actividad(tipoActi);
                actividad.setLista_Precios_idLista_Precios(listaPrecio);

                // Si se sube nueva foto la reemplazamos; si no, conservamos la actual
                String rutaFotoNueva = procesarFoto(request);
                if (rutaFotoNueva != null) {
                    actividad.setfoto_actividad(rutaFotoNueva);
                } else {
                    // Conservar foto actual que viene como campo oculto
                    String fotoActual = request.getParameter("fotoActual");
                    actividad.setfoto_actividad(fotoActual);
                }

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

    /**
     * Procesa la parte de la foto del formulario multipart.
     * Guarda el archivo en CARPETA_FOTOS y devuelve la ruta relativa
     * (e.g. "Vista/Imagenes/actividades/1234567890_foto.jpg"),
     * o null si no se seleccionó ningún archivo.
     */
    private String procesarFoto(HttpServletRequest request) throws IOException, ServletException {
        Part fotoPart = request.getPart("fotoActividad");
        if (fotoPart == null) return null;

        String nombreArchivo = obtenerNombreArchivo(fotoPart);
        if (nombreArchivo == null || nombreArchivo.isEmpty()) return null;

        // Directorio real en el sistema de archivos
        String dirRealPath = getServletContext().getRealPath("") + File.separator
                + CARPETA_FOTOS.replace("/", File.separator);
        File dir = new File(dirRealPath);
        if (!dir.exists()) dir.mkdirs();

        // Nombre único para evitar colisiones
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

        // Ruta relativa para guardar en BD y referenciar desde JSP
        return CARPETA_FOTOS + "/" + nombreUnico;
    }

    /** Extrae el nombre del archivo del header Content-Disposition. */
    private String obtenerNombreArchivo(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return null;
        for (String token : contentDisposition.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String nombre = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                // Solo el nombre base (sin ruta completa de Windows/Linux)
                int sep = nombre.lastIndexOf('/');
                if (sep < 0) sep = nombre.lastIndexOf('\\');
                if (sep >= 0) nombre = nombre.substring(sep + 1);
                return nombre;
            }
        }
        return null;
    }
}
