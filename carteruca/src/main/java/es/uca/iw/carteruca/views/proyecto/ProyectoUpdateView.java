package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dialog.Dialog;
import es.uca.iw.carteruca.models.*;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Actualizar Proyectos")
@Route(value = "/proyectos/update", layout = MainLayout.class)
@RolesAllowed("OTP")
public class ProyectoUpdateView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final SolicitudService solicitudService;
    private final Usuario currentUser;

    private final Grid<Proyecto> proyecto_tabla = new Grid<>(Proyecto.class);
    private final NumberField porcentaje = new NumberField();
    @Autowired
    public ProyectoUpdateView(ProyectoService proyectoService, AuthenticatedUser authenticatedUser,
                              SolicitudService solicitudService) {
        this.proyectoService = proyectoService;
        this.solicitudService = solicitudService;
        this.currentUser = authenticatedUser.get().get();

        common.creartituloComposite("Actualizar Proyecto",this);

        crearTabla();

        getContent().add(common.botones_proyecto());

    }

    private void crearTabla() {

        proyecto_tabla.setEmptyStateText("No hay proyectos");
        proyecto_tabla.setWidthFull();
        proyecto_tabla.setHeight("400px");

        proyecto_tabla.removeAllColumns();

        proyecto_tabla.addColumn(proyecto -> proyecto.getSolicitud().getTitulo()).setHeader("Titulo");

        proyecto_tabla.addComponentColumn(proyecto -> {
            Button modificar = new Button("Actualizar Porcentaje");
            modificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            modificar.addClickListener(event -> updateProyecto(proyecto));
            return modificar;
        });

        List<Proyecto> lista = proyectoService.getProyectosPorJefeYEstado(currentUser);
        proyecto_tabla.setItems(lista);
        getContent().add(proyecto_tabla);

    }

    private void updateProyecto(Proyecto proyecto) {

        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Actualizar Porcentaje");

        FormLayout formLayout = new FormLayout();

        // Establecer el valor mínimo desde el proyecto y el valor máximo fijo a 100
        porcentaje.setLabel("Porcentaje del Proyecto");
        porcentaje.getElement().setAttribute("aria-label", "Porcentaje del Proyecto");
        porcentaje.setTooltipText("Porcentaje del total del Proyecto");
        porcentaje.setMin(proyecto.getPorcentaje());  // Mínimo desde el proyecto
        porcentaje.setMax(100);  // Máximo fijo a 100
        porcentaje.setRequiredIndicatorVisible(true);

        // Mostrar/Ocultar tooltip
        Button porcentaje_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        porcentaje_toggleTooltip.addClickListener(event -> {
            Tooltip directorTooltip = porcentaje.getTooltip();
            if (directorTooltip != null) {
                directorTooltip.setOpened(!directorTooltip.isOpened());
            }
        });

        formLayout.add(porcentaje);

        Button guardar = new Button("Guardar", event -> {
            try {
                // Validar que el porcentaje esté dentro del rango especificado
                if (porcentaje.getValue() != null && porcentaje.getValue() >= proyecto.getPorcentaje() && porcentaje.getValue() <= 100) {

                    if (porcentaje.getValue() == 100) { //IMPORTANTE no tengo claro esto
                        Solicitud solicitud = proyecto.getSolicitud();
                        solicitudService.TerminarSolicitud(solicitud);
                    }
                    
                    proyecto.setPorcentaje(porcentaje.getValue().floatValue());
                    proyectoService.cambiarPorcentaje(proyecto);
                    common.showSuccessNotification("Porcentaje Actualizado");
                    actualizarTabla();
                    dialog.close();
                } else {
                    common.showErrorNotification("El porcentaje debe estar entre " + proyecto.getPorcentaje() + " y 100.");
                }

            } catch (Exception ex) {
                common.showErrorNotification("Porcentaje no actualizado: " + ex.getMessage());
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout boton = new HorizontalLayout(guardar, cancelar);
        boton.setSpacing(true);
        boton.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        boton.setAlignItems(FlexComponent.Alignment.END);

        Button volver = new Button("Volver", e -> dialog.close());
        volver.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botonesLayout = new HorizontalLayout(volver, boton);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Justificar "Volver" a la izquierda y los demás a la derecha
        botonesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente

        dialog.add(formLayout, botonesLayout);
        dialog.open();
    }

    private void actualizarTabla() {
        List<Proyecto> listaActualizada = proyectoService.getProyectosPorJefeYEstado(currentUser);
        proyecto_tabla.setItems(listaActualizada);
    }

}
