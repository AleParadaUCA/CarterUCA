package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Footer extends Composite<HorizontalLayout> {

    public Footer() {
        HorizontalLayout layout = getContent();
        layout.add(/* aquí añade los componentes del footer, como texto de copyright */);
        layout.setWidthFull();
        layout.getStyle().setBackground("#384850")
                        .setColor("white")
                        .setPadding("10px");
        layout.add(new Span("Footer"));
    }
}
