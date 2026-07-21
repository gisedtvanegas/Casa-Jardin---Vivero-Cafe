package Servlet;
import Controlador.ProductoMenuDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/ProductoImagen")
public class ProductoImagen extends HttpServlet {
 @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException { try { ProductoMenuDAO.Imagen imagen = new ProductoMenuDAO().obtenerImagen(Integer.parseInt(req.getParameter("id"))); if (imagen == null) { res.sendError(404); return; } res.setContentType(imagen.tipo == null ? "image/jpeg" : imagen.tipo); res.setContentLength(imagen.datos.length); res.getOutputStream().write(imagen.datos); } catch (Exception e) { res.sendError(404); } }
}
