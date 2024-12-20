package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
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

        HorizontalLayout primera_fila = new HorizontalLayout();
        HorizontalLayout segunda_fila = new HorizontalLayout();

        // Primera fila
        Div usuario = common.createSquare("Usuarios", VaadinIcon.USER);
        usuario.addClickListener(e -> UI.getCurrent().navigate(UsuarioAllView.class));

        Div centro = common.createSquare("Centros", VaadinIcon.ACADEMY_CAP);
        centro.addClickListener(e -> UI.getCurrent().navigate(CentroAllView.class));

        primera_fila.add(usuario, centro);
        primera_fila.setSpacing(true); // Espaciado entre columnas

        // Segunda fila
        Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);
        cartera.addClickListener(e -> UI.getCurrent().navigate(CarteraAllView.class));

        Div criterio = common.createSquare("Criterio", VaadinIcon.CHECK_CIRCLE);
        criterio.addClickListener(e -> UI.getCurrent().navigate(CriterioAllView.class));

        segunda_fila.add(cartera, criterio);
        segunda_fila.setSpacing(true); // Espaciado entre columnas

        // Añadir filas al layout principal
        getContent().add(primera_fila, segunda_fila);
    }
}
