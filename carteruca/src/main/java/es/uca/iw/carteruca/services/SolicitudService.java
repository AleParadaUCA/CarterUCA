package es.uca.iw.carteruca.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.SolicitudRepository;

@Service
public class SolicitudService {
    private final SolicitudRepository repository;

    @Autowired
    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
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
    }

    public void  delete_solicitud(Long id) {
        Optional<Solicitud> criterioOptional = repository.findById(id);
        if (criterioOptional.isPresent()) {
            repository.deleteById(id);
        }
    }



    public List<Solicitud> getSolicitudesByUsuario(Usuario usuario) {
        // Consultar todas las solicitudes asociadas a este usuario, excluyendo las CANCELADAS
        return repository.findBySolicitanteAndEstadoNot(usuario, Estado.CANCELADO);
    }


    public List<Solicitud> getSolicitudesByPromotor(Usuario promotor) {
        return repository.findByPromotorAndEstado(promotor, Estado.EN_TRAMITE);
    }

    public void updateSolicitud(Solicitud solicitud) {
        repository.save(solicitud);  // Guarda la solicitud actualizada
    }

    public List<Solicitud> getSolicitudByEstado(Estado estado) {
        return repository.findByEstado(estado);
    }





}
