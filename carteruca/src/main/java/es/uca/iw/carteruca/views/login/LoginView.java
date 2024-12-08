package es.uca.iw.carteruca.views.login;

import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.registro.RegistroView;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "/login", layout = MainLayout.class)
public class LoginView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final LoginForm loginForm;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        // Crear el formulario de login
        loginForm = new LoginForm();

        // Configurar el endpoint para las credenciales
        loginForm.setAction("login");

        // Configurar internacionalización
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.getForm().setTitle("Iniciar sesión");
        loginI18n.getForm().setUsername("Usuario");
        loginI18n.getForm().setPassword("Contraseña");
        loginI18n.getForm().setSubmit("Iniciar sesión");
        loginI18n.getForm().setForgotPassword("¿Olvidó su contraseña?");
        loginForm.setI18n(loginI18n);

        // Diseño principal
        VerticalLayout layout = new VerticalLayout(loginForm);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Botón adicional (registrarse)
        Button registroButton = new Button("Registrarse", e -> UI.getCurrent().navigate(RegistroView.class));
        HorizontalLayout botonLayout = new HorizontalLayout(registroButton);
        botonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        botonLayout.setWidthFull();

        // Añadir componentes al contenido principal
        getContent().add(layout, botonLayout);
        getContent().setSpacing(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true); // Muestra mensaje de error si la autenticación falla
        }

        Optional<Usuario> optionalUser = authenticatedUser.get();
        if (optionalUser.isPresent()) {
            event.forwardTo("home");
        }
    }
}
