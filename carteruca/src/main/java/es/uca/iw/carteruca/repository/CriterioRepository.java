package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Criterio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CriterioRepository extends JpaRepository<Criterio, Long> {

    Criterio findByDescripcion(String descripcion);

    List<Float> findAllPeso();//Devuelve el peso dado un id (esto servirá para el calculo de puntuación)

    void deleteById(Long id);

}
