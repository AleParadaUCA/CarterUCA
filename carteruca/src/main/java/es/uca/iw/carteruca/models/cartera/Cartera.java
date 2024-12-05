package es.uca.iw.carteruca.models.cartera;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"fechaInicio", "fechaFin"})//indica que la tupla es única
)
public class Cartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private LocalDateTime fecha_apertura_solicitud;

    @Column(nullable = false)
    private LocalDateTime fecha_cierre_solicitud;


//GETTERS Y SETTERS
    public Long getId() {return id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public LocalDateTime getFecha_inicio() {return fechaInicio;}
    public void setFecha_inicio(LocalDateTime fechaInicio) {this.fechaInicio = fechaInicio;}

    public LocalDateTime getFecha_fin() {return fechaFin;}
    public void setFecha_fin(LocalDateTime fechaFin) {this.fechaFin = fechaFin;}

    public LocalDateTime getFecha_apertura_solicitud() {return fecha_apertura_solicitud;}
    public void setFecha_apertura_solicitud(LocalDateTime fecha_apertura_solicitud){this.fecha_apertura_solicitud = fecha_apertura_solicitud;}

    public LocalDateTime getFecha_cierre_solicitud  () {return fecha_cierre_solicitud;}
    public void setFecha_cierre_solicitud(LocalDateTime fecha_cierre_solicitud){this.fecha_cierre_solicitud = fecha_cierre_solicitud;}


}