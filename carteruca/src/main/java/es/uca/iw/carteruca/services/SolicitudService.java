package es.uca.iw.carteruca.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.SolicitudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SolicitudService {
    private static final Logger logger = LoggerFactory.getLogger(SolicitudService.class);
    private final SolicitudRepository repository;
    private final EmailService emailService;

    @Autowired
    public SolicitudService(SolicitudRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public void crearSolicitud(String titulo, String nombre, LocalDateTime fechaPuesta, String interesados, String alineamiento, String alcance, String normativa, MultiFileMemoryBuffer buffer, Usuario avalador, Usuario solictante, Cartera cartera) {

        //comprobar ".pdf" if (buffer.endsWith(".pdf"))
        List<String> memoria = CommonService.guardarFile(buffer, cartera.getId().toString()); //IMPORTANTE cambiar esto en producción

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

        String subject = "Solicitud Creada";
        String body = "Hola " + solictante.getNombre() + ",\n\n" +
                "Tu solicitud para la cartera " + cartera.getNombre() + " con el título \"" +
                solicitud.getTitulo() + "\" ha sido creada exitosamente.\n\n" +
                "¡Gracias por tu aportación!\n\n" +
                "Saludos,\nEl equipo de Carteruca.";

        repository.save(solicitud);
        logger.info("Creado solicitud con ID: {}", solicitud.getId());
        emailService.enviarCorreo(solictante.getEmail(), subject, body);
    }

    public void updateSolicitud(Solicitud solicitud, MultiFileMemoryBuffer buffer) {
        // Verificar si el buffer contiene archivos
        if (!buffer.getFiles().isEmpty()) {
            // Eliminar el archivo anterior
            CommonService.eliminarFile(solicitud.getMemoria());
            // Guardar el nuevo archivo y actualizar la memoria de la solicitud
            solicitud.setMemoria(CommonService.guardarFile(buffer, solicitud.getCartera().getId().toString()).get(0)); // IMPORTANTE cambiar esto en producción
        }

        // Enviar notificación de actualización
        String subject = "Solicitud Modificada";
        String body = "Hola " + solicitud.getSolicitante().getNombre() + ",\n\n" +
                "Tu solicitud con el título \"" + solicitud.getTitulo() + "\" ha sido modificada exitosamente.\n\n" +
                "Saludos,\n" +
                "El equipo de Carteruca.";

        repository.save(solicitud);
        logger.info("Actualizado solicitud con ID: {}", solicitud.getId());
        emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
    }

    public List<Solicitud> getSolicitudesByUsuario(Usuario usuario) {
        // Consultar todas las solicitudes asociadas a este usuario, excluyendo las CANCELADAS
        return repository.findBySolicitanteAndEstadoNot(usuario, Estado.CANCELADO);
    }

    public List<Solicitud> getSolicitudesByPromotor(Usuario promotor) {
        return repository.findByPromotorAndEstado(promotor, Estado.EN_TRAMITE);
    }

    /**
     * Avala o rechaza una solicitud dependiendo del nivel de importancia proporcionado.
     * Si se proporciona una importancia, la solicitud será aprobada y pasará al estado "EN_TRAMITE_AVALADO".
     * Si no se proporciona una importancia, la solicitud será rechazada y se establecerá su estado como "RECHAZADO".
     * Además, se envía un correo electrónico al solicitante notificando el resultado.
     *
     * @param solicitud   La solicitud que será avalada o rechazada.
     * @param importancia Nivel de importancia asignado por el avalador. Si es {@code null}, la solicitud será rechazada.
     */
    public void AvalarSolicitud(Solicitud solicitud, Integer importancia) {
        String subject = "Actualización solicitud '" + solicitud.getNombre() + "'";

        String estadoMensaje;
        if (importancia != null) {
            solicitud.setImportancia_promotor(importancia);
            solicitud.setEstado(Estado.EN_TRAMITE_AVALADO);
            estadoMensaje = "aprobada";
        } else {
            solicitud.setEstado(Estado.RECHAZADO);
            estadoMensaje = "rechazada";
        }

        String body = "Hola " + solicitud.getSolicitante().getNombre() + ",\n\n" +
                "Tu solicitud con el título \"" + solicitud.getTitulo() +
                "\" ha sido " + estadoMensaje + " por el Avalador.\n\nSaludos,\nEl equipo de Carteruca.";

        // Guardar la solicitud en la base de datos
        repository.save(solicitud);
        logger.info("Avalado solicitud con ID: {}", solicitud.getId());

        // Enviar correo de notificación al solicitante
        emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
    }


    /**
     * Resuelve una solicitud cambiando su estado a "ACEPTADO" o "RECHAZADO" según el parámetro proporcionado.
     * Si la solicitud es rechazada, se envía un correo electrónico al solicitante notificándole la resolución.
     *
     * @param solicitud La solicitud que será resuelta.
     * @param aprobado  {@code true} si la solicitud es aceptada; {@code false} si es rechazada.
     */
    public void ResolucionSolicitud(Solicitud solicitud, boolean aprobado) {
        if (aprobado) {
            solicitud.setEstado(Estado.ACEPTADO);
        } else {
            solicitud.setEstado(Estado.RECHAZADO);

            // Preparar y enviar el correo de rechazo
            String subject = "Resolución solicitud '" + solicitud.getNombre() + "'";
            String body = "Hola " + solicitud.getSolicitante().getNombre() + ",\n\n" +
                    "Tu solicitud con el título \"" + solicitud.getTitulo() +
                    "\" ha sido rechazada por el CIO.\n\nSaludos,\nEl equipo de Carteruca.";
            emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
        }

        // Guardar los cambios en la base de datos
        repository.save(solicitud);
        logger.info("Cambio de estado en la solicitud con ID: {}", solicitud.getId());
    }


    /**
     * Cancela una solicitud, actualizando su estado a "CANCELADO" y enviando un correo de confirmación
     * al solicitante notificándole sobre la cancelación.
     *
     * @param solicitud La solicitud que será cancelada.
     */
    public void CancelarSolicitud(Solicitud solicitud) {
        // Cambiar el estado de la solicitud a CANCELADO
        solicitud.setEstado(Estado.CANCELADO);

        // Preparar los detalles del correo
        String subject = "Cancelar solicitud '" + solicitud.getNombre() + "'";
        String body = "Hola " + solicitud.getSolicitante().getNombre() +
                ",\n\n Mensaje de confirmación sobre la cancelación de tu solicitud " + solicitud.getTitulo() +
                "\n\nSaludos,\nEl equipo de Carteruca.";

        // Guardar los cambios en la base de datos
        repository.save(solicitud);
        logger.info("Cancelado solicitud con ID: {}", solicitud.getId());

        // Enviar el correo de notificación al solicitante
        emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
    }
    
    /**
     * Marca una solicitud como terminada, actualizando su estado a "TERMINADO" y enviando un correo
     * de notificación al solicitante para informarle sobre la finalización del proyecto.
     *
     * @param solicitud La solicitud que será marcada como terminada.
     */
    public void TerminarSolicitud(Solicitud solicitud) {
        // Cambiar el estado de la solicitud a TERMINADO
        solicitud.setEstado(Estado.TERMINADO);

        // Preparar los detalles del correo de notificación
        String subject = "Cancelar solicitud '" + solicitud.getNombre() + "'";
        String body = "Hola " + solicitud.getSolicitante().getNombre() + ",\n\n" +
                "Mensaje de notificación sobre la completación del proyecto " + solicitud.getTitulo() +
                ".\n\nSaludos,\nEl equipo de Carteruca.";

        // Guardar los cambios en la base de datos
        repository.save(solicitud);
        logger.info("Estado Terminado de la solicitud con ID: {}", solicitud.getId());

        // Enviar el correo de notificación al solicitante
        emailService.enviarCorreo(solicitud.getSolicitante().getEmail(), subject, body);
    }


    public List<Solicitud> getSolicitudByEstado(Estado estado) {
        return repository.findByEstado(estado);
    }
}
