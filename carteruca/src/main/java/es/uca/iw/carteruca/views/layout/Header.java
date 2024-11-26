package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import es.uca.iw.carteruca.views.login.LoginView;
import es.uca.iw.carteruca.views.registro.RegistroView;

public class Header extends Composite<VerticalLayout> {

    public Header(AuthenticatedUser authenticatedUser) {
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
        //topLayout.getStyle().set("border-bottom", "1px solid #ccc");

        // **Parte superior izquierda: Logo**
    //    Anchor homeLink = new Anchor("https://www.uca.es", new Image("images/Universidad_de_cadiz_2.0.png", "Foto UCA"));
        Anchor homeLink = new Anchor("https://www.uca.es", new Image("layout/Universidad_de_cadiz_2.0.png", "Foto UCA"));
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
            bellIcon.addClickListener(e -> UI.getCurrent().navigate("/notificaciones"));

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
                authenticatedUser.logout();
            });

            iconsLayout.add(bellIcon, userMenuBar);
        }
        topLayout.add(iconsLayout);

        // **Parte inferior: Layout horizontal**
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        bottomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // **Parte inferior izquierda: Menú**
        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("rounded-menu-bar");
        setMenuItems(menuBar);
        bottomLayout.add(menuBar);

        // **Parte inferior derecha: Buscador**
        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("20%");
        searchField.addClassName("search-color");

        // Añadir ícono de búsqueda
        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.getStyle().set("color", "#384850");
        searchField.setSuffixComponent(searchIcon);

        bottomLayout.add(searchField);

        // Añadir las dos partes al layout principal
        header.add(topLayout, bottomLayout);

    }

    private void setMenuItems(MenuBar menuBar) {
        menuBar.addItem("Proyectos", e -> {
            // Redirigir a "/"
            UI.getCurrent().navigate("/");
        }).getElement().getClassList().add("menu-item");


        menuBar.addItem("Iniciar Session", e -> UI.getCurrent().navigate(LoginView.class))
                .getElement().getClassList().add("menu-item");

        menuBar.addItem("Registrarse", e -> UI.getCurrent().navigate(RegistroView.class))
                .getElement().getClassList().add("menu-item");

        if (isUserLoggedIn()) {
            menuBar.addItem("Home", e -> {
                // Redirigir a "/home"
                UI.getCurrent().navigate("/home");
            }).getElement().getClassList().add("menu-item");
        }
    }

    private boolean isUserLoggedIn() {
        // Aquí deberías implementar la lógica para verificar si el usuario está autenticado
        return true; // Simulación de que el usuario está autenticado
    }
}
