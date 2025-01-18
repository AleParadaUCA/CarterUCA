package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.layout.MainLayout;
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
        Div add = common.createSquare("Añadir Solicitud", VaadinIcon.PLUS);
        add.addClickListener(e -> UI.getCurrent().navigate(SolicitudAddView.class));

        // Ver Solicitudes
        Div solicitudes = common.createSquare("Ver Solicitudes", VaadinIcon.SEARCH);
        solicitudes.addClickListener(e -> UI.getCurrent().navigate(SolicitudSeeView.class));

        // Agregar elementos a las dos columnas
        VerticalLayout column1 = new VerticalLayout(add, solicitudes);
        column1.setSpacing(true);
        column1.setPadding(true);
        column1.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);

        // Agregar columnas al contenedor
        twoColumnsLayout.add(column1);


        // Añadir todo al layout principal
        mainLayout.add(twoColumnsLayout);
        mainLayout.add(common.botones_Registrado());
    }
}

