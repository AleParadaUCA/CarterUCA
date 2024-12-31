package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.Criterio;
import es.uca.iw.carteruca.repository.CriterioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CriterioService {
    private final CriterioRepository criterioRepository;

    @Autowired
    public CriterioService(CriterioRepository criterioRepository) {
        this.criterioRepository = criterioRepository;
    }

    public List<Criterio> getAllCriterios() {
        return criterioRepository.findAll();
    }

    //Buscar un criterio por su descripcion
    public Criterio getCriterioByDescripcion(String descripcion) {
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

    //Eliminar un criterio

    public void deleteCriterio(Long id) {
        Optional<Criterio> criterioOptional = criterioRepository.findById(id);
        if (criterioOptional.isPresent()) {
            criterioRepository.deleteById(id);
        }
    }
}
