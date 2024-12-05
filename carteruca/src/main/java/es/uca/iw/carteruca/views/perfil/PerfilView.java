package es.uca.iw.carteruca.views.perfil;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Perfil")
@Route(value = "/perfil", layout = MainLayout.class)
@RolesAllowed({"Admin", "CIO", "Promotor", "Solicitante", "OTP"})
public class PerfilView extends Composite<VerticalLayout> {

    private final UsuarioService usuarioService;
    private Usuario currentUser;

    // Campos de la vista principal
    private TextField nombreField;
    private TextField apellidosField;
    private TextField emailField;
    private TextField usernameField;
    private TextField rolField;

    @Autowired
    public PerfilView(AuthenticatedUser authenticatedUser, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        // Obtener el usuario actual de la sesión
        this.currentUser = authenticatedUser.get().get();

        if (currentUser == null) {
            // Si no se encuentra un usuario, redirigir al login
            getUI().ifPresent(ui -> ui.navigate(""));
            Notification.show("Sesión expirada o no autenticado. Vuelve a iniciar sesión.", 3000, Notification.Position.MIDDLE);
            return; // No se debe cargar la vista si la sesión está expirada
        }

        // Construcción de la vista
        createHeader();
        createProfileForm();
        createButton();
        createFooter();
    }

    private void createHeader() {
        H2 header = new H2("Datos Personales");

        Avatar avatar = new Avatar(currentUser.getNombre() + " " + currentUser.getApellidos());
        String uniqueColor = common.getColorFromName(currentUser.getNombre());
        avatar.getElement().getStyle().set("background-color", uniqueColor);
        avatar.getElement().getStyle().set("width", "100px").set("height", "100px");

        HorizontalLayout headerLayout = new HorizontalLayout(avatar, header);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(headerLayout);
    }

    private void createProfileForm() {
        FormLayout formLayout = new FormLayout();

        // Crear los campos de la vista principal
        nombreField = new TextField("Nombre");
        nombreField.setValue(currentUser.getNombre());
        nombreField.setReadOnly(true);

        apellidosField = new TextField("Apellidos");
        apellidosField.setValue(currentUser.getApellidos());
        apellidosField.setReadOnly(true);

        emailField = new TextField("Email");
        emailField.setValue(currentUser.getEmail());
        emailField.setReadOnly(true);

        usernameField = new TextField("Usuario");
        usernameField.setValue(currentUser.getUsername());
        usernameField.setReadOnly(true);

        rolField = new TextField("Rol");
        rolField.setValue(currentUser.getRol().name());
        rolField.setReadOnly(true);

        formLayout.add(nombreField, apellidosField, emailField, usernameField, rolField);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        getContent().add(formLayout);
    }

    private void createButton() {
        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button eliminar = new Button("Eliminar");
        eliminar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        eliminar.addClickListener(e -> openEliminarDialog());

        Button modificar = new Button("Modificar");
        modificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        modificar.addClickListener(e -> openEditDialog());

        buttonLayout.add(modificar, eliminar);
        buttonLayout.setWidthFull(); // Asegúrate de que ocupe todo el ancho disponible
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centra los botones horizontalmente
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Centra los botones verticalmente (opcional)

        getContent().add(buttonLayout);
    }

    private void createFooter() {
        Button volverButton = new Button("Volver");
        volverButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        volverButton.addClickListener(e -> volver());

        HorizontalLayout footerLayout = new HorizontalLayout(volverButton);
        footerLayout.setWidthFull();
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        getContent().add(footerLayout);
    }

    private void volver() {
        // Redirigir según el rol del usuario
        if (currentUser.getRol().name().equals("Admin")) {
            getUI().ifPresent(ui -> ui.navigate(HomeAdminView.class));
        } else {
            getUI().ifPresent(ui -> ui.navigate(HomeSolicitanteView.class));
        }
    }

    private void openEditDialog() {
        Dialog editDialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos de edición
        TextField nombreFieldDialog = new TextField("Nombre");
        nombreFieldDialog.setValue(currentUser.getNombre());

        TextField apellidosFieldDialog = new TextField("Apellidos");
        apellidosFieldDialog.setValue(currentUser.getApellidos());

        TextField emailFieldDialog = new TextField("Email");
        emailFieldDialog.setValue(currentUser.getEmail());


        formLayout.add(nombreFieldDialog, apellidosFieldDialog, emailFieldDialog);

        // Botón para guardar los cambios
        Button saveButton = new Button("Guardar", event -> {
            String result = usuarioService.updateUser(
                    currentUser.getId(),
                    nombreFieldDialog.getValue(),
                    apellidosFieldDialog.getValue(),
                    emailFieldDialog.getValue()
            );


            if (result.equals("Usuario actualizado correctamente.")) {
                // Actualizamos currentUser con los nuevos datos
                currentUser.setNombre(nombreFieldDialog.getValue());
                currentUser.setApellidos(apellidosFieldDialog.getValue());
                currentUser.setEmail(emailFieldDialog.getValue());

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

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonLayout);
        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void openEliminarDialog() {
        Dialog eliminarDialog = new Dialog();

        // Título de la confirmación
        H4 titulo = new H4("¿Estás seguro de querer eliminar la cuenta?");

        // Botones de acción
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        /*
        eliminarButton.addClickListener(event -> {
            // Llamar al servicio para eliminar el usuario
            usuarioService.deleteUser(currentUser.getId());
            common.showSuccessNotification("Tu cuenta ha sido eliminada correctamente.");

            // Cerrar sesión y redirigir al inicio
            getUI().ifPresent(ui -> {
                ui.getSession().close();
                ui.navigate("");  // Redirige al inicio
            });

            eliminarDialog.close();
        });

         */

        Button volverButton = new Button("Volver");
        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volverButton.addClickListener(event -> eliminarDialog.close());

        // Crear un HorizontalLayout para los botones
        HorizontalLayout buttonLayout = new HorizontalLayout(eliminarButton, volverButton);
        buttonLayout.setSpacing(true); // Para un espacio entre los botones
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




}



