package es.uca.iw.carteruca.views.perfil;

import com.vaadin.flow.component.html.Span;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Perfil")
@Route(value = "/perfil", layout = MainLayout.class)
@PermitAll
public class PerfilView extends Composite<VerticalLayout> {

    private final UsuarioService usuarioService;
    private final AuthenticatedUser authenticatedUser;
    private final Usuario currentUser;

    // Campos de la vista principal
    private TextField nombreField;
    private TextField apellidosField;
    private TextField emailField;
    private TextField usernameField;
    private TextField rolField;

    @Autowired
    public PerfilView(AuthenticatedUser authenticatedUser, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.authenticatedUser = authenticatedUser;
        this.currentUser = authenticatedUser.get().get();

        crearPerfilForm();
        update_password();
        BotonesConfiguracion();
        getContent().add(common.boton_dinamico(currentUser));
    }

    private TextField LecturaTextField(String label, String value) {
        TextField textField = new TextField(label);
        textField.setValue(value);
        textField.setReadOnly(true);
        return textField;
    }

    private void crearPerfilForm() {

        H2 header = new H2("Datos Personales");

        Avatar avatar = new Avatar(currentUser.getNombre() + " " + currentUser.getApellidos());
        String uniqueColor = common.getColorFromName(currentUser.getNombre());
        avatar.getElement().getStyle().set("background-color", uniqueColor);
        avatar.getElement().getStyle().set("width", "100px").set("height", "100px");

        HorizontalLayout headerLayout = new HorizontalLayout(avatar, header);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(headerLayout);

        FormLayout formLayout = new FormLayout();

        // Crear los campos de la vista principal
        nombreField = LecturaTextField("Nombre", currentUser.getNombre());
        apellidosField = LecturaTextField("Apellidos", currentUser.getApellidos());
        emailField = LecturaTextField("Email", currentUser.getEmail());
        usernameField = LecturaTextField("Usuario", currentUser.getUsername());
        rolField = LecturaTextField("Rol", currentUser.getRol().name());

        formLayout.add(nombreField, apellidosField, emailField, usernameField, rolField);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        getContent().add(formLayout);
    }


    private void openEditarDialog() {
        Dialog editDialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos de edición
        TextField nombreFieldDialog = new TextField("Nombre");
        nombreFieldDialog.setValue(currentUser.getNombre());

        TextField apellidosFieldDialog = new TextField("Apellidos");
        apellidosFieldDialog.setValue(currentUser.getApellidos());

        TextField emailFieldDialog = new TextField("Email");
        emailFieldDialog.setValue(currentUser.getEmail());

        TextField usernameFieldDialog = new TextField("Usuario");
        usernameFieldDialog.setValue(currentUser.getUsername());


        formLayout.add(nombreFieldDialog, apellidosFieldDialog, emailFieldDialog, usernameFieldDialog);

        // Botón para guardar los cambios
        Button saveButton = new Button("Guardar", event -> {
            String result = usuarioService.updateUser(
                    currentUser.getId(),
                    nombreFieldDialog.getValue(),
                    apellidosFieldDialog.getValue(),
                    emailFieldDialog.getValue(),
                    usernameFieldDialog.getValue()
            );


            if (result.equals("Usuario actualizado correctamente.")) {
                // Actualizamos currentUser con los nuevos datos
                currentUser.setNombre(nombreFieldDialog.getValue());
                currentUser.setApellidos(apellidosFieldDialog.getValue());
                currentUser.setEmail(emailFieldDialog.getValue());
                currentUser.setUsername(usernameFieldDialog.getValue());

                // **Actualizamos los campos de la vista principal directamente**
                nombreField.setValue(currentUser.getNombre());
                apellidosField.setValue(currentUser.getApellidos());
                emailField.setValue(currentUser.getEmail());
                usernameField.setValue(currentUser.getUsername());
                rolField.setValue(currentUser.getRol().name());

                // Mostrar notificación
                common.showSuccessNotification(result);
                editDialog.close();
            } else {
                // Notificación de error
                common.showErrorNotification("Error: " + result);
            }
        });

        // Botón para cancelar
        Button cancelButton = new Button("Cancelar", event -> editDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Alinear los botones al final
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setAlignItems(FlexComponent.Alignment.END);

        // Contenedor principal del diálogo
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonLayout);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        // Añadir diseño al diálogo
        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void openEliminarDialog() {
        Dialog eliminarDialog = new Dialog();

        // Título de la confirmación
        Span titulo = new Span("¿Estás seguro de querer eliminar la cuenta?");


        // Botones de acción
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminarButton.addClickListener(event -> {
            // Llamar al servicio para eliminar el usuario
            usuarioService.deleteUser(currentUser.getId());
            common.showSuccessNotification("Tu cuenta ha sido eliminada correctamente.");

            authenticatedUser.logout();

            eliminarDialog.close();
        });


        Button volverButton = new Button("Volver");
        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volverButton.addClickListener(event -> eliminarDialog.close());

        // Crear un HorizontalLayout para los botones
        HorizontalLayout buttonLayout = new HorizontalLayout(eliminarButton, volverButton);
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true); // Para un espacio entre los botones
        buttonLayout.setPadding(true);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centra los botones
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Agregar el título y los botones al Dialog
        VerticalLayout dialogContent = new VerticalLayout(titulo, buttonLayout);
        dialogContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        dialogContent.setSpacing(true);
        eliminarDialog.add(dialogContent);

        // Abrir el diálogo
        eliminarDialog.open();
    }

    private void openCambiarPasswordDialog() {
        Dialog changePasswordDialog = new Dialog();
        changePasswordDialog.setWidth("400px");

        PasswordField currentPasswordField = new PasswordField("Contraseña Actual");
        PasswordField newPasswordField = new PasswordField("Nueva Contraseña");
        PasswordField confirmPasswordField = new PasswordField("Confirmar Nueva Contraseña");

        Button saveButton = new Button("Cambiar Contraseña", event -> {
            if (!usuarioService.checkPassword(currentUser, currentPasswordField.getValue())) {
                common.showErrorNotification("Contraseña actual incorrecta");
                return;
            }
            if (!newPasswordField.getValue().equals(confirmPasswordField.getValue())) {
                common.showErrorNotification("Las nuevas contraseñas no coinciden");
                return;
            }
            usuarioService.updatePassword(currentUser, newPasswordField.getValue());
            common.showSuccessNotification("Contraseña actualizada correctamente");
            changePasswordDialog.close();
        });

        Button cancelButton = new Button("Volver", event -> changePasswordDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(currentPasswordField, newPasswordField, confirmPasswordField, buttonLayout);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        changePasswordDialog.add(dialogLayout);
        changePasswordDialog.open();
    }

    private void BotonesConfiguracion() {
        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button eliminar = new Button("Eliminar");
        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminar.addClickListener(e -> openEliminarDialog());

        Button modificar = new Button("Modificar");
        modificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        modificar.addClickListener(e -> openEditarDialog());

        buttonLayout.add(modificar, eliminar);
        buttonLayout.setWidthFull(); // Asegúrate de que ocupe todo el ancho disponible
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centra los botones horizontalmente
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Centra los botones verticalmente (opcional)

        getContent().add(buttonLayout);
    }

    private void update_password() {
        HorizontalLayout password = new HorizontalLayout();
        password.setSpacing(true);
        password.setWidthFull();
        password.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button cambiarPasswordButton = new Button("Cambiar Contraseña", e -> openCambiarPasswordDialog());
        cambiarPasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        password.add(cambiarPasswordButton);

        getContent().add(password);

    }

}



