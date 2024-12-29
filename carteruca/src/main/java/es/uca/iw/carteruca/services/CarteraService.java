package es.uca.iw.carteruca.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.repository.CarteraRepository;
import es.uca.iw.carteruca.repository.SolicitudRepository;

@Service
public class CarteraService {

    private static final Logger logger = LoggerFactory.getLogger(CarteraService.class);
    private final CarteraRepository carteraRepository;
    private final SolicitudRepository solicitudRepository;

    @Autowired
    public CarteraService(CarteraRepository carteraRepository, SolicitudRepository solicitudRepository) {
        this.carteraRepository = carteraRepository;
        this.solicitudRepository = solicitudRepository;
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

        if(cartera.getFecha_apertura_evaluacion() == null){
            throw new IllegalArgumentException("La fecha apertura evaluacion es obligatorio");
        }

        if(cartera.getFecha_cierre_evaluacion() == null){
            throw new IllegalArgumentException("La fecha cierre evaluacion es obligatorio");
        }

        if(cartera.getN_max_tecnicos() <= 0) {
            throw new IllegalArgumentException("El número de técnicos de la cartera es obligatorio y debe ser mayor que 0");
        }

        if(cartera.getPresupuesto_total() <= 0.0){
            throw new IllegalArgumentException("El presupuesto es obligatirio ");
        }

        if(cartera.getN_horas() <= 0.0){
            throw new IllegalArgumentException("El numero de horas es oblgatorio");
        }

        logger.info("Nueva cartera agregada exitosamente: {}", cartera.getNombre());

        // Comprobar si ya existe una cartera con la misma tupla única
        Optional<Cartera> existingCartera = carteraRepository.findByFechaInicioAndFechaFin(  cartera.getFecha_inicio(), cartera.getFecha_fin());
        if (existingCartera.isPresent()) {
            throw new IllegalArgumentException("Ya existe una cartera con el mismo nombre, fecha de inicio y fecha de fin");
        }

        return carteraRepository.save(cartera);
    }

    @Transactional
    public Cartera updateCartera(Long id, Cartera cartera) {
        // Obtener la cartera original de la base de datos
        Cartera originalCartera = carteraRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cartera no encontrada"));

        // Solo actualizamos los campos si los nuevos valores son diferentes a los actuales

        if (cartera.getNombre() != null && !cartera.getNombre().trim().isEmpty() && !cartera.getNombre().equals(originalCartera.getNombre())) {
            originalCartera.setNombre(cartera.getNombre());
        }

        if (cartera.getFecha_inicio() != null && !cartera.getFecha_inicio().equals(originalCartera.getFecha_inicio())) {
            originalCartera.setFecha_inicio(cartera.getFecha_inicio());
        }

        if (cartera.getFecha_fin() != null && !cartera.getFecha_fin().equals(originalCartera.getFecha_fin())) {
            originalCartera.setFecha_fin(cartera.getFecha_fin());
        }

        if (cartera.getFecha_apertura_solicitud() != null && !cartera.getFecha_apertura_solicitud().equals(originalCartera.getFecha_apertura_solicitud())) {
            originalCartera.setFecha_apertura_solicitud(cartera.getFecha_apertura_solicitud());
        }

        if (cartera.getFecha_cierre_solicitud() != null && !cartera.getFecha_cierre_solicitud().equals(originalCartera.getFecha_cierre_solicitud())) {
            originalCartera.setFecha_cierre_solicitud(cartera.getFecha_cierre_solicitud());
        }

        if (cartera.getFecha_apertura_evaluacion() != null && !cartera.getFecha_apertura_evaluacion().equals(originalCartera.getFecha_apertura_evaluacion())) {
            originalCartera.setFecha_apertura_evaluacion(cartera.getFecha_apertura_evaluacion());
        }

        if (cartera.getFecha_cierre_evaluacion() != null && !cartera.getFecha_cierre_evaluacion().equals(originalCartera.getFecha_cierre_evaluacion())) {
            originalCartera.setFecha_cierre_evaluacion(cartera.getFecha_cierre_evaluacion());
        }

        // Comparar y actualizar el valor de n_max_tecnicos, solo si el valor ha cambiado
        if (cartera.getN_max_tecnicos() != 0 && cartera.getN_max_tecnicos() != originalCartera.getN_max_tecnicos()) {
            originalCartera.setN_max_tecnicos(cartera.getN_max_tecnicos());
        }

        // Comparar y actualizar el valor de n_horas, solo si el valor ha cambiado
        if (cartera.getN_horas() != 0 && cartera.getN_horas() != originalCartera.getN_horas()) {
            originalCartera.setN_horas(cartera.getN_horas());
        }

        // Comparar y actualizar el valor de presupuesto_total, solo si el valor ha cambiado
        if (cartera.getPresupuesto_total() != 0 && cartera.getPresupuesto_total() != originalCartera.getPresupuesto_total()) {
            originalCartera.setPresupuesto_total(cartera.getPresupuesto_total());
        }

        // Comprobar si ya existe una cartera con la misma tupla única
        Optional<Cartera> existingCartera = carteraRepository.findByFechaInicioAndFechaFin(  cartera.getFecha_inicio(), cartera.getFecha_fin());
        if (existingCartera.isPresent() && !existingCartera.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una cartera con el mismo nombre, fecha de inicio y fecha de fin");
        }

        // Guardar la cartera actualizada si hay algún cambio
        Cartera updatedCartera = carteraRepository.save(originalCartera);
        logger.info("Cartera actualizada exitosamente: {}", updatedCartera);
        return updatedCartera;
    }

    //Eliminar un centro
    public void deleteCartera(Long id) {
        Optional<Cartera> cartera = carteraRepository.findById(id);
        if (cartera.isPresent()) {
            List<Solicitud> solicitudes = solicitudRepository.findByCartera(cartera.get());
            if (!solicitudes.isEmpty()) {
                throw new IllegalArgumentException("No se puede eliminar la cartera porque tiene solicitudes asociadas.");
            }
            carteraRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("La cartera no existe.");
        }
    }

    public Optional<Cartera> getCarteraActual() {
        LocalDateTime now = LocalDateTime.now();
        return carteraRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(now, now);
    }
}
