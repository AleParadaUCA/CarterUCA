package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Solicitudes a Proyectos")
@Route(value = "/solicitud-a-proyecto", layout = MainLayout.class)
@RolesAllowed("CIO")
public class SolicitudChangeView extends Composite<VerticalLayout> {

    private final SolicitudService solicitudService;
    private final ProyectoService proyectoService;
    private final AuthenticatedUser authenticatedUser;
    private Usuario currentUser;

    private final Grid<Solicitud> solicitud_tabla = new Grid<>(Solicitud.class);

    @Autowired
    public SolicitudChangeView(SolicitudService solicitudService, ProyectoService proyectoService,
                               AuthenticatedUser authenticatedUser) {
        this.solicitudService = solicitudService;
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;
        this.currentUser = authenticatedUser.get().get();

        common.creartitulo("Aceptación a Proyectos",this);
        crearTabla();
        getContent().add(common.boton_dinamico(currentUser));

    }

    private void crearTabla() {
        solicitud_tabla.setEmptyStateText("No hay Solicitudes que pasar a Proyectos");

        solicitud_tabla.removeAllColumns();

        solicitud_tabla.addColumn(Solicitud::getTitulo).setHeader("Titulo");
        solicitud_tabla.addColumn(Solicitud::getNombre).setHeader("Nombre");


        solicitud_tabla.addComponentColumn(solicitudes -> {
            Button proyectos = new Button("Proyectos");
            proyectos.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            proyectos.addClickListener(event -> Change_Dialog(solicitudes));
            return proyectos;
        });

        List<Solicitud> lista_solicitudes = solicitudService.getSolicitudByEstado(Estado.EN_TRAMITE_AVALADO);
        solicitud_tabla.setItems(lista_solicitudes);

        getContent().add(solicitud_tabla);

    }

    private void Change_Dialog(Solicitud solicitud) {

        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();

        Span titulo = new Span("¿Estás seguro de cambiar " + solicitud.getNombre() + " a Proyecto?");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setSpacing(true);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button BtnSi = new Button("Si", event -> {
            try {
                // Cambiar el estado de la solicitud
                solicitudService.ResolucionSolicitud(solicitud, true);

                // Crear el proyecto asociado a la solicitud
                proyectoService.crearProyecto(solicitud);

                // Mostrar una notificación de éxito
                common.showSuccessNotification("Solicitud aceptada y proyecto creado con éxito.");

                // Cerrar el diálogo
                dialog.close();

                // Actualizar la tabla para reflejar el cambio
                actualizarTabla();

            } catch (Exception e) {
                // Manejo de errores: mostrar mensaje en caso de error
                common.showErrorNotification("Ha ocurrido un error al cambiar la solicitud a proyecto. Intente nuevamente.");
            }
        });
        BtnSi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button BtnNo = new Button("No", click -> {
            try {
                // Cambiar el estado de la solicitud a RECHAZADO
                solicitudService.ResolucionSolicitud(solicitud, false);

                // Mostrar una notificación de rechazo
                common.showSuccessNotification("Solicitud rechazada correctamente.");

                // Cerrar el diálogo
                dialog.close();

                // Actualizar la tabla para reflejar el cambio
                actualizarTabla();
            } catch (Exception e) {
                // Manejo de errores: mostrar mensaje en caso de error
                common.showErrorNotification("Ha ocurrido un error al rechazar la solicitud. Intente nuevamente.");
            }
        });
        BtnNo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        buttons.add(BtnSi, BtnNo);

        layout.add(titulo, buttons);

        dialog.add(layout);
        dialog.open();
    }


    private void actualizarTabla() {
        List<Solicitud> lista_solicitudes = solicitudService.getSolicitudByEstado(Estado.EN_TRAMITE_AVALADO);
        solicitud_tabla.setItems(lista_solicitudes);
    }

}
