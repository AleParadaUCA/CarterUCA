package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Solicitud;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Notificaciones")
@Route("/notificaciones")
@RolesAllowed({"Promotor","CIO","Solicitante"}) //no esta muy claro
public class NotificacionesView extends Composite<VerticalLayout> {

    public NotificacionesView() {

        Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class, false);
        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Titulo");
        solicitudes.addColumn(Solicitud::getFecha_solicitud).setHeader("Fecha Solicitud");
        solicitudes.addColumn(Solicitud::getEstado).setHeader("Estado");

        //List<Solicitud> solicitud = DataService.

        solicitudes.setEmptyStateText("No hay solicitudes");

        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        volver.addClickListener(e -> UI.getCurrent().navigate("HomeSolicitanteView.class"));

        // Layout para el footer con el botón
        HorizontalLayout footer = new HorizontalLayout(volver);
        footer.setWidthFull();
        footer.add(volver);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);


        getContent().add(solicitudes);
        getContent().add(footer);
    }
}
