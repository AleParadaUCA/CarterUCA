package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
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

            // Obtener proyectos finalizados con todos los campos completos para la cartera actual
            List<Proyecto> listaDeProyectos = proyectoService.getProyectosFinalizadosPorCartera(cartera.getId());

            if (listaDeProyectos.isEmpty()) {
                proyectosLayout.add(new Span("No hay proyectos para esta cartera."));
            } else {
                // Crear un Grid para mostrar los proyectos
                Grid<Proyecto> proyect = new Grid<>(Proyecto.class, false);

                // Columna de nombre del proyecto
                proyect.addColumn(proyecto -> proyecto.getSolicitud().getNombre())
                        .setHeader("Nombre del Proyecto");

                // Columna de director
                proyect.addColumn(Proyecto::getDirector_de_proyecto)
                        .setHeader("Director");

                // Columna de jefe
                proyect.addColumn(proyecto -> {
                    // Si tienes un atributo nombre en Usuario, puedes ajustarlo aquí
                    return proyecto.getJefe() != null ? proyecto.getJefe().getNombre() : "No asignado";
                }).setHeader("Jefe");

                // Columna de horas con Badge
                proyect.addComponentColumn(proyecto -> {
                    Span badge = new Span(String.valueOf(proyecto.getHoras()));
                    badge.getElement().getStyle().set("font-size", "var(--lumo-font-size-m)"); // Tamaño de fuente más pequeño
                    badge.getElement().getStyle().set("background-color", "#009688");
                    badge.getElement().getStyle().set("color", "white");
                    badge.getElement().getStyle().set("border-radius", "12px"); // Radio de borde más pequeño
                    badge.getElement().getStyle().set("padding", "2px 6px"); // Padding más pequeño
                    return badge;
                }).setHeader("Horas");


                // Columna de progreso con ProgressBar
                proyect.addComponentColumn(proyecto -> {
                    ProgressBar progressBar = new ProgressBar();
                    progressBar.setValue(proyecto.getPorcentaje() / 100f); // Convertir el porcentaje a valor entre 0 y 1
                    progressBar.setWidth("100%");
                    return progressBar;
                }).setHeader("Progreso");

                // Asignar los proyectos al Grid
                proyect.setItems(listaDeProyectos);

                // Añadir el Grid al layout de proyectos
                proyectosLayout.add(proyect);
            }

            // Añadir la sección de la cartera al Accordion
            carteras.add(cartera.getNombre(), proyectosLayout);
        }
    }

}


