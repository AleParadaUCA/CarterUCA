package es.uca.iw.carteruca.views.login;

import es.uca.iw.carteruca.models.usuario.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
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
    private LoginForm loginForm = new LoginForm();

    public LoginView(AuthenticatedUser authenticatedUser) {

        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        // Crear el formulario de login

        // Configuración de traducciones para el formulario
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.setHeader(new LoginI18n.Header());
        loginI18n.getHeader().setTitle("CarterUCA");
        setI18n(loginI18n);
        loginForm.setI18n(loginI18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        /*

        //funcion no terminada
        if (authenticatedUser.get().isPresent()) {
            setOpened(false);
            event.forwardTo(HomeRegistradoView.class);
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
        */
    }

}
