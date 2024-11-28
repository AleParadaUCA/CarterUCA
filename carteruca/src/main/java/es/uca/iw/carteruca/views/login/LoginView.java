package es.uca.iw.carteruca.views.login;

//import es.uca.iw.carteruca.security.AuthenticatedUser;
//// import es.uca.iw.carteruca.views.home.HomeRegistradoView;
//// import es.uca.iw.carteruca.views.registro.RegistroView;
//// import com.vaadin.flow.component.UI;
//// import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.login.LoginForm;
//import com.vaadin.flow.component.login.LoginI18n;
//// import com.vaadin.flow.component.orderedlayout.FlexComponent;
//// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.login.LoginOverlay;
//import com.vaadin.flow.router.BeforeEnterEvent;
//import com.vaadin.flow.router.BeforeEnterObserver;
//import com.vaadin.flow.router.internal.RouteUtil;
//import com.vaadin.flow.server.VaadinService;
//
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.server.auth.AnonymousAllowed;
//@AnonymousAllowed
//@PageTitle("Login")
//@Route("/login")
//
//public class LoginView extends LoginOverlay implements BeforeEnterObserver {
//
//    private final AuthenticatedUser authenticatedUser;
//    private LoginForm loginForm = new LoginForm();
//
//    public LoginView(AuthenticatedUser authenticatedUser) {
//
//        this.authenticatedUser = authenticatedUser;
//        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
//        // Crear el formulario de login
//
//        // Configuración de traducciones para el formulario
//        LoginI18n loginI18n = LoginI18n.createDefault();
//        loginI18n.setHeader(new LoginI18n.Header());
//        loginI18n.getHeader().setTitle("CarterUCA");
//        setI18n(loginI18n);
//        loginForm.setI18n(loginI18n);
//
//        setForgotPasswordButtonVisible(false);
//        setOpened(true);
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        /*
//
//        //funcion no terminada
//        if (authenticatedUser.get().isPresent()) {
//            setOpened(false);
//            event.forwardTo(HomeRegistradoView.class);
//        }
//
//        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
//        */
//    }
//
//}

//login temporal

import com.vaadin.flow.component.UI;
import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.Optional;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Login");
        i18n.getHeader().setDescription("Login using user/user or admin/admin");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);

        getElement().setAttribute("aria-label", "Login");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Usuario> optionalUser = authenticatedUser.get();
        System.out.println("beforeEnter");

        if (optionalUser.isPresent()) {
            Rol userRole = optionalUser.get().getRol();

            if (userRole == Rol.Admin) {
                UI.getCurrent().navigate("home-admin");
            } else {
                UI.getCurrent().navigate("home");
            }
            setOpened(false);
        } else {
            System.out.println("No authenticated user found");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

}

