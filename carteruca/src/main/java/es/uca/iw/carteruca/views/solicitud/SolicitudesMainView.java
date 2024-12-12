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
import es.uca.iw.carteruca.views.solicitud.SolicitudAddView;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.carteruca.views.common.common;


@PageTitle("Solicitudes")
@Route(value = "/solicitudes", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante"})
public class SolicitudesMainView extends Composite<VerticalLayout> {

    public SolicitudesMainView() {

        VerticalLayout mainLayout = getContent();
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        // Contenedor para dos columnas
        HorizontalLayout twoColumnsLayout = new HorizontalLayout();
        twoColumnsLayout.setWidthFull();
        twoColumnsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Opcional para centrar las columnas
        twoColumnsLayout.setSpacing(true); // Espaciado entre columnas

        // Añadir Solicitud
        Div añadir = common.createSquare("Añadir Solicitud", VaadinIcon.PLUS);
        añadir.addClickListener(e -> UI.getCurrent().navigate(SolicitudAddView.class));

        // Modificar Solicitud
        Div modificar = common.createSquare("Modificar Solicitud", VaadinIcon.EDIT);

        // Cancelar Solicitud
        Div cancelar = common.createSquare("Cancelar Solicitud", VaadinIcon.CLOSE);

        // Ver Solicitudes
        Div solicitudes = common.createSquare("Ver Solicitudes", VaadinIcon.SEARCH);

        // Agregar elementos a las dos columnas
        VerticalLayout column1 = new VerticalLayout(añadir, modificar);
        column1.setSpacing(false);
        column1.setPadding(false);
        column1.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout column2 = new VerticalLayout(cancelar, solicitudes);
        column2.setSpacing(false);
        column2.setPadding(false);
        column2.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        // Agregar columnas al contenedor
        twoColumnsLayout.add(column1, column2);

        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeSolicitanteView.class));

        // Footer layout con margen superior
        HorizontalLayout footer = new HorizontalLayout(volver);
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.getStyle().set("margin-top", "50px");

        // Añadir todo al layout principal
        mainLayout.add(twoColumnsLayout, footer);
    }
}

