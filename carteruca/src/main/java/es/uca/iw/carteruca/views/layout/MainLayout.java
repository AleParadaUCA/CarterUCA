package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Layout
@AnonymousAllowed
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {

    public MainLayout() {
        //configurar tamaño
        VerticalLayout layout = getContent();
        layout.getStyle().set("margin", "0").set("padding", "0");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getStyle().set("display", "flex")
                    .set("flex-direction", "column")
                    .set("min-height", "100vh");

        // Crear el área de contenido donde se cargarán las vistas
        VerticalLayout contentArea = new VerticalLayout();
        contentArea.setSizeFull();
        contentArea.setPadding(false);
        contentArea.setSpacing(false);
        layout.add(contentArea);

        // Almacena contentArea en el contexto de UI
        ComponentUtil.setData(UI.getCurrent(), "contentArea", contentArea);

        layout.add(new Header(), contentArea, new Footer());

        contentArea.getStyle().set("flex-grow", "1");
    }
}