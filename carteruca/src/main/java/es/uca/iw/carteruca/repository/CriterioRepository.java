package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Criterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CriterioRepository extends JpaRepository<Criterio, Long> {

    Criterio findByDescripcion(String descripcion);

    void deleteById(Long id);

    @Query("select c.peso from Criterio c")
    List<Float> findAllPesos();

}
