package es.uca.iw.carteruca.models.cartera;

import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"fecha_inicio", "fecha_fin"})//indica que la tupla es única
)
public class Cartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fecha_inicio;

    @Column(nullable = false)
    private LocalDateTime fecha_fin;

    @Column(nullable = false)
    private LocalDateTime fecha_apertura_solicitud;

    @Column(nullable = false)
    private LocalDateTime fecha_cierre_solicitud;


//GETTERS Y SETTERS
    public Long getId() {return id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public LocalDateTime getFecha_inicio() {return fecha_inicio;}
    public void setFecha_inicio(LocalDateTime fecha_inicio) {this.fecha_inicio = fecha_inicio;}

    public LocalDateTime getFecha_fin() {return fecha_fin;}
    public void setFecha_fin(LocalDateTime fecha_fin) {this.fecha_fin = fecha_fin;}

    public LocalDateTime getFecha_apertura_solicitude() {return fecha_apertura_solicitud;}
    public void setFecha_apertura_solicitud(LocalDateTime fecha_apertura_solicitud){this.fecha_apertura_solicitud = fecha_apertura_solicitud;}

    public LocalDateTime getFecha_cierre_solicitude() {return fecha_cierre_solicitud;}
    public void setFecha_cierre_solicitud(LocalDateTime fecha_cierre_solicitud){this.fecha_cierre_solicitud = fecha_cierre_solicitud;}


}