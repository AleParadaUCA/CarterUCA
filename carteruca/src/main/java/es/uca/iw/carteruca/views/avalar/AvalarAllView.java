package es.uca.iw.carteruca.views.avalar;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("All Solicitudes")
@Route(value ="/avalar-solicitudes/all", layout = MainLayout.class)
@RolesAllowed("promotor")
public class AvalarAllView extends Composite<VerticalLayout> {

    public AvalarAllView() {

    }
}
