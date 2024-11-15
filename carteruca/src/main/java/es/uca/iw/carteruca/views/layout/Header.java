package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
public class Header extends Composite<HorizontalLayout> {

    public Header() {
        HorizontalLayout header = getContent();

        header.setWidthFull();
        header.getStyle()
                .setBackground("#384850")
//                .setColor("white")
                .setPadding("10px");

        //Barra de Búsqueda
        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("20%");
        searchField.getStyle()
                .set("--vaadin-input-field-placeholder-color", "white")
                .setBackground("#455a64")
                .set("color", "white");

        // Añadir un ícono de lupa blanco
        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.getStyle().set("color", "white");
        searchField.setSuffixComponent(searchIcon);


        header.add(searchField);
        header.add(new Span("header"));
    }
}
