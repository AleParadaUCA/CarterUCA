package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.solicitud.SolicitudesMainView;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.carteruca.views.common.common;

@PageTitle("Home")
@Route(value = "/home", layout = MainLayout.class)
//@RolesAllowed({"Solicitante", "CIO", "OTP", "Promotor"})
@PermitAll
public class HomeSolicitanteView extends Composite<VerticalLayout>{

    Span mensaje_bienvenido = new Span();

    public HomeSolicitanteView() {

        mensaje_bienvenido.setText("Bienvenido, usuario");
        mensaje_bienvenido.getStyle().set("color", "blue");
        getContent().add(mensaje_bienvenido);

        // Añadir los cuadros usando funciones
        Div solicitudes = common.createSquare("Solicitudes", VaadinIcon.FILE_O);

        solicitudes.addClickListener(event -> UI.getCurrent().navigate(SolicitudesMainView.class));

        getContent().add(solicitudes);

        Div avalar = common.createSquare("Avalar Solicitudes", VaadinIcon.BOOK);
        getContent().add(avalar);

    }

}
