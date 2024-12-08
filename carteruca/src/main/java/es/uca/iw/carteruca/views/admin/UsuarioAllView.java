package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.carteruca.services.UsuarioService;

import java.util.List;

@PageTitle("Usuarios")
@Route(value = "/home-admin/usuario", layout = MainLayout.class)
@RolesAllowed("Admin")
public class UsuarioAllView extends Composite<VerticalLayout> {

    private final Grid<Usuario> tablaUsuarios = new Grid<>(Usuario.class);
    private final UsuarioService usuarioService;

    public UsuarioAllView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        configurarVista();
    }

    private void configurarVista() {
        // Configurar el título
        common.creartitulo("Usuarios", this);

        // Configurar la tabla
        configurarGrid();

        // Añadir componentes al layout
        getContent().add(tablaUsuarios);

        addBotones();
    }

    private void configurarGrid() {
        tablaUsuarios.removeAllColumns();

        // Añadir columnas
        tablaUsuarios.addColumn(Usuario::getUsername).setHeader("Usuario").setSortable(true);
        tablaUsuarios.addColumn(Usuario::getNombre).setHeader("Nombre").setSortable(true);
        tablaUsuarios.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true);

        // Columna para cambiar el rol
        tablaUsuarios.addComponentColumn(usuario -> {
            Select<Rol> rolSelect = new Select<>();
            rolSelect.setItems(usuarioService.getRolesExcludingAdmin());
            rolSelect.setValue(usuario.getRol());
            rolSelect.addValueChangeListener(event -> {
                Rol nuevoRol = event.getValue();
                if (nuevoRol != null) {
                    mostrarDialogoConfirmacion(usuario, nuevoRol);
                }
            });
            return rolSelect;
        }).setHeader("Rol");

        // Filtrar usuarios con roles distintos a Admin
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);
    }

    private void mostrarDialogoConfirmacion(Usuario usuario, Rol nuevoRol) {
        Dialog dialogo = new Dialog();

        // Texto de confirmación
        VerticalLayout contenido = new VerticalLayout();
        contenido.add("¿Estás seguro de que deseas cambiar el rol del usuario " + usuario.getUsername() + " a " + nuevoRol + "?");

        // Botones de acción
        Button botonSi = new Button("Sí", e -> {
            usuarioService.updateUsuarioRol(usuario.getId(), nuevoRol); // Actualizar rol
            dialogo.close();
            Notification.show("Rol actualizado con éxito.", 2000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            recargarTabla(); // Actualizar la tabla
        });

        Button botonNo = new Button("No", e -> dialogo.close());

        // Estilo de los botones
        botonSi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        botonNo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout botones = new HorizontalLayout(botonSi, botonNo);
        contenido.add(botones);
        contenido.setAlignItems(FlexComponent.Alignment.CENTER);

        dialogo.add(contenido);
        dialogo.open();
    }

    private void recargarTabla() {
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);
    }


    private void addBotones() {
            // Botón "Volver" redirigiendo a HomeAdminView
            RouterLink link_volver = new RouterLink();
            Button volver = new Button("Volver");
            volver.addClickListener(e -> UI.getCurrent().navigate(HomeAdminView.class));
            volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            link_volver.add(volver);
            volver.getElement().setAttribute("aria-label", "Volver");

            link_volver.getElement().getStyle().set("margin-top", "8px");
            link_volver.getElement().getStyle().set("text-decoration", "none");

            // Layout para el botón de "Volver"
            HorizontalLayout botones = new HorizontalLayout(link_volver);
            botones.setWidthFull(); // Usar todo el ancho disponible
            botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Alinear el botón al final

            // Añadir el botón al contenido
            getContent().add(botones);
    }
}

