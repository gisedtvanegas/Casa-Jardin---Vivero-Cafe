package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.Reserva;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReservaDAO {

    private static final int CUPO_LOCAL = 30;
    Conexion conexion = new Conexion();

    public boolean insertarReservaPagada(Reserva reserva) throws SQLException {
        Connection con = conexion.getConn();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        boolean autoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            reserva.setDisponibilidad_idDisponibilidad(obtenerDisponibilidadYValidarCupo(
                    con, reserva.getFecha(), reserva.getHora(), reserva.getNum_personas(), 0));
            reserva.setEstado_reserva_idEstado_reserva(obtenerEstadoConfirmada(con));
            reserva.setPagos_idPagos(insertarPagoPagado(con));
            String sql = "INSERT INTO reserva (num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, reserva.getNum_personas()); ps.setTime(2, reserva.getHora()); ps.setDate(3, reserva.getFecha());
                ps.setInt(4, reserva.getUsuarios_idUsuarios()); ps.setInt(5, reserva.getDisponibilidad_idDisponibilidad());
                ps.setInt(6, reserva.getEstado_reserva_idEstado_reserva()); ps.setInt(7, reserva.getActividad_idActividad()); ps.setInt(8, reserva.getPagos_idPagos());
                if (ps.executeUpdate() != 1) throw new SQLException("No se pudo guardar la reserva.");
            }
            actualizarCupoDisponible(con, reserva.getFecha());
            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally { con.setAutoCommit(autoCommit); }
    }

    /** El aforo es diario y fijo: 30 personas para todo el local. */
    private int obtenerDisponibilidadYValidarCupo(Connection con, Date fecha, java.sql.Time hora, int personas, int idReservaExcluida) throws SQLException {
        int disponibilidadId = buscarOCrearDisponibilidadDelDia(con, fecha, hora);
        if (personasReservadasParaFecha(con, fecha, idReservaExcluida) + personas > CUPO_LOCAL) {
            throw new SQLException("CUPO_NO_DISPONIBLE");
        }
        return disponibilidadId;
    }

    private int buscarOCrearDisponibilidadDelDia(Connection con, Date fecha, java.sql.Time hora) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT idDisponibilidad FROM disponibilidad WHERE fecha = ? ORDER BY idDisponibilidad LIMIT 1 FOR UPDATE")) {
            ps.setDate(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        int horarioId;
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO horarios (hora_ini, hora_fin) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setTime(1, hora);
            ps.setTime(2, hora);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("No se pudo crear el horario de la reserva.");
                horarioId = rs.getInt(1);
            }
        }
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO disponibilidad (fecha, cupo_total, cupo_disponible, Horarios_idHorarios) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, fecha);
            ps.setInt(2, CUPO_LOCAL);
            ps.setInt(3, CUPO_LOCAL);
            ps.setInt(4, horarioId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear la disponibilidad de la reserva.");
    }

    private int personasReservadasParaFecha(Connection con, Date fecha, int idReservaExcluida) throws SQLException {
        String sql = "SELECT COALESCE(SUM(r.num_personas), 0) FROM reserva r "
                + "JOIN estado_reserva e ON e.idEstado_reserva = r.Estado_reserva_idEstado_reserva "
                + "WHERE r.fecha = ? AND LOWER(e.descripcion_esta) <> 'cancelada' AND r.idReserva <> ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, fecha);
            ps.setInt(2, idReservaExcluida);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private void actualizarCupoDisponible(Connection con, Date fecha) throws SQLException {
        int disponible = Math.max(0, CUPO_LOCAL - personasReservadasParaFecha(con, fecha, 0));
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE disponibilidad SET cupo_total = ?, cupo_disponible = ? WHERE fecha = ?")) {
            ps.setInt(1, CUPO_LOCAL);
            ps.setInt(2, disponible);
            ps.setDate(3, fecha);
            ps.executeUpdate();
        }
    }

    private int obtenerEstadoConfirmada(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT idEstado_reserva FROM estado_reserva WHERE descripcion_esta = ? LIMIT 1")) {
            ps.setString(1, "Confirmada");
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        }
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO estado_reserva (descripcion_esta) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Confirmada"); ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) { if (claves.next()) return claves.getInt(1); }
        }
        throw new SQLException("No se pudo crear el estado de la reserva.");
    }

    private int insertarPagoPagado(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO pagos (estado_pago) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Pagado");
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo registrar el pago.");
    }

    public boolean insertarReserva(Reserva Mireserva) {
        boolean insertado = false;
        Connection con = conexion.getConn();

        boolean insertarConId = Mireserva.getidReserva() > 0;
        String sql = insertarConId
                ? "INSERT INTO reserva (idReserva, num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                : "INSERT INTO reserva (num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            if (insertarConId) {
                ps.setInt(i++, Mireserva.getidReserva());
            }
            ps.setInt(i++, Mireserva.getNum_personas());
            ps.setTime(i++, Mireserva.getHora());
            ps.setDate(i++, Mireserva.getFecha());
            ps.setInt(i++, Mireserva.getUsuarios_idUsuarios());
            ps.setInt(i++, Mireserva.getDisponibilidad_idDisponibilidad());
            ps.setInt(i++, Mireserva.getEstado_reserva_idEstado_reserva());
            ps.setInt(i++, Mireserva.getActividad_idActividad());
            ps.setInt(i, Mireserva.getPagos_idPagos());
            
            ps.executeUpdate();
            insertado = true;

            System.out.println("Reserva insertada con éxito.");

        } catch (SQLException e) {
            System.out.println("Error al insertar Reserva: " + e.getMessage());
        }

        return insertado;
    }
    
    public Reserva ConsultarReserva(int idReserva) throws SQLException {
        Reserva reserva = null;
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();

        try {
            String querySQL = "SELECT idReserva, num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos FROM reserva WHERE idReserva = ? ";
            PreparedStatement ps = con.prepareStatement(querySQL);
            ps.setInt(1, idReserva);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                reserva = new Reserva();
                reserva.setidReserva(rs.getInt(1));
                reserva.setNum_personas(rs.getInt(2));
                reserva.setHora(rs.getTime(3));
                reserva.setFecha(rs.getDate(4));
                reserva.setUsuarios_idUsuarios(rs.getInt(5));
                reserva.setDisponibilidad_idDisponibilidad(rs.getInt(6));
                reserva.setEstado_reserva_idEstado_reserva(rs.getInt(7));
                reserva.setActividad_idActividad(rs.getInt(8));
                reserva.setPagos_idPagos(rs.getInt(9));
            }
            return reserva;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return reserva;
        }
    }

    

    public boolean actualizarReserva(Reserva reserva) throws SQLException {
        boolean actualizado = false;

        String sql = "UPDATE reserva SET num_personas = ?, hora = ?, fecha = ?, Usuarios_idUsuarios = ?, Disponibilidad_idDisponibilidad = ?, Estado_reserva_idEstado_reserva = ?, Actividad_idActividad = ?, Pagos_idPagos = ? WHERE idReserva = ?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
          
            ps.setInt(1, reserva.getNum_personas());
            ps.setTime(2, reserva.getHora());
            ps.setDate(3, reserva.getFecha());
            ps.setInt(4, reserva.getUsuarios_idUsuarios());
            ps.setInt(5, reserva.getDisponibilidad_idDisponibilidad());
            ps.setInt(6, reserva.getEstado_reserva_idEstado_reserva());
            ps.setInt(7, reserva.getActividad_idActividad());
            ps.setInt(8, reserva.getPagos_idPagos());
            ps.setInt(9, reserva.getidReserva());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                actualizado = true;
                System.out.println("Reserva actualizada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar reserva: " + e.getMessage());
        }
        return actualizado;
    }

    public boolean eliminarReserva(int id) throws SQLException {
        boolean eliminado = false;
        String sql = "DELETE FROM reserva WHERE idReserva = ?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                eliminado = true;
                System.out.println("Reserva eliminada de VIVEROBD.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar reserva: " + e.getMessage());
        }
        return eliminado;
    }

    public List<Reserva> listarReservasPorUsuario(int idUsuario) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.idReserva, r.num_personas, r.hora, r.fecha, r.Usuarios_idUsuarios, "
                + "r.Disponibilidad_idDisponibilidad, r.Estado_reserva_idEstado_reserva, r.Actividad_idActividad, r.Pagos_idPagos, "
                + "e.descripcion_esta, a.descripcion_actividad "
                + "FROM reserva r JOIN estado_reserva e ON e.idEstado_reserva = r.Estado_reserva_idEstado_reserva "
                + "JOIN actividad a ON a.idActividad = r.Actividad_idActividad "
                + "WHERE r.Usuarios_idUsuarios = ? ORDER BY r.fecha DESC, r.hora DESC";
        try (Connection con = new Conexion().getConn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reserva r = new Reserva();
                    r.setidReserva(rs.getInt(1)); r.setNum_personas(rs.getInt(2)); r.setHora(rs.getTime(3)); r.setFecha(rs.getDate(4));
                    r.setUsuarios_idUsuarios(rs.getInt(5)); r.setDisponibilidad_idDisponibilidad(rs.getInt(6));
                    r.setEstado_reserva_idEstado_reserva(rs.getInt(7)); r.setActividad_idActividad(rs.getInt(8)); r.setPagos_idPagos(rs.getInt(9));
                    r.setDescripcionEstado(rs.getString(10)); r.setDescripcionActividad(rs.getString(11));
                    r.setCancelada("Cancelada".equalsIgnoreCase(r.getDescripcionEstado()));
                    r.setPuedeEditar(!r.isCancelada() && r.getFecha().toLocalDate().isAfter(LocalDate.now().plusDays(3)));
                    lista.add(r);
                }
            }
        } catch (Exception e) { System.out.println("Error al listar reservas del usuario: " + e.getMessage()); }
        return lista;
    }

    public boolean actualizarReservaUsuario(int idReserva, int idUsuario, int personas, Date fecha, java.sql.Time hora) throws SQLException {
        try (Connection con = new Conexion().getConn()) {
            if (con == null) throw new SQLException("No hay conexion con la base de datos.");
            boolean autoCommit = con.getAutoCommit();
            try {
                con.setAutoCommit(false);
                Date fechaAnterior = null;
                try (PreparedStatement consulta = con.prepareStatement("SELECT fecha FROM reserva WHERE idReserva = ? AND Usuarios_idUsuarios = ? FOR UPDATE")) {
                    consulta.setInt(1, idReserva); consulta.setInt(2, idUsuario);
                    try (ResultSet rs = consulta.executeQuery()) { if (rs.next()) fechaAnterior = rs.getDate(1); }
                }
                if (fechaAnterior == null) { con.rollback(); return false; }
                int disponibilidadId = obtenerDisponibilidadYValidarCupo(con, fecha, hora, personas, idReserva);
                String sql = "UPDATE reserva r JOIN estado_reserva e ON e.idEstado_reserva = r.Estado_reserva_idEstado_reserva "
                        + "SET r.num_personas = ?, r.fecha = ?, r.hora = ?, r.Disponibilidad_idDisponibilidad = ? "
                        + "WHERE r.idReserva = ? AND r.Usuarios_idUsuarios = ? AND LOWER(e.descripcion_esta) <> 'cancelada' "
                        + "AND r.fecha > DATE_ADD(CURDATE(), INTERVAL 3 DAY)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, personas); ps.setDate(2, fecha); ps.setTime(3, hora); ps.setInt(4, disponibilidadId);
                    ps.setInt(5, idReserva); ps.setInt(6, idUsuario);
                    if (ps.executeUpdate() != 1) { con.rollback(); return false; }
                }
                actualizarCupoDisponible(con, fechaAnterior);
                if (!fechaAnterior.equals(fecha)) actualizarCupoDisponible(con, fecha);
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally { con.setAutoCommit(autoCommit); }
        }
    }

    public boolean cancelarReservaUsuario(int idReserva, int idUsuario) throws SQLException {
        try (Connection con = new Conexion().getConn()) {
            if (con == null) throw new SQLException("No hay conexion con la base de datos.");
            boolean autoCommit = con.getAutoCommit();
            try {
                con.setAutoCommit(false);
                Date fecha = null;
                try (PreparedStatement consulta = con.prepareStatement("SELECT fecha FROM reserva WHERE idReserva = ? AND Usuarios_idUsuarios = ? FOR UPDATE")) {
                    consulta.setInt(1, idReserva); consulta.setInt(2, idUsuario);
                    try (ResultSet rs = consulta.executeQuery()) { if (rs.next()) fecha = rs.getDate(1); }
                }
                int estadoCancelada = obtenerOCrearEstado(con, "Cancelada");
                String sql = "UPDATE reserva r JOIN estado_reserva e ON e.idEstado_reserva = r.Estado_reserva_idEstado_reserva "
                        + "SET r.Estado_reserva_idEstado_reserva = ? WHERE r.idReserva = ? AND r.Usuarios_idUsuarios = ? "
                        + "AND LOWER(e.descripcion_esta) <> 'cancelada'";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, estadoCancelada); ps.setInt(2, idReserva); ps.setInt(3, idUsuario);
                    if (ps.executeUpdate() != 1) { con.rollback(); return false; }
                }
                if (fecha != null) actualizarCupoDisponible(con, fecha);
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally { con.setAutoCommit(autoCommit); }
        }
    }

    private int obtenerOCrearEstado(Connection con, String descripcion) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT idEstado_reserva FROM estado_reserva WHERE descripcion_esta = ? LIMIT 1")) {
            ps.setString(1, descripcion); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        }
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO estado_reserva (descripcion_esta) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, descripcion); ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        throw new SQLException("No se pudo crear el estado de reserva.");
    }
    public List<Reserva> listarReserva() {
    List<Reserva> lista = new ArrayList<>();
    Conexion conexion = new Conexion();
    Connection con = conexion.getConn();
    try {
        String sql = "SELECT idReserva, num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos FROM reserva";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Reserva reserva = new Reserva();
            reserva.setidReserva(rs.getInt(1));
            reserva.setNum_personas(rs.getInt(2));
            reserva.setHora(rs.getTime(3));
            reserva.setFecha(rs.getDate(4));
            reserva.setUsuarios_idUsuarios(rs.getInt(5));
            reserva.setDisponibilidad_idDisponibilidad(rs.getInt(6));
            reserva.setEstado_reserva_idEstado_reserva(rs.getInt(7));
            reserva.setActividad_idActividad(rs.getInt(8));
            reserva.setPagos_idPagos(rs.getInt(9));
            lista.add(reserva);
        }
    } catch (Exception e) {
        System.out.println("Error al listar reservas: " + e.getMessage());
    }
    return lista;
}

}
