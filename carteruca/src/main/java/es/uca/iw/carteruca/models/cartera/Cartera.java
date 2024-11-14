package es.uca.iw.carteruca.models.cartera;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Cartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;
    private LocalDateTime fecha_apertura_solicitud;
    private LocalDateTime fecha_cierre_solicitud;


    //GETTERS Y SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDateTime getFecha_inicio() {return fecha_inicio;}
    public void setFecha_inicio(LocalDateTime fecha_inicio) {this.fecha_inicio = fecha_inicio;}

    public LocalDateTime getFecha_fin() {return fecha_fin;}
    public void setFecha_fin(LocalDateTime fecha_fin) {this.fecha_fin = fecha_fin;}

    public LocalDateTime getFecha_apertura_solicitude() {return fecha_apertura_solicitud;}
    public void setFecha_apertura_solicitud(LocalDateTime fecha_apertura_solicitud){this.fecha_apertura_solicitud = fecha_apertura_solicitud;}

    public LocalDateTime getFecha_cierre_solicitude() {return fecha_cierre_solicitud;}
    public void setFecha_cierre_solicitud(LocalDateTime fecha_cierre_solicitud){this.fecha_cierre_solicitud = fecha_cierre_solicitud;}

}