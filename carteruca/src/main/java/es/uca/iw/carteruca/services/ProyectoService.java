package es.uca.iw.carteruca.services;

import java.util.List;

import es.uca.iw.carteruca.models.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import java.util.stream.Collectors;

import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.repository.ProyectoRepository;
import es.uca.iw.carteruca.repository.CriterioRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository repository;
    private final CriterioRepository criterioRepository;

    @Autowired
    public ProyectoService(ProyectoRepository repository, CriterioRepository criterioRepository) {
        this.repository = repository;
        this.criterioRepository = criterioRepository;
    }

    public void guardarProyecto( MultiFileMemoryBuffer buffer, Solicitud solicitud) { //Esto es para OTP

        List<String> documento = CommonService.guardarFile(buffer, "../archivos/Cartera"+solicitud.getCartera().getId()+"/proyectos"); //IMPORTANTE cambiar esto en producción

        Proyecto proyecto = new Proyecto();
        float porcentaje = 0.0f;

        proyecto.setPorcentaje(porcentaje);
        proyecto.setEspecificacion_tecnica(documento.get(0));
        proyecto.setPresupuesto(documento.get(1));
        proyecto.setSolicitud(solicitud);

        repository.save(proyecto);
    }

    public void updateProyecto(Proyecto proyecto) {
        repository.save(proyecto);
    }

    public List<Proyecto> getProyectosFinalizadosPorCartera(Long carteraId) {
        return repository.findBySolicitud_Cartera_IdAndSolicitud_Estado(carteraId, Estado.ACEPTADO).stream()
                .filter(proyecto -> proyecto.getHoras() > 0.0f)
                .filter(proyecto -> proyecto.getPresupuesto() != null && !proyecto.getPresupuesto().isEmpty())
                .filter(proyecto -> proyecto.getPorcentaje() > 0.0f)
                .filter(proyecto -> proyecto.getEspecificacion_tecnica() != null && !proyecto.getEspecificacion_tecnica().isEmpty())
                .filter(proyecto -> proyecto.getPuntuacionTotal() > 0.0f)
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
                .collect(Collectors.toList());
    }


    public void puntuacionTotal( Proyecto proyecto, List<Float> puntuaciones) {  //Esto es para CIO
        List<Float> pesos = criterioRepository.findAllPesos();
        float total = 0.0f;

        if (pesos.size() != puntuaciones.size()) {
            throw new IllegalArgumentException("Error, no coincide la cantidad de criterios con las puntuaciones dadas");
        }

        for (int i = 0; i < pesos.size(); i++) {
            total += pesos.get(i) * puntuaciones.get(i);
        }

        String stringpuntuaciones = puntuaciones.stream().map(String::valueOf).collect(Collectors.joining("/"));

        proyecto.setPuntuaciones(stringpuntuaciones);
        proyecto.setPuntuacionTotal(total);
        repository.save(proyecto);

    }

    public void guardarProyecto(Solicitud solicitud) {
        // Crear un nuevo proyecto con la solicitud
        Proyecto proyecto = new Proyecto();

        // Aquí deberías establecer todos los campos necesarios para el proyecto
        proyecto.setSolicitud(solicitud);
        proyecto.setPorcentaje(0.0f);  // Puedes inicializar el porcentaje si es necesario
        proyecto.setHoras(0.0f);
        // Guardar el proyecto en la base de datos
        repository.save(proyecto);
    }

    public float sumarHorasByCarteraAndEstado(Long carteraId) {
        List<Proyecto> proyectos = repository.findBySolicitud_Cartera_IdAndSolicitud_Estado(carteraId, Estado.ACEPTADO);

        // Usamos mapToDouble en vez de mapToFloat
        return (float) proyectos.stream()
                .mapToDouble(Proyecto::getHoras)  // Aquí mapeamos las horas como double
                .sum();  // Suma de todos los valores y luego convertimos el resultado a float
    }




}
