package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Cartera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;


public interface CarteraRepository extends JpaRepository<Cartera, Long> {

    Optional<Cartera> findByFechaInicioAndFechaFin(LocalDateTime inicio, LocalDateTime end);

    Cartera findByFechaInicio(LocalDateTime fecha_inicio);//Busca una Cartera que tenga una fecha_inicio x

    void deleteByFechaInicio(LocalDateTime fecha_inicio);//Elimina cartera dada una fecha de inicio

    Optional<Cartera> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDateTime start, LocalDateTime end); //escoger la cartera que esta actualmente

    //Es la que funciona cuando hay mas de una cartera
    @Query("SELECT c FROM Cartera c WHERE c.fechaInicio <= :fechaActual AND c.fechaFin >= :fechaActual ORDER BY c.fechaInicio DESC")
    Optional<Cartera> buscarCarteraVigente(@Param("fechaActual") LocalDateTime fechaActual);


}
