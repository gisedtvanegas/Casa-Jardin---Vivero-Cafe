package Modelo;

import java.math.BigDecimal;

public class ProductoMenu {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private boolean tieneImagen;

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isTieneImagen() { return tieneImagen; }
    public void setTieneImagen(boolean tieneImagen) { this.tieneImagen = tieneImagen; }
}
