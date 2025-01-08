package es.uca.iw.carteruca.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByPromotorAndEstado(Usuario promotor, Estado estado);

    List<Solicitud> findBySolicitanteAndEstadoNot(Usuario solicitante, Estado estado);

    List<Solicitud> findByEstado(Estado estado);

    public List<Solicitud> findByCartera(Cartera get);
}
