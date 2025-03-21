package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.views.avalar.AvalarMainView;
import es.uca.iw.carteruca.views.cartera.CarteraAllView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.proyecto.ProyectoMainView;
import es.uca.iw.carteruca.views.proyecto.ProyectoSeeView;
import es.uca.iw.carteruca.views.solicitud.SolicitudesMainView;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.carteruca.views.common.common;

@PageTitle("Home")
@Route(value = "/home", layout = MainLayout.class)
@RolesAllowed({"Solicitante", "CIO", "OTP", "Promotor"})
public class HomeSolicitanteView extends Composite<VerticalLayout>{

    public HomeSolicitanteView( AuthenticatedUser authenticatedUser) {
        Rol userRol = authenticatedUser.get().get().getRol();

        Span mensaje_bienvenido = new Span();
        mensaje_bienvenido.setText("Bienvenido, usuario " + authenticatedUser.get().get().getNombre());
        mensaje_bienvenido.getElement().setAttribute("aria-label", "Bienvenido, usuario");
        mensaje_bienvenido.getStyle().set("color", "blue");
        getContent().add(mensaje_bienvenido);

        // Añadir los cuadros usando funciones
        Div solicitudes = common.createSquare("Solicitudes", VaadinIcon.FILE_O);
        solicitudes.getElement().setAttribute("aria-label", "Solicitudes");

        solicitudes.addClickListener(event -> UI.getCurrent().navigate(SolicitudesMainView.class));

        getContent().add(solicitudes);

        if (userRol == Rol.Promotor) {

            Div avalar = common.createSquare("Avalar Solicitudes", VaadinIcon.BOOK);
            avalar.getElement().setAttribute("aria-label", "Avalar Solicitudes");
            avalar.addClickListener(event -> UI.getCurrent().navigate(AvalarMainView.class));
            getContent().add(avalar);
        }

        if (userRol == Rol.CIO || userRol == Rol.OTP) {

            Div proyectos = common.createSquare("Proyectos", VaadinIcon.COG);
            proyectos.getElement().setAttribute("aria-label", "Proyectos");
            proyectos.addClickListener(event -> UI.getCurrent().navigate(ProyectoMainView.class));
            getContent().add(proyectos);
        }

        if (userRol == Rol.CIO) {
            // Div para Cartera
            Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);
            cartera.getElement().setAttribute("aria-label", "Cartera");
            cartera.addClickListener(e -> UI.getCurrent().navigate(CarteraAllView.class));
            getContent().add(cartera);
        }

        Div consultar_proyectos = common.createSquare("Consultar Proyectos", VaadinIcon.RECORDS);
        consultar_proyectos.getElement().setAttribute("aria-label", "Consultar");
        consultar_proyectos.addClickListener(event -> UI.getCurrent().navigate(ProyectoSeeView.class));
        getContent().add(consultar_proyectos);


    }

}
