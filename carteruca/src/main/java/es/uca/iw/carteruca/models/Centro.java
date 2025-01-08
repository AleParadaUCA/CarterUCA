package es.uca.iw.carteruca.models;


import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
public class Centro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Unique
    private String nombre;

    @Column(nullable = false)
    @Unique
    private String acronimo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //Constructor

    public Centro() {}


    //GETTERS Y SETTERS

    public Long getId() {return id;}


    public @Unique String getAcronimo() {return acronimo;}
    public void setAcronimo(@Unique String acronimo) {this.acronimo = acronimo;}

    public @Unique String getNombre() {return nombre;}
    public void setNombre(@Unique String nombre) {this.nombre = nombre;}
}
