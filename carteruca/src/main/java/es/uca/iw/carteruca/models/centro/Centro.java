package es.uca.iw.carteruca.models.centro;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Centro {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String nombre;

    //GETTERS Y SETTERS

    public Long getId() {return id;}

    public String getNombre() {return nombre;}
}
