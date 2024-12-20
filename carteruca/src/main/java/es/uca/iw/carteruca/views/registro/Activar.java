package es.uca.iw.carteruca.views.registro;

import es.uca.iw.carteruca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Activate User")
@Route(value = "useractivation")
@Component
@Scope("prototype")
@AnonymousAllowed
public class Activar extends VerticalLayout {

    private final UsuarioService usuarioService;
    private final H1 title;
    private final TextField email;
    private final TextField secretCode;
    private final Button activate;
    private final H4 status;

    @Autowired
    public Activar(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        title = new H1("Activate User");
        email = new TextField("Your email");
        email.setId("email");

        secretCode = new TextField("Your secret code");
        secretCode.setId("secretCode");

        status = new H4();
        status.setId("status");

        activate = new Button("Activate");
        activate.setId("activate");

        status.setVisible(false);

        setMargin(true);

        add(title, new HorizontalLayout(email, secretCode), activate, status);

        activate.addClickListener(e -> onActivateButtonClick());
    }

    private void onActivateButtonClick() {
        status.setVisible(true);

        if (usuarioService.activateUser(email.getValue(), secretCode.getValue())) {
            status.setText("Congrats. The user has been activated");
        } else {
            status.setText("Ups. The user could not be activated");
        }
    }
}