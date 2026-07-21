package Servlet;

import Controlador.UsuariosDAO;
import Modelo.Usuarios;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Iniciar")
public class InicioSesion extends HttpServlet {

    /**
     * Método para procesar el login con POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Captura los datos del formulario
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("pass");

        // Consulta el usuario en la base de datos
        UsuariosDAO midao = new UsuariosDAO();
        Usuarios usuarioBD = midao.ConsultaUsuarios(usuario);

        if (usuarioBD == null) {
            // Usuario no existe
            request.setAttribute("mensaje", "El documento no existe");
            request.getRequestDispatcher("/Vista/InicioSesion.jsp").forward(request, response);

        } else if (!usuarioBD.getclave().equals(password)) {
            // Contraseña incorrecta
            request.setAttribute("mensaje", "Clave incorrecta");

            request.getRequestDispatcher("/Vista/InicioSesion.jsp").forward(request, response);

        } else {
            // Login exitoso, enviamos datos al JSP
            HttpSession sesion = request.getSession();
            sesion.setAttribute("idUsuario", usuarioBD.getidUsuarios());
            sesion.setAttribute("nombreUsuario", usuarioBD.getnombre());
            sesion.setAttribute("perfil", usuarioBD.getRoles_idRoles());
            request.setAttribute("mensaje", "Bienvenido: " + usuarioBD.getnombre());

            // Aquí va la separación de roles
           if (usuarioBD.getRoles_idRoles() == 1) {
                response.sendRedirect(request.getContextPath() + "/PanelAdmin.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/PanelUsuario.jsp");
            }

        }
    }

    /**
     * Método GET (opcional, aquí solo puedes redirigir al login)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige al login si alguien intenta acceder vía GET
        request.getRequestDispatcher("/Vista/InicioSesion.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para manejar inicio de sesión de usuarios";
    }
}
