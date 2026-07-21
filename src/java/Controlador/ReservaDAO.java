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

public class ReservaDAO {

    Conexion conexion = new Conexion();

    public boolean insertarReservaPagada(Reserva reserva) throws SQLException {
        Connection con = conexion.getConn();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        boolean autoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            reserva.setEstado_reserva_idEstado_reserva(obtenerEstadoConfirmada(con));
            reserva.setPagos_idPagos(insertarPagoPagado(con));
            String sql = "INSERT INTO Reserva (num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, reserva.getNum_personas()); ps.setTime(2, reserva.getHora()); ps.setDate(3, reserva.getFecha());
                ps.setInt(4, reserva.getUsuarios_idUsuarios()); ps.setInt(5, reserva.getDisponibilidad_idDisponibilidad());
                ps.setInt(6, reserva.getEstado_reserva_idEstado_reserva()); ps.setInt(7, reserva.getActividad_idActividad()); ps.setInt(8, reserva.getPagos_idPagos());
                if (ps.executeUpdate() != 1) throw new SQLException("No se pudo guardar la reserva.");
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally { con.setAutoCommit(autoCommit); }
    }

    private int obtenerEstadoConfirmada(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT idEstado_reserva FROM Estado_reserva WHERE descripcion_esta = ? LIMIT 1")) {
            ps.setString(1, "Confirmada");
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        }
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Estado_reserva (descripcion_esta) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Confirmada"); ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) { if (claves.next()) return claves.getInt(1); }
        }
        throw new SQLException("No se pudo crear el estado de la reserva.");
    }

    private int insertarPagoPagado(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Pagos (estado_pago) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Pagado"); ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) { if (claves.next()) return claves.getInt(1); }
        }
        throw new SQLException("No se pudo registrar el pago.");
    }

    public boolean insertarReserva(Reserva Mireserva) {
        boolean insertado = false;
        Connection con = conexion.getConn();

        boolean insertarConId = Mireserva.getidReserva() > 0;
        String sql = insertarConId
                ? "INSERT INTO Reserva (idReserva, num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                : "INSERT INTO Reserva (num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            String querySQL = "SELECT idReserva, num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos FROM Reserva WHERE idReserva = ? ";
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

        String sql = "UPDATE Reserva SET num_personas = ?, hora = ?, fecha = ?, Usuarios_idUsuarios = ?, Disponibilidad_idDisponibilidad = ?, Estado_reserva_idEstado_reserva = ?, Actividad_idActividad = ?, Pagos_idPagos = ? WHERE idReserva = ?";
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
        String sql = "DELETE FROM Reserva WHERE idReserva = ?";
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
    public List<Reserva> listarReserva() {
    List<Reserva> lista = new ArrayList<>();
    Conexion conexion = new Conexion();
    Connection con = conexion.getConn();
    try {
        String sql = "SELECT idReserva, num_personas, hora, fecha, Usuarios_idUsuarios, Disponibilidad_idDisponibilidad, Estado_reserva_idEstado_reserva, Actividad_idActividad, Pagos_idPagos FROM Reserva";
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
