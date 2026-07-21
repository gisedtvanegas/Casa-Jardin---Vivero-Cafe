/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.Actividad;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Aprendiz
 */
public class ActividadDAO {

    public boolean insertarActividad(Actividad actividad) throws SQLException {
        boolean insertado = false;
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        String sql = "INSERT INTO actividad (descripcion_actividad, Tipo_Actividad_idTipo_Actividad, Lista_Precios_idLista_Precios) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, actividad.getdescripcion_actividad());
            ps.setInt(2, actividad.getTipo_Actividad_idTipo_Actividad());
            ps.setInt(3, actividad.getLista_Precios_idLista_Precios());
            
            ps.executeUpdate(); 
            insertado = true;
            System.out.println("Actividad insertada con exito.");

        } catch (SQLException e) {
            System.out.println("Error al insertar actividad:" + e.getMessage());
        }

        return insertado;

    }

    public Actividad ConsultaActividad(int idActividad){
        Actividad actividad = null;
    Conexion conexion =  new Conexion();
    Connection con = conexion.getConn();
   
    try{
            String querySQL = "SELECT idActividad, descripcion_actividad, Tipo_Actividad_idTipo_Actividad, Lista_Precios_idLista_Precios FROM actividad WHERE idActividad = ?";
        PreparedStatement ps = con.prepareStatement(querySQL);
        ps.setInt(1, idActividad);
       
        ResultSet rs = ps.executeQuery();
       
        if(rs.next()){
            actividad = new Actividad();
            
            actividad.setidActividad(rs.getInt(1));
            actividad.setdescripcion_actividad (rs.getString(2));
            actividad.setTipo_Actividad_idTipo_Actividad(rs.getInt(3));
            actividad.setLista_Precios_idLista_Precios(rs.getInt(4));
            
         }
       
        return actividad;
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return actividad;
        }
    }

    

    public boolean actualizarActividad(Actividad actividad) throws SQLException {
        boolean actualizado = false;

        String sql = "UPDATE actividad SET descripcion_actividad = ?, Tipo_Actividad_idTipo_Actividad = ?, Lista_Precios_idLista_Precios = ? WHERE idActividad = ?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, actividad.getdescripcion_actividad());
            ps.setInt(2, actividad.getTipo_Actividad_idTipo_Actividad());
            ps.setInt(3, actividad.getLista_Precios_idLista_Precios());
            ps.setInt(4, actividad.getidActividad());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizado = true;
                System.out.println("Actividad actualizada con éxito en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar actividad: " + e.getMessage());
        }
        return actualizado;
    }

    public boolean eliminarActividad(int id) throws SQLException {
        boolean eliminado = false;
        String sql = "DELETE FROM actividad WHERE idActividad = ?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                eliminado = true;
                System.out.println("Actividad eliminada de la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
        return eliminado;
    }
    
    public List<Actividad> Actividad() {
        List<Actividad> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();
        try {
            String sql = "SELECT idActividad, descripcion_actividad, Tipo_Actividad_idTipo_Actividad, Lista_Precios_idLista_Precios FROM actividad";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Actividad activi = new Actividad();
                activi.setidActividad(rs.getInt(1));
                activi.setdescripcion_actividad(rs.getString(2));
                activi.setTipo_Actividad_idTipo_Actividad(rs.getInt(3));
                activi.setLista_Precios_idLista_Precios(rs.getInt(4));
                lista.add(activi);
            }
        } catch (Exception e) {
            System.out.println("Error al listar actividad: " + e.getMessage());
        }
        return lista;
    }

    /** Obtiene como maximo las actividades que se muestran en la pagina publica. */
    public List<Actividad> listarActividadesPublicas(int limite) {
        List<Actividad> lista = new ArrayList<>();
        String sql = "SELECT idActividad, descripcion_actividad, Tipo_Actividad_idTipo_Actividad, Lista_Precios_idLista_Precios "
                + "FROM actividad ORDER BY idActividad LIMIT ?";
        try (Connection con = new Conexion().getConn();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Actividad actividad = new Actividad();
                    actividad.setidActividad(rs.getInt("idActividad"));
                    actividad.setdescripcion_actividad(rs.getString("descripcion_actividad"));
                    actividad.setTipo_Actividad_idTipo_Actividad(rs.getInt("Tipo_Actividad_idTipo_Actividad"));
                    actividad.setLista_Precios_idLista_Precios(rs.getInt("Lista_Precios_idLista_Precios"));
                    lista.add(actividad);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar actividades publicas: " + e.getMessage());
        }

        // La seccion publica siempre conserva cuatro tarjetas de muestra. Si aun
        // hay menos de cuatro registros, reutiliza temporalmente datos reales de
        // la base; al registrar nuevas actividades se muestran sin repetirse.
        int cantidadOriginal = lista.size();
        while (cantidadOriginal > 0 && lista.size() < limite) {
            lista.add(lista.get((lista.size() - cantidadOriginal) % cantidadOriginal));
        }
        return lista;
    }
}
