package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Configurar Proyectos")
@Route(value = "/configurar-proyectos", layout = MainLayout.class)
@RolesAllowed("OTP")
public class ProyectoConfigureView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final AuthenticatedUser authenticatedUser;
    private Usuario currentUser;

    @Autowired
    public ProyectoConfigureView(ProyectoService proyectoService,
                                 AuthenticatedUser authenticatedUser) {
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;
        this.currentUser = authenticatedUser.get().get();;

        common.creartitulo("Configurar Proyectos",this);

        common.boton_dinamico(currentUser);


    }
}
