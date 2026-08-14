package servlet;

import Controlador.ReservaDAO;
import Controlador.Lista_preciosDAO;
import Controlador.ActividadDAO;
import Controlador.UsuariosDAO;
import Controlador.PagosDAO;
import Modelo.Reserva;
import Modelo.Lista_precios;
import Modelo.Actividad;
import Modelo.Usuarios;
import Modelo.Pagos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/RegistroPago")
public class Pagosfake extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("idUsuario") == null
                || !"Usuario".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        int idUsuario = Integer.parseInt(String.valueOf(session.getAttribute("idUsuario")));
        int idReserva = parseInt(request.getParameter("id"));

        try {
            ReservaDAO reservaDAO = new ReservaDAO();
            Reserva reserva = reservaDAO.ConsultarReserva(idReserva);

            if (reserva == null || reserva.getUsuarios_idUsuarios() != idUsuario) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reserva no encontrada.");
                return;
            }

            UsuariosDAO usuariosDAO = new UsuariosDAO();
            Usuarios cliente = usuariosDAO.ConsultaUsuariosPorId(reserva.getUsuarios_idUsuarios());

            ActividadDAO actividadDAO = new ActividadDAO();
            Actividad actividad = actividadDAO.ConsultaActividad(reserva.getActividad_idActividad());

            Lista_preciosDAO preciosDAO = new Lista_preciosDAO();
            Lista_precios precio = preciosDAO.ConsultarLista_precios(actividad.getLista_Precios_idLista_Precios());

            PagosDAO pagosDAO = new PagosDAO();
            Pagos pago = pagosDAO.consultarPagos(reserva.getPagos_idPagos());

            // Calcular total
            int numPersonas = reserva.getNum_personas();
            int precioUnitario = Integer.parseInt(precio.getdescrip_precio());
            int total = numPersonas * precioUnitario;

            // Generar HTML
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Registro de Pago</title>");
                out.println("<style>");
                out.println("body{margin:0;background:#f3f4f6;color:#111827;font-family:Arial,sans-serif;}");
                out.println(".wrap{max-width:820px;margin:32px auto;padding:0 16px;}");
                out.println(".registro{background:#fff;border:1px solid #d1d5db;padding:34px;border-radius:8px;}");
                out.println(".top{display:flex;justify-content:space-between;gap:20px;border-bottom:2px solid #111827;padding-bottom:18px;}");
                out.println("h1{margin:0;font-size:28px;} h2{margin:0 0 6px;font-size:18px;} p{margin:4px 0;color:#374151;}");
                out.println("table{width:100%;border-collapse:collapse;margin-top:16px;} th,td{border-bottom:1px solid #e5e7eb;padding:12px;text-align:left;} th{background:#f9fafb;}");
                out.println(".total{text-align:right;font-size:22px;font-weight:700;margin-top:22px;}");
                out.println(".actions{display:flex;gap:10px;justify-content:flex-end;margin:18px 0;}");
                out.println("button{border:0;background:#111827;color:#fff;border-radius:6px;padding:10px 14px;cursor:pointer;} button.sec{background:#4b5563;}");
                out.println("@media print{body{background:#fff}.actions{display:none}.wrap{margin:0;max-width:none}.registro{border:0;border-radius:0}}");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='wrap'>");
                out.println("<div class='actions'>");
                out.println("<button onclick='window.print()'>Imprimir / guardar PDF</button>");
                out.println("</div>");
                out.println("<main class='registro'>");
                out.println("<div class='top'>");
                out.println("<div><h1>VIVERO CAFÉ</h1><p>Registro de pago</p></div>");
                out.println("<div><h2>Reserva #" + reserva.getidReserva() + "</h2><p>Fecha: " + reserva.getFecha() + "</p></div>");
                out.println("</div>");
                out.println("<section>");
                out.println("<h2>Cliente</h2><p>" + cliente.getnombre() + " " + cliente.getapellido() + "</p><p>" + cliente.getcorreo() + "</p>");
                out.println("<h2>Actividad</h2><p>" + actividad.getdescripcion_actividad() + "</p>");
                out.println("<h2>Pago</h2><p>Estado: " + pago.getestado_pago() + "</p>");
                out.println("</section>");
                out.println("<table>");
                out.println("<thead><tr><th>Concepto</th><th>Cantidad</th><th>Valor unitario</th><th>Total</th></tr></thead>");
                out.println("<tbody><tr>");
                out.println("<td>" + actividad.getdescripcion_actividad() + "</td>");
                out.println("<td>" + numPersonas + "</td>");
                out.println("<td>$ " + precioUnitario + "</td>");
                out.println("<td>$ " + total + "</td>");
                out.println("</tr></tbody>");
                out.println("</table>");
                out.println("<div class='total'>Total: $ " + total + "</div>");
                out.println("</main>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se pudo cargar el registro de pago.");
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
