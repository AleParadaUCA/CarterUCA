package es.uca.iw.carteruca.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private String presupuesto; // es un documento especificando la financiacion aportada

    @Column
    private String especificacion_tecnica;// es un documento con las especificaciones tecnologicas

    @Column
    private Float puntuacion; // este se debe calcular mediante los criterios

    @Column
    private Float porcentaje; // este campo mostrará el porcentaje de avance del proyecto que este en marcha.

    @Column
    private float horas;
    // *hasta aqui lo rellena CIO y OTP



    public Long getId() {return id; }

    public String getPresupuesto() {return presupuesto;}
    public void setPresupuesto(String presupuesto) {this.presupuesto = presupuesto;}

    public String getEspecificacion_tecnica() {return especificacion_tecnica;}
    public void setEspecificacion_tecnica(String especificacion_tecnica) {this.especificacion_tecnica = especificacion_tecnica;}

    public Float getPuntuacion() {return puntuacion;}
    public void setPuntuacion(Float puntuacion) {this.puntuacion = puntuacion;}

    public Float getPorcentaje() {return porcentaje;}
    public void setPorcentaje(Float porcentaje) {this.porcentaje = porcentaje;}

    public Solicitud getSolicitud() {return solicitud;}
    public void setSolicitud(Solicitud solicitud) {this.solicitud = solicitud;}

    public float getHoras() {return horas;}
    public void setHoras(float horas) {this.horas = horas;}
}
