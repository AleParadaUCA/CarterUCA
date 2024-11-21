package es.uca.iw.carteruca.models.solicitud;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
@Entity
public class Normativa {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    public Long getId() {return id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}
}
