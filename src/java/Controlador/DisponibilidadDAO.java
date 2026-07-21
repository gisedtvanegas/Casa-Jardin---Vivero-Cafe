/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.Disponibilidad;
import Modelo.Estado_reserva;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class DisponibilidadDAO {
    public boolean insertarDisponibilidad(Disponibilidad disp) throws SQLException {
        boolean insertado = false;
        boolean insertarConId = disp.getidDisponibilidad() > 0;
        String sql = insertarConId
                ? "INSERT INTO disponibilidad (idDisponibilidad, fecha, cupo_total, cupo_disponible, Horarios_idHorarios) VALUES (?, ?, ?, ?, ?)"
                : "INSERT INTO disponibilidad (fecha, cupo_total, cupo_disponible, Horarios_idHorarios) VALUES (?, ?, ?, ?)";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            if (insertarConId) {
                ps.setInt(i++, disp.getidDisponibilidad());
            }
            ps.setDate(i++, new java.sql.Date(disp.getfecha().getTime()));
            ps.setInt(i++, disp.getcupo_total());
            ps.setInt(i++, disp.getcupo_disponible());
            ps.setInt(i, disp.getHorarios_idHorarios());

            if (ps.executeUpdate() > 0) insertado = true;
        }
        return insertado;
    }

    public Disponibilidad ConsultarDisponibilidad(int idDisponibilidad) throws SQLException {
        Disponibilidad dispo = null;
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();

        try {
            String querySQL = "SELECT idDisponibilidad, fecha, cupo_total, cupo_disponible, Horarios_idHorarios FROM Disponibilidad WHERE idDisponibilidad = ? ";

            PreparedStatement ps = con.prepareStatement(querySQL);
            ps.setInt(1, idDisponibilidad);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dispo = new Disponibilidad();
                dispo.setidDisponibilidad(rs.getInt(1));
                dispo.setfecha(rs.getDate(2));
                dispo.setcupo_total(rs.getInt(3));
                dispo.setcupo_disponible(rs.getInt(4));
                dispo.setHorarios_idHorarios(rs.getInt(5));

            }
            return dispo;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return dispo;

        }

    }

    public boolean actualizarDisponibilidad(Disponibilidad disp) throws SQLException {
        boolean actualizado = false;
        String sql = "UPDATE disponibilidad SET fecha=?, cupo_total=?, cupo_disponible=?, Horarios_idHorarios=? WHERE idDisponibilidad=?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(disp.getfecha().getTime()));
            ps.setInt(2, disp.getcupo_total());
            ps.setInt(3, disp.getcupo_disponible());
            ps.setInt(4, disp.getHorarios_idHorarios());
            ps.setInt(5, disp.getidDisponibilidad());

            if (ps.executeUpdate() > 0) actualizado = true;
        }
        return actualizado;
    }


    public boolean eliminarDisponibilidad(int id) throws SQLException {
        boolean eliminado = false;
        String sql = "DELETE FROM disponibilidad WHERE idDisponibilidad = ?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) eliminado = true;
        }
        return eliminado;
    }
    public List<Disponibilidad> Disponibilidad() {
        List<Disponibilidad> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();
        try {
            String sql = "SELECT idDisponibilidad, fecha, cupo_total, cupo_disponible, Horarios_idHorarios FROM Disponibilidad";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Disponibilidad dispo = new Disponibilidad();
                dispo.setidDisponibilidad(rs.getInt(1));
                dispo.setfecha(rs.getDate(2));
                dispo.setcupo_total(rs.getInt(3));
                dispo.setcupo_disponible(rs.getInt(4));
                dispo.setHorarios_idHorarios(rs.getInt(5));
                lista.add(dispo);
            }
        } catch (Exception e) {
            System.out.println("Error al listar roles: " + e.getMessage());
        }
        return lista;
    }
}
    

