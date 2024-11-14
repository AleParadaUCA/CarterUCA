package es.uca.iw.carteruca.views.login;

import es.uca.iw.carteruca.models.usuario.security.AuthenticatedUser;
import es.uca.iw.carteruca.views.home.HomeRegistradoView;
import es.uca.iw.carteruca.views.registro.RegistroView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
@AnonymousAllowed
@PageTitle("Login")
@Route("/login")

public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {

        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        // Crear el formulario de login
        LoginForm loginForm = new LoginForm();

        // Configuración de traducciones para el formulario
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.setHeader(new LoginI18n.Header());
        loginI18n.getForm().setTitle("Iniciar sesión");
        loginI18n.getForm().setUsername("Usuario");
        loginI18n.getForm().setPassword("Contraseña");
        loginI18n.getForm().setSubmit("Iniciar sesión");
        loginI18n.getForm().setForgotPassword("¿Olvidó su contraseña?");
        loginForm.setI18n(loginI18n);

        // Crear botón de registro
        Button registerButton = new Button("Registrarse");

        VerticalLayout mainContainer = new VerticalLayout(loginForm, registerButton); // Añade el botón aquí
        mainContainer.setAlignItems(FlexComponent.Alignment.CENTER); // Centra ambos elementos
        mainContainer.setSpacing(true); // Añade espacio entre el formulario y el botón

        setForgotPasswordButtonVisible(false);
        setOpened(true);

        // Acción del botón de registro
        registerButton.addClickListener(e -> UI.getCurrent().navigate(RegistroView.class)); // Navega a la vista de registro

        //Anidar los botones

        registerButton.addClickListener( e -> UI.getCurrent().navigate(RegistroView.class)); // Navega a la vista de registro
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        /*
        if (authenticatedUser.get().isPresent()) {
            setOpened(false);
            event.forwardTo(HomeRegistradoView.class);
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
        */
    }

}
