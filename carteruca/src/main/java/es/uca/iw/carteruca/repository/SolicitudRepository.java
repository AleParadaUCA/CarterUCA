package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findBytitulo(String titulo);

    List<Solicitud> findByestadoAndCartera_FechaInicio(Solicitud estado, LocalDateTime fecha);

    List<Solicitud> findByCartera_FechaInicio(LocalDateTime fecha);
    //Devuelve lista de solicitudes que pertenezcan a la cartera con fecha de inicio dada

    List<Solicitud> findBySolicitante(Usuario solicitante);    //Busca las solicitudes hechas por un usuario

}
