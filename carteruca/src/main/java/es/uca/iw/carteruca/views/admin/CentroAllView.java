package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.models.usuario.Centro;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.H2;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Centros")
@Route(value = "/centro", layout = MainLayout.class)
public class CentroAllView extends Composite<VerticalLayout> {
    private List<Centro> centros = new ArrayList<>();
    private Grid<Centro> grid = new Grid<>(Centro.class);

    public CentroAllView() {
        createTitle(); // Crear el título
        configureGrid(); // Configurar la tabla
        getContent().add(grid); // Añadir la tabla
        createButtons(); // Crear los botones
    }

    private void createTitle() {
        H2 title = new H2("Centros"); // Crear un título H2
        getContent().add(title); // Añadir el título al contenido
    }

    private void configureGrid() {
        grid.removeAllColumns(); // Eliminar todas las columnas para personalizar
        grid.addColumn(Centro::getNombre).setHeader("Nombre").setSortable(true); // Solo mostrar el nombre

        // Botón de editar
        grid.addComponentColumn(centro -> {
            Icon editIcon = VaadinIcon.EDIT.create();
            Button editButton = new Button(editIcon, click -> openEditDialog(centro));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return editButton;
        }).setHeader("Editar");

        // Botón de eliminar
        grid.addComponentColumn(centro -> {
            Icon deleteIcon = VaadinIcon.TRASH.create();
            Button deleteButton = new Button(deleteIcon, click -> {
                centros.remove(centro);
                updateGrid();
                showSuccessNotification("Centro eliminado con éxito");
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return deleteButton;
        }).setHeader("Eliminar");

        grid.setItems(centros);
    }

    private void createButtons() {
        // Botón "Agregar Centro"
        Button addButton = new Button("Agregar Centro", event -> openAddDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botón "Volver" redirigiendo a HomeAdminView
        RouterLink backLink = new RouterLink();
        Button volver = new Button("Volver");
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeAdminView.class));
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backLink.add(volver);

        backLink.getElement().getStyle().set("margin-top", "8px");
        backLink.getElement().getStyle().set("text-decoration", "none");

        // Layout para el botón de "Agregar Centro"
        HorizontalLayout addButtonLayout = new HorizontalLayout(addButton);
        addButtonLayout.setWidthFull(); // Usar todo el ancho disponible
        addButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Alinear el botón al inicio

        // Layout para el botón de "Volver"
        HorizontalLayout backButtonLayout = new HorizontalLayout(backLink);
        backButtonLayout.setWidthFull(); // Usar todo el ancho disponible
        backButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Alinear el botón al final

        // Añadir ambos botones al contenido
        getContent().add(addButtonLayout, backButtonLayout);
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre del Centro");

        Button saveButton = new Button("Guardar", event -> {
            if (!nombreField.getValue().trim().isEmpty()) {
                centros.add(new Centro(nombreField.getValue().trim()));
                updateGrid();
                dialog.close();
                showSuccessNotification("Centro añadido con éxito");
            } else {
                Notification.show("El nombre no puede estar vacío", 2000, Notification.Position.BOTTOM_START);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(nombreField, buttons);
        dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openEditDialog(Centro centro) {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre del Centro", centro.getNombre());

        Button saveButton = new Button("Guardar", event -> {
            if (!nombreField.getValue().trim().isEmpty()) {
                centro.setNombre(nombreField.getValue().trim());
                updateGrid();
                dialog.close();
                showSuccessNotification("Centro modificado con éxito");
            } else {
                Notification.show("El nombre no puede estar vacío", 2000, Notification.Position.BOTTOM_START);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(nombreField, buttons);
        dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void updateGrid() {
        grid.setItems(centros);
    }

    private void showSuccessNotification(String message) {
        Notification successNotification = new Notification(message, 2000, Notification.Position.MIDDLE);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.open();
    }
}
