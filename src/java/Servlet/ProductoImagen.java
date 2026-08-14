package Servlet;

import Controlador.ProductoMenuDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ProductoImagen")
public class ProductoImagen extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idProducto = enteroPositivo(request.getParameter("id"));
            ProductoMenuDAO.Imagen imagen = new ProductoMenuDAO().obtenerImagen(idProducto);
            if (imagen == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType(imagen.tipo == null ? "image/jpeg" : imagen.tipo);
            response.setContentLength(imagen.datos.length);
            response.getOutputStream().write(imagen.datos);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private int enteroPositivo(String valor) {
        try {
            int numero = Integer.parseInt(valor);
            if (numero <= 0) throw new NumberFormatException();
            return numero;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Identificador de producto inválido.", e);
        }
    }
}
