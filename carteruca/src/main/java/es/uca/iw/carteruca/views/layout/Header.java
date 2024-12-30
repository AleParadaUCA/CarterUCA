package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.proyecto.ProyectoAllView;

public class Header extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;

    public Header(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        VerticalLayout header = getContent();
        header.setWidthFull();
        header.addClassName("header");

        header.add(createTopLayout(), createBottomLayout());
    }

    private HorizontalLayout createTopLayout() {
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        topLayout.add(createLogo(), createIconsLayout());
        return topLayout;
    }

    private Anchor createLogo() {

    // Enlace principal con clase personalizada
    Anchor link = new Anchor("https://www.uca.es/", "Universidad");
    link.addClassName("header-brand-link");

    // Subtítulo
    Span smallText = new Span("de");
    smallText.addClassName("header-brand-small");

    // Texto final
    Span finalText = new Span("Cádiz");

    // Añadir spans al enlace
    link.add(smallText, finalText);

    return link;
}

    private HorizontalLayout createIconsLayout() {
        HorizontalLayout iconsLayout = new HorizontalLayout();
        iconsLayout.setAlignItems(FlexComponent.Alignment.END);
        iconsLayout.setSpacing(false);
        iconsLayout.setPadding(false);
        

        if (authenticatedUser.get().isPresent()) {

            // Obtén el usuario autenticado
            String userName = authenticatedUser.get().get().getNombre();
            String userSurname = authenticatedUser.get().get().getApellidos();

            // Crea el Avatar usando el nombre y apellido del usuario
            Avatar avatar = new Avatar(userName + " " + userSurname);

            // Genera el color único basado en el nombre del usuario
            String uniqueColor = common.getColorFromName(userName);

            // Establece el color de fondo del avatar basado en el hash del nombre
            avatar.getElement().getStyle().set("background-color", uniqueColor);

            // Establece el tamaño del avatar (puedes ajustarlo según tus necesidades)
            avatar.getElement().getStyle().set("width", "30px").set("height", "30px");

            // Agrega el avatar a un MenuBar para mostrar el menú desplegable
            MenuBar userMenuBar = new MenuBar();
            MenuItem userMenuItem = userMenuBar.addItem(avatar);
            SubMenu userSubMenu = userMenuItem.getSubMenu();
            userMenuBar.getStyle()
                    .set("margin", "0")   // Elimina márgenes
                    .set("padding", "0"); // Elimina padding

            // Agregar opciones al menú (Perfil, Cerrar sesión)
            userSubMenu.addItem("Perfil", e -> UI.getCurrent().navigate("/perfil"));
            userSubMenu.addItem("Cerrar sesión", e -> authenticatedUser.logout());

            iconsLayout.add(userMenuBar);
        } else {
            // Si el usuario no está autenticado, muestra un icono de usuario
            Icon userIcon = new Icon(VaadinIcon.USER);
            userIcon.getStyle()
                    .set("font-size", "20px")
                    .set("cursor", "pointer")
                    .set("color", "white");

            MenuBar userMenuBar = new MenuBar();
            MenuItem userMenuItem = userMenuBar.addItem(userIcon);
            SubMenu userSubMenu = userMenuItem.getSubMenu();

            userSubMenu.addItem("Iniciar sesión", e -> UI.getCurrent().navigate("/login"));
            userSubMenu.addItem("Registrarse", e -> UI.getCurrent().navigate("registro"));

            iconsLayout.add(userMenuBar);
        }

        return iconsLayout;
    }


    private HorizontalLayout createBottomLayout() {
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        bottomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        bottomLayout.add(createMenuBar(), createSearchField());
        bottomLayout.add(createMenuBarMovil(), createSearchFieldMovil());
        return bottomLayout;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("rounded-menu-bar");
        menuBar.addClassName("full-menu");

        String redirigir;
        if (authenticatedUser.get().isPresent()) {
            if (authenticatedUser.get().get().getRol() == Rol.Admin){
                redirigir ="/home-admin";
            }else {
                redirigir ="/home";
            }
        } else {
            redirigir = "";
        }

        menuBar.addItem(VaadinIcon.HOME.create(), e -> UI.getCurrent().navigate(redirigir))
                .getElement().getClassList().add("menu-item");

        menuBar.addItem("Proyectos", e -> UI.getCurrent().navigate(ProyectoAllView.class))
                .getElement().getClassList().add("menu-item");

        menuBar.addItem("Cartera", e -> UI.getCurrent().navigate("/cartera"))
                .getElement().getClassList().add("menu-item");
        
        return menuBar;
    }

    private HorizontalLayout createMenuBarMovil() {
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setSpacing(false);
        menuLayout.setPadding(false);
        menuLayout.addClassName("compact-menu");

        // Crear el icono de "hamburguesa"
        Icon menuIcon = VaadinIcon.MENU.create();
        menuIcon.setClassName("menu-item");
        menuIcon.getStyle()
                .set("font-size", "24px")
                .set("cursor", "pointer")
                .set("color", "white");

        // Crear el popup para el menú
        Dialog menuDialog = new Dialog();
        menuDialog.setWidth("80%");
        menuDialog.setCloseOnOutsideClick(true);

        // Crear el menú dentro del popup
        VerticalLayout menuItemsLayout = new VerticalLayout();
        menuItemsLayout.addClassName("menu-popup");

        menuItemsLayout.add(new Anchor("/", "Proyectos"));
        if (authenticatedUser.get().isPresent()) {
            if (authenticatedUser.get().get().getRol() == Rol.Admin) {
                menuItemsLayout.add(new Anchor("/home-admin", "Home-Admin"));
            } else {
                menuItemsLayout.add(new Anchor("/home", "Home"));
            }
        }
        menuItemsLayout.add(new Anchor("/cartera", "Cartera"));

        // Añadir los elementos al diálogo
        menuDialog.add(menuItemsLayout);

        // Abrir el popup al hacer clic en el icono
        menuIcon.addClickListener(e -> menuDialog.open());

        // Añadir el icono al layout
        menuLayout.add(menuIcon);
        menuLayout.add(menuDialog);

        return menuLayout;
    }


    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("20%");
        searchField.addClassName("search-color");
        searchField.addClassName("full-menu");

        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.addClassName("menu-item");
        searchField.setSuffixComponent(searchIcon);

        searchIcon.addClickListener(e -> {
            String searchText = searchField.getValue();
            if (!searchText.isEmpty()) {
                UI.getCurrent().navigate("/proyectos?search=" + searchText);
            } else {
                UI.getCurrent().navigate("/proyectos");
            }
        });
        return searchField;
    }

    private HorizontalLayout createSearchFieldMovil() {
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setSpacing(false);
        searchLayout.setPadding(false);
        searchLayout.addClassName("compact-menu");

        // Crear el icono de búsqueda
        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.setClassName("menu-item");
        searchIcon.getStyle()
                .set("font-size", "24px")
                .set("cursor", "pointer")
                .set("color", "white");

        // Crear el popup para la barra de búsqueda
        Dialog searchDialog = new Dialog();
        searchDialog.setWidth("80%");
        searchDialog.setCloseOnOutsideClick(true);

        // Crear el campo de búsqueda dentro del popup
        TextField searchField = new TextField("Buscar");
        searchField.setWidthFull();
        searchField.setPlaceholder("Introduce tu búsqueda...");
        searchField.addClassName("search-color");

        // Añadir la barra de búsqueda al diálogo
        searchDialog.add(searchField);

        // Abrir el popup al hacer clic en el icono
        searchIcon.addClickListener(e -> searchDialog.open());

        // Añadir el icono al layout
        searchLayout.add(searchIcon);
        searchLayout.add(searchDialog);

        return searchLayout;
    }

}
