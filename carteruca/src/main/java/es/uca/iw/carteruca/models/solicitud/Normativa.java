package es.uca.iw.carteruca.models.solicitud;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;

@Entity
public class Normativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    public Long getId() {return id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public Normativa() {
        // Constructor vacío requerido por JPA
    }
    @Override
    public String toString() {
        return nombre; // Representación visual por defecto
    }
}
