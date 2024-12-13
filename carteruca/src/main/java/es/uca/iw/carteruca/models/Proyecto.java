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
    // *hasta aqui lo rellena CIO y OTP

    public String getPresupuesto() {return presupuesto;}
    public void setPresupuesto(String presupuesto) {this.presupuesto = presupuesto;}

    public String getEspecificacion_tecnica() {return especificacion_tecnica;}
    public void setEspecificacion_tecnica(String especificacion_tecnica) {this.especificacion_tecnica = especificacion_tecnica;}

    public Float getPuntuacion() {return puntuacion;}
    public void setPuntuacion(Float puntuacion) {this.puntuacion = puntuacion;}

    public Float getPorcentaje() {return porcentaje;}
    public void setPorcentaje(Float porcentaje) {this.porcentaje = porcentaje;}

}
