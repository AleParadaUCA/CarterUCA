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
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Home")
@Route(value = "/home", layout = MainLayout.class)
@RolesAllowed({"Solicitante", "CIO", "OTP", "Promotor"})
public class HomeSolicitanteView extends Composite<VerticalLayout>{

    Span mensaje_bienvenido = new Span();

    public HomeSolicitanteView() {

        mensaje_bienvenido.setText("Bienvenido, usuario");
        mensaje_bienvenido.getStyle().set("color", "blue");
        getContent().add(mensaje_bienvenido);

        // Añadir los cuadros usando funciones
        Div solicitudes = createSquare("Solicitudes", VaadinIcon.FILE_O);

        solicitudes.addClickListener(event -> UI.getCurrent().navigate(SolicitudesMainView.class));

        getContent().add(solicitudes);

        String rol;
        if("Promotor".equals(rol = "promotor")){
            Div avalar = createSquare("Avalar Solicitudes", VaadinIcon.BOOK);
            getContent().add(avalar);
        }
    }

    private Div createSquare(String text, VaadinIcon iconType) {
        Div square = new Div();

        HorizontalLayout content = new HorizontalLayout();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Icon icon = iconType.create();
        icon.setSize("40px");
        icon.getStyle().set("color", "hsl(214, 33%, 38%)");

        Span label = new Span(text);
        label.getStyle().set("color","black");
        label.getStyle().set("font-size", "24px");

        content.add(icon, label);
        content.getStyle().set("align-items", "center");
        content.getStyle().set("justify-content", "flex-start");

        // Estilos del cuadrado
        square.getStyle().set("width", "400px");
        square.getStyle().set("height", "100px");
        square.getStyle().set("background-color", "#ffffff");
        square.getStyle().set("border", "1px solid #000");
        square.getStyle().set("display", "flex");
        square.getStyle().set("align-items", "center");
        square.getStyle().set("justify-content", "center");
        square.getStyle().set("cursor", "pointer");
        square.getStyle().set("margin-left", "50px");
        square.getStyle().set("margin-top", "20px");

        square.add(content);

        return square;
    }

}
