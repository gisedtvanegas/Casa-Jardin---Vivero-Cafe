/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Aprendiz
 */
public class Actividad {
    
    
    private int idActividad;
    private String descripcion_actividad;
    private int Tipo_Actividad_idTipo_Actividad;
    private int Lista_Precios_idLista_Precios;
    
    public int getidActividad() {
        return idActividad;
    }    
    
    public void setidActividad(int idActividad){
        this.idActividad = idActividad;
    }
    
    public String getdescripcion_actividad (){
        return descripcion_actividad;
    }
    
    public void setdescripcion_actividad (String descripcion_actividad){
        this.descripcion_actividad = descripcion_actividad;
    }
    
    public int getTipo_Actividad_idTipo_Actividad() {
        return Tipo_Actividad_idTipo_Actividad;
    }    
    
    public void setTipo_Actividad_idTipo_Actividad(int Tipo_Actividad_idTipo_Actividad){
        this.Tipo_Actividad_idTipo_Actividad = Tipo_Actividad_idTipo_Actividad;
    }
    
    public int getLista_Precios_idLista_Precios() {
        return Lista_Precios_idLista_Precios;
    }    
    
    public void setLista_Precios_idLista_Precios(int Lista_Precios_idLista_Precios){
        this.Lista_Precios_idLista_Precios = Lista_Precios_idLista_Precios;
    }
}
