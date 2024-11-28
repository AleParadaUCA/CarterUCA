package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import es.uca.iw.carteruca.models.usuario.Centro;
import es.uca.iw.carteruca.services.CentroService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.H2;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Centros")
@Route("/centro") // Ruta para la página
@PermitAll
public class CentroAllView extends Composite<VerticalLayout> {

    @Autowired
    private final CentroService centroService;

    private Grid<Centro> grid = new Grid<>(Centro.class);

    public CentroAllView(CentroService centroService) {
        this.centroService = centroService;
        createTitle(); // Crear el título
        configureGrid(); // Configurar la tabla
        createLayout(); // Crear el layout general con la tabla y el botón
    }

    private void createTitle() {
        H2 title = new H2("Centros");
        getContent().add(title);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(Centro::getNombre).setHeader("Nombre").setSortable(true);
        grid.addColumn(Centro::getAcronimo).setHeader("Acrónimo").setSortable(true);

        grid.addComponentColumn(centro -> {
            Icon editIcon = VaadinIcon.EDIT.create();
            Button editButton = new Button(editIcon, click -> openEditDialog(centro));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return editButton;
        }).setHeader("Editar");

        grid.addComponentColumn(centro -> {
            Icon deleteIcon = VaadinIcon.TRASH.create();
            Button deleteButton = new Button(deleteIcon, click -> {
                centroService.deleteCentro(centro.getId());
                updateGrid();
                showSuccessNotification("Centro eliminado con éxito");
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return deleteButton;
        }).setHeader("Eliminar");

        updateGrid();
    }

    private void createLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Button addButton = createAddButton();
        layout.add(grid, addButton);
        layout.setSpacing(true);

        getContent().add(layout);
    }

    private Button createAddButton() {
        Button addButton = new Button("Agregar Centro", event -> openAddDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return addButton;
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre del Centro");
        TextField acronimoField = new TextField("Acrónimo del Centro"); // Campo para el acronimo

        Button saveButton = new Button("Guardar", event -> {
            if (!nombreField.getValue().trim().isEmpty() && !acronimoField.getValue().trim().isEmpty()) {
                Centro nuevoCentro = new Centro();
                nuevoCentro.setNombre(nombreField.getValue().trim());
                nuevoCentro.setAcronimo(acronimoField.getValue().trim()); // Setear el acronimo
                centroService.addCentro(nuevoCentro); // Guardar a través del servicio
                updateGrid(); // Refrescar la tabla
                dialog.close();
                showSuccessNotification("Centro añadido con éxito");
            } else {
                Notification.show("Todos los campos son obligatorios", 2000, Notification.Position.BOTTOM_START);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(nombreField, acronimoField, buttons);
        dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openEditDialog(Centro centro) {
        Dialog dialog = new Dialog();
        TextField nombreField = new TextField("Nombre del Centro", centro.getNombre());
        TextField acronimoField = new TextField("Acrónimo del Centro", centro.getAcronimo()); // Campo para editar acronimo

        Button saveButton = new Button("Guardar", event -> {
            if (!nombreField.getValue().trim().isEmpty() && !acronimoField.getValue().trim().isEmpty()) {
                centro.setNombre(nombreField.getValue().trim());
                centro.setAcronimo(acronimoField.getValue().trim()); // Actualizar el acronimo
                centroService.updateCentro(centro);
                updateGrid();
                dialog.close();
                showSuccessNotification("Centro modificado con éxito");
            } else {
                Notification.show("Todos los campos son obligatorios", 2000, Notification.Position.BOTTOM_START);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(nombreField, acronimoField, buttons);
        dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void updateGrid() {
        List<Centro> centros = centroService.getAllCentros();
        grid.setItems(centros);
    }

    private void showSuccessNotification(String message) {
        Notification successNotification = new Notification(message, 2000, Notification.Position.MIDDLE);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.open();
    }
}
