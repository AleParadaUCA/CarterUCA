package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
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

import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.views.common.common;

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
        Image logoImage = new Image("layout/Universidad_de_cadiz_2.0.png", "Foto UCA");
        logoImage.getStyle()
                .set("max-width", "75%")
                .set("height", "auto")
                .set("margin-right", "auto");

        return new Anchor("https://www.uca.es", logoImage);
    }

    private HorizontalLayout createIconsLayout() {
        HorizontalLayout iconsLayout = new HorizontalLayout();
        iconsLayout.setAlignItems(FlexComponent.Alignment.END);
        iconsLayout.setSpacing(false);
        iconsLayout.setPadding(false);

        Icon languageIcon = new Icon(VaadinIcon.GLOBE);
        languageIcon.setClassName("icon-button");
        iconsLayout.add(languageIcon);

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
        return bottomLayout;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("rounded-menu-bar");

        menuBar.addItem("Proyectos", e -> UI.getCurrent().navigate("/"))
                .getElement().getClassList().add("menu-item");
        //menuBar.addItem("Registrarse", e -> UI.getCurrent().navigate(RegistroView.class));

        if (authenticatedUser.get().isPresent()) {
            if (authenticatedUser.get().get().getRol() == Rol.Admin){
                menuBar.addItem("Home-Admin", e -> UI.getCurrent().navigate("/home-admin"))
                        .getElement().getClassList().add("menu-item");
            }else {
                menuBar.addItem("Home", e -> UI.getCurrent().navigate("/home"))
                        .getElement().getClassList().add("menu-item");
            }
        }

        menuBar.addItem("Cartera", e -> UI.getCurrent().navigate("/cartera"))
                .getElement().getClassList().add("menu-item");

        return menuBar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("20%");
        searchField.addClassName("search-color");

        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.addClassName("menu-item");
        searchField.setSuffixComponent(searchIcon);

        return searchField;
    }
}
