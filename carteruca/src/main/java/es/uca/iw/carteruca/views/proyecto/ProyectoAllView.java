package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.views.layout.MainLayout;

@PageTitle("Proyectos")
@Route(value = "/proyectos", layout = MainLayout.class)
@AnonymousAllowed

public class ProyectoAllView extends Composite<VerticalLayout> {
    public ProyectoAllView() {

    }
}
