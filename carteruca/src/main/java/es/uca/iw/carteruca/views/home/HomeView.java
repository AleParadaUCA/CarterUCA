package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.common.common;

@PageTitle("CarterUCA")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    public HomeView(){

        Div proyectos = common.createSquare("Proyectos", VaadinIcon.FILE_O);

        getContent().add(proyectos);
    }
    
}
