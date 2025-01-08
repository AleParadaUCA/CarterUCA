package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Centro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroRepository extends JpaRepository<Centro, Long>{

    Centro findByNombre(String nombre);

    void deleteById(Long id);

}
