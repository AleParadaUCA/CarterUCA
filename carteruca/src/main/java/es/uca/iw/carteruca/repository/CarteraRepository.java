package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Cartera;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;


public interface CarteraRepository extends JpaRepository<Cartera, Long> {

    //se puede usar Optional<clase> por si no existe la instancia de esa clase devolver error

    Cartera findByFechaInicio(LocalDateTime fecha_inicio);//Busca una Cartera que tenga una fecha_inicio x

    void deleteByFechaInicio(LocalDateTime fecha_inicio);//Elimina cartera dada una fecha de inicio
}
