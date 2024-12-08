package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.views.cartera.CarteraActualView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.common.common;

@PageTitle("CarterUCA")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    public HomeView(){

        Div proyectos = common.createSquare("Proyectos", VaadinIcon.FILE_O);
        proyectos.getElement().setAttribute("aria-label", "Proyectos");

        getContent().add(proyectos);

        Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);
        cartera.getElement().setAttribute("aria-label", "Cartera");
        cartera.addClickListener(event -> UI.getCurrent().navigate(CarteraActualView.class));

        getContent().add(cartera);

    }

}
