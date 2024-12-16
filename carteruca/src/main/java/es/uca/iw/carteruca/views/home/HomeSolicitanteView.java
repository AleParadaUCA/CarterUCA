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
import es.uca.iw.carteruca.views.layout.MainLayout;
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
        mensaje_bienvenido.setText("Bienvenido, usuario");
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
            getContent().add(avalar);
        }

    }

}
