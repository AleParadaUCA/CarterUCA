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
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.views.cartera.CarteraActualView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.common.common;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Role;

@PageTitle("CarterUCA")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    public HomeView() {
        VerticalLayout container = new VerticalLayout();
        container.addClassName("responsive-container");

        // Crear cuadros con funcionalidad de navegación
        Div proyectos = common.createSquare("Proyectos", VaadinIcon.FILE_TEXT);
        proyectos.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("proyectos")));

        Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);
        cartera.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("cartera")));

        // Agregar los cuadros al contenedor
        container.add(proyectos, cartera);

        // Agregar el contenedor al contenido principal
        getContent().add(container);
    }
}
