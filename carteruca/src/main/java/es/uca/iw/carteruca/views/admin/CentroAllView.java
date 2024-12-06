package es.uca.iw.carteruca.views.admin;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.models.usuario.Centro;
import es.uca.iw.carteruca.services.CentroService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;

import java.util.List;

@PageTitle("Centros")
@Route(value = "/home-admin/centro", layout = MainLayout.class)
@RolesAllowed("Admin")
public class CentroAllView extends Composite<VerticalLayout> {

    @Autowired
    private final CentroService centroService;

    private Grid<Centro> tabla_centros = new Grid<>(Centro.class);

    public CentroAllView(CentroService centroService) {
        this.centroService = centroService;
        common.creartitulo("Centros", this); // Usar el título común
        configurar_tabla(); // Configurar la tabla
        crear_vista(); // Crear el layout general con la tabla y el botón
    }

    private void crear_vista() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Button add_button = add_boton();  // Botón para agregar un nuevo centro
        Button volver_boton = addVolverButton(); // Botón de volver

        HorizontalLayout volver = new HorizontalLayout(volver_boton);
        volver.setWidthFull(); // Para que ocupe todo el ancho
        volver.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Alinear a la izquierda
        layout.add(volver, tabla_centros, add_button);  // Agregar los botones y la tabla al layout
        layout.setSpacing(true);

        getContent().add(layout);
        getContent().add(volver);
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

        tabla_centros.addComponentColumn(centro -> {
            Icon eliminar_icono = VaadinIcon.TRASH.create();
            Button eliminar = new Button(eliminar_icono, click -> {
                centroService.deleteCentro(centro.getId());
                updateGrid();
                common.showSuccessNotification("Centro eliminado con éxito");
            });
            eliminar.getElement().setAttribute("aria-label", "Eliminar");
            eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return eliminar;
        }).setHeader("Eliminar");

        updateGrid();
    }

    private Button add_boton() {
        Button add = new Button("Agregar Centro", event -> openAddDialog());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.getElement().setAttribute("aria-label", "Agregar Centro");
        return add;
    }

    private Button addVolverButton() {

        Button volver = new Button("Volver", event -> {
            // Redirigir al usuario a la vista HomeAdminView
            UI.getCurrent().navigate("/home-admin");
        });
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volver.getElement().setAttribute("aria-label", "Volver");
        return volver;
    }

    // Método para abrir el diálogo de agregar centro
    private void openAddDialog() {
        common.openDialog("Agregar Centro", "", "", null, false, centroService, this::updateGrid);
    }

    // Método para abrir el diálogo de editar centro
    private void openEditDialog(Centro centro) {
        common.openDialog("Editar Centro", centro.getNombre(), centro.getAcronimo(), centro, true, centroService, this::updateGrid);
    }

    private void updateGrid() {
        List<Centro> centros = centroService.getAllCentros();
        tabla_centros.setItems(centros);
    }

}



