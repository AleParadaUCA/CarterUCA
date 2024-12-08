package es.uca.iw.carteruca.views.cartera;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Carteras")
@Route(value = "/home-admin/cartera", layout = MainLayout.class)
@RolesAllowed({"Admin","CIO"})

public class CarteraAllView extends VerticalLayout {

    @Autowired
    private final CarteraService carteraService;

    private final Grid<Cartera> tablaCarteras = new Grid<>(Cartera.class);

    public CarteraAllView(CarteraService carteraService) {
        this.carteraService = carteraService;

        setWidthFull();
        setSpacing(true);
        setPadding(true);

        crearTitulo("Carteras");
        configurarTabla();
        crearVista();
    }

    private void crearTitulo(String titulo) {
        add(new com.vaadin.flow.component.html.H1(titulo));
    }

    private void crearVista() {

        HorizontalLayout boton_agregar = new HorizontalLayout();
        Button addButton = new Button("Agregar Cartera", click -> openAddDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        boton_agregar.add(addButton);
        boton_agregar.setWidthFull();
        boton_agregar.setJustifyContentMode(JustifyContentMode.CENTER);
        HorizontalLayout boton_volver = new HorizontalLayout();
        Button volverButton = new Button("Volver", e-> UI.getCurrent().navigate(HomeAdminView.class));
        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        boton_volver.add(volverButton);
        boton_volver.setWidthFull();
        boton_volver.setJustifyContentMode(JustifyContentMode.END);

        add(tablaCarteras, boton_agregar, boton_volver);
    }

    private void configurarTabla() {
        tablaCarteras.removeAllColumns();
        tablaCarteras.addColumn(Cartera::getNombre).setHeader("Nombre").setSortable(true);
        tablaCarteras.addColumn(Cartera::getFecha_inicio).setHeader("Fecha Inicio").setSortable(true);
        tablaCarteras.addColumn(Cartera::getFecha_fin).setHeader("Fecha Fin").setSortable(true);

        tablaCarteras.addComponentColumn(cartera -> {
            Button editButton = new Button(VaadinIcon.EDIT.create(), click -> openEditDialog(cartera));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return editButton;
        }).setHeader("Editar");

        tablaCarteras.addComponentColumn(cartera -> {
            Icon delete = VaadinIcon.TRASH.create();
            Button deleteButton = new Button(delete, click -> {
                carteraService.deleteCartera(cartera.getId());
                updateGrid();
                Notification.show("Cartera eliminada con éxito");
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return deleteButton;
        }).setHeader("Eliminar");

        updateGrid();
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre");
        DatePicker fechaInicioField = new DatePicker("Fecha Inicio");
        DatePicker fechaFinField = new DatePicker("Fecha Fin");
        DatePicker aperturaSolicitudField = new DatePicker("Apertura de Solicitudes");
        DatePicker cierreSolicitudField = new DatePicker("Cierre de Solicitudes");

        formLayout.add(nombreField, fechaInicioField, fechaFinField, aperturaSolicitudField, cierreSolicitudField);

        Button saveButton = new Button("Guardar", event -> {
            try {
                Cartera nuevaCartera = new Cartera();
                nuevaCartera.setNombre(nombreField.getValue());

                // Convertir LocalDate a LocalDateTime
                nuevaCartera.setFecha_inicio(fechaInicioField.getValue().atStartOfDay());
                nuevaCartera.setFecha_fin(fechaFinField.getValue().atStartOfDay());
                nuevaCartera.setFecha_apertura_solicitud(aperturaSolicitudField.getValue().atStartOfDay());
                nuevaCartera.setFecha_cierre_solicitud(cierreSolicitudField.getValue().atStartOfDay());

                carteraService.addCartera(nuevaCartera);
                updateGrid();
                dialog.close();
                Notification.show("Cartera agregada con éxito");
            } catch (IllegalArgumentException e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.add(formLayout, new VerticalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void openEditDialog(Cartera cartera) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre", cartera.getNombre());

        // Convertir LocalDateTime a LocalDate para usarlo en DatePicker
        DatePicker fechaInicioField = new DatePicker("Fecha Inicio",
                cartera.getFecha_inicio() != null ? cartera.getFecha_inicio().toLocalDate() : null);
        DatePicker fechaFinField = new DatePicker("Fecha Fin",
                cartera.getFecha_fin() != null ? cartera.getFecha_fin().toLocalDate() : null);
        DatePicker aperturaSolicitudField = new DatePicker("Apertura de Solicitudes",
                cartera.getFecha_apertura_solicitud() != null ? cartera.getFecha_apertura_solicitud().toLocalDate() : null);
        DatePicker cierreSolicitudField = new DatePicker("Cierre de Solicitudes",
                cartera.getFecha_cierre_solicitud() != null ? cartera.getFecha_cierre_solicitud().toLocalDate() : null);

        formLayout.add(nombreField, fechaInicioField, fechaFinField, aperturaSolicitudField, cierreSolicitudField);

        Button saveButton = new Button("Guardar", event -> {
            try {
                cartera.setNombre(nombreField.getValue());

                // Convertir LocalDate a LocalDateTime para guardar en Cartera
                cartera.setFecha_inicio(fechaInicioField.getValue() != null ? fechaInicioField.getValue().atStartOfDay() : null);
                cartera.setFecha_fin(fechaFinField.getValue() != null ? fechaFinField.getValue().atStartOfDay() : null);
                cartera.setFecha_apertura_solicitud(aperturaSolicitudField.getValue() != null ? aperturaSolicitudField.getValue().atStartOfDay() : null);
                cartera.setFecha_cierre_solicitud(cierreSolicitudField.getValue() != null ? cierreSolicitudField.getValue().atStartOfDay() : null);

                carteraService.updateCartera(cartera.getId(), cartera);
                updateGrid();
                dialog.close();
                Notification.show("Cartera actualizada con éxito");
            } catch (IllegalArgumentException e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.add(formLayout, new VerticalLayout(saveButton, cancelButton));
        dialog.open();
    }


    private void updateGrid() {
        List<Cartera> carteras = carteraService.getAllCarteras();
        tablaCarteras.setItems(carteras);
    }
}
