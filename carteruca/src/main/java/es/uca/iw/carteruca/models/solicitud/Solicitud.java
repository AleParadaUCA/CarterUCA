package es.uca.iw.carteruca.models.solicitud;

import es.uca.iw.carteruca.models.cartera.Cartera;
import es.uca.iw.carteruca.models.usuario.usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
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

    @OneToOne
    @JoinColumn(name = "normativa_id", nullable = false)  // Crea una columna normativa_id en la tabla Solicitud
    private Normativa normativa;//Codigo y/o descripcion de la normativa de aplicación obligatoria

    @Column
    private String memoria; //Por ahora usaremos una URL
    // *hasta aqui lo rellena el solicitante
    //hasta aqui solicitud


    @Column
    private Integer importancia_promotor; // si el promotor tiene varios proyectos avalados el promotor tiene que indicar importancia de cada proyecto que avale
    //* hasta aqui rellena el avalador


    @Column
    private String presupuesto; // es un documento especificando la financiacion aportada

    @Column
    private String especificacion_tecnica;// es un documento con las especificaciones tecnologicas

    @Column
    @Enumerated(EnumType.STRING)
    private Estado estado;
    // *hasta aqui lo rellena CIO y OTP

    @ManyToOne
    @JoinColumn(name = "solicitante_id") //FK hacia usuario solicitante
    private usuario solicitante;

    @OneToOne
    @JoinColumn(name = "promotor_id")// FK hacia usuario promotor
    private usuario promotor;

    @OneToOne
    @JoinColumn(name = "cartera_id")// FK hacia cartera
    private Cartera cartera;// referencia a la cartera a la que pertenece

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

    public Normativa getNormativa() {return normativa;}
    public void setNormativa(Normativa normativa) {this.normativa = normativa;}

    public String getMemoria() {return memoria;}
    public void setMemoria(String memoria) {this.memoria = memoria;}

    public Integer getImportancia_promotor() {return importancia_promotor;}
    public void setImportancia_promotor(Integer importancia_promotor) {this.importancia_promotor = importancia_promotor;}

    public String getPresupuesto() {return presupuesto;}
    public void setPresupuesto(String presupuesto) {this.presupuesto = presupuesto;}

    public String getEspecificacion_tecnica() {return especificacion_tecnica;}
    public void setEspecificacion_tecnica(String especificacion_tecnica) {this.especificacion_tecnica = especificacion_tecnica;}

    public usuario getSolicitante() {return solicitante;}
    public void setSolicitante(usuario solicitante) {this.solicitante = solicitante;}

    //Hay que comprobar que sea avalador
    public usuario getAvalador() {return promotor;}
    public void setAvalador(usuario promotor) {this.promotor = this.promotor;}
    
    public Cartera getCartera() {return cartera;}
    public void setCartera(Cartera cartera) {this.cartera = cartera;}

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}