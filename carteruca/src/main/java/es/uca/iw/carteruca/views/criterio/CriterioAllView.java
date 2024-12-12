package es.uca.iw.carteruca.views.criterio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Criterio;
import es.uca.iw.carteruca.services.CriterioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Crtierios")
@Route(value = "/home-admin/criterio", layout = MainLayout.class)
@RolesAllowed("Admin")
public class CriterioAllView extends VerticalLayout {

    @Autowired
    private final CriterioService criterioService;

    private final Grid<Criterio> tabla_criterio = new Grid<>(Criterio.class);

    public CriterioAllView(CriterioService criterioService) {
        this.criterioService = criterioService;

        setWidthFull();
        setSpacing(true);
        setPadding(true);

        crearTitulo("Criterios");
        configurartabla();
        crearvista();

    }
    private void crearTitulo(String titulo) {
        add(new com.vaadin.flow.component.html.H1(titulo));
    }

    private void crearvista() {

        HorizontalLayout add = new HorizontalLayout();
        Button addButton = new Button("Agregar Criterio",click -> openAddDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.add(addButton);
        add.setWidthFull();
        add.setJustifyContentMode(JustifyContentMode.CENTER);
        HorizontalLayout volver = new HorizontalLayout();
        Button volverButton = new Button("Volver", e -> UI.getCurrent().navigate(HomeAdminView.class));
        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volver.add(volverButton);
        volver.setWidthFull();
        volver.setJustifyContentMode(JustifyContentMode.END);

        add(tabla_criterio, add, volver);
    }

    private void configurartabla(){
        tabla_criterio.removeAllColumns();
        tabla_criterio.addColumn(Criterio::getDescripcion).setHeader("Descripcion").setSortable(true);
        tabla_criterio.addColumn(Criterio::getPeso).setHeader("Peso").setSortable(true);

        tabla_criterio.addComponentColumn(criterio -> {
            Button editButton = new Button(VaadinIcon.EDIT.create(), click -> openEditDialog(criterio));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return editButton;
        }).setHeader("Editar");

        tabla_criterio.addComponentColumn(criterio -> {
            Icon delete = VaadinIcon.TRASH.create();
            Button deleteButton = new Button(delete, click -> {
                criterioService.deleteCriterio(criterio.getId());
                updateGrid();
                common.showSuccessNotification("Cartera eliminada con éxito");
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return deleteButton;
        }).setHeader("Eliminar");

        updateGrid();
    }

    private void updateGrid() {
        List<Criterio> criterios = criterioService.getAllCriterios();
        tabla_criterio.setItems(criterios);
    }

    private void openAddDialog() {

        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

        TextField descripcion = new TextField("Descripcion");
        NumberField peso = new NumberField("Peso");

        form.add(descripcion, peso);

        Button saveButton = new Button("Guardar", event -> {
            try{
                Criterio nuevoCriterio = new Criterio();
                nuevoCriterio.setDescripcion(descripcion.getValue());
                nuevoCriterio.setPeso(peso.getValue().floatValue());

                criterioService.addCriterio(nuevoCriterio);
                updateGrid();
                dialog.close();
                common.showSuccessNotification("Criterio agregado");
            }catch (IllegalArgumentException e){
                common.showErrorNotification("Error " + e.getMessage());
            }
        });

        HorizontalLayout layout = new HorizontalLayout();
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        layout.add(form, saveButton, cancelButton);
        layout.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(form,layout);
        dialog.open();

    }

    private void openEditDialog(Criterio criterio) {

        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

        TextField descripcion = new TextField("Descripcion", criterio.getDescripcion());
        // Crear el campo de número con el label "Peso"
        NumberField peso = new NumberField("Peso");
        peso.setValue(criterio.getPeso().doubleValue());

        form.add(descripcion, peso);

        Button saveButton = new Button("Guardar", event -> {
            try {
                criterio.setDescripcion(descripcion.getValue());
                criterio.setPeso(peso.getValue().floatValue());

                criterioService.updateCriterio(criterio);
                updateGrid();
                dialog.close();
                common.showSuccessNotification("Criterio editado");
            }catch (IllegalArgumentException e){
                common.showErrorNotification("Error " + e.getMessage());
            }
        });

        HorizontalLayout layout = new HorizontalLayout();
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        layout.add(saveButton, cancelButton);

        layout.setJustifyContentMode(JustifyContentMode.END);
        dialog.add(form,layout);
        dialog.open();
        
    }


}
