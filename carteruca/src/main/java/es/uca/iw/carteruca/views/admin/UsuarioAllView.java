package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.models.usuario.Rol;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Usuarios")
@Route(value = "/usuario", layout = MainLayout.class)
//todavia en estatico
public class UsuarioAllView extends Composite<VerticalLayout> {

    private Grid<Usuario> grid = new Grid<>(Usuario.class);
    private List<Usuario> usuarios = new ArrayList<>(); // Lista de usuarios

    private TextField usuarioField = new TextField("Usuario");
    private TextField nombreField = new TextField("Nombre");
    private TextField emailField = new TextField("Email");
    private Select<Rol> rolSelect = new Select<>();

    // Cuadro de diálogo para agregar un nuevo usuario
    private Dialog addUserDialog = new Dialog();

    public UsuarioAllView() {

        createTitle(); // Título de la vista
        configureGrid(); // Configuración de la tabla
        getContent().add(grid); // Mostrar la tabla
        createButtons();

    }

    private void createTitle() {
        getContent().add(new com.vaadin.flow.component.html.H2("Usuarios"));
    }

    private void configureGrid() {
        // Configurar la tabla (Grid)
        grid.removeAllColumns(); // Limpiar las columnas previas
        grid.addColumn(Usuario::getUsername).setHeader("Usuario").setSortable(true); // Columna de Usuario
        grid.addColumn(Usuario::getNombre).setHeader("Nombre").setSortable(true); // Columna de Nombre
        grid.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true); // Columna de Email

        // Columna para editar el rol
        grid.addComponentColumn(usuario -> {
            Select<Rol> rolSelect = new Select<>();
            rolSelect.setLabel("Rol");
            rolSelect.setItems(getRolesExcludingAdmin()); // Mostrar todos los roles excepto Admin
            rolSelect.setValue(usuario.getRol()); // Establecer el rol actual
            rolSelect.addValueChangeListener(event -> {
                usuario.setRol(event.getValue());
                updateGrid(); // Actualizar la tabla con el nuevo rol
                showSuccessNotification("Rol cambiado con éxito");
            });

            return rolSelect; // Retornar el selector de roles
        }).setHeader("Rol");

        // Añadir los usuarios al grid
        grid.setItems(usuarios); // Aquí se asignan los usuarios para ser mostrados
    }

    private void openAddUserDialog() {
        // Limpiar los campos antes de abrir el formulario
        usuarioField.clear();
        nombreField.clear();
        emailField.clear();

        // Crear el botón de guardar
        Button saveButton = new Button("Guardar", event -> saveUser());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Estilo primario (azul)

        // Crear el botón de cancelar
        Button cancelButton = new Button("Cancelar", event -> addUserDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR); // Estilo de error (rojo)

        // Crear los botones en una fila horizontal
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        // Agregar los componentes al cuadro de diálogo
        VerticalLayout formLayout = new VerticalLayout(usuarioField, nombreField, emailField, buttonLayout);
        addUserDialog.removeAll(); // Limpiar contenido previo
        addUserDialog.add(formLayout);
        addUserDialog.open(); // Abrir el cuadro de diálogo
    }

    private void saveUser() {
        // Crear un nuevo usuario con los datos proporcionados
        Usuario newUser = new Usuario();
        newUser.setUsername(usuarioField.getValue());
        newUser.setNombre(nombreField.getValue());
        newUser.setEmail(emailField.getValue());
        newUser.setRol(Rol.Solicitante); // Asignar el rol por defecto de Solicitante

        // Agregar el usuario a la lista de usuarios
        usuarios.add(newUser);

        // Limpiar los campos del formulario
        usuarioField.clear();
        nombreField.clear();
        emailField.clear();

        // Actualizar la tabla para reflejar el nuevo usuario
        updateGrid();

        // Cerrar el cuadro de diálogo
        addUserDialog.close();

        // Mostrar una notificación de éxito
        showSuccessNotification("Usuario agregado con éxito");
    }

    private void updateGrid() {
        grid.setItems(usuarios); // Actualizar la tabla con los usuarios
    }

    private void showSuccessNotification(String message) {
        // Notificación de éxito en verde
        Notification successNotification = new Notification(message, 2000, Notification.Position.MIDDLE);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.open();
    }

    // Método para obtener los roles excluyendo Admin
    private List<Rol> getRolesExcludingAdmin() {
        List<Rol> roles = new ArrayList<>();
        for (Rol rol : Rol.values()) {
            if (rol != Rol.Admin) {
                roles.add(rol); // Agregar todos los roles excepto Admin
            }
        }
        return roles;
    }

    private void createButtons() {

        // Botón "Volver" redirigiendo a HomeAdminView
        RouterLink backLink = new RouterLink();
        Button volver = new Button("Volver");
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeAdminView.class));
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backLink.add(volver);

        backLink.getElement().getStyle().set("margin-top", "8px");
        backLink.getElement().getStyle().set("text-decoration", "none");


        // Layout para el botón de "Volver"
        HorizontalLayout backButtonLayout = new HorizontalLayout(backLink);
        backButtonLayout.setWidthFull(); // Usar todo el ancho disponible
        backButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Alinear el botón al final

        // Añadir ambos botones al contenido
        getContent().add(backButtonLayout);
    }
}
