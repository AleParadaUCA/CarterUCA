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
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import jakarta.annotation.security.PermitAll;

@PageTitle("Notificaciones")
@Route("/notificaciones")
@PermitAll
public class NotificacionesView extends Composite<VerticalLayout> {

    private final UsuarioService usuarioService;
    private final AuthenticatedUser authenticatedUser;
    private final SolicitudService solicitudService;
    private final Usuario currentUser;

    public NotificacionesView(UsuarioService usuarioService, AuthenticatedUser authenticatedUser,
                              SolicitudService solicitudService) {
        this.usuarioService = usuarioService;
        this.authenticatedUser = authenticatedUser;
        this.solicitudService = solicitudService;
        this.currentUser = authenticatedUser.get().get();

        Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class, false);
        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Titulo");
        solicitudes.addColumn(Solicitud::getFecha_solicitud).setHeader("Fecha Solicitud");
        solicitudes.addColumn(Solicitud::getEstado).setHeader("Estado");

        //List<Solicitud> solicitud = DataService.

        solicitudes.setEmptyStateText("No hay solicitudes");

        getContent().add(solicitudes);
        getContent().add(common.boton_dinamico(currentUser));


    }
}
