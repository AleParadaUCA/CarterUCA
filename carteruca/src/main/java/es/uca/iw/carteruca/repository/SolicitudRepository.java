package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.solicitud.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    //se puede usar Optional<clase> por si no existe la instancia de esa clase devolver error

    List<Solicitud> findBytitulo(String titulo);

    List<Solicitud> findByestadoAndCartera_fecha_inicio(Solicitud estado, LocalDateTime fecha);

    List<Solicitud> findByCartera_fecha_inicio(LocalDateTime fecha);
    //Devuelve lista de solicitudes que pertenezcan a la cartera con fecha de inicio dada

}
