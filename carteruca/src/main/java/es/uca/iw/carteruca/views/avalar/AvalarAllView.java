package es.uca.iw.carteruca.views.avalar;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.services.CommonService;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@PageTitle("All Solicitudes")
@Route(value ="/avalar-solicitudes/all", layout = MainLayout.class)
@RolesAllowed("Promotor")
public class AvalarAllView extends Composite<VerticalLayout> {

    private final SolicitudService solicitudService;
    private final AuthenticatedUser authenticatedUser;
    private final Usuario currentUser;
    private final Cartera carteraActual;

    private final Grid<Solicitud> solicitudes_tabla = new Grid<>(Solicitud.class);


    @Autowired
    public AvalarAllView(SolicitudService solicitudService,
                         AuthenticatedUser authenticatedUser, CarteraService carteraService) {
        this.solicitudService = solicitudService;
        this.authenticatedUser = authenticatedUser;
        this.currentUser = authenticatedUser.get().get();
        this.carteraActual = carteraService.getCarteraActual().orElse(null);

        if (carteraActual == null) {
            common.showErrorNotification("No hay Cartera para la que Avalar Solicitudes");
            UI.getCurrent().access(() -> UI.getCurrent().navigate(HomeSolicitanteView.class));
            return;
        }

        if (LocalDateTime.now().isAfter(carteraActual.getFecha_cierre_solicitud().toLocalDate().atStartOfDay())){
            common.showErrorNotification("El plazo de avalar solicitudes esta cerrado");
            UI.getCurrent().access(() -> UI.getCurrent().navigate("/home"));
            return;
        }

        getContent().setSizeFull();

        common.creartitulo("Avalar Solicitudes",this);

        crearTabla();

        getContent().add(common.boton_avalar());
    }

    private void crearTabla() {
        solicitudes_tabla.setEmptyStateText("No hay solicitudes");
        solicitudes_tabla.setWidthFull();  // Asegurarse de que el grid ocupe todo el ancho disponible
        solicitudes_tabla.setHeight("400px");  // Ajustar la altura si es necesario

        // Elimina las columnas automáticas generadas
        solicitudes_tabla.removeAllColumns();

        solicitudes_tabla.addColumn(Solicitud::getTitulo).setHeader("Título del Proyecto");
        solicitudes_tabla.addColumn(Solicitud::getNombre).setHeader("Nombre Corto del Proyecto");

        solicitudes_tabla.addColumn(createToggleDetailsRenderer(solicitudes_tabla)).setHeader("Detalles");
        // Renderizar detalles (detalles ocultos por defecto)
        solicitudes_tabla.setItemDetailsRenderer(createStaticDetailsRenderer());
        solicitudes_tabla.setDetailsVisibleOnClick(true);

        solicitudes_tabla.addComponentColumn(solicitud -> {
            Button avalarButton = new Button("Avalar");
            avalarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            avalarButton.addClickListener(e -> abrirDialogAvalar(solicitud));
            return avalarButton;
        });

        refrescarTabla();

        List<Solicitud> lista = solicitudService.getSolicitudesByPromotor(currentUser);
        solicitudes_tabla.setItems(lista);

        getContent().add(solicitudes_tabla);
    }

    // Crear el renderizado para alternar los detalles
    private ComponentRenderer<Button, Solicitud> createToggleDetailsRenderer(Grid<Solicitud> grid) {
        return new ComponentRenderer<>(solicitud -> {
            Button toggleButton = new Button("Detalles", e -> {
                // Alternar la visibilidad de los detalles
                boolean visible = grid.isDetailsVisible(solicitud);
                grid.setDetailsVisible(solicitud, !visible);
            });
            toggleButton.getElement().setAttribute("theme", "tertiary"); // Estilo del botón
            return toggleButton;
        });
    }

    private ComponentRenderer<Div, Solicitud> createStaticDetailsRenderer() {
        return new ComponentRenderer<>(solicitud -> {
            FormLayout formLayout = new FormLayout();

            // Título de la solicitud
            TextField titulo = new TextField("Título de la solicitud");
            titulo.setValue(solicitud.getTitulo() != null ? solicitud.getTitulo() : "No disponible");
            titulo.setReadOnly(true);

            // Nombre corto del proyecto
            TextField nombre = new TextField("Nombre corto del proyecto");
            nombre.setValue(solicitud.getNombre() != null ? solicitud.getNombre() : "No disponible");
            nombre.setReadOnly(true);

            // Interesados del proyecto
            TextField interesados = new TextField("Interesados del proyecto");
            interesados.setValue(solicitud.getInteresados() != null ? solicitud.getInteresados() : "No disponibles");
            interesados.setReadOnly(true);

            // Objetivos del proyecto
            TextField alineamiento = new TextField("Objetivos del proyecto");
            alineamiento.setValue(solicitud.getAlineamiento() != null ? solicitud.getAlineamiento() : "No disponibles");
            alineamiento.setReadOnly(true);

            // Alcance del proyecto
            TextField alcance = new TextField("Alcance");
            alcance.setValue(solicitud.getAlcance() != null ? solicitud.getAlcance() : "No disponible");
            alcance.setReadOnly(true);

            // Normativa
            TextField normativa = new TextField("Normativa");
            normativa.setValue(solicitud.getNormativa() != null ? solicitud.getNormativa() : "No disponible");
            normativa.setReadOnly(true);

            // Avalador (promotor)
            TextField solicitante = new TextField("Solicitante");
            solicitante.setValue(solicitud.getSolicitante() != null && solicitud.getSolicitante().getNombre() != null ? solicitud.getSolicitante().getNombre() : "No asignado");
            solicitante.setReadOnly(true);

            // Fecha de solicitud
            TextField fechaSolicitud = new TextField("Fecha de solicitud");
            fechaSolicitud.setValue(solicitud.getFecha_solicitud() != null ? solicitud.getFecha_solicitud().toString() : "No disponible");
            fechaSolicitud.setReadOnly(true);

            // Añadir los campos al FormLayout
            formLayout.add(titulo, nombre,solicitante, interesados, alineamiento, alcance, normativa, fechaSolicitud);

            // Crear el layout final para los detalles
            Div detailsLayout = new Div();
            detailsLayout.add(formLayout);

            // Botón para descargar el archivo de memoria
            String memoriaPath = solicitud.getMemoria();
            if (memoriaPath != null && !memoriaPath.isEmpty()) {
                Anchor downloadAnchor = CommonService.descargarFile(memoriaPath, "Descargar Memoria");
                formLayout.add(downloadAnchor);
            } else {

                Button descargarMemoriaButton = new Button("Descargar Memoria");
                descargarMemoriaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                descargarMemoriaButton.addClassName("disabled-button");
                formLayout.add(descargarMemoriaButton);
                descargarMemoriaButton.setEnabled(false);
            }
            detailsLayout.add(formLayout);

            return detailsLayout;
        });
    }

    private void abrirDialogAvalar(Solicitud solicitud) {
        // Crear el diálogo
        Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();
        dialog.setHeaderTitle("Avalar Solicitud");

        dialog.setWidth("400px"); // Puedes ajustar el ancho
        dialog.setHeight("250px"); // Puedes ajustar la altura

        // Campo de Importancia
        IntegerField importanciaField = new IntegerField("Importancia");
        importanciaField.setPlaceholder("Ingrese la importancia...");
        importanciaField.setRequired(true);
        importanciaField.setTooltipText("La importancia debe ser un numero del 1 al 10");

        Button importancia_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        importancia_toggleTooltip.addClickListener(event -> {
            Tooltip importancia_tooltip = importanciaField.getTooltip();
            if (importancia_tooltip != null) {
                importancia_tooltip.setOpened(!importancia_tooltip.isOpened());
            }

        });

        importanciaField.setMin(1);
        importanciaField.setMax(10);


        Button btnSi = new Button("Sí", e -> {
            if (importanciaField.isEmpty()) {
                importanciaField.setInvalid(true);
                importanciaField.setErrorMessage("La importancia es obligatoria");
            } else if (importanciaField.getValue() < 1 || importanciaField.getValue() > 10) {
                // Validar que el valor esté entre 1 y 10
                importanciaField.setInvalid(true);
                importanciaField.setErrorMessage("La importancia debe estar entre 1 y 10");
            } else {
                // Actualizar estado a "Avalado" si el valor es válido
                solicitud.setEstado(Estado.EN_TRAMITE_AVALADO);
                solicitud.setImportancia_promotor(importanciaField.getValue());
                solicitudService.updateSolicitud(solicitud);
                dialog.close();
                common.showSuccessNotification("Solicitud avalada correctamente");
                refrescarTabla();  // Refrescar la tabla para ver los cambios
            }
        });
        btnSi.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        // Botón No
        Button btnNo = new Button("No", e -> {
            // Actualizar estado a "Cancelado"
            solicitud.setEstado(Estado.RECHAZADO);
            solicitudService.updateSolicitud(solicitud);
            dialog.close();
            common.showSuccessNotification("Solicitud cancelada");
            refrescarTabla();
        });
        btnNo.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Layout de botones
        HorizontalLayout botonesLayout = new HorizontalLayout(btnSi, btnNo);
        botonesLayout.setSpacing(true);

        // Añadir componentes al diálogo
        dialog.add(importanciaField, botonesLayout);
        dialog.open();
    }

    private void refrescarTabla() {
        List<Solicitud> lista = solicitudService.getSolicitudesByPromotor(currentUser);
        solicitudes_tabla.setItems(lista);
    }




}
