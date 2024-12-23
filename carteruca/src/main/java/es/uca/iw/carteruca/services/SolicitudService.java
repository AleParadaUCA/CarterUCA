package es.uca.iw.carteruca.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.SolicitudRepository;

@Service
public class SolicitudService {
    private final SolicitudRepository repository;
    private final EmailService emailService;

    @Autowired
    public SolicitudService(SolicitudRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public void guardar(String titulo, String nombre, LocalDateTime fechaPuesta, String interesados, String alineamiento, String alcance, String normativa, MultiFileMemoryBuffer buffer, Usuario avalador, Usuario solictante, Cartera cartera) {
        
        //Faltan comprobaciones...

        //comprobar ".pdf" if (buffer.endsWith(".pdf"))
        List<String> memoria = CommonService.guardarFile(buffer, "../archivos/Cartera"+ cartera.getId()); //IMPORTANTE cambiar esto en producción

        Solicitud solicitud = new Solicitud();
        solicitud.setTitulo(titulo);
        solicitud.setNombre(nombre);
        solicitud.setFecha_solicitud(LocalDateTime.now());
        solicitud.setFecha_puesta(fechaPuesta);
        solicitud.setInteresados(interesados);
        solicitud.setAlineamiento(alineamiento);
        solicitud.setAlcance(alcance);
        solicitud.setNormativa(normativa);
        solicitud.setMemoria(memoria.get(0)); //path
        solicitud.setEstado(Estado.EN_TRAMITE);
        solicitud.setSolicitante(solictante);
        solicitud.setAvalador(avalador);
        solicitud.setCartera(cartera);

        repository.save(solicitud);

        String subject = "Solicitud Creada";
        String body = "Hola " + solictante.getNombre() + ",\n\n" +
                    "Tu solicitud para la cartera " + cartera.getNombre() + " con el título \"" +
                    solicitud.getTitulo() + "\" ha sido creada exitosamente.\n\n" +
                    "¡Gracias por tu aportación!\n\n" +
                    "Saludos,\n" +
                    "El equipo de Carteruca";
        emailService.enviarCorreo(solictante.getEmail(), subject, body);
    }

    public void update_solicitud(Long id, String titulo, String nombre, LocalDateTime fechaPuesta,
                                 String interesados, String alineamiento, String alcance, String normativa, Usuario avalador, MultiFileMemoryBuffer buffer) {
        // Buscar la solicitud por su ID
        Solicitud solicitud = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con el ID: " + id));

        // Actualizar los campos de la solicitud
        solicitud.setTitulo(titulo);
        solicitud.setNombre(nombre);
        solicitud.setFecha_puesta(fechaPuesta);
        solicitud.setInteresados(interesados);
        solicitud.setAlineamiento(alineamiento);
        solicitud.setAlcance(alcance);
        solicitud.setNormativa(normativa);
        solicitud.setAvalador(avalador);

        if (!buffer.getFiles().isEmpty()) {
            CommonService.eliminarFile(solicitud.getMemoria());
            solicitud.setMemoria( CommonService.guardarFile(buffer, "../archivos/Cartera"+ solicitud.getCartera().getId()).get(0)); //IMPORTANTE cambiar esto en producción
        }

        // Guardar los cambios en la base de datos
        repository.save(solicitud);
        
        String subject = "Solicitud Modificada";
        String body = "Hola " + solicitud.getSolicitante().getNombre() + ",\n\n" +
                    "Tu solicitud con el título \"" + solicitud.getTitulo() + "\" ha sido modificada exitosamente.\n\n" +
                    "Saludos,\n" +
                    "El equipo de Carteruca";
        emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
    }

    public List<Solicitud> getSolicitudesByUsuario(Usuario usuario) {
        // Consultar todas las solicitudes asociadas a este usuario, excluyendo las CANCELADAS
        return repository.findBySolicitanteAndEstadoNot(usuario, Estado.CANCELADO);
    }

    public List<Solicitud> getSolicitudesByPromotor(Usuario promotor) {
        return repository.findByPromotorAndEstado(promotor, Estado.EN_TRAMITE);
    }

    public void updateSolicitud(Solicitud solicitud, Rol rol, boolean respuesta) {
        repository.save(solicitud);  // Guarda la solicitud actualizada

        String subject = getSubject(rol);
        String body = getBody(solicitud, rol, respuesta);

        emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
    }

    private String getSubject(Rol rol) {
        return switch (rol) {
            case Promotor -> "Actualización de estado.";
            case Solicitante -> "Solicitud Cancelada";
            case CIO -> "Actualización de estado por CIO";
            default -> "Actualización de solicitud";
        };
    }

    private String getBody(Solicitud solicitud, Rol rol, boolean respuesta) {
        String nombre = solicitud.getSolicitante().getNombre();
        String titulo = solicitud.getTitulo();

        return switch (rol) {
            case Promotor -> "Hola " + nombre + ",\n\n" +
                    "Tu solicitud con el título \"" + titulo + "\" ha sido " +
                    (respuesta ? "aprobada.\n\n" : "rechazada.\n\n") +
                    "Saludos,\nEl equipo de Carteruca\n";
            case Solicitante -> "Hola " + nombre + ",\n\n" +
                    "Has cancelado tu solicitud con el título \"" + titulo + "\" correctamente.\n\n" +
                    "Saludos,\nEl equipo de Carteruca";
            case CIO -> "Hola " + nombre + ",\n\n" +
                    "Tu solicitud con el título \"" + titulo + "\" ha sido actualizada por el CIO.\n\n" +
                    "Saludos,\nEl equipo de Carteruca";
            default -> "Hola " + nombre + ",\n\n" +
                    "Tu solicitud con el título \"" + titulo + "\" ha sido actualizada.\n\n" +
                    "Saludos,\nEl equipo de Carteruca";
        };
    }

    public List<Solicitud> getSolicitudByEstado(Estado estado) {
        return repository.findByEstado(estado);
    }

}
