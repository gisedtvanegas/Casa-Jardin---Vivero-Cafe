/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author USER
 */
public class Disponibilidad {
    
    private int idDisponibilidad;
    private Date fecha;
    private int cupo_total;
    private int cupo_disponible; 
    private int Horarios_idHorarios;
    
    
    
    public int getidDisponibilidad () {
       return idDisponibilidad;
    }
    
    public void setidDisponibilidad (int idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }
    
    public Date getfecha () {
       return fecha;
    }
    
    public void setfecha (Date fecha) {
        this.fecha = fecha;
    }
    
    public int getcupo_total () {
        return cupo_total;
    }
    
    public void setcupo_total(int cupo_total) {
        this.cupo_total = cupo_total;
    }
    
    public int getcupo_disponible () {
    return cupo_disponible;
    }
    
    public void setcupo_disponible (int cupo_disponible) {
        this.cupo_disponible = cupo_disponible;
    }
    
    public int getHorarios_idHorarios() {
       return Horarios_idHorarios;   
    }
    
    public void setHorarios_idHorarios( int Horarios_idHorarios) {
        this.Horarios_idHorarios = Horarios_idHorarios;
    }
  
}

    
