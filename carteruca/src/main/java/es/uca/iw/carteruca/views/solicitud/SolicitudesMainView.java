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
@RolesAllowed({"Promotor","CIO","Solicitante", "OTP"})
public class SolicitudesMainView extends Composite<VerticalLayout> {

    public SolicitudesMainView() {

        //common.creartitulo("Solicitudes",this);


        VerticalLayout mainLayout = getContent();
        common.crearTitulo("Solicitudes",mainLayout);
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

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
        modificar.addClickListener(e -> UI.getCurrent().navigate(SolicitudUpdateView.class));

        // Cancelar Solicitud
        Div cancelar = common.createSquare("Cancelar Solicitud", VaadinIcon.CLOSE);
        cancelar.addClickListener(e -> UI.getCurrent().navigate(SolicitudDeleteView.class));

        // Ver Solicitudes
        Div solicitudes = common.createSquare("Ver Solicitudes", VaadinIcon.SEARCH);
        solicitudes.addClickListener(e -> UI.getCurrent().navigate(SolicitudSeeView.class));

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


        // Añadir todo al layout principal
        mainLayout.add(twoColumnsLayout);
        mainLayout.add(common.botones_Registrado());
    }
}

