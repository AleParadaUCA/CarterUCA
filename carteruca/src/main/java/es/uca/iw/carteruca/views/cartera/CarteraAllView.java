package es.uca.iw.carteruca.views.cartera;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

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

        common.crearTitulo("Carteras",this);
        configurarTabla();
        HorizontalLayout boton_agregar = new HorizontalLayout();
        Button addButton = new Button("Agregar Cartera", click -> openAddDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        boton_agregar.add(addButton);
        boton_agregar.setWidthFull();
        boton_agregar.setJustifyContentMode(JustifyContentMode.CENTER);

        add(tablaCarteras, boton_agregar);

        add(common.botones_Admin());
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
                try {
                    carteraService.deleteCartera(cartera.getId());
                    updateGrid();
                    common.showSuccessNotification("Cartera eliminada con éxito");
                } catch (IllegalArgumentException e) {
                    common.showErrorNotification("Error al eliminar la cartera: " + e.getMessage());
                }
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
        IntegerField n_tecnicosField = new IntegerField("Número Máximo de Técnicos");
        DatePicker fechaInicioField = new DatePicker("Fecha Inicio de Plazo");
        DatePicker fechaFinField = new DatePicker("Fecha Fin de Plazo");
        DatePicker aperturaSolicitudField = new DatePicker("Fecha de Apertura de Solicitudes");
        DatePicker cierreSolicitudField = new DatePicker("Fecha de Cierre de Solicitudes");
        DatePicker aperturaEvaluacionField = new DatePicker("Fecha de Apertura de Evaluacion");
        DatePicker cierreEvaluacionField = new DatePicker("Fecha de Cierre de Evaluacion");
        NumberField n_horasField = new NumberField("Numero de Horas");
        NumberField presupuestoField = new NumberField("Presupuesto Total");
        formLayout.add(nombreField, n_tecnicosField,fechaInicioField, fechaFinField, aperturaSolicitudField, cierreSolicitudField,
                aperturaEvaluacionField, cierreEvaluacionField, n_horasField, presupuestoField);

        Button saveButton = new Button("Guardar", event -> {
            try {
                if (fechaInicioField.getValue().isAfter(fechaFinField.getValue())) {
                    common.showErrorNotification("La fecha de inicio debe ser anterior a la fecha de fin");
                    return;
                }

                if (aperturaSolicitudField.getValue().isBefore(fechaInicioField.getValue()) || aperturaSolicitudField.getValue().isAfter(fechaFinField.getValue()) ||
                    cierreSolicitudField.getValue().isBefore(fechaInicioField.getValue()) || cierreSolicitudField.getValue().isAfter(fechaFinField.getValue()) ||
                    aperturaEvaluacionField.getValue().isBefore(fechaInicioField.getValue()) || aperturaEvaluacionField.getValue().isAfter(fechaFinField.getValue()) ||
                    cierreEvaluacionField.getValue().isBefore(fechaInicioField.getValue()) || cierreEvaluacionField.getValue().isAfter(fechaFinField.getValue())) {
                    common.showErrorNotification("Todas las fechas deben estar entre la fecha de inicio y la fecha de fin");
                    return;
                }

                Cartera nuevaCartera = new Cartera();
                nuevaCartera.setNombre(nombreField.getValue());
                nuevaCartera.setFecha_inicio(fechaInicioField.getValue().atStartOfDay());
                nuevaCartera.setFecha_fin(fechaFinField.getValue().atStartOfDay());
                nuevaCartera.setFecha_apertura_solicitud(aperturaSolicitudField.getValue().atStartOfDay());
                nuevaCartera.setFecha_cierre_solicitud(cierreSolicitudField.getValue().atStartOfDay());
                nuevaCartera.setFecha_apertura_evaluacion(aperturaEvaluacionField.getValue().atStartOfDay());
                nuevaCartera.setFecha_cierre_evaluacion(cierreEvaluacionField.getValue().atStartOfDay());
                nuevaCartera.setN_horas(n_horasField.getValue().floatValue());
                nuevaCartera.setPresupuesto_total(presupuestoField.getValue().floatValue());
                nuevaCartera.setN_max_tecnicos(n_tecnicosField.getValue());

                carteraService.addCartera(nuevaCartera);
                updateGrid();
                dialog.close();
                common.showSuccessNotification("Cartera agregada con éxito");
            } catch (IllegalArgumentException e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout layout = new HorizontalLayout();

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        layout.add(saveButton, cancelButton);
        layout.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(formLayout, layout);
        dialog.open();
    }

    private void openEditDialog(Cartera cartera) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre", cartera.getNombre());
        DatePicker fechaInicioField = new DatePicker("Fecha Inicio de Plazo",
                cartera.getFecha_inicio() != null ? cartera.getFecha_inicio().toLocalDate() : null);
        DatePicker fechaFinField = new DatePicker("Fecha Fin de Plazo",
                cartera.getFecha_fin() != null ? cartera.getFecha_fin().toLocalDate() : null);
        DatePicker aperturaSolicitudField = new DatePicker("Fecha de Apertura de Solicitudes",
                cartera.getFecha_apertura_solicitud() != null ? cartera.getFecha_apertura_solicitud().toLocalDate() : null);
        DatePicker cierreSolicitudField = new DatePicker("Fecha de Cierre de Solicitudes",
                cartera.getFecha_cierre_solicitud() != null ? cartera.getFecha_cierre_solicitud().toLocalDate() : null);
        DatePicker aperturaEvaluacionField = new DatePicker("Fecha de Apertura de evaluacion",
                cartera.getFecha_apertura_evaluacion() != null ? cartera.getFecha_apertura_evaluacion().toLocalDate() : null);
        DatePicker cierreEvaluacionField = new DatePicker("Fecha de Cierre de evaluacion",
                cartera.getFecha_cierre_evaluacion() != null ? cartera.getFecha_cierre_evaluacion().toLocalDate() : null);
        IntegerField n_tecnicosField = new IntegerField("Número Máximo de Técnicos");
        n_tecnicosField.setValue(cartera.getN_max_tecnicos());

        NumberField n_horasField = new NumberField("Número máximo de Horas");
        n_horasField.setValue(Double.valueOf(cartera.getN_horas()));

        NumberField presupuestoField = new NumberField("Presupuesto");
        presupuestoField.setValue(Double.valueOf(cartera.getPresupuesto_total()));

        formLayout.add(nombreField, n_tecnicosField, fechaInicioField, fechaFinField, aperturaSolicitudField, cierreSolicitudField,
                aperturaEvaluacionField, cierreEvaluacionField, n_horasField, presupuestoField);

        Button saveButton = new Button("Guardar", event -> {
            try {
                if (fechaInicioField.getValue().isAfter(fechaFinField.getValue())) {
                    common.showErrorNotification("La fecha de inicio debe ser anterior a la fecha de fin");
                    return;
                }

                if (aperturaSolicitudField.getValue().isBefore(fechaInicioField.getValue()) || aperturaSolicitudField.getValue().isAfter(fechaFinField.getValue()) ||
                    cierreSolicitudField.getValue().isBefore(fechaInicioField.getValue()) || cierreSolicitudField.getValue().isAfter(fechaFinField.getValue()) ||
                    aperturaEvaluacionField.getValue().isBefore(fechaInicioField.getValue()) || aperturaEvaluacionField.getValue().isAfter(fechaFinField.getValue()) ||
                    cierreEvaluacionField.getValue().isBefore(fechaInicioField.getValue()) || cierreEvaluacionField.getValue().isAfter(fechaFinField.getValue())) {
                    common.showErrorNotification("Todas las fechas deben estar entre la fecha de inicio y la fecha de fin");
                    return;
                }

                cartera.setNombre(nombreField.getValue());
                cartera.setFecha_inicio(fechaInicioField.getValue() != null ? fechaInicioField.getValue().atStartOfDay() : null);
                cartera.setFecha_fin(fechaFinField.getValue() != null ? fechaFinField.getValue().atStartOfDay() : null);
                cartera.setFecha_apertura_solicitud(aperturaSolicitudField.getValue() != null ? aperturaSolicitudField.getValue().atStartOfDay() : null);
                cartera.setFecha_cierre_solicitud(cierreSolicitudField.getValue() != null ? cierreSolicitudField.getValue().atStartOfDay() : null);
                cartera.setFecha_apertura_evaluacion(aperturaEvaluacionField.getValue() != null ? aperturaEvaluacionField.getValue().atStartOfDay() : null);
                cartera.setFecha_cierre_evaluacion(cierreEvaluacionField.getValue() != null ? cierreEvaluacionField.getValue().atStartOfDay() : null);

                cartera.setPresupuesto_total(presupuestoField.getValue().floatValue());
                cartera.setN_max_tecnicos(n_tecnicosField.getValue());
                cartera.setN_horas(n_horasField.getValue().floatValue());

                // Actualiza la cartera en la base de datos
                carteraService.updateCartera(cartera.getId(), cartera);
                updateGrid();

                dialog.close();
                common.showSuccessNotification("Cartera actualizada con éxito");
            } catch (IllegalArgumentException e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout layout = new HorizontalLayout();

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        layout.add(saveButton, cancelButton);
        layout.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(formLayout, layout);
        dialog.open();
    }


    private void updateGrid() {
        List<Cartera> carteras = carteraService.getAllCarteras();
        tablaCarteras.setItems(carteras);
    }

    // Método para mostrar el diálogo de confirmación
    private void showDeleteConfirmationDialog(Cartera cartera) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Span message = new Span("¿Desea eliminar esta cartera?");
        Button confirmButton = new Button("Sí", event -> {
            carteraService.deleteCartera(cartera.getId());
            updateGrid();
            common.showSuccessNotification("Cartera eliminada con éxito");
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("No", event -> {
            dialog.close();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(message, buttons);
        dialogLayout.setSizeFull();
        dialogLayout.setSpacing(true);
        dialogLayout.setAlignItems(Alignment.CENTER);
        dialogLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        dialog.add(dialogLayout);
        dialog.open();
    }

}
