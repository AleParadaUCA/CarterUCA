package es.uca.iw.carteruca.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.carteruca.models.Criterio;
import es.uca.iw.carteruca.repository.CriterioRepository;

@Service
public class CriterioService {
    private final CriterioRepository criterioRepository;
    private static final Logger logger = LoggerFactory.getLogger(CriterioService.class);

    @Autowired
    public CriterioService(CriterioRepository criterioRepository) {
        this.criterioRepository = criterioRepository;
    }

    public List<Criterio> getAllCriterios() {
        logger.info("Obtener todos los criterios");
        return criterioRepository.findAll();
    }

    //Buscar un criterio por su descripcion
    public Criterio getCriterioByDescripcion(String descripcion) {
        logger.info("Obtener criterio con descripcion: {}", descripcion);
        return criterioRepository.findByDescripcion(descripcion);
    }

    //Agregar un nuevo criterio
    public Criterio addCriterio(Criterio criterio) {
        return criterioRepository.save(criterio);
    }

    //Actualizar un criterio
    public Criterio updateCriterio(Criterio criterio) {
        return criterioRepository.save(criterio);
    }
}
