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
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Eliminar Solicitudes")
@Route(value = "/solicitudes/delete-solicitud", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante", "OTP"})
public class SolicitudDeleteView extends Composite<VerticalLayout> {

    private final SolicitudService solicitudService;
    private final AuthenticatedUser authenticatedUser;
    private final Usuario currentUser;

    private final Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class);

    @Autowired
    public SolicitudDeleteView(SolicitudService solicitudService,
                               AuthenticatedUser authenticatedUser) {
        this.solicitudService = solicitudService;
        this.authenticatedUser = authenticatedUser;
        currentUser = authenticatedUser.get().get();

        common.creartitulo("Eliminar Solicitudes",this);
        crearTabla();
        getContent().add(solicitudes);
        getContent().add(common.botones_solicitud());

    }

    private void crearTabla() {

        solicitudes.removeAllColumns();

        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Titulo del Proyecto");
        solicitudes.addColumn(Solicitud::getNombre).setHeader("Nombre del Solicitud");

        solicitudes.addComponentColumn(solicitud -> {
            if(solicitud.getEstado() == Estado.EN_TRAMITE) {
                Button eliminar = new Button("Eliminar Solicitud");
                eliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                eliminar.addClickListener(event -> EliminarDialog(solicitud));
                return eliminar;
            }else{
                return null;
            }

        });

        List<Solicitud> lista_solicitudes = solicitudService.getSolicitudesByUsuario(currentUser);
        solicitudes.setItems(lista_solicitudes);

    }

    private void updateGrid() {
        List<Solicitud> lista_solicitudes = solicitudService.getSolicitudesByUsuario(currentUser);
        solicitudes.setItems(lista_solicitudes);
    }

    private void EliminarDialog(Solicitud solicitud) {
        Dialog dialog = new Dialog();

        VerticalLayout dialogo = new VerticalLayout();
        Span titulo = new Span("¿Quieres eliminar la solicitud?");

        HorizontalLayout botones = new HorizontalLayout();

        Button Btnsi = new Button("Sí", event -> {
            try {
                // Cambiar el estado de la solicitud a CANCELADO
                solicitudService.CancelarSolicitud(solicitud);

                // Mostrar notificación de éxito
                common.showSuccessNotification("Solicitud cancelada correctamente.");

                // Actualizar el grid para reflejar los cambios
                updateGrid();

                // Cerrar el diálogo después de la operación
                dialog.close();
            } catch (Exception e) {
                // Manejar excepciones y mostrar mensaje de error
                common.showErrorNotification("Error al cancelar la solicitud: " + e.getMessage());
            }
        });
        Btnsi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);



        Button BtnNo = new Button("No",click -> dialog.close());
        BtnNo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        botones.add(Btnsi, BtnNo);

        dialogo.add(titulo, botones);

        dialog.add(dialogo);
        dialog.open();
    }

}
