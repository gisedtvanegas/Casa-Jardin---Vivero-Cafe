package Servlet;

import Controlador.ActividadDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Muestra actividades en la vista correcta según la sesión del visitante. */
@WebServlet(urlPatterns = {"/ActividadesPublicas", "/ActividadesUsuario"})
public class ActividadesPublicas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaActividades", new ActividadDAO().listarActividadesPublicas(4));
        HttpSession sesion = request.getSession(false);
        boolean esUsuarioAutenticado = sesion != null
                && sesion.getAttribute("perfil") instanceof Integer
                && !Integer.valueOf(1).equals(sesion.getAttribute("perfil"));
        request.getRequestDispatcher(esUsuarioAutenticado
                ? "/Vista/ActividadUsuario.jsp"
                : "/Vista/Actividad.jsp").forward(request, response);
    }
}
