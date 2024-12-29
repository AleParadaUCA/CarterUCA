package es.uca.iw.carteruca.views.registro;

import com.vaadin.flow.component.button.ButtonVariant;
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
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);

        title = new H1("Activar Usuario");
        email = new TextField("Correo electronico");
        email.setId("email");

        secretCode = new TextField("Código secreto");
        secretCode.setId("secretCode");

        status = new H4();
        status.setId("status");

        activate = new Button("Activar");
        activate.setId("activate");
        activate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        status.setVisible(false);

        setMargin(true);
        HorizontalLayout formulario = new HorizontalLayout();
        formulario.setSpacing(true);
        formulario.setAlignItems(Alignment.CENTER);
        formulario.setJustifyContentMode(JustifyContentMode.CENTER);
        formulario.add(email, secretCode);

        HorizontalLayout buttons = new HorizontalLayout(activate, status);
        buttons.setAlignItems(Alignment.CENTER);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);
        buttons.setWidthFull();

        layout.add(formulario,buttons);

        add(title, layout);

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