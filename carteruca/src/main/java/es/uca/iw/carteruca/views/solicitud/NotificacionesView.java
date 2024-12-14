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
import es.uca.iw.carteruca.views.common.common;
import jakarta.annotation.security.PermitAll;

@PageTitle("Notificaciones")
@Route("/notificaciones")
@PermitAll
public class NotificacionesView extends Composite<VerticalLayout> {

    public NotificacionesView() {

        Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class, false);
        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Titulo");
        solicitudes.addColumn(Solicitud::getFecha_solicitud).setHeader("Fecha Solicitud");
        solicitudes.addColumn(Solicitud::getEstado).setHeader("Estado");

        //List<Solicitud> solicitud = DataService.

        solicitudes.setEmptyStateText("No hay solicitudes");

        getContent().add(solicitudes);
        getContent().add(common.botones_Registrado());
    }
}
