package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

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

        common.creartitulo("Pasar a Proyectos",this);
        crearTabla();
        getContent().add(common.boton_dinamico(currentUser));

    }

    private void crearTabla() {
        solicitud_tabla.setEmptyStateText("No hay Solicitudes que pasar a proyectos");

    }
}
