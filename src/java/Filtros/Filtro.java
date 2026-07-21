package Filtros;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class Filtro implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();

        // La raiz del proyecto es publica y debe resolver siempre a la portada.
        if (path.equals(req.getContextPath()) || path.equals(req.getContextPath() + "/")) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        if (path.endsWith("/Vista/Actividad.jsp")) {
            res.sendRedirect(req.getContextPath() + "/ActividadesPublicas");
            return;
        }
        if (path.endsWith("/Vista/Menu.jsp")) {
            res.sendRedirect(req.getContextPath() + "/MenuPublico");
            return;
        }

        // Excluir recursos estáticos
        if (path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png") || path.endsWith(".jpg")) {
            chain.doFilter(request, response);
            return;
        }

        // Excluir páginas públicas y servlets de acceso
        if (path.endsWith("index.jsp")
            || path.endsWith("InicioSesion.jsp")
            || path.endsWith("Registrarse.jsp")
            || path.endsWith("Actividad.jsp")
            || path.endsWith("Historia.jsp")
            || path.endsWith("Menu.jsp")
            || path.endsWith("Reserva.jsp")
            || path.contains("CargarRegistro")
            || path.contains("Registrarse")
            || path.contains("Iniciar")
            || path.contains("ActividadesPublicas")
            || path.contains("MenuPublico")
            || path.contains("ProductoImagen")
            || path.contains("RecuperarClave")
            || path.contains("CambiarClave")
            || path.contains("CerrarSesion")) {
            chain.doFilter(request, response);
            return;
        }

        // Validar sesión para páginas privadas
        if (session == null || session.getAttribute("perfil") == null) {
            res.sendRedirect(req.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        chain.doFilter(request, response);
            }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}

