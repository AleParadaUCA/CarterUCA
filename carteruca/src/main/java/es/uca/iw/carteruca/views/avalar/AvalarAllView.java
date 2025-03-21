package es.uca.iw.carteruca.views.avalar;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.*;
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
    private final Usuario currentUser;
    private final Cartera carteraActual;

    private final Grid<Solicitud> solicitudes_tabla = new Grid<>(Solicitud.class);


    @Autowired
    public AvalarAllView(SolicitudService solicitudService,
                         AuthenticatedUser authenticatedUser, CarteraService carteraService) {
        this.solicitudService = solicitudService;
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

        common.creartituloComposite("Avalar Solicitudes",this);

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

        solicitudes_tabla.addColumn(common.createToggleDetailsRenderer(solicitudes_tabla)).setHeader("Detalles");
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

        ListDataProvider<Solicitud> dataProvider = new ListDataProvider<>(lista);
        solicitudes_tabla.setDataProvider(dataProvider);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("50%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener(event -> {
            String searchTerm = searchField.getValue().trim().toLowerCase();
            dataProvider.setFilter(solicitud -> {
                String titulo = solicitud.getTitulo().toLowerCase();
                String nombre = solicitud.getNombre().toLowerCase();
                return titulo.contains(searchTerm) || nombre.contains(searchTerm);
            });
        });

        getContent().add(searchField, solicitudes_tabla);
    }

    /**
     * Crea un renderer estático que genera una vista detallada de los datos de una solicitud en un formulario de solo lectura.
     * Este renderer incluye información como el título, nombre del proyecto, solicitante, fecha de solicitud,
     * objetivos, alcance, normativa, y proporciona un enlace para descargar la memoria del proyecto si está disponible.
     *
     * @return Un {@link ComponentRenderer} que renderiza un componente {@link Div} con los detalles de la solicitud.
     */
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

            Span memoria = new Span("Memoria");
            formLayout.add(memoria);
            formLayout.setColspan(memoria,2);

            memoria.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior

            // Botón para descargar el archivo de memoria
            String memoriaPath = solicitud.getMemoria();
            if (memoriaPath != null && !memoriaPath.isEmpty()) {
                Anchor downloadAnchor = CommonService.descargarFile(memoriaPath, "Descargar Memoria");
                formLayout.add(downloadAnchor);
                formLayout.setColspan(downloadAnchor,1);

            } else {

                Button descargarMemoriaButton = new Button("Descargar Memoria");
                descargarMemoriaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                descargarMemoriaButton.addClassName("disabled-button");
                formLayout.add(descargarMemoriaButton);
                formLayout.setColspan(descargarMemoriaButton,1);

                descargarMemoriaButton.setEnabled(false);
            }


            detailsLayout.add(formLayout);

            return detailsLayout;
        });
    }

    private void abrirDialogAvalar(Solicitud solicitud) {
        // Crear el diálogo
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Avalar Solicitud");

        dialog.setWidth("40%"); // Tamaño relativo (40% del ancho del contenedor)
        dialog.setHeight("auto"); // Altura ajustada automáticamente al contenido

        FormLayout importancia = new FormLayout();
        // Campo de Importancia
        IntegerField importanciaField = new IntegerField("Importancia");
        importanciaField.setPlaceholder("Ingrese la importancia...");
        importanciaField.setRequired(true);
        importanciaField.setTooltipText("La importancia debe ser un número del 1 al 10");
        importanciaField.setMin(1);
        importanciaField.setMax(10);
        importanciaField.setWidth("50%"); // Asegurar que el campo use todo el ancho disponible

        // Botón para Mostrar/Ocultar Tooltip
        Button importanciaToggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        importanciaToggleTooltip.addClickListener(event -> {
            Tooltip importanciaTooltip = importanciaField.getTooltip();
            if (importanciaTooltip != null) {
                importanciaTooltip.setOpened(!importanciaTooltip.isOpened());
            }
        });

        // Botones de acción
        Button btnSi = new Button("Avalar", e -> {
            if (importanciaField.isEmpty()) {
                importanciaField.setInvalid(true);
                importanciaField.setErrorMessage("La importancia es obligatoria");
            } else if (importanciaField.getValue() < 1 || importanciaField.getValue() > 10) {
                importanciaField.setInvalid(true);
                importanciaField.setErrorMessage("La importancia debe estar entre 1 y 10");
            } else {
                solicitudService.AvalarSolicitud(solicitud, importanciaField.getValue());
                dialog.close();
                common.showSuccessNotification("Solicitud avalada correctamente");
                refrescarTabla();
            }
        });
        btnSi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnNo = new Button("Rechazar", e -> {
            solicitudService.AvalarSolicitud(solicitud, null);
            dialog.close();
            common.showSuccessNotification("Solicitud cancelada");
            refrescarTabla();
        });
        btnNo.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button volver = new Button("Volver", e -> dialog.close());
        volver.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        // Layout para los botones "Avalar" y "Rechazar"
        HorizontalLayout accionesLayout = new HorizontalLayout(btnSi, btnNo);
        accionesLayout.setSpacing(true); // Espacio entre los botones
        accionesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente

        // Layout principal con "Volver" a la izquierda y "Avalar" y "Rechazar" a la derecha
        HorizontalLayout botonesLayout = new HorizontalLayout(volver, accionesLayout);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Justificar "Volver" a la izquierda y los demás a la derecha
        botonesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente

        // FormLayout para campos
        importancia.add(importanciaField);
        importancia.setWidthFull(); // Ajustar ancho completo del FormLayout

        // Añadir componentes al diálogo
        VerticalLayout contentLayout = new VerticalLayout(importancia, botonesLayout);
        contentLayout.setPadding(false); // Ajustar diseño sin relleno extra
        contentLayout.setSpacing(true);
        dialog.add(contentLayout);
        dialog.open();
    }


    private void refrescarTabla() {
        List<Solicitud> lista = solicitudService.getSolicitudesByPromotor(currentUser);
        solicitudes_tabla.setItems(lista);
    }




}
