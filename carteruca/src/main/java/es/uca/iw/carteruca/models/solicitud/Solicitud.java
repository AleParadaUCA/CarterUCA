package es.uca.iw.carteruca.models.solicitud;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String nombre; //nombre corto para el proyecto

    @Column
    private LocalDateTime fecha_solicitud; //fecha de cuando se inicio la solicito

    @Column
    private LocalDateTime fecha_puesta;

    @Column
    private String interesados; // Personas o grupo de personas que quieren que se lleve a cabo el proyecto

    @Column
    private String alineamiento; // objetivos que tiene el proyecto.

    @Column
    private String alcance; // Cantidad y grupo de personas a las que beneficiará el proyecto

    @Column
    private String normativa;//Codigo y/o descripcion de la normativa de aplicación obligatoria

    @Column
    private String memoria; //Por ahora usaremos una URL
    // *hasta aqui lo rellena el solicitante
    //hasta aqui solicitud
    @Column
    private Integer importancia_promotor; // si el promotor tiene varios proyectos avalados el promotor tiene que indicar importancia de cada proyecto que avale

    @Column
    private Boolean avalado;// True si esta avalado, False si no lo esta.
    // *hasta aqui lo rellena el promotor

    @Column
    private String presupuesto; // es un documento especificando la financiacion aportada

    @Column
    private String especificacion_tecnica;// es un documento con las especificaciones tecnologicas

    @Column
    @Enumerated(EnumType.STRING)
    private Estado estado;
    // *hasta aqui lo rellena CIO y OTP

    private Long id_solicitante; // referencia al solicitante
    private Long id_promotor; // referencia al promotor
    private Long id_cartera; // referencia a la cartera a la que pertenece


    //GETTERS Y SETTERS
    public Long getId() {return id;}

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public LocalDateTime getFecha_solicitud() {return fecha_solicitud;}
    public void setFecha_solicitud(LocalDateTime fecha_solicitud) {this.fecha_solicitud = fecha_solicitud;}

    public LocalDateTime getFecha_puesta() {return fecha_puesta;}
    public void setFecha_puesta(LocalDateTime fecha_puesta) {this.fecha_puesta = fecha_puesta;}

    public String getInteresados() {return interesados;}
    public void setInteresados(String interesados) {this.interesados = interesados;}

    public String getAlineamiento() {return alineamiento;}
    public void setAlineamiento(String alineamiento) {this.alineamiento = alineamiento;}

    public String getAlcance() {return alcance;}
    public void setAlcance(String alcance) {this.alcance = alcance;}

    public String getNormativa() {return normativa;}
    public void setNormativa(String normativa) {this.normativa = normativa;}

    public String getMemoria() {return memoria;}
    public void setMemoria(String memoria) {this.memoria = memoria;}

    public Integer getImportancia_promotor() {return importancia_promotor;}
    public void setImportancia_promotor(Integer importancia_promotor) {this.importancia_promotor = importancia_promotor;}

    public Boolean getAvalado() {return avalado;}
    public void setAvalado(Boolean avalado) {this.avalado = avalado;}

    public String getPresupuesto() {return presupuesto;}
    public void setPresupuesto(String presupuesto) {this.presupuesto = presupuesto;}

    public String getEspecificacion_tecnica() {return especificacion_tecnica;}
    public void setEspecificacion_tecnica(String especificacion_tecnica) {this.especificacion_tecnica = especificacion_tecnica;}

    public Long getId_solicitante() {return id_solicitante;}
    public void setId_solicitante(Long id_solicitante) {this.id_solicitante = id_solicitante;}

    public Long getId_promotor() {return id_promotor;}
    public void setId_promotor(Long id_promotor) {this.id_promotor = id_promotor;}

    public Long getId_cartera() {return id_cartera;}
    public void setid_cartera(Long id_cartera) {this.id_cartera = id_cartera;}

}