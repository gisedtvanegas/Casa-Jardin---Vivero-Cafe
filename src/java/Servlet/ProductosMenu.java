package Servlet;

import Controlador.ProductoMenuDAO;
import Modelo.ProductoMenu;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@WebServlet("/ProductosMenu")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class ProductosMenu extends HttpServlet {
    private static final String VISTA = "/Vista/MenuAdmin.jsp";

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { cargar(req); req.getRequestDispatcher(VISTA).forward(req, res); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ProductoMenuDAO dao = new ProductoMenuDAO();
        try {
            String accion = req.getParameter("accion");
            if ("eliminar".equals(accion)) {
                req.setAttribute("mensaje", dao.eliminar(entero(req.getParameter("idProducto"))) ? "Producto eliminado correctamente." : "No fue posible eliminar el producto.");
            } else {
                ProductoMenu p = productoDesde(req);
                Part foto = req.getPart("foto");
                boolean tieneFotoNueva = foto != null && foto.getSize() > 0;
                if (tieneFotoNueva && !foto.getContentType().startsWith("image/")) throw new IllegalArgumentException("El archivo debe ser una imagen.");
                InputStream imagen = tieneFotoNueva ? foto.getInputStream() : null;
                boolean ok;
                try { ok = "actualizar".equals(accion) ? dao.actualizar(p, imagen, tieneFotoNueva ? foto.getContentType() : null) : dao.insertar(p, imagen, tieneFotoNueva ? foto.getContentType() : null); }
                finally { if (imagen != null) imagen.close(); }
                req.setAttribute("mensaje", ok ? ("actualizar".equals(accion) ? "Producto actualizado correctamente." : "Producto agregado al menú.") : "No fue posible guardar el producto.");
            }
        } catch (Exception e) { req.setAttribute("mensaje", "Error: " + e.getMessage()); }
        cargar(req); req.getRequestDispatcher(VISTA).forward(req, res);
    }
    private ProductoMenu productoDesde(HttpServletRequest req) {
        ProductoMenu p = new ProductoMenu();
        if ("actualizar".equals(req.getParameter("accion"))) p.setIdProducto(entero(req.getParameter("idProducto")));
        p.setNombre(req.getParameter("nombre").trim()); p.setDescripcion(req.getParameter("descripcion").trim());
        p.setPrecio(new BigDecimal(req.getParameter("precio"))); return p;
    }
    private int entero(String valor) { return Integer.parseInt(valor); }
    private void cargar(HttpServletRequest req) { req.setAttribute("productos", new ProductoMenuDAO().listar()); }
}
