package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Usuarios")
@Route(value = "/usuario", layout = MainLayout.class)
@RolesAllowed("Admin")
public class UsuarioAllView extends Composite<VerticalLayout> {

    private Grid<Usuario> tabla_usuarios = new Grid<>(Usuario.class);
    private List<Usuario> usuarios = new ArrayList<>(); // Lista de usuarios

    public UsuarioAllView() {
        common.creartitulo("Usuarios",this);
        configurarGrid(); // Configuración de la tabla
        getContent().add(tabla_usuarios); // Mostrar la tabla
        addBotones(); // Crear los botones
    }

    private void configurarGrid() {
        // Configurar la tabla (Grid)
        tabla_usuarios.removeAllColumns(); // Limpiar las columnas previas
        tabla_usuarios.addColumn(Usuario::getUsername).setHeader("Usuario").setSortable(true); // Columna de Usuario
        tabla_usuarios.addColumn(Usuario::getNombre).setHeader("Nombre").setSortable(true); // Columna de Nombre
        tabla_usuarios.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true); // Columna de Email

        // Columna para editar el rol
        tabla_usuarios.addComponentColumn(usuario -> {
            Select<Rol> rolSelect = new Select<>();
            rolSelect.setLabel("Rol");
            rolSelect.setItems(getRolesExcludingAdmin()); // Mostrar todos los roles excepto Admin
            rolSelect.setValue(usuario.getRol()); // Establecer el rol actual
            rolSelect.addValueChangeListener(event -> {
                usuario.setRol(event.getValue());
                updateGrid(); // Actualizar la tabla con el nuevo rol
                showSuccessNotification("Rol cambiado con éxito");
            });
            rolSelect.getElement().setAttribute("aria-label", "Seleccionar rol para " + usuario.getUsername());

            return rolSelect; // Retornar el selector de roles
        }).setHeader("Rol");

        // Añadir los usuarios al grid
        tabla_usuarios.setItems(usuarios); // Aquí se asignan los usuarios para ser mostrados
        tabla_usuarios.getElement().setAttribute("aria-label", "Tabla de usuarios"); // Atributo aria-label para la tabla
    }

    private void updateGrid() {

        tabla_usuarios.setItems(usuarios); // Actualizar la tabla con los usuarios
    }

    private void showSuccessNotification(String mensaje) {
        // Notificación de éxito en verde
        Notification exito_notificacion = new Notification(mensaje, 2000, Notification.Position.MIDDLE);
        exito_notificacion.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        exito_notificacion.open();
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

    private void addBotones() {
        // Botón "Volver" redirigiendo a HomeAdminView
        RouterLink link_volver = new RouterLink();
        Button volver = new Button("Volver");
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeAdminView.class));
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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

