package es.uca.iw.carteruca.models.usuario;


import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;

@Entity
@Table
public class Centro {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Unique
    private String nombre;

    //Constructor

    public Centro() {}

    public Centro(String nombre) {
        this.nombre = nombre;
    }

    //GETTERS Y SETTERS

    public Long getId() {return id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
}
