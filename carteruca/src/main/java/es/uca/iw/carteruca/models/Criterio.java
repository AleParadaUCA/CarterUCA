package es.uca.iw.carteruca.models;

import jakarta.persistence.*;

@Entity
@Table
public class Criterio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Float peso;

//Getter y Setters
    public Long getId() {return id;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public Float getPeso() {return peso;}
    public void setPeso(Float peso) {this.peso = peso;}

}
