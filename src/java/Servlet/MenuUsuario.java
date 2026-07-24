package Servlet;

import Controlador.ProductoMenuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/MenuUsuario")
public class MenuUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("productos", new ProductoMenuDAO().listar());
        req.getRequestDispatcher("/Vista/MenuUsuario.jsp").forward(req, res);
    }
}
