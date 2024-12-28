package es.uca.iw.carteruca.views.proyecto;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.button.ButtonVariant;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Criterio;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.services.CommonService;
import es.uca.iw.carteruca.services.CriterioService;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Consultar Proyectos")
@Route(value = "/proyecto/consultar-proyectos", layout = MainLayout.class)
@RolesAllowed({"CIO", "OTP"})

public class ProyectoConsultarView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final CriterioService criterioService;

    private final Grid<Proyecto> proyectos_tabla = new Grid<>(Proyecto.class);

    @Autowired
    public ProyectoConsultarView(ProyectoService proyectoService,
                                 CriterioService criterioService) {
        this.proyectoService = proyectoService;
        this.criterioService = criterioService;

        common.creartitulo("Consultar Proyectos",this);

        crearTabla();

        getContent().add(common.botones_proyecto());

    }

    private void crearTabla() {

        proyectos_tabla.setEmptyStateText("No hay Proyectos que consultar");
        proyectos_tabla.setWidthFull();
        proyectos_tabla.setHeight("400px");
        proyectos_tabla.removeAllColumns();

        proyectos_tabla.addColumn(proyecto -> proyecto.getSolicitud().getTitulo()).setHeader("Titulo");
        proyectos_tabla.addColumn(common.createToggleDetailsRenderer(proyectos_tabla));
        proyectos_tabla.setItemDetailsRenderer(createStaticDetailsRendererConsulta());
        proyectos_tabla.setDetailsVisibleOnClick(true);  // Hacemos visibles los detalles cuando se hace clic en una fila


        List<Proyecto> proyectos = proyectoService.getProyectosConfiguradosSegunEstado();
        proyectos_tabla.setItems(proyectos);
        getContent().add(proyectos_tabla);
    }


    private ComponentRenderer<Div, Proyecto> createStaticDetailsRendererConsulta() {
        return new ComponentRenderer<>(proyecto -> {
            FormLayout formLayout = new FormLayout();

            // Campo Presupuesto Valor
            TextField presupuestoValorField = new TextField("Presupuesto Valor (€)");
            presupuestoValorField.setValue(proyecto.getPresupuesto_valor().toString());
            presupuestoValorField.setReadOnly(true);

            // Campo Puntuación Total
            TextField puntuacionTotalField = new TextField("Puntuación Total");
            puntuacionTotalField.setValue(proyecto.getPuntuacionTotal().toString());
            puntuacionTotalField.setReadOnly(true);

            Span porcentaje = new Span("Porcentaje");
            porcentaje.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior

            // ProgressBar para Porcentaje con Span
            Div progressBarContainer = new Div();
            //Span porcentajeSpan = new Span(proyecto.getPorcentaje() + "%");
            ProgressBar progressBar = new ProgressBar();
            progressBar.setValue(proyecto.getPorcentaje() / 100.0);
            progressBar.setWidthFull();
            progressBarContainer.add(progressBar);

            // Campo Horas
            TextField horasField = new TextField("Horas Totales");
            horasField.setValue(String.valueOf(proyecto.getHoras()));
            horasField.setReadOnly(true);

            // Campo Director del Proyecto
            TextField directorField = new TextField("Director del Proyecto");
            directorField.setValue(proyecto.getDirector_de_proyecto());
            directorField.setReadOnly(true);

            // Campo Jefe del Proyecto
            TextField jefeField = new TextField("Jefe del Proyecto");
            jefeField.setValue(proyecto.getJefe().getNombre());
            jefeField.setReadOnly(true);

            formLayout.add(presupuestoValorField,horasField,directorField,jefeField);
            formLayout.add(puntuacionTotalField);

            Span criterioSpan = new Span("Criterios");
            criterioSpan.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior


            Button verCriteriosButton = new Button("Ver Criterios", event -> {
                List<Criterio> criterios = criterioService.getAllCriterios();
                Dialog criteriosDialog = new Dialog();
                criteriosDialog.setHeaderTitle("Criterios");

                Grid<Criterio> criteriosGrid = new Grid<>(Criterio.class, false);
                criteriosGrid.addColumn(Criterio::getDescripcion).setHeader("Descripción");

                String puntuaciones = proyecto.getPuntuaciones();
                if (puntuaciones.startsWith("[") && puntuaciones.endsWith("]")) {
                    puntuaciones = puntuaciones.substring(1, puntuaciones.length() - 1);
                }
                List<String> puntuacionesList = Arrays.asList(puntuaciones.split(","));
                criteriosGrid.addColumn(criterio -> {
                    int index = criterios.indexOf(criterio);
                    return puntuacionesList.get(index).trim();
                }).setHeader("Puntuación");

                criteriosGrid.setItems(criterios);
                criteriosGrid.setSizeFull();

                Button cerrarButton = new Button("Cerrar", e -> criteriosDialog.close());
                HorizontalLayout buttonLayout = new HorizontalLayout(cerrarButton);
                buttonLayout.setWidthFull();
                buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

                VerticalLayout dialogLayout = new VerticalLayout(criteriosGrid, buttonLayout);
                dialogLayout.setSizeFull();
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);

                criteriosDialog.add(dialogLayout);
                criteriosDialog.setWidth("80%");
                criteriosDialog.setHeight("80%");
                criteriosDialog.open();
            });
            verCriteriosButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


            Span presupuestoSpan = new Span("Presupuesto");
            presupuestoSpan.getElement().setAttribute("aria-label", "Adjunte la memoria del proyecto");
            presupuestoSpan.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior

            String presupuestoPath = proyecto.getPresupuesto();
            Anchor downloadPresupuestoAnchor = CommonService.descargarFile(presupuestoPath, "Descargar Presupuesto");

            Span especificacionSpan = new Span("Especificacion");
            especificacionSpan.getElement().setAttribute("aria-label", "Adjunte la memoria del proyecto");
            especificacionSpan.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior

            String especificacionPath = proyecto.getEspecificacion_tecnica();
            Anchor downloadEspecificacionAnchor = CommonService.descargarFile(especificacionPath, "Descargar Especificación Técnica");


            // Contenedor para el porcentaje
            Div porcentajeContainer = new Div();
            porcentajeContainer.add(porcentaje);
            porcentajeContainer.setWidth("100%");
            formLayout.add(porcentajeContainer, 2);

            // El ProgressBar ocupa toda la fila
            formLayout.add(progressBarContainer, 1);

            formLayout.add(criterioSpan,2);
            VerticalLayout criterioBotones = new VerticalLayout();
            criterioBotones.add(verCriteriosButton);

            formLayout.add(criterioBotones);

            formLayout.add(presupuestoSpan,2);
            VerticalLayout presupuestoButtonLayout = new VerticalLayout();
            presupuestoButtonLayout.add(downloadPresupuestoAnchor);
            formLayout.add(presupuestoButtonLayout, 2);
            formLayout.add(especificacionSpan, 2);
            VerticalLayout especificacionButtonLayout = new VerticalLayout();
            especificacionButtonLayout.add(downloadEspecificacionAnchor);
            formLayout.add(especificacionButtonLayout, 2);

            

            // Crear el layout final para los detalles
            Div detailsLayout = new Div();
            detailsLayout.add(formLayout);

            return detailsLayout;
        });
    }




}
