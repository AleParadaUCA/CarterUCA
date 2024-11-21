package es.uca.iw.carteruca.views.avalar;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import es.uca.iw.carteruca.views.layout.MainLayout;


@PageTitle("Avalar Solicitudes")
@Route(value = "/avalar-solicitudes", layout = MainLayout.class)
public class AvalarMainView extends Composite<VerticalLayout> {

    public AvalarMainView() {
        Div avalar = createSquare("Avalar", VaadinIcon.CHECK);

        avalar.addClickListener(e -> UI.getCurrent().navigate(AvalarAllView.class));

        // Botón "Volver"
        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        volver.addClassName("boton-avalar");
        // Footer layout para el botón
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.add(volver);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Evento al hacer clic en "Volver"
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeSolicitanteView.class));

        // Añadir componentes a la vista
        getContent().add(avalar, footer);
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