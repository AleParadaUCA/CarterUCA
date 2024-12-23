package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.solicitud.SolicitudChangeView;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Proyectos")
@Route(value = "/proyecto", layout = MainLayout.class)
@RolesAllowed({"CIO","OTP"})

public class ProyectoMainView extends Composite<VerticalLayout> {
    private Usuario currentUser;

    public ProyectoMainView(AuthenticatedUser authenticatedUser) {
        currentUser = authenticatedUser.get().get();

        common.creartitulo("Proyectos",this);

        if(currentUser.getRol() == Rol.CIO){

            Div aceptacion_a_proyectosproyectos = common.createSquare("Aceptación a Proyectos", VaadinIcon.CHECK_CIRCLE);
            aceptacion_a_proyectosproyectos.getElement().setAttribute("aria-label", "Proyectos");
            aceptacion_a_proyectosproyectos.addClickListener(event -> UI.getCurrent().navigate(SolicitudChangeView.class));
            getContent().add(aceptacion_a_proyectosproyectos);

            Div cancel = common.createSquare("Cancelar Proyecto", VaadinIcon.TRASH);
            cancel.getElement().setAttribute("aria-label", "Cancelar Proyecto");
            //cancel.addClickListener(event -> UI.getCurrent().navigate(SolicitudCancelView.class));
            getContent().add(cancel);


        }

        if(currentUser.getRol() == Rol.OTP){

            Div configure = common.createSquare("Configurar Proyectos", VaadinIcon.COG);
            configure.getElement().setAttribute("aria-label", "Configurar Proyectos");
            configure.addClickListener(event -> UI.getCurrent().navigate(ProyectoConfigureView.class));
            getContent().add(configure);

            Div update = common.createSquare("Actualizar Proyectos", VaadinIcon.REFRESH);
            update.getElement().setAttribute("aria-label", "Actualizar Proyectos");
            update.addClickListener(event -> UI.getCurrent().navigate(ProyectoUpdateView.class));
            getContent().add(update);

        }

        getContent().add(common.boton_dinamico(currentUser));

    }
}
