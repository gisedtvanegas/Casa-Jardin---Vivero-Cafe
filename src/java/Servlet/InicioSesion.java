package Servlet;

import Controlador.UsuariosDAO;
import Controlador.PasswordSecurity;
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

        } else if (!coincideClave(password, usuarioBD)) {
            // Contraseña incorrecta
            request.setAttribute("mensaje", "Clave incorrecta");

            request.getRequestDispatcher("/Vista/InicioSesion.jsp").forward(request, response);

        } else {
            // Evita fijación de sesión: cualquier identificador anterior se
            // invalida y se crea uno nuevo solo después de autenticar.
            HttpSession anterior = request.getSession(false);
            if (anterior != null) {
                anterior.invalidate();
            }
            HttpSession sesion = request.getSession(true);
            // Mantiene la sesión durante una jornada activa. El cierre
            // explícito sigue invalidándola y el identificador se renueva.
            sesion.setMaxInactiveInterval(8 * 60 * 60);
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
        // Volver atrás no debe mostrar un inicio de sesión cuando ya existe una
        // sesión activa; se devuelve al panel correspondiente.
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("perfil") instanceof Integer) {
            int perfil = (Integer) sesion.getAttribute("perfil");
            response.sendRedirect(request.getContextPath()
                    + (perfil == 1 ? "/PanelAdmin.jsp" : "/PanelUsuario.jsp"));
            return;
        }
        request.getRequestDispatcher("/Vista/InicioSesion.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para manejar inicio de sesión de usuarios";
    }

    private boolean coincideClave(String claveIngresada, Usuarios usuario) {
        String claveAlmacenada = usuario.getclave();
        if (PasswordSecurity.esHash(claveAlmacenada)) {
            return PasswordSecurity.verificar(claveIngresada, claveAlmacenada);
        }

        // Compatibilidad temporal: al primer inicio correcto se actualiza la
        // cuenta antigua a PBKDF2, sin dejar contraseñas nuevas en texto plano.
        boolean coincide = claveIngresada != null && claveIngresada.equals(claveAlmacenada);
        if (coincide) {
            new UsuariosDAO().actualizarClave(usuario.getidUsuarios(), PasswordSecurity.hash(claveIngresada));
            // La migración se completa después de crear la sesión, usando el
            // identificador disponible en el objeto autenticado.
        }
        return coincide;
    }
}
