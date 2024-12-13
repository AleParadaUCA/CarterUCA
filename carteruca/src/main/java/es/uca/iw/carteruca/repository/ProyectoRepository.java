package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
}
