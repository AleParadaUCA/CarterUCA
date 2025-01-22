package es.uca.iw.carteruca.views.admin;

import java.util.List;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Rol;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

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
        common.creartituloComposite("Usuarios", this);

        // Configurar la tabla
        configurarGrid();

        getContent().add(common.botones_Admin());
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

        tablaUsuarios.addComponentColumn(usuario -> {
            Button editar = new Button(VaadinIcon.EDIT.create(), click -> openEditDialog(usuario));
            editar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editar.getElement().setAttribute("aria-label", "Editar");
            return editar;
        }).setHeader("Editar");

        // Añadir columna para eliminar usuarios
        tablaUsuarios.addComponentColumn(usuario -> {
            Button eliminar = new Button(VaadinIcon.TRASH.create(), click -> mostrarDialogoEliminar(usuario));
            eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            eliminar.getElement().setAttribute("aria-label", "Eliminar");
            return eliminar;
        }).setHeader("Eliminar");


        // Filtrar usuarios con roles distintos a Admin
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);

        ListDataProvider<Usuario> dataProvider = new ListDataProvider<>(usuarios);
        tablaUsuarios.setDataProvider(dataProvider);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("50%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener(event -> {
            String searchTerm = event.getValue().trim().toLowerCase();
            dataProvider.setFilter(usuario -> {
                String username = usuario.getUsername().toLowerCase();
                String nombre = usuario.getNombre().toLowerCase();
                String email = usuario.getEmail().toLowerCase();
                String rol = usuario.getRol().name().toLowerCase();
                return username.contains(searchTerm) || nombre.contains(searchTerm) || email.contains(searchTerm) || rol.contains(searchTerm);
            });
        });

        // Añadir componentes al layout
        getContent().add(searchField, tablaUsuarios);
    }

    private void mostrarDialogoConfirmacion(Usuario usuario, Rol nuevoRol) {
        Dialog dialogo = new Dialog();
        VerticalLayout contenido = new VerticalLayout();

        // Agregar solo una vez el contenido al dialogo
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

        dialogo.add(contenido); // Añadir el contenido solo una vez
        dialogo.open();
    }

    private void recargarTabla() {
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);
    }



    private void openEditDialog(Usuario usuario) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Editar Usuario");
        FormLayout contenido = new FormLayout();

        // Campos de edición
        TextField nombreFieldDialog = new TextField("Nombre");
        nombreFieldDialog.setValue(usuario.getNombre());

        TextField apellidosFieldDialog = new TextField("Apellidos");
        apellidosFieldDialog.setValue(usuario.getApellidos());

        TextField emailFieldDialog = new TextField("Email");
        emailFieldDialog.setValue(usuario.getEmail());

        TextField usernameFieldDialog = new TextField("Usuario");
        usernameFieldDialog.setValue(usuario.getUsername());

        contenido.add(nombreFieldDialog, apellidosFieldDialog, emailFieldDialog, usernameFieldDialog);

        Button saveButton = new Button("Guardar",  event -> {
            String result = usuarioService.updateUser(
                    usuario.getId(),
                    nombreFieldDialog.getValue(),
                    apellidosFieldDialog.getValue(),
                    emailFieldDialog.getValue(),
                    usernameFieldDialog.getValue()
            );

            if (result.equals("Usuario actualizado correctamente.")) {
                // Actualizamos currentUser con los nuevos datos
                usuario.setNombre(nombreFieldDialog.getValue());
                usuario.setApellidos(apellidosFieldDialog.getValue());
                usuario.setEmail(emailFieldDialog.getValue());
                usuario.setUsername(usernameFieldDialog.getValue());

                // Mostrar notificación
                common.showSuccessNotification(result);
                updateGrid();
                dialogo.close();
            } else {
                // Notificación de error
                common.showErrorNotification("Error: " + result);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // Botón para cancelar
        Button cancelButton = new Button("Cancelar", event -> dialogo.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Alinear los botones al final
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setAlignItems(FlexComponent.Alignment.END);

        Button volverButton = new Button("Volver", event -> dialogo.close());
        volverButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botonesLayout = new HorizontalLayout(volverButton, buttonLayout);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Justificar "Volver" a la izquierda y los demás a la derecha
        botonesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente


        // Contenedor principal del diálogo
        VerticalLayout dialogLayout = new VerticalLayout(contenido, botonesLayout);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        dialogo.add(dialogLayout); // Añadir solo una vez
        dialogo.open();
    }

    private void mostrarDialogoEliminar(Usuario usuario) {
        Dialog dialogo = new Dialog();
        VerticalLayout contenido = new VerticalLayout();

        contenido.add("¿Estás seguro de que deseas eliminar al usuario " + usuario.getUsername() + "? Esta acción no se puede deshacer.");

        Button botonSi = new Button("Sí", e -> {
            usuarioService.deleteUser(usuario.getId()); // Eliminar usuario
            dialogo.close();
            common.showSuccessNotification("Usuario eliminado con éxito.");
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


    private void updateGrid() {
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);
    }
}


