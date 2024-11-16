package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class Header extends Composite<VerticalLayout> {

    public Header() {
        VerticalLayout header = getContent();
        header.setWidthFull();
        header.getStyle()
                .setBackground("#384850")
                .setColor("white")
                .setPadding("10px");

        // **Parte superior: Layout horizontal**
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Para separar el logo de los íconos
        //topLayout.getStyle().set("margin-bottom", "5px");

        // **Parte superior izquierda: Logo**
        Anchor homeLink = new Anchor("https://www.uca.es", new Image("images/Universidad_de_cadiz_2.0.png", "Foto UCA"));
        homeLink.getElement().getStyle()
            .set("width", "100px") // Ajustado el ancho del logo
            .set("height", "auto")
            .setMargin("0")
            .setPadding("0");
        topLayout.add(homeLink);

        // **Parte superior derecha: Íconos (Idioma, Notificaciones, Usuario)**
        HorizontalLayout iconsLayout = new HorizontalLayout();
        iconsLayout.setAlignItems(FlexComponent.Alignment.END);
        Icon languageIcon = new Icon(VaadinIcon.GLOBE);
            languageIcon.getStyle()
                    .set("cursor", "pointer")
                    .set("margin-bottom", "3px");
        iconsLayout.add(languageIcon);

        if (isUserLoggedIn()) {

            MenuBar userMenuBar = new MenuBar();
            userMenuBar.getStyle()
                    .set("align-self", "flex-end")
                    .set("padding", "0")
                    .set("margin", "0");
            userMenuBar.setWidth("auto"); // Asegúrate de que el menú no tenga ancho fijo

            // Ícono de notificaciones
            Icon bellIcon = new Icon(VaadinIcon.BELL);
            bellIcon.getStyle()
                    .set("cursor", "pointer")
                    .set("margin-bottom", "3px");
            //bellIcon.addClickListener(e -> UI.getCurrent().navigate("/notificaciones"));

            // Ícono de usuario
            Icon userIcon = new Icon(VaadinIcon.USER);
            userIcon.getStyle()
                    .set("font-size", "20px")
                    .set("cursor", "pointer")
                    .set("color", "white")
                    .set("position", "absolute")  // Posicionarlo dentro de su contenedor
                    .set("bottom", "0")
                    .set("left", "0");

            MenuItem userMenuItem = userMenuBar.addItem(userIcon);
            SubMenu userSubMenu = userMenuItem.getSubMenu();
            //userSubMenu.addItem("Ver Perfil", e -> UI.getCurrent().navigate("/profile"));
            userSubMenu.addItem("Cerrar Sesión", e -> {
                // Lógica de cierre de sesión
            });

            iconsLayout.add(bellIcon, userMenuBar);
        }
        topLayout.add(iconsLayout);

        // **Parte inferior: Layout vertical**
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        bottomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // **Parte inferior izquierda: Menú**
        MenuBar menuBar = new MenuBar();
        setMenuItems(menuBar);
        bottomLayout.add(menuBar);

        // **Parte inferior derecha: Buscador**
        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("20%");
        searchField.getStyle()
                .set("--vaadin-input-field-placeholder-color", "white")
                .setBackground("#455a64")
                .set("margin-right", "1%")
                .set("color", "white");

        // Añadir ícono de búsqueda
        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.getStyle().set("color", "white");
        searchField.setSuffixComponent(searchIcon);

        bottomLayout.add(searchField);

        // **Línea separadora entre la parte superior e inferior**
        Div separator = new Div();
        separator.getStyle()
                .set("border-top", "1px solid #ccc")
                .set("width", "99%")
                .set("margin-top", "5px");

        // Añadir las dos partes al layout principal
        header.add(topLayout, separator, bottomLayout);
    }

    private void setMenuItems(MenuBar menuBar) {
        menuBar.addItem("Home", e -> {/* Navegación */}).getElement().getClassList().add("menu-item");
        menuBar.addItem("Proyectos", e -> {/* Navegación */}).getElement().getClassList().add("menu-item");
    }

    private boolean isUserLoggedIn() {
        // Aquí deberías implementar la lógica para verificar si el usuario está autenticado
        return true; // Simulación de que el usuario está autenticado
    }
}
