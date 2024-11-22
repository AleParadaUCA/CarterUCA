package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.solicitud.Solicitud;

import java.util.List;

@PageTitle("Notificaciones")
@Route("/notificaciones")
public class NotificacionesView extends Composite<VerticalLayout> {

    public NotificacionesView() {

        Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class, false);
        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Titulo");
        solicitudes.addColumn(Solicitud::getFecha_solicitud).setHeader("Fecha Solicitud");
        solicitudes.addColumn(Solicitud::getEstado).setHeader("Estado");

        //List<Solicitud> solicitud = DataService.

        solicitudes.setEmptyStateText("No hay solicitudes");

        getContent().add(solicitudes);
    }
}
