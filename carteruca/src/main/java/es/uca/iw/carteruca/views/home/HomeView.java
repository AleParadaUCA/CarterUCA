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
import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.views.cartera.CarteraActualView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.common.common;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Role;

@PageTitle("CarterUCA")
@Route(value = "", layout = MainLayout.class)
@RolesAllowed({"Promotor","Solicitante","CIO","OTP" })
public class HomeView extends Composite<VerticalLayout> {

    public HomeView() {
        VerticalLayout container = new VerticalLayout();
        container.addClassName("responsive-container");

        // Agregar los cuadros al contenedor
        container.add(
                common.createSquare("Proyectos", VaadinIcon.FILE_TEXT),
                common.createSquare("Cartera", VaadinIcon.CLIPBOARD)
        );

        // Agregar el contenedor al contenido principal
        getContent().add(container);
    }
}
