package es.uca.iw.carteruca.models.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroRepository extends JpaRepository<Centro, Long>{

    Centro findByNombre(String nombre);
}
