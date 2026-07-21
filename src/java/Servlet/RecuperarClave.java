package Servlet;

import Controlador.CorreoRecuperacion;
import Controlador.UsuariosDAO;
import Modelo.Usuarios;
import jakarta.mail.AuthenticationFailedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;

@WebServlet("/RecuperarClave")
public class RecuperarClave extends HttpServlet {
    private static final SecureRandom ALEATORIO = new SecureRandom();
    private static final long VIGENCIA_CODIGO_MS = 15 * 60 * 1000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Vista/RecuperarClave.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo = request.getParameter("correo");
        Usuarios usuario = new UsuariosDAO().consultaUsuarioPorCorreo(correo);

        if (usuario == null) {
            request.setAttribute("mensaje", "No encontramos una cuenta registrada con ese correo.");
            request.getRequestDispatcher("/Vista/RecuperarClave.jsp").forward(request, response);
            return;
        }

        String codigo = String.format("%05d", ALEATORIO.nextInt(100000));
        try {
            new CorreoRecuperacion().enviarCodigo(usuario.getcorreo(), codigo);
            HttpSession sesion = request.getSession();
            sesion.setAttribute("recuperacionCodigo", codigo);
            sesion.setAttribute("recuperacionUsuarioId", usuario.getidUsuarios());
            sesion.setAttribute("recuperacionVence", System.currentTimeMillis() + VIGENCIA_CODIGO_MS);
            getServletContext().log("Correo de recuperación enviado correctamente.");
            response.sendRedirect(request.getContextPath() + "/CambiarClave");
        } catch (AuthenticationFailedException e) {
            getServletContext().log("Gmail rechazo las credenciales del remitente.", e);
            request.setAttribute("mensaje", "Gmail rechazó el correo o la contraseña de aplicación configurada.");
            request.getRequestDispatcher("/Vista/RecuperarClave.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("No fue posible enviar el correo de recuperacion.", e);
            request.setAttribute("mensaje", "No fue posible enviar el correo. Revise la configuración de Gmail e intente nuevamente.");
            request.getRequestDispatcher("/Vista/RecuperarClave.jsp").forward(request, response);
        }
    }
}
