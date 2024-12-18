package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.carteruca.services.EmailService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;

@PageTitle("CarterUCA")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    public HomeView(EmailService emailService) {
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

        // Botón para enviar correo
        Button sendEmailButton = new Button("Enviar Correo", event -> {
            emailService.enviarCorreo("carterucaiw@gmail.com", "Asunto del correo", "enviar correos");
            Notification.show("Envío correcto");
        });
        getContent().add(sendEmailButton);

    }
}
