package es.uca.iw.carteruca.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
    private String presupuesto; // es un documento especificando la financiación aportada

    @Column
    private Float presupuesto_valor; // presupuesto en número para mostrarlo si procede

    @Column
    private String especificacion_tecnica;// es un documento con las especificaciones tecnológicas

    @Column
    private Float puntuacionTotal; // este se debe calcular mediante los criterios

    @Column
    private Float porcentaje; // este campo mostrará el porcentaje de avance del proyecto que esté en marcha.

    @Column
    private Float horas;

    @Column
    private String puntuaciones;

    @Column
    private String director_de_proyecto;

    @ManyToOne
    @JoinColumn(name = "jefe_id")
    private Usuario jefe;

    public Long getId() {return id; }

    public String getPresupuesto() {return presupuesto;}
    public void setPresupuesto(String presupuesto) {this.presupuesto = presupuesto;}

    public String getEspecificacion_tecnica() {return especificacion_tecnica;}
    public void setEspecificacion_tecnica(String especificacion_tecnica) {this.especificacion_tecnica = especificacion_tecnica;}

    public Float getPuntuacionTotal() {return puntuacionTotal;}
    public void setPuntuacionTotal(Float puntuacion) {this.puntuacionTotal = puntuacion;}

    public Float getPorcentaje() {return porcentaje;}
    public void setPorcentaje(Float porcentaje) {this.porcentaje = porcentaje;}

    public Solicitud getSolicitud() {return solicitud;}
    public void setSolicitud(Solicitud solicitud) {this.solicitud = solicitud;}

    public float getHoras() {return horas;}
    public void setHoras(float horas) {this.horas = horas;}

    public String getPuntuaciones() {return puntuaciones;}
    public void setPuntuaciones(String puntuaciones) {this.puntuaciones = puntuaciones;}

    public Usuario getJefe() {return jefe;}
    public void setJefe(Usuario jefe) {this.jefe = jefe;}

    public String getDirector_de_proyecto() {return director_de_proyecto;}
    public void setDirector_de_proyecto(String director_de_proyecto) {this.director_de_proyecto = director_de_proyecto;}

    public Float getPresupuesto_valor() {
        return presupuesto_valor;
    }
    public void setPresupuesto_valor( float presupuesto) { this.presupuesto_valor = presupuesto; }
}
