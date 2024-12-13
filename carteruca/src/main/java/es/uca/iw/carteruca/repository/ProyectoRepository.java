package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findProyectoBy();
}
