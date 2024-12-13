package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.repository.CriterioRepository;
import es.uca.iw.carteruca.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProyectoService {

    private ProyectoRepository repository;
    private CriterioRepository criterioRepository;

    @Autowired
    public ProyectoService(ProyectoRepository repository, CriterioRepository criterioRepository) {
        this.repository = repository;
        this.criterioRepository = criterioRepository;
    }

    public Proyecto guardarProyecto(Proyecto proyecto) {return repository.save(proyecto);}
    public List<Proyecto> obtenerProyectos() {return repository.findAll();}

}
