package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.Usuarios;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 *
 * @author Aprendiz
 */
public class UsuariosDAO {

    Conexion conexion = new Conexion();

    public boolean insertarUsuarios(Usuarios usuarios) throws SQLException {
        boolean insertado = false;
        Connection con = conexion.getConn();

        String sql = "INSERT INTO usuarios (nombre, apellido, documento, telefono, correo, clave, fecha_nac, fecha_cad, checkbox, Tipo_documento_idTipo_documento, Roles_idRoles) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuarios.getnombre());
            ps.setString(2, usuarios.getapellido());
            ps.setString(3, usuarios.getdocumento());
            ps.setString(4, usuarios.gettelefono());
            ps.setString(5, usuarios.getcorreo());
            ps.setString(6, usuarios.getclave());
            ps.setDate(7, new java.sql.Date(usuarios.getfecha_nac().getTime()));
            ps.setDate(8, new java.sql.Date(usuarios.getfecha_cad().getTime()));
            ps.setBoolean(9, usuarios.ischeckbox());
            ps.setInt(10, usuarios.getTipo_documento_idTipo_documento());
            ps.setInt(11, usuarios.getRoles_idRoles());

            ps.executeUpdate();
            insertado = true;

            System.out.println("Usuario insertado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario." + e.getMessage());
        }
        return insertado;
    }

    public Usuarios ConsultaUsuarios(String documento) {
        Usuarios usuario = null;
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();

        try {
            String querySQL = "SELECT idUsuarios, nombre, apellido, documento, telefono, correo, clave, fecha_nac, fecha_cad, checkbox, Tipo_documento_idTipo_documento, Roles_idRoles FROM Usuarios WHERE documento = ? ";
            PreparedStatement ps = con.prepareStatement(querySQL);
            ps.setString(1, documento);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuarios();
                usuario.setidUsuarios(rs.getInt(1));

                usuario.setnombre(rs.getString(2));
                usuario.setapellido(rs.getString(3));
                usuario.setdocumento(rs.getString(4));
                usuario.settelefono(rs.getString(5));
                usuario.setcorreo(rs.getString(6));
                usuario.setclave(rs.getString(7));
                usuario.setfecha_nac(rs.getDate(8));
                usuario.setfecha_cad(rs.getDate(9));
                usuario.setcheckbox(rs.getBoolean(10));
                usuario.setTipo_documento_idTipo_documento(rs.getInt(11));
                usuario.setRoles_idRoles(rs.getInt(12));

            }

            return usuario;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return usuario;
        }
    }

    public Usuarios consultaUsuarioPorCorreo(String correo) {
        Usuarios usuario = null;
        String sql = "SELECT idUsuarios, nombre, correo FROM usuarios WHERE correo = ?";
        try (Connection con = new Conexion().getConn();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuarios();
                    usuario.setidUsuarios(rs.getInt("idUsuarios"));
                    usuario.setnombre(rs.getString("nombre"));
                    usuario.setcorreo(rs.getString("correo"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al consultar usuario por correo: " + e.getMessage());
        }
        return usuario;
    }

    /** Actualiza solo la clave: evita sobrescribir los demas datos del usuario. */
    public boolean actualizarClave(int idUsuario, String clave) {
        String sql = "UPDATE usuarios SET clave = ? WHERE idUsuarios = ?";
        try (Connection con = new Conexion().getConn();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clave);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("Error al actualizar la clave: " + e.getMessage());
            return false;
        }
    }
    
    public Usuarios ConsultaUsuariosPorId(int idUsuarios) {
    Usuarios usuario = null;
    Conexion conexion = new Conexion();
    Connection con = conexion.getConn();

    try {
            String querySQL = "SELECT nombre, apellido, documento, telefono, correo, clave, fecha_nac, fecha_cad, checkbox, Tipo_documento_idTipo_documento, Roles_idRoles FROM Usuarios WHERE idUsuarios = ?";
            PreparedStatement ps = con.prepareStatement(querySQL);
            ps.setInt(1, idUsuarios);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuarios();
                usuario.setidUsuarios(idUsuarios);
                usuario.setnombre(rs.getString("nombre"));
                usuario.setapellido(rs.getString("apellido"));
                usuario.setdocumento(rs.getString("documento"));
                usuario.settelefono(rs.getString("telefono"));
                usuario.setcorreo(rs.getString("correo"));
                usuario.setclave(rs.getString("clave"));
                usuario.setfecha_nac(rs.getDate("fecha_nac"));
                usuario.setfecha_cad(rs.getDate("fecha_cad"));
                usuario.setcheckbox(rs.getBoolean("checkbox"));
                usuario.setTipo_documento_idTipo_documento(rs.getInt("Tipo_documento_idTipo_documento"));
                usuario.setRoles_idRoles(rs.getInt("Roles_idRoles"));
            }

            return usuario;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return usuario;
        }
    }


    public boolean actualizarUsuario(Usuarios usuarios) throws SQLException {
        boolean actualizado = false;
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, documento=?, telefono=?, correo=?, clave=?, fecha_nac=?, fecha_cad=?, checkbox=?, Tipo_documento_idTipo_documento=?, Roles_idRoles=? WHERE idUsuarios=?";        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuarios.getnombre());
            ps.setString(2, usuarios.getapellido());
            ps.setString(3, usuarios.getdocumento());
            ps.setString(4, usuarios.gettelefono());
            ps.setString(5, usuarios.getcorreo());
            ps.setString(6, usuarios.getclave());
            ps.setDate(7, new java.sql.Date(usuarios.getfecha_nac().getTime()));
            ps.setDate(8, new java.sql.Date(usuarios.getfecha_cad().getTime()));
            ps.setBoolean(9, usuarios.ischeckbox());
            ps.setInt(10, usuarios.getTipo_documento_idTipo_documento());
            ps.setInt(11, usuarios.getRoles_idRoles());
            ps.setInt(12, usuarios.getidUsuarios());

            if (ps.executeUpdate() > 0) {
                actualizado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el usuario: " + e.getMessage());
        }
        return actualizado;
    }

    public boolean eliminarUsuario(int id) throws SQLException {
        boolean eliminado = false;
        String sql = "DELETE FROM usuarios WHERE idUsuarios = ?";
        Conexion conexion = new Conexion();
        Connection con = (Connection) conexion.getConn();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
        }
        return eliminado;
    }

    public boolean existeUsuario(String documento) {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();
        try {
            String sql = "SELECT documento FROM Usuarios WHERE documento = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            return false;
        }
    }    
             

    public List<Usuarios> listarUsuarios() {
        List<Usuarios> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        Connection con = conexion.getConn();
        String sql = "SELECT idUsuarios, nombre, apellido, documento, telefono, correo, clave, fecha_nac, fecha_cad, checkbox, Tipo_documento_idTipo_documento, Roles_idRoles FROM Usuarios";  

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Usuarios usuario = new Usuarios();
                usuario.setidUsuarios(rs.getInt(1));
                usuario.setnombre(rs.getString(2));
                usuario.setapellido(rs.getString(3));
                usuario.setdocumento(rs.getString(4));
                usuario.settelefono(rs.getString(5));
                usuario.setcorreo(rs.getString(6));
                usuario.setclave(rs.getString(7));
                usuario.setfecha_nac(rs.getDate(8));
                usuario.setfecha_cad(rs.getDate(9));
                usuario.setcheckbox(rs.getBoolean(10));
                usuario.setTipo_documento_idTipo_documento(rs.getInt(11));
                usuario.setRoles_idRoles(rs.getInt(12));
                

                lista.add(usuario);
            }
        } catch (Exception e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}
