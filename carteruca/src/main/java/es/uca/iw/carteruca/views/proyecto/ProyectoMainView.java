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

@PageTitle("Proyectos")
@Route(value = "/proyecto", layout = MainLayout.class)
@RolesAllowed({"CIO","OTP"})

public class ProyectoMainView extends Composite<VerticalLayout> {
    private final Usuario currentUser;

    public ProyectoMainView(AuthenticatedUser authenticatedUser) {
        currentUser = authenticatedUser.get().get();

        common.creartitulo("Proyectos",this);

        Div consultar = common.createSquare("Consultar Proyectos",VaadinIcon.SEARCH);
        consultar.getElement().setAttribute("aria-label", "Consultar Proyectos");
        consultar.addClickListener(event -> UI.getCurrent().navigate(ProyectoConsultarView.class));
        getContent().add(consultar);

        if(currentUser.getRol() == Rol.CIO){

            Div aceptacion_a_proyectosproyectos = common.createSquare("Aceptación a Proyectos", VaadinIcon.CHECK_CIRCLE);
            aceptacion_a_proyectosproyectos.getElement().setAttribute("aria-label", "Proyectos");
            aceptacion_a_proyectosproyectos.addClickListener(event -> UI.getCurrent().navigate(SolicitudChangeView.class));
            getContent().add(aceptacion_a_proyectosproyectos);

            Div cancel = common.createSquare("Cancelar Proyecto", VaadinIcon.TRASH);
            cancel.getElement().setAttribute("aria-label", "Cancelar Proyecto");
            cancel.addClickListener(event -> UI.getCurrent().navigate(ProyectoCancelView.class));
            getContent().add(cancel);

            Div puntuar = common.createSquare("Puntuar", VaadinIcon.CHECK_SQUARE);
            puntuar.getElement().setAttribute("aria-label", "Puntuar");
            puntuar.addClickListener(event -> UI.getCurrent().navigate(ProyectoPuntuarView.class));
            getContent().add(puntuar);


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

            Div reasignar = common.createSquare("Reasignar Jefe", VaadinIcon.USER_STAR);
            reasignar.getElement().setAttribute("aria-label", "Reasignar Jefe");
            reasignar.addClickListener(event -> UI.getCurrent().navigate(ProyectoReasignarView.class));
            getContent().add(reasignar);
        }

        getContent().add(common.boton_dinamico(currentUser));

    }
}
