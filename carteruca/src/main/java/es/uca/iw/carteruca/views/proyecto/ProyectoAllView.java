package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Proyectos")
@Route(value = "/proyectos", layout = MainLayout.class)
@AnonymousAllowed

public class ProyectoAllView extends Composite<VerticalLayout> {

    private final CarteraService carteraService;
    private final ProyectoService proyectoService;

    private final Accordion carteras = new Accordion();

    @Autowired
    public ProyectoAllView(CarteraService carteraService, ProyectoService proyectoService, AuthenticatedUser authenticatedUser) {

        this.carteraService = carteraService;
        this.proyectoService = proyectoService;

        getContent().setSizeFull();
        getContent().setPadding(true);
        getContent().setSpacing(true);

        common.creartitulo("Proyectos",this);

        carteras.setSizeFull();
        loadCarteras();
        getContent().add(carteras);
        getContent().add(common.boton_dinamico(authenticatedUser.get().orElse(null)));
    }

    private void loadCarteras() {
        // Obtener todas las carteras del servicio
        List<Cartera> cartera_lista = carteraService.getAllCarteras();

        for (Cartera cartera : cartera_lista) {
            // Layout para mostrar los proyectos
            VerticalLayout proyectosLayout = new VerticalLayout();
            proyectosLayout.setSpacing(true);
            proyectosLayout.setPadding(false);

            // Obtener proyectos finalizados para la cartera actual
            List<Proyecto> proyectosList = proyectoService.getProyectosFinalizadosPorCartera(cartera.getId());

            if (proyectosList.isEmpty()) {
                proyectosLayout.add(new Span("No hay proyectos finalizados para esta cartera."));
            } else {
                // Crear un Grid para mostrar los proyectos
                Grid<Proyecto> proyect = new Grid<>(Proyecto.class, false);

                // Columnas del Grid
                proyect.addColumn(proyecto -> proyecto.getSolicitud().getNombre())
                             .setHeader("Nombre del Proyecto");

                proyect.addComponentColumn(proyecto -> common.createBadgeForEstado(proyecto.getSolicitud().getEstado()))
                             .setHeader("Estado");

                /*
                proyect.addColumn(Proyecto::getEspecificacion_tecnica)
                                .setHeader("Especificacion");
                                

                proyect.addColumn(Proyecto::getPresupuesto)
                             .setHeader("Presupuesto (€)");

                 */

                proyect.addColumn(Proyecto::getHoras)
                        .setHeader("Horas");

                proyect.addColumn(Proyecto::getPorcentaje)
                        .setHeader("Porcentaje");



                // Asignar los proyectos al Grid
                proyect.setItems(proyectosList);

                // Añadir el Grid al layout de proyectos
                proyectosLayout.add(proyect);
            }

            // Añadir la sección de la cartera al Accordion
            carteras.add(cartera.getNombre(), proyectosLayout);
        }
    }




}


