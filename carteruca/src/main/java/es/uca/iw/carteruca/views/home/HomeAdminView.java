package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.views.admin.CentroAllView;
import es.uca.iw.carteruca.views.admin.UsuarioAllView;
import es.uca.iw.carteruca.views.cartera.CarteraAllView;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.criterio.CriterioAllView;
import jakarta.annotation.security.RolesAllowed;

@Route("/home-admin")
@PageTitle("Home")
@RolesAllowed("Admin")
public class HomeAdminView extends Composite<VerticalLayout> {

    public HomeAdminView() {

        // Layout principal
        VerticalLayout mainLayout = getContent();
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Contenedor para dos columnas
        HorizontalLayout twoColumnsLayout = new HorizontalLayout();
        twoColumnsLayout.setWidthFull();
        twoColumnsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centrar las columnas
        twoColumnsLayout.setSpacing(true); // Espaciado entre columnas

        // Div para Usuarios
        Div usuario = common.createSquare("Usuarios", VaadinIcon.USER);
        usuario.addClickListener(e -> UI.getCurrent().navigate(UsuarioAllView.class));
        usuario.getStyle().set("margin-top", "10px"); // Espaciado entre Divs

        // Div para Centros
        Div centro = common.createSquare("Centros", VaadinIcon.ACADEMY_CAP);
        centro.addClickListener(e -> UI.getCurrent().navigate(CentroAllView.class));
        centro.getStyle().set("margin-top", "10px"); // Espaciado entre Divs

        // Div para Cartera
        Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);
        cartera.addClickListener(e -> UI.getCurrent().navigate(CarteraAllView.class));
        cartera.getStyle().set("margin-top", "10px");

        // Div para Criterio
        Div criterio = common.createSquare("Criterio", VaadinIcon.CHECK_CIRCLE);
        criterio.addClickListener(e -> UI.getCurrent().navigate(CriterioAllView.class));
        criterio.getStyle().set("margin-top", "10px");
        
        // Agregar elementos a las dos columnas con más espacio superior
        VerticalLayout column1 = new VerticalLayout(usuario, centro);
        column1.setSpacing(false);
        column1.setPadding(false);
        column1.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        column1.getStyle().set("margin-top", "20px"); // Espaciado superior para la primera columna

        VerticalLayout column2 = new VerticalLayout(cartera, criterio);
        column2.setSpacing(false);
        column2.setPadding(false);
        column2.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        column2.getStyle().set("margin-top", "20px"); // Espaciado superior para la segunda columna

        // Agregar columnas al contenedor
        twoColumnsLayout.add(column1, column2);

        // Agregar contenedor al layout principal
        mainLayout.add(twoColumnsLayout);
    }
}
