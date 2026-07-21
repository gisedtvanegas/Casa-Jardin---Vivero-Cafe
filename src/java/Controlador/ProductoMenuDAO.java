package Controlador;

import Modelo.ProductoMenu;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoMenuDAO {
    private void asegurarTabla(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS producto_menu ("
                + "idProducto INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre VARCHAR(120) NOT NULL, descripcion VARCHAR(500) NOT NULL, "
                + "precio DECIMAL(12,2) NOT NULL, imagen LONGBLOB NULL, "
                + "tipo_imagen VARCHAR(80) NULL, creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = con.prepareStatement(sql)) { ps.executeUpdate(); }
    }

    public List<ProductoMenu> listar() {
        List<ProductoMenu> productos = new ArrayList<>();
        try (Connection con = new Conexion().getConn()) {
            asegurarTabla(con);
            try (PreparedStatement ps = con.prepareStatement("SELECT idProducto, nombre, descripcion, precio, imagen IS NOT NULL AS tieneImagen FROM producto_menu ORDER BY idProducto DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) productos.add(mapear(rs));
            }
        } catch (Exception e) { System.out.println("Error al listar productos del menu: " + e.getMessage()); }
        return productos;
    }

    public boolean insertar(ProductoMenu producto, InputStream imagen, String tipoImagen) throws SQLException {
        try (Connection con = new Conexion().getConn()) {
            asegurarTabla(con);
            String sql = "INSERT INTO producto_menu (nombre, descripcion, precio, imagen, tipo_imagen) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre()); ps.setString(2, producto.getDescripcion()); ps.setBigDecimal(3, producto.getPrecio());
                if (imagen == null) ps.setNull(4, java.sql.Types.LONGVARBINARY); else ps.setBlob(4, imagen);
                ps.setString(5, tipoImagen);
                return ps.executeUpdate() == 1;
            }
        }
    }

    public boolean actualizar(ProductoMenu producto, InputStream imagen, String tipoImagen) throws SQLException {
        try (Connection con = new Conexion().getConn()) {
            asegurarTabla(con);
            boolean cambiarImagen = imagen != null;
            String sql = cambiarImagen
                    ? "UPDATE producto_menu SET nombre=?, descripcion=?, precio=?, imagen=?, tipo_imagen=? WHERE idProducto=?"
                    : "UPDATE producto_menu SET nombre=?, descripcion=?, precio=? WHERE idProducto=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre()); ps.setString(2, producto.getDescripcion()); ps.setBigDecimal(3, producto.getPrecio());
                if (cambiarImagen) { ps.setBlob(4, imagen); ps.setString(5, tipoImagen); ps.setInt(6, producto.getIdProducto()); }
                else ps.setInt(4, producto.getIdProducto());
                return ps.executeUpdate() == 1;
            }
        }
    }

    public boolean eliminar(int id) throws SQLException {
        try (Connection con = new Conexion().getConn()) {
            asegurarTabla(con);
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM producto_menu WHERE idProducto=?")) {
                ps.setInt(1, id); return ps.executeUpdate() == 1;
            }
        }
    }

    public Imagen obtenerImagen(int id) {
        try (Connection con = new Conexion().getConn()) {
            asegurarTabla(con);
            try (PreparedStatement ps = con.prepareStatement("SELECT imagen, tipo_imagen FROM producto_menu WHERE idProducto=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getBytes("imagen") != null) return new Imagen(rs.getBytes("imagen"), rs.getString("tipo_imagen"));
                }
            }
        } catch (Exception e) { System.out.println("Error al obtener imagen: " + e.getMessage()); }
        return null;
    }

    private ProductoMenu mapear(ResultSet rs) throws SQLException {
        ProductoMenu p = new ProductoMenu();
        p.setIdProducto(rs.getInt("idProducto")); p.setNombre(rs.getString("nombre")); p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio")); p.setTieneImagen(rs.getBoolean("tieneImagen")); return p;
    }

    public static class Imagen {
        public final byte[] datos; public final String tipo;
        public Imagen(byte[] datos, String tipo) { this.datos = datos; this.tipo = tipo; }
    }
}
