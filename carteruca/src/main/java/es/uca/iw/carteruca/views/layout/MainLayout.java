package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.security.AuthenticatedUser;

@Layout
@AnonymousAllowed
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {

    private AuthenticatedUser authenticatedUser;

    public MainLayout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;


        // Configurar el diseño principal
        VerticalLayout layout = getContent();
        layout.getStyle()
                .set("margin", "0")
                .set("padding", "0")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("min-height", "100vh"); // Altura mínima para ocupar toda la ventana
        layout.setSizeFull(); // Ocupa todo el espacio disponible
        layout.setPadding(false);
        layout.setSpacing(false);

        // Crear y añadir el encabezado y pie de página
        Header header = new Header(authenticatedUser);
        Footer footer = new Footer();
        footer.getElement().getStyle().set("order", "999");

        layout.add(header, footer);
    }
}
