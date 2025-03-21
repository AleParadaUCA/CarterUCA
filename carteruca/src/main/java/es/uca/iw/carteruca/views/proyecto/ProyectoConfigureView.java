package es.uca.iw.carteruca.views.proyecto;

import java.util.List;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Evaluar Proyectos")
@Route(value = "/proyectos/evaluar-proyectos", layout = MainLayout.class)
@RolesAllowed("OTP")
public class ProyectoConfigureView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final UsuarioService usuarioService;

    private final ComboBox<Usuario> otp = new ComboBox<>();
    private final TextField director = new TextField();
    private final NumberField n_horasField = new NumberField();
    private final NumberField presupuesto_valorField = new NumberField();
    private final IntegerField n_tecnicosField = new IntegerField();
    private final Grid<Proyecto> proyectos_tabla = new Grid<>(Proyecto.class);

    private final MultiFileMemoryBuffer especificacionBuffer = new MultiFileMemoryBuffer();
    private final Upload especificacionUpload = new Upload(especificacionBuffer);
    private final MultiFileMemoryBuffer presupuestoBuffer = new MultiFileMemoryBuffer();
    private final Upload presupuestoUpload = new Upload(presupuestoBuffer);

    @Autowired
    public ProyectoConfigureView(ProyectoService proyectoService,
                                 AuthenticatedUser authenticatedUser,
                                 UsuarioService usuarioService) {
        this.proyectoService = proyectoService;
        this.usuarioService = usuarioService;

        common.creartituloComposite("Configurar Proyectos", this);

        crearTabla();

        getContent().add(common.botones_proyecto());
    }

    private void crearTabla() {

        proyectos_tabla.setEmptyStateText("No hay proyectos que necesiten configurarse");
        proyectos_tabla.setWidthFull();
        proyectos_tabla.setHeight("400px");

        proyectos_tabla.removeAllColumns();

        proyectos_tabla.addColumn(proyecto ->
                proyecto.getSolicitud().getTitulo()).setHeader("Título de la Solicitud");

        proyectos_tabla.addColumn(proyecto ->
                proyecto.getSolicitud().getCartera().getNombre()).setHeader("Cartera");

        proyectos_tabla.addColumn(common.createToggleDetailsRenderer(proyectos_tabla));
        proyectos_tabla.setItemDetailsRenderer(common.createStaticDetailsRendererProyecto());
        proyectos_tabla.setDetailsVisibleOnClick(true);

        proyectos_tabla.addComponentColumn(proyecto -> {
            Button configureButton = new Button("Configurar Proyecto");
            configureButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            configureButton.addClickListener(e -> DialogConfigurar(proyecto));
            return configureButton;
        });

        List<Proyecto> lista = proyectoService.getProyectosSinConfigurar();
        proyectos_tabla.setItems(lista);

        ListDataProvider<Proyecto> dataProvider = new ListDataProvider<>(lista);
        proyectos_tabla.setDataProvider(dataProvider);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("50%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener(event -> {
            String searchTerm = event.getValue().trim().toLowerCase();
            dataProvider.setFilter(proyecto -> {
                // Obtener los campos relevantes
                String titulo = proyecto.getSolicitud().getTitulo().toLowerCase();
                String cartera = proyecto.getSolicitud().getCartera().getNombre().toLowerCase();
                String nombreCartera = proyecto.getSolicitud().getCartera().getNombre().toLowerCase();

                // Comprobar si algún campo coincide con el término de búsqueda
                return titulo.contains(searchTerm) || cartera.contains(searchTerm) || nombreCartera.contains(searchTerm);
            });
        });

        getContent().add(searchField, proyectos_tabla);
    }

    private void DialogConfigurar(Proyecto proyecto) {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Configurar Proyecto");

        FormLayout formulario = new FormLayout();

        director.setLabel("Director del Proyecto");
        director.getElement().setAttribute("aria-label", "Director del Proyecto");
        director.setTooltipText("Director que debe dirigir el proyecto");

        Button director_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        director_toggleTooltip.addClickListener(event -> {
            Tooltip directorTooltip = director.getTooltip();
            if (directorTooltip != null) {
                directorTooltip.setOpened(!directorTooltip.isOpened());
            }
        });

        director.setRequiredIndicatorVisible(true);

        otp.setLabel("Jefe de Proyecto");
        otp.getElement().setAttribute("aria-label", "Jefe de Proyecto");
        List<Usuario> user_otp = usuarioService.getOTP();
        otp.setItems(user_otp);
        otp.setItemLabelGenerator(Usuario::getNombre);
        otp.setRequiredIndicatorVisible(true);

        // Se obtiene el total de horas usadas en la cartera
        float totalHorasCartera = proyectoService.sumarHorasByCarteraAndEstado(proyecto.getSolicitud().getCartera().getId());
        float totalPresupuestoCartera = proyectoService.sumarPresupuestoByCartera(proyecto.getSolicitud().getCartera().getId());
        Integer totalntecnicos = proyectoService.sumarN_tecnicosByCarteraAndEstado(proyecto.getSolicitud().getCartera().getId());

        // Definimos los campos para las horas, validando el máximo permitido
        n_horasField.setLabel("Número de Horas");
        n_horasField.setMin(1.0);
        float horasMaximas = proyecto.getSolicitud().getCartera().getN_horas();
        n_horasField.setMax(horasMaximas - totalHorasCartera);  // El máximo de horas será lo que queda disponible en la cartera
        n_horasField.getElement().setAttribute("aria-label", "Numero de Horas");
        n_horasField.setTooltipText("Número de Horas disponibles para este proyecto");
        n_horasField.setRequiredIndicatorVisible(true);

        Button n_horas_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        n_horas_toggleTooltip.addClickListener(event -> {
            Tooltip n_horasTooltip = n_horasField.getTooltip();
            if (n_horasTooltip != null) {
                n_horasTooltip.setOpened(!n_horasTooltip.isOpened());
            }
        });

        // Configuración del campo presupuesto_valor
        presupuesto_valorField.setLabel("Presupuesto Valor");
        presupuesto_valorField.setMin(0.0);
        float presupuestoMaximo = proyecto.getSolicitud().getCartera().getPresupuesto_total();
        presupuesto_valorField.setMax(presupuestoMaximo - totalPresupuestoCartera);
        presupuesto_valorField.getElement().setAttribute("aria-label", "Presupuesto Valor");
        presupuesto_valorField.setTooltipText("Este campo va en relación al PDF de presupuesto");
        presupuesto_valorField.setRequiredIndicatorVisible(true);

        Button presupuesto_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        presupuesto_toggleTooltip.addClickListener(event -> {
            Tooltip presupuesto_Tooltip = n_horasField.getTooltip();
            if (presupuesto_Tooltip != null) {
                presupuesto_Tooltip.setOpened(!presupuesto_Tooltip.isOpened());
            }
        });

        n_tecnicosField.setLabel("Número de Téncicos asignados");
        n_tecnicosField.setMin(1);
        int tecnicostotales = proyecto.getSolicitud().getCartera().getN_max_tecnicos() - totalntecnicos;
        n_tecnicosField.setMax(totalntecnicos);
        n_tecnicosField.getElement().setAttribute("aria-label","Número de Técnicos asignados");
        n_tecnicosField.setTooltipText("Este campo va en relación al número de técnicos que se deben asignar para realizar el proyecto");
        n_tecnicosField.setRequiredIndicatorVisible(true);

        Button n_tecnicos_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        n_tecnicos_toggleTooltip.addClickListener(event -> {
            Tooltip n_tecnicosTooltip = n_tecnicosField.getTooltip();
            if (n_tecnicosTooltip != null) {
                n_tecnicosTooltip.setOpened(false);
            }
        });

        // Configurar los componentes de subida de archivos
        Span especificacion = new Span("Especificación Técnica (20MB)");
        especificacion.getElement().setAttribute("aria-label", "Adjunte la memoria del proyecto");
        especificacion.getStyle()
                .set("font-size", "14px") // Tamaño de fuente
                .set("font-weight", "600") // Negrita
                .set("color", "grey") // Color del texto
                .set("margin-bottom", "8px"); // Espaciado inferior

        especificacionUpload.setDropLabel(new Span("Arrastra tu archivo aquí o haz clic para subir la Especificación Técnica"));
        especificacionUpload.setMaxFiles(1);
        especificacionUpload.setAcceptedFileTypes(".pdf");
        especificacionUpload.setMaxFileSize(20 * 1024 * 1024);

        Span presupuesto = new Span("Presupuesto (20MB)");
        presupuesto.getElement().setAttribute("aria-label", "Adjunte la memoria del proyecto");
        presupuesto.getStyle()
                .set("font-size", "14px") // Tamaño de fuente
                .set("font-weight", "600") // Negrita
                .set("color", "grey") // Color del texto
                .set("margin-bottom", "8px"); // Espaciado inferior

        presupuestoUpload.setDropLabel(new Span("Arrastra tu archivo aquí o haz clic para subir el Presupuesto"));
        presupuestoUpload.setMaxFiles(1);
        presupuestoUpload.setAcceptedFileTypes(".pdf");
        presupuestoUpload.setMaxFileSize(20 * 1024 * 1024);


        // Configuramos el botón de "Guardar" para verificar las horas
        Button guardarButton = new Button("Guardar", event -> {
            float horasNuevas = n_horasField.getValue().floatValue();
            float presupuestoValor = presupuesto_valorField.getValue().floatValue();
            int n_tecnicos = n_tecnicosField.getValue().intValue();
            // Si las horas ingresadas exceden el límite, mostramos un mensaje de error
            if (totalHorasCartera + horasNuevas > horasMaximas) {
                common.showErrorNotification("El número de horas excede el límite permitido en la cartera.");
            } else if (totalPresupuestoCartera + presupuestoValor > presupuestoMaximo) {
                common.showErrorNotification("El Presupuesto excede el límite de la cartera.");
            } else if (totalntecnicos +  n_tecnicos > tecnicostotales ) {
                common.showErrorNotification("El número de técnicos asignados excede el limite de la cartera.");
            } else {
                try {
                    // Si la validación es exitosa, asignamos los valores al proyecto
                    proyecto.setDirector_de_proyecto(director.getValue());

                    Usuario jefeSeleccionado = otp.getValue();
                    if (jefeSeleccionado != null) {
                        proyecto.setJefe(jefeSeleccionado);
                    }

                    // Asignamos las horas al proyecto
                    proyecto.setHoras(horasNuevas);
                    proyecto.setPresupuesto_valor(presupuestoValor);
                    proyecto.setTecnicos_Asignados(n_tecnicos);

                    // Llamamos al servicio para actualizar el proyecto
                    proyectoService.changeProyecto(proyecto, presupuestoBuffer, especificacionBuffer);

                    // Mostrar notificación de éxito
                    common.showSuccessNotification("Proyecto configurado correctamente.");
                    dialog.close();

                    // **Actualizar el Grid después de guardar**
                    List<Proyecto> listaActualizada = proyectoService.getProyectosSinConfigurar();  // Obtener proyectos actualizados
                    proyectos_tabla.setItems(listaActualizada);  // Actualizar la vista del Grid

                } catch (Exception ex) {
                    common.showErrorNotification("Ocurrió un error inesperado: " + ex.getMessage());
                }
            }
        });

        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button volverButton = new Button("Volver", event -> dialog.close());
        volverButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout boton = new HorizontalLayout(guardarButton,cancelButton);
        boton.setSpacing(true);
        boton.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        boton.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout botonesLayout = new HorizontalLayout(volverButton,boton);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Justificar "Volver" a la izquierda y los demás a la derecha
        botonesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente

        formulario.add(director, otp, n_horasField, n_tecnicosField, presupuesto_valorField, especificacion, especificacionUpload, presupuesto, presupuestoUpload);

        formulario.setColspan(director,1);
        formulario.setColspan(otp,1);
        formulario.setColspan(n_horasField,1);
        formulario.setColspan(n_tecnicosField,1);
        formulario.setColspan(especificacion,2);
        formulario.setColspan(presupuesto,2);

        formulario.setColspan(presupuesto, 2);
        formulario.setColspan(especificacion, 2);
        formulario.setColspan(presupuestoUpload, 2);
        formulario.setColspan(especificacionUpload, 2);

        // Añadir el espaciador después de botonesLayout en el layout vertical
        VerticalLayout contenido = new VerticalLayout(formulario, botonesLayout);
        contenido.setPadding(false);
        contenido.setSpacing(false);
        contenido.setSizeFull();

        dialog.add(contenido);
        dialog.setWidth("600px");
        dialog.open();
    }
}