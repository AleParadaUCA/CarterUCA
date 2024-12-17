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

@Service
public class SolicitudService {
    private final SolicitudRepository repository;

    @Autowired
    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }

    public void guardar(String titulo, String nombre, LocalDateTime fechaPuesta, String interesados, String alineamiento, String alcance, String normativa, MultiFileMemoryBuffer buffer, Usuario avalador, Usuario solictante, Cartera cartera) {
        
        //Faltan comprobaciones...

        List<String> memoria = CommonService.guardarFiles(buffer, "../archivos/Cartera"+ cartera.getId());
        
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
                                 String interesados, String alineamiento, String alcance, String normativa, Usuario avalador) {
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

        // Guardar los cambios en la base de datos
        repository.save(solicitud);
    }



    public List<Solicitud> getSolicitudesByUsuario(Usuario usuario) {
        // Consultar todas las solicitudes asociadas a este usuario, basándonos en los proyectos y estados que tiene
        return repository.findBySolicitante(usuario);
    }
}
