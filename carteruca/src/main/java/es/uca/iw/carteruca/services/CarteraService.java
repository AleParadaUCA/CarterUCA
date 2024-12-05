package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.cartera.Cartera;
import es.uca.iw.carteruca.repository.CarteraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarteraService {

    private static final Logger logger = LoggerFactory.getLogger(CarteraService.class);
    private final CarteraRepository carteraRepository;

    @Autowired
    public CarteraService(CarteraRepository carteraRepository) {
        this.carteraRepository = carteraRepository;
    }

    // Obtener todas las carteras
    public List<Cartera> getAllCarteras() {
        return carteraRepository.findAll();
    }

    //Agregar una nueva cartera
    public Cartera addCartera(Cartera cartera) {
        if(cartera.getNombre() == null || cartera.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cartera es obligatorio");
        }
        if(cartera.getFecha_inicio() == null){
            throw new IllegalArgumentException("La fecha inicio del cartera es obligatorio");
        }
        if(cartera.getFecha_fin() == null){
            throw new IllegalArgumentException("La fecha fin del cartera es obligatorio");
        }

        if(cartera.getFecha_apertura_solicitud() == null){
            throw new IllegalArgumentException("La fecha de inicio de apertura de solicitudes" +
                    " de la cartera es obligatorio");
        }
        if(cartera.getFecha_cierre_solicitud() == null){
            throw new IllegalArgumentException("La fecha de cierre de envio de solicitudes es obligatorio");
        }
        logger.info("Nueva cartera agregada exitosamente: {}", cartera.getNombre());
        return carteraRepository.save(cartera);
    }

    //Actualizar una cartera existente
    public Cartera updateCartera(Long id, Cartera cartera) {
        // Actualizar solo los campos que no sean nulos
        if (cartera.getNombre() != null && !cartera.getNombre().trim().isEmpty()) {
            cartera.setNombre(cartera.getNombre());
            logger.info("Nombre actualizado a: {}", cartera.getNombre());
        }

        if (cartera.getFecha_inicio() != null) {
            cartera.setFecha_inicio(cartera.getFecha_inicio());
            logger.info("Fecha de inicio actualizada a: {}", cartera.getFecha_inicio());
        }

        if (cartera.getFecha_fin() != null) {
            cartera.setFecha_fin(cartera.getFecha_fin());
            logger.info("Fecha de fin actualizada a: {}", cartera.getFecha_fin());
        }

        if (cartera.getFecha_apertura_solicitud() != null) {
            cartera.setFecha_apertura_solicitud(cartera.getFecha_apertura_solicitud());
            logger.info("Fecha de apertura de solicitudes actualizada a: {}", cartera.getFecha_apertura_solicitud());
        }

        if (cartera.getFecha_cierre_solicitud() != null) {
            cartera.setFecha_cierre_solicitud(cartera.getFecha_cierre_solicitud());
            logger.info("Fecha de cierre de solicitudes actualizada a: {}", cartera.getFecha_cierre_solicitud());
        }

        // Guardar y devolver la cartera actualizada
        Cartera updatedCartera = carteraRepository.save(cartera);
        logger.info("Cartera actualizada exitosamente: {}", updatedCartera);
        return updatedCartera;
    }

    //Eliminar un centro
    public void deleteCartera(Long id) {
        Optional<Cartera> cartera = carteraRepository.findById(id);
        if(cartera.isPresent()) {
            carteraRepository.deleteById(id);
        }else{
            logger.warn("Intento de eliminar una cartera con ID inexistente: {}", id);
            throw new IllegalArgumentException("La cartera no existe");
        }
    }



}
