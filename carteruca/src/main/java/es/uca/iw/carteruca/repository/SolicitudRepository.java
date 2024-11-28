package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.solicitud.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import es.uca.iw.carteruca.models.solicitud.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findBytitulo(String titulo);

    List<Solicitud> findByestadoAndCartera_FechaInicio(Solicitud estado, LocalDateTime fecha);

    List<Solicitud> findByCartera_FechaInicio(LocalDateTime fecha);
    //Devuelve lista de solicitudes que pertenezcan a la cartera con fecha de inicio dada

}
