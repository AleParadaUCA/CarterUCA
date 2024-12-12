package es.uca.iw.carteruca.views.avalar;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.common.common;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;


@PageTitle("Avalar Solicitudes")
@Route(value = "/avalar-solicitudes", layout = MainLayout.class)
@RolesAllowed("promotor")
public class AvalarMainView extends Composite<VerticalLayout> {

    public AvalarMainView() {
        Div avalar = common.createSquare("Avalar", VaadinIcon.CHECK);
        avalar.getElement().setAttribute("aria-label", "Avalar");

        avalar.addClickListener(e -> UI.getCurrent().navigate(AvalarAllView.class));

        // Botón "Volver"
        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        volver.addClassName("boton-avalar");
        volver.getElement().setAttribute("aria-label", "Volver");
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