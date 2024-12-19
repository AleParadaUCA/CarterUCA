package es.uca.iw.carteruca.views.registro;

import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.carteruca.views.layout.MainLayout;

@Route(value = "activar", layout = MainLayout.class)
@AnonymousAllowed
public class activar extends Composite<VerticalLayout> {

    public activar() {
        System.out.print(VaadinRequest.getCurrent());
        
        // Obtener el parámetro `token` de la solicitud actual
        Optional<String> tokenOptional = Optional.ofNullable(
            VaadinRequest.getCurrent().getParameter("token")
        );

        if (tokenOptional.isPresent() && !tokenOptional.get().isEmpty()) {
            String token = tokenOptional.get();
            System.out.print(token);
            String result = activateUser(token);
            Notification.show(result);
        } else {
            Notification.show("Token de activación no proporcionado.");
        }
    }

    private String activateUser(String token) {
        // Llamar al controlador para activar el usuario
        return UI.getCurrent().getPage().executeJs("return fetch('/api/usuarios/activar/' + $0).then(response => response.text())", token).toString();
    }
}
