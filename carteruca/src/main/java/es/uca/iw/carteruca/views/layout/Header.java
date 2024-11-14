package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class Header extends Composite<HorizontalLayout> {

    public Header() {
        HorizontalLayout layout = getContent();
        layout.setWidthFull();
        layout.getStyle().setBackground("#384850")
                        .setColor("white")
                        .setPadding("10px");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.addClassName("text-field-white");
        searchField.setWidth("400px");

        layout.add(searchField);
    }
}
