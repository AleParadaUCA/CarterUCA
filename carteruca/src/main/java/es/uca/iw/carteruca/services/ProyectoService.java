package es.uca.iw.carteruca.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.repository.ProyectoRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository repository;
    //private final SolicitudService solicitudService;

    @Autowired
    public ProyectoService(ProyectoRepository repository) {
        this.repository = repository;
        //this.solicitudService = solicitudService;
    }

    public void guardarProyecto(Float puntuacion, Float porcentaje, MultiFileMemoryBuffer buffer, Solicitud solicitud) {

        List<String> documento = CommonService.guardarFile(buffer, "../archivos"+solicitud.getCartera().getNombre()+"/proyectos");

        Proyecto proyecto = new Proyecto();

        proyecto.setPuntuacion(puntuacion);
        proyecto.setPorcentaje(porcentaje);
        proyecto.setEspecificacion_tecnica(documento.get(0));
        proyecto.setPresupuesto(documento.get(1));
        proyecto.setSolicitud(solicitud);

        repository.save(proyecto);
    }

    public void updateProyecto(Proyecto proyecto) {
        repository.save(proyecto);
    }
}
