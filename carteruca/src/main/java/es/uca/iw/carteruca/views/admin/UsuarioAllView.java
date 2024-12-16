package es.uca.iw.carteruca.views.admin;

import java.util.List;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
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
        common.creartitulo("Usuarios", this);

        // Configurar la tabla
        configurarGrid();

        // Añadir componentes al layout
        getContent().add(tablaUsuarios);
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

        // Filtrar usuarios con roles distintos a Admin
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);
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

        // Botón para cancelar
        Button cancelButton = new Button("Cancelar", event -> dialogo.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Alinear los botones al final
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setAlignItems(FlexComponent.Alignment.END);

        // Contenedor principal del diálogo
        VerticalLayout dialogLayout = new VerticalLayout(contenido, buttonLayout);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        dialogo.add(dialogLayout); // Añadir solo una vez
        dialogo.open();
    }

    private void updateGrid() {
        List<Usuario> usuarios = usuarioService.findAllUsuariosExcludingAdmin();
        tablaUsuarios.setItems(usuarios);
    }
}


