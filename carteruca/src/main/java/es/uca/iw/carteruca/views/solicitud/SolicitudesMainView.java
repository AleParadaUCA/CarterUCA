package es.uca.iw.carteruca.views.solicitud;

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

import es.uca.iw.carteruca.views.home.HomeRegistradoView;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.layout.MainLayout;


@PageTitle("Solicitudes")
@Route(value = "/solicitudes", layout = MainLayout.class)
public class SolicitudesMainView extends Composite<VerticalLayout> {

    public SolicitudesMainView() {

        //Añadir Solicitud

        Div añadir = createSquare("Añadir Solicitud", VaadinIcon.PLUS);

        /*// Agregar el ClickListener para navegar a SolicitudesMainView
        añadir.addClickListener(event -> {
            UI.getCurrent().navigate(SolicitudesMainView.class); // Navega a la vista de destino
        });*/

        getContent().add(añadir);

        //Modificar Solicitud

        Div modificar = createSquare("Modificar Solicitud",VaadinIcon.EDIT);

        /*// Agregar el ClickListener para navegar a SolicitudesMainView
        modificar.addClickListener(event -> {
            UI.getCurrent().navigate(SolicitudesMainView.class); // Navega a la vista de destino
        });*/

        getContent().add(modificar);


        //Cancelar Solicitud

        Div cancelar = createSquare("Cancelar Solicitud",VaadinIcon.CLOSE);

        /*// Agregar el ClickListener para navegar a SolicitudesMainView
        modificar.addClickListener(event -> {
            UI.getCurrent().navigate(SolicitudesMainView.class); // Navega a la vista de destino
        });*/

        getContent().add(cancelar);

        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Layout para el botón en el footer
        HorizontalLayout footer = new HorizontalLayout(volver);
        footer.setWidthFull();
        footer.add(volver);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Evento al hacer clic en "Volver"
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeRegistradoView.class));

        // Añadir componentes a la vista
        getContent().add(añadir, modificar, cancelar, footer);

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

