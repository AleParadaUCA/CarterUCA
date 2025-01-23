package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.services.CentroService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Centros")
@Route(value = "/home-admin/centro", layout = MainLayout.class)
@RolesAllowed("Admin")
public class CentroAllView extends Composite<VerticalLayout> {

    private final CentroService centroService;
    private final Grid<Centro> tabla_centros = new Grid<>(Centro.class);

    public CentroAllView(CentroService centroService) {
        this.centroService = centroService;

        common.creartituloComposite("Centros", this); // Usar el título común
        configurar_tabla(); // Configurar la tabla

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Button add_button = new Button("Agregar Centro", event -> openAddDialog());
        add_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add_button.getElement().setAttribute("aria-label", "Agregar Centro");

        layout.add(add_button);
        layout.setSpacing(true);

        getContent().add(layout);
        getContent().add(common.botones_Admin());
    }

    private void configurar_tabla() {
        tabla_centros.removeAllColumns();
        tabla_centros.addColumn(Centro::getNombre).setHeader("Nombre").setSortable(true);
        tabla_centros.addColumn(Centro::getAcronimo).setHeader("Acrónimo").setSortable(true);

        tabla_centros.addComponentColumn(centro -> {
            Icon editar = VaadinIcon.EDIT.create();
            Button boton_editar = new Button(editar, click -> openEditDialog(centro));
            boton_editar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            boton_editar.getElement().setAttribute("aria-label", "Editar");
            return boton_editar;
        }).setHeader("Editar");

        List<Centro> centros = centroService.getAllCentros();
        tabla_centros.setItems(centros);

        ListDataProvider<Centro> dataProvider =  new ListDataProvider<>(centros);
        tabla_centros.setDataProvider(dataProvider);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("50%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener(event -> {
            String searchTerm = searchField.getValue().trim().toLowerCase();
            dataProvider.setFilter(centro -> {
                String nombre = centro.getNombre().toLowerCase();
                String acronimo = centro.getAcronimo().toLowerCase();
                return nombre.contains(searchTerm) || acronimo.contains(searchTerm);
            });
        });

        getContent().add(searchField, tabla_centros);

    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();

        H4 add = new H4("Añadir Centro");

        dialog.add(add);

        TextField nombre = new TextField("Nombre del Centro");
        nombre.getElement().setAttribute("aria-label", "Nombre del Centro");
        TextField acronimo = new TextField("Acrónimo del Centro");
        acronimo.getElement().setAttribute("aria-label", "Acrónimo del Centro");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombre, acronimo);

        Button guardar = new Button("Guardar", event -> {
            if (!nombre.getValue().trim().isEmpty() && !acronimo.getValue().trim().isEmpty()) {
                Centro nuevoCentro = new Centro();
                nuevoCentro.setNombre(nombre.getValue().trim());
                nuevoCentro.setAcronimo(acronimo.getValue().trim());
                centroService.addCentro(nuevoCentro);
                common.showSuccessNotification("Centro añadido con éxito");
                updateGrid();
                dialog.close();
            } else {
                common.showErrorNotification("Todos los campos son obligatorios");
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, botones);
        dialogLayout.setAlignItems(FlexComponent.Alignment.END);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openEditDialog(Centro centro) {
        Dialog dialog = new Dialog();

        H4 editar = new H4("Editar Centro");
        dialog.add(editar);

        TextField nombre = new TextField("Nombre del Centro", centro.getNombre());
        nombre.getElement().setAttribute("aria-label", "Nombre del Centro");
        TextField acronimo = new TextField("Acrónimo del Centro", centro.getAcronimo());
        acronimo.getElement().setAttribute("aria-label", "Acrónimo del Centro");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nombre, acronimo);

        Button guardar = new Button("Guardar", event -> {
            boolean actualizado = false;

            if (!nombre.getValue().trim().isEmpty()) {
                centro.setNombre(nombre.getValue().trim());
                actualizado = true;
            }
            if (!acronimo.getValue().trim().isEmpty()) {
                centro.setAcronimo(acronimo.getValue().trim());
                actualizado = true;
            }

            if (actualizado) {
                centroService.updateCentro(centro);
                common.showSuccessNotification("Centro modificado con éxito");
                updateGrid();
                dialog.close();
            } else {
                common.showErrorNotification("No se han realizado cambios. Por favor, edite al menos un campo.");
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, botones);
        dialogLayout.setAlignItems(FlexComponent.Alignment.END);

        dialog.add(dialogLayout);
        dialog.open();
    }


    private void updateGrid() {
        List<Centro> centros = centroService.getAllCentros();
        tabla_centros.setItems(centros);
    }
}




