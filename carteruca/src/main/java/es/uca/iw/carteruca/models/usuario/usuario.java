package es.uca.iw.carteruca.models.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private String unidad;//Centro o unidad a la que pertenece el usuario
    //puede que se tenga que poner en la solicitud y no en el usuario
    private boolean es_CIO;
    private boolean es_OTP;
    private boolean es_Promotor;
    private boolean es_admin;

    // Getters y setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUser() {return user;}
    public void setUser(String user) {this.user = user;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getUnidad() {return unidad;}
    public void setUnidad(String unidad) {this.unidad = unidad;}

    public boolean isEs_CIO() {
        return es_CIO;
    }
    public void setEs_CIO(boolean es_CIO) {
        this.es_CIO = es_CIO;
    }

    public boolean isEs_OTP() {
        return es_OTP;
    }
    public void setEs_OTP(boolean es_OTP) {
        this.es_OTP = es_OTP;
    }

    public boolean isEs_Promotor() {
        return es_Promotor;
    }
    public void setEs_Promotor(boolean es_Promotor) {
        this.es_Promotor = es_Promotor;
    }

    public boolean isEs_admin() {
        return es_admin;
    }
    public void setEs_admin(boolean es_admin) {
        this.es_admin = es_admin;
    }

}