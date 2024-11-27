package es.uca.iw.carteruca.models.usuario;

import es.uca.iw.carteruca.models.solicitud.Solicitud;
import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

@Entity
@Table
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false)
    @Unique
    private String usuario;

    @Column(length = 25, nullable = false)
    private String password;

    @Column(length = 25, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellidos;

    @Column(length = 25, nullable = false)
    @Unique
    private String email;

    @ManyToOne
    private Centro centro;

    @OneToMany
    private List<Solicitud> solicitudes;

    @Column
    @Enumerated(EnumType.STRING)
    private Rol rol;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }
}
