package es.uca.iw.carteruca.repository;

import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findBySolicitud_Cartera_IdAndSolicitud_EstadoIn(Long carteraId, List<Estado> estados);
    List<Proyecto> findByJefeAndSolicitud_Estado(Usuario jefe, Estado estado);
    List<Proyecto> findBySolicitud_Estado(Estado estado);
    List<Proyecto> findAll();
    List<Proyecto> findByPuntuacionesIsNullAndPuntuacionTotalIsNull();
    List<Proyecto> findAllBySolicitudEstadoInAndSolicitudSolicitante(List<Estado> estados, Usuario solicitante);

}
