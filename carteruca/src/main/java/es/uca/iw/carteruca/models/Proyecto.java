package es.uca.iw.carteruca.models;

import jakarta.persistence.*;

@Entity
@Table
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "solicitud_id")
    private Solicitud solicitud;

    @Column
    private String presupuesto; // es un documento especificando la financiacion aportada

    @Column
    private String especificacion_tecnica;// es un documento con las especificaciones tecnologicas

    @Column
    private Float puntuacion; // este se debe calcular mediante los criterios

    @Column
    private Float porcentaje; // este campo mostrará el porcentaje de avance del proyecto que este en marcha.

    @Column
    private String director_de_proyecto;

    @ManyToOne
    @JoinColumn(name = "jefe_id")
    private Usuario jefe;

    public String getPresupuesto() {return presupuesto;}
    public void setPresupuesto(String presupuesto) {this.presupuesto = presupuesto;}

    public String getEspecificacion_tecnica() {return especificacion_tecnica;}
    public void setEspecificacion_tecnica(String especificacion_tecnica) {this.especificacion_tecnica = especificacion_tecnica;}

    public Float getPuntuacion() {return puntuacion;}
    public void setPuntuacion(Float puntuacion) {this.puntuacion = puntuacion;}

    public Float getPorcentaje() {return porcentaje;}
    public void setPorcentaje(Float porcentaje) {this.porcentaje = porcentaje;}

    public String getDirector_de_proyecto() {return director_de_proyecto;}
    public void setDirector_de_proyecto(String director_de_proyecto) {this.director_de_proyecto = director_de_proyecto;}

    public Usuario getJefe() {return jefe;}
    public void setJefe(Usuario jefe) {this.jefe = jefe;}
}
