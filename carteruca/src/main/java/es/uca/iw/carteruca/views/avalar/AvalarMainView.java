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
        Div avalar = new Div();

        HorizontalLayout content = new HorizontalLayout();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Icon approveIcon = VaadinIcon.CHECK.create();
        approveIcon.setSize("40px");
        approveIcon.getStyle().set("color", "hsl(214, 33%, 38%)");

        Span title = new Span("Avalar");
        title.getStyle().set("font-size", "24px");

        content.add(approveIcon, title);
        content.getStyle().set("align-items", "center");
        content.getStyle().set("justify-content", "flex-start");

        avalar.getStyle().set("width", "400px");
        avalar.getStyle().set("height", "100px");
        avalar.getStyle().set("background-color", "#ffffff");
        avalar.getStyle().set("border", "1px solid #000");
        avalar.getStyle().set("display", "flex");
        avalar.getStyle().set("align-items", "center");
        avalar.getStyle().set("justify-content", "center");
        avalar.getStyle().set("cursor", "pointer");
        avalar.getStyle().set("margin-left", "50px");
        avalar.getStyle().set("margin-top", "20px");

        avalar.add(content);

        // Botón "Volver"
        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

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
}