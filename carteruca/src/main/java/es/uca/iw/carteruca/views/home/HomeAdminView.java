package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
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

        Div usuario = common.createSquare("Usuarios", VaadinIcon.USER);

        usuario.addClickListener(e-> UI.getCurrent().navigate(UsuarioAllView.class));

        getContent().add(usuario);

        Div centro = common.createSquare("Centros", VaadinIcon.ACADEMY_CAP);

        centro.addClickListener(e-> UI.getCurrent().navigate(CentroAllView.class));

        getContent().add(centro);

        Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);

        cartera.addClickListener(e -> UI.getCurrent().navigate(CarteraAllView.class));

        getContent().add(cartera);

        Div criterio = common.createSquare("Criterio", VaadinIcon.CHECK_CIRCLE);

        criterio.addClickListener(e -> UI.getCurrent().navigate(CriterioAllView.class));

        getContent().add(criterio);
    }

}
