package es.uca.iw.carteruca.models.usuario;

import jakarta.persistence.*;

@Entity
@Table
public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(length = 25,nullable = false)
        private String usuario;

        @Column(length = 25,nullable = false)
        private String password;

        @Column(length = 25,nullable = false)
        private String nombre;

        @Column(length = 50,nullable = false)
        private String apellidos;

        @Column(length = 25,nullable = false)
        private String email;

        /*@Column
        @OneToOne
        Centro centro;*/

        @Column
        @Enumerated(EnumType.STRING)
        private Rol rol; //cambiar a set

// Getters y setters

        public Long getId() {return id;}

        public String getUsuario() {return usuario;}
        public void setUsuario(String usuario) {this.usuario = usuario;}

        public String getPassword() {return password;}
        public void setPassword(String password) {this.password = password;}

        public String getNombre() {return nombre;}
        public void setNombre(String nombre) {this.nombre = nombre;}

        public String getApellidos() {return apellidos;}
        public void setApellidos(String apellidos) {this.apellidos = apellidos;}

        public String getEmail() {return email;}
        public void setEmail(String email) {this.email = email;}

        public Rol getRol() {return rol;}
        public void setRol(Rol rol) {this.rol = rol;}
}
