package es.uca.iw.carteruca.models;


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

    @Column(nullable = false)
    @Unique
    private String acronimo;

    //Constructor

    public Centro() {}

    public Centro(String nombre, String acronimo) {

        this.nombre = nombre;
        this.acronimo = acronimo;
    }

    //GETTERS Y SETTERS

    public Long getId() {return id;}


    public @Unique String getAcronimo() {return acronimo;}
    public void setAcronimo(@Unique String acronimo) {this.acronimo = acronimo;}

    public @Unique String getNombre() {return nombre;}
    public void setNombre(@Unique String nombre) {this.nombre = nombre;}
}
