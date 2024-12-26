package es.uca.iw.carteruca.services;

import java.util.Arrays;
import java.util.List;

import es.uca.iw.carteruca.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import java.util.stream.Collectors;

import es.uca.iw.carteruca.repository.ProyectoRepository;
import es.uca.iw.carteruca.repository.CriterioRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository repository;
    private final CriterioRepository criterioRepository;
    private final EmailService emailService;

    @Autowired
    public ProyectoService(ProyectoRepository repository, CriterioRepository criterioRepository, EmailService emailService) {
        this.repository = repository;
        this.criterioRepository = criterioRepository;
        this.emailService = emailService;
    }

    public void crearProyecto(Solicitud solicitud) {
        // Crear un nuevo proyecto con la solicitud
        Proyecto proyecto = new Proyecto();

        // Aquí deberías establecer todos los campos necesarios para el proyecto
        proyecto.setSolicitud(solicitud);
        proyecto.setPorcentaje(0.0f);  // Puedes inicializar el porcentaje si es necesario
        proyecto.setHoras(0.0f);

        String subject = "Solicitud Creada";
        String body = "Hola " + proyecto.getSolicitud().getNombre() + ",\n\n" +
                "Mensaje de notificación sobre una actualización del porcentaje del Proyecto " +
                proyecto.getSolicitud().getNombre()+
                ".\n\nSaludos,\nEl equipo de Carteruca\n";

        repository.save(proyecto);
        emailService.enviarCorreo(proyecto.getSolicitud().getSolicitante().getEmail(),subject, body);

    }

    public void changeProyecto(Proyecto proyecto, MultiFileMemoryBuffer presupuesto, MultiFileMemoryBuffer especificacion) {

        String path = "../archivos/Cartera"+proyecto.getSolicitud().getCartera().getId()+"/proyectos"; //IMPORTANTE cambiar esto en producción
        List<String> presupuestoPath = CommonService.guardarFile(presupuesto, path);
        List<String> especificacionPath = CommonService.guardarFile(especificacion, path);

        proyecto.setPresupuesto(presupuestoPath.get(0));
        proyecto.setEspecificacion_tecnica(especificacionPath.get(0));

        repository.save(proyecto);
    }

    public void cambiarPorcentaje(Proyecto proyecto) {
        repository.save(proyecto);
    }

    public void guardarPuntuaciones(Proyecto proyecto, List<Long> idsCriterios, List<Float> puntuaciones) {
        if (idsCriterios.size() != puntuaciones.size()) {
            throw new IllegalArgumentException("Las listas de IDs y puntuaciones deben tener el mismo tamaño.");
        }

        // Obtener todos los criterios para calcular el peso
        List<Criterio> criterios = criterioRepository.findAllById(idsCriterios);

        float puntuacionTotal = 0.0f;

        for (int i = 0; i < idsCriterios.size(); i++) {
            Long criterioId = idsCriterios.get(i);
            Float puntuacion = puntuaciones.get(i);

            // Buscar el criterio correspondiente
            Criterio criterio = criterios.stream()
                    .filter(c -> c.getId().equals(criterioId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Criterio no encontrado: " + criterioId));

            // Calcular puntuación ponderada
            puntuacionTotal += puntuacion * criterio.getPeso();
        }

        // Actualizar proyecto con las puntuaciones y puntuación total
        proyecto.setPuntuacionTotal(puntuacionTotal);
        proyecto.setPuntuaciones(puntuaciones.toString()); // Guardar puntuaciones como cadena JSON o similar

        repository.save(proyecto); // Persistir en la base de datos
    }

    public List<Proyecto> getProyectosFinalizadosPorCartera(Long carteraId) {
        List<Estado> estadosPermitidos = List.of(Estado.ACEPTADO, Estado.TERMINADO);
        return repository.findBySolicitud_Cartera_IdAndSolicitud_EstadoIn(carteraId, estadosPermitidos).stream()
                // Filtrar proyectos donde todos los campos necesarios están completos
                .filter(proyecto -> proyecto.getHoras() > 0.0f)  // Verifica que las horas estén completas
                .filter(proyecto -> proyecto.getPresupuesto() != null && !proyecto.getPresupuesto().isEmpty())  // Verifica que el presupuesto no esté vacío
                .filter(proyecto -> proyecto.getPorcentaje() >= 0.0f)  // Verifica que el porcentaje esté completo
                .filter(proyecto -> proyecto.getEspecificacion_tecnica() != null && !proyecto.getEspecificacion_tecnica().isEmpty())  // Verifica la especificación técnica
                .filter(proyecto -> proyecto.getPuntuacionTotal() > 0.0f)  // Verifica que la puntuación total esté completa
                .filter(proyecto -> proyecto.getDirector_de_proyecto() != null && !proyecto.getDirector_de_proyecto().isEmpty())  // Verifica que el director esté asignado
                .filter(proyecto -> proyecto.getJefe() != null)  // Verifica que el jefe esté asignado
                .collect(Collectors.toList());
    }

    public List<Proyecto> getProyectosSinConfigurar() {
        // Obtenemos todos los proyectos de la base de datos
        List<Proyecto> proyectos = repository.findAll();

        // Filtramos los proyectos que no tienen la configuración
        return proyectos.stream()
                // Filtra proyectos donde la especificación técnica es nula o vacía
                .filter(proyecto -> proyecto.getEspecificacion_tecnica() == null || proyecto.getEspecificacion_tecnica().isEmpty())
                // Filtra proyectos donde las horas son 0.0
                .filter(proyecto -> proyecto.getHoras() == 0.0f)
                // Filtra proyectos donde el presupuesto es nulo o vacío
                .filter(proyecto -> proyecto.getPresupuesto() == null || proyecto.getPresupuesto().isEmpty())
                // Filtra proyectos donde el porcentaje es 0.0
                .filter(proyecto -> proyecto.getPorcentaje() == 0.0f)
                // Filtra proyectos donde el director de proyecto es nulo o vacío
                .filter(proyecto -> proyecto.getDirector_de_proyecto() == null || proyecto.getDirector_de_proyecto().isEmpty())
                // Filtra proyectos donde el jefe es nulo
                .filter(proyecto -> proyecto.getJefe() == null)
                .filter(proyecto -> proyecto.getSolicitud().getEstado() == Estado.ACEPTADO)
                .collect(Collectors.toList());
    }

    public float sumarHorasByCarteraAndEstado(Long carteraId) {

        // Filtrar proyectos con estados ACEPTADO o FINALIZADO
        List<Proyecto> proyectos = repository.findBySolicitud_Cartera_IdAndSolicitud_EstadoIn(
        carteraId, Arrays.asList(Estado.ACEPTADO, Estado.TERMINADO)
        );

        // Usamos mapToDouble en vez de mapToFloat
        return (float) proyectos.stream()
                .mapToDouble(Proyecto::getHoras)  // Aquí mapeamos las horas como double
                .sum();  // Suma de todos los valores y luego convertimos el resultado a float
    }

    public List<Proyecto> getProyectosPorJefeYEstado(Usuario jefe) {
        return repository.findByJefeAndSolicitud_Estado(jefe, Estado.ACEPTADO);
    }

    public List<Proyecto> getProyectosPorEstado(){
        return repository.findBySolicitud_Estado(Estado.ACEPTADO);
    }

    public List<Proyecto> getProyectosSinPuntuacion() {
        // Obtenemos los proyectos que no tienen puntuaciones ni puntuación total
        List<Proyecto> proyectos = repository.findByPuntuacionesIsNullAndPuntuacionTotalIsNull();

        // Filtramos los proyectos que tienen todos los campos necesarios rellenos (excepto puntuaciones y puntuacionTotal)
        return proyectos.stream()
                // Filtra proyectos donde solicitud no sea nula
                .filter(proyecto -> proyecto.getSolicitud() != null)
                // Filtra proyectos donde presupuesto no sea nulo ni vacío
                .filter(proyecto -> proyecto.getPresupuesto() != null && !proyecto.getPresupuesto().isEmpty())
                // Filtra proyectos donde especificacion_tecnica no sea nula ni vacía
                .filter(proyecto -> proyecto.getEspecificacion_tecnica() != null && !proyecto.getEspecificacion_tecnica().isEmpty())
                // Filtra proyectos donde porcentaje no sea nulo
                .filter(proyecto -> proyecto.getPorcentaje() != null)
                // Filtra proyectos donde horas no sea nulo
                .filter(proyecto -> proyecto.getHoras() != 0.0)
                // Filtra proyectos donde director_de_proyecto no sea nulo ni vacío
                .filter(proyecto -> proyecto.getDirector_de_proyecto() != null && !proyecto.getDirector_de_proyecto().isEmpty())
                // Filtra proyectos donde jefe no sea nulo
                .filter(proyecto -> proyecto.getJefe() != null)
                // Recoge los proyectos que cumplen todos los criterios
                .collect(Collectors.toList());
    }

}
