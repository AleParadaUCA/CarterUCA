package es.uca.iw.carteruca.views.proyecto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Cancelar Proyectos")
@Route(value = "proyectos/cancelar-proyectos", layout = MainLayout.class)
@RolesAllowed("CIO")
public class ProyectoCancelView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final SolicitudService solicitudService;

    private final Grid<Proyecto> proyectos_tabla = new Grid<>(Proyecto.class);

    @Autowired
    public ProyectoCancelView(ProyectoService proyectoService,
                              SolicitudService solicitudService) {

        this.proyectoService = proyectoService;
        this.solicitudService = solicitudService;

        common.creartitulo("Cancelar Proyectos",this);

        crearTabla();

        getContent().add(common.botones_proyecto());
        
    }

    private void crearTabla() {

        proyectos_tabla.setEmptyStateText("No hay Proyectos que cancelar");

        proyectos_tabla.setWidthFull();
        proyectos_tabla.setHeight("400px");

        proyectos_tabla.removeAllColumns();

        proyectos_tabla.addColumn(proyecto ->
                proyecto.getSolicitud().getTitulo()).setHeader("Título de la Solicitud");

        proyectos_tabla.addComponentColumn(proyecto -> {
            Button cancelarButton = new Button(VaadinIcon.TRASH.create());
            cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            cancelarButton.getElement().setAttribute("aria-label", "Cancelar Proyecto");

            cancelarButton.addClickListener(e -> EliminarDialog(proyecto));

            return cancelarButton;
        });

        List<Proyecto> lista = proyectoService.getProyectosPorEstado();
        proyectos_tabla.setItems(lista);

        getContent().add(proyectos_tabla);

    }

    private void EliminarDialog(Proyecto proyecto) {
        Dialog dialog = new Dialog();

        Span titulo = new Span("¿Desea eliminar el Proyecto?");

        Button BtnSi = new Button("Si", e -> {
            try {
                Solicitud solicitud = proyecto.getSolicitud();

                solicitudService.ResolucionSolicitud(solicitud,false);

//                // Guardar los cambios en el proyecto
//                proyectoService.update(proyecto);   //este update no hace nada pq no cambia nada, ¿no?

                // Mostrar notificación de éxito
                common.showSuccessNotification("Proyecto cancelado correctamente");

                // Cerrar el diálogo
                dialog.close();

                // Recargar la lista de proyectos desde la base de datos
                List<Proyecto> listaActualizada = proyectoService.getProyectosPorEstado(); // Obtén los proyectos con estado actualizado
                proyectos_tabla.setItems(listaActualizada); // Actualiza los datos en la tabla

            } catch (Exception ex) {
                // En caso de error, mostrar mensaje de error
                common.showErrorNotification("Error al cancelar el proyecto: " + ex.getMessage());
            }
        });
        BtnSi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        Button BtnNo = new Button("No", event -> dialog.close());
        BtnNo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Configurar el contenido y disposición de los botones en el diálogo
        VerticalLayout contenido = new VerticalLayout();

        HorizontalLayout buttons = new HorizontalLayout(BtnSi, BtnNo);
        buttons.setSpacing(true);
        buttons.setWidthFull();
        buttons.setAlignItems(FlexComponent.Alignment.CENTER);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        contenido.add(titulo, buttons);
        contenido.setAlignItems(FlexComponent.Alignment.CENTER);
        dialog.add(contenido);

        // Mostrar el diálogo
        dialog.open();
    }


}
