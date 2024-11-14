package es.uca.iw.carteruca.models.usuario;

import jakarta.persistence.*;

@Entity
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25,nullable = false)
    private String user;

    @Column(length = 25,nullable = false)
    private String password;

    @Column(length = 25,nullable = false)
    private String name;

    @Column(length = 50,nullable = false)
    private String lastname;

    @Column(length = 25,nullable = false)
    private String email;

    @Column
    private String unidad;//Centro o unidad a la que pertenece el usuario
    //puede que se tenga que poner en la solicitud y no en el usuario

    @Column
    private Rol rol;

    // Getters y setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUser() {return user;}
    public void setUser(String user) {this.user = user;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getLastname() {return lastname;}
    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getUnidad() {return unidad;}
    public void setUnidad(String unidad) {this.unidad = unidad;}

    public Rol getRol() {return rol;}
    public void setRol(Rol rol) {this.rol = rol;}
}