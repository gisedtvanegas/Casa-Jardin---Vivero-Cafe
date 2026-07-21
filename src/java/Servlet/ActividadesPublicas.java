package Servlet;

import Controlador.ActividadDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Muestra las actividades disponibles en la pagina publica. */
@WebServlet("/ActividadesPublicas")
public class ActividadesPublicas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaActividades", new ActividadDAO().listarActividadesPublicas(4));
        request.getRequestDispatcher("/Vista/Actividad.jsp").forward(request, response);
    }
}
