package es.uca.iw.carteruca.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.repository.CentroRepository;

@Service
public class CentroService {

    private final CentroRepository centroRepository;

    private static final Logger logger = LoggerFactory.getLogger(CentroService.class);

    @Autowired
    public CentroService(CentroRepository centroRepository) {
        this.centroRepository = centroRepository;
    }

    // Obtener todos los centros
    public List<Centro> getAllCentros() {
        return centroRepository.findAll();
    }

    // Agregar un nuevo centro
    public Centro addCentro(Centro centro) {
        if (centro.getNombre() == null || centro.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del centro no puede estar vacío");
        }
        if (centro.getAcronimo() == null || centro.getAcronimo().trim().isEmpty()) {
            throw new IllegalArgumentException("El acrónimo no puede estar vacío");
        }
        logger.info("Guardando centro: {} con acrónimo: {}", centro.getNombre(), centro.getAcronimo());
        return centroRepository.save(centro);
    }

    // Actualizar un centro existente
    public Centro updateCentro(Centro centro) {
        if (centro.getId() == null || !centroRepository.existsById(centro.getId())) {
            throw new IllegalArgumentException("No se puede actualizar. El centro no existe.");
        }
        if (centro.getNombre() == null || centro.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del centro no puede estar vacío");
        }
        if (centro.getAcronimo() == null || centro.getAcronimo().trim().isEmpty()) {
            throw new IllegalArgumentException("El acrónimo no puede estar vacío");
        }
        logger.info("Actualizando centro con ID: {}, nuevo nombre: {}, nuevo acrónimo: {}",
                centro.getId(), centro.getNombre(), centro.getAcronimo());
        return centroRepository.save(centro);
    }

}
