package Servlet;

import Controlador.UsuariosDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CambiarClave")
public class CambiarClave extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Vista/CambiarClave.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        String codigo = request.getParameter("codigo");
        String clave = request.getParameter("clave");
        String confirmarClave = request.getParameter("confirmarClave");

        if (!recuperacionValida(sesion, codigo)) {
            request.setAttribute("mensaje", "El código no es válido o ya venció. Solicita uno nuevo.");
        } else if (clave == null || clave.length() < 6) {
            request.setAttribute("mensaje", "La contraseña debe tener al menos 6 caracteres.");
        } else if (!clave.equals(confirmarClave)) {
            request.setAttribute("mensaje", "Las contraseñas no coinciden.");
        } else {
            int idUsuario = (Integer) sesion.getAttribute("recuperacionUsuarioId");
            if (new UsuariosDAO().actualizarClave(idUsuario, clave)) {
                sesion.removeAttribute("recuperacionCodigo");
                sesion.removeAttribute("recuperacionUsuarioId");
                sesion.removeAttribute("recuperacionVence");
                response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp?mensaje=Contraseña actualizada. Inicia sesión.");
                return;
            }
            request.setAttribute("mensaje", "No fue posible actualizar la contraseña. Intenta nuevamente.");
        }
        request.getRequestDispatcher("/Vista/CambiarClave.jsp").forward(request, response);
    }

    private boolean recuperacionValida(HttpSession sesion, String codigo) {
        if (sesion == null || codigo == null) return false;
        Object codigoGuardado = sesion.getAttribute("recuperacionCodigo");
        Object vence = sesion.getAttribute("recuperacionVence");
        return codigoGuardado != null && codigo.equals(codigoGuardado)
                && vence instanceof Long && System.currentTimeMillis() <= (Long) vence;
    }
}
