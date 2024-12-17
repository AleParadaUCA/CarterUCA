package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@PageTitle("Modificar Solicitud")
@Route(value = "/solicitudes/update-solicitud", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante", "OTP"})
public class SolicitudUpdateView extends Composite<VerticalLayout> {

    private final SolicitudService solicitudService;
    private final AuthenticatedUser authenticatedUser;
    private final UsuarioService usuarioService;
    private Usuario currentUser;

    private final TextField titulo = new TextField();
    private final TextField nombre = new TextField();   //nombre corto del proyecto
    private final TextField interesados = new TextField();
    private final TextField objetivos = new TextField();
    private final TextField alcance = new TextField();
    private final TextField normativa = new TextField();
    private final DatePicker fecha_puesta = new DatePicker();
    private final ComboBox<Usuario> avaladores = new ComboBox<>();
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();

    private final Grid<Solicitud> solicitud_tabla = new Grid<>(Solicitud.class);

    @Autowired
    public SolicitudUpdateView(SolicitudService solicitudService,
                               AuthenticatedUser authenticatedUser,
                               UsuarioService usuarioService) {
        this.solicitudService = solicitudService;
        this.authenticatedUser = authenticatedUser;
        this.usuarioService = usuarioService;
        currentUser = authenticatedUser.get().get();

        common.creartitulo("Modificar Solicitudes",this);
        crearTabla();
        getContent().add(solicitud_tabla);
        getContent().add(common.botones_solicitud());


    }

    private void crearTabla() {

        solicitud_tabla.removeAllColumns();

        solicitud_tabla.addColumn(Solicitud::getTitulo).setHeader("Titulo del Proyecto");
        solicitud_tabla.addColumn(Solicitud::getNombre).setHeader("Nombre Corto del Proyecto");
        solicitud_tabla.addColumn(new ComponentRenderer<>(solicitud ->
                common.createBadgeForEstado(solicitud.getEstado()))
        ).setHeader("Estado");

        // Columna con botón condicional dependiendo del estado "EN_TRAMITE"
        solicitud_tabla.addColumn(new ComponentRenderer<>(solicitud -> {
            // Verificar si el estado es "EN_TRAMITE"
            if (solicitud.getEstado() == Estado.EN_TRAMITE) {
                Button editarButton = new Button("Editar");
                editarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                editarButton.addClickListener(e -> EditDialog(solicitud));
                return editarButton;
            } else {
                return null; // No renderiza ningún componente si la condición no se cumple
            }
        }));

        // Obtener y asignar los datos de la tabla
        List<Solicitud> lista_solicitudes = solicitudService.getSolicitudesByUsuario(currentUser);
        solicitud_tabla.setItems(lista_solicitudes);

    }


    private void EditDialog(Solicitud solicitud) {
        Dialog dialog = new Dialog();
        FormLayout contenido = new FormLayout();

        H4 tituloSpan = new H4("Modificar Solicitud");

        //Campos de edicion

        titulo.setLabel("Titulo");
        titulo.getElement().setAttribute("aria-label", "Introduzca el titulo");
        titulo.setValue(solicitud.getTitulo());

        nombre.setLabel("Nombre Corto del Proyecto");
        nombre.getElement().setAttribute("aria-label", "Introduzca el nombre Corto del Proyecto");
        nombre.setValue(solicitud.getNombre());
        nombre.setTooltipText("Acrónimo del Nombre del Proyecto");

        Button nombre_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        nombre_toggleTooltip.addClickListener(event -> {
            Tooltip nombreTooltip = nombre.getTooltip();
            if (nombreTooltip != null) {
                nombreTooltip.setOpened(!nombreTooltip.isOpened());
            }
        });

        interesados.setLabel("Interesados del Proyecto");
        interesados.getElement().setAttribute("aria-label", "Introduzca los interesados");
        interesados.setValue(solicitud.getInteresados());
        interesados.setTooltipText("Personas o grupo de personas que quieren que se lleve a cabo el proyecto");

        Button interesados_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        interesados_toggleTooltip.addClickListener(event -> {
            Tooltip interesadosTooltip = interesados.getTooltip();
            if (interesadosTooltip != null) {
                interesadosTooltip.setOpened(!interesadosTooltip.isOpened());
            }
        });

        objetivos.setLabel("Objetivos del Proyecto");
        objetivos.getElement().setAttribute("aria-label", "Introduzca los objetivos");
        objetivos.setValue(solicitud.getAlineamiento());
        objetivos.setTooltipText("Objetivos que quiere conseguir el proyecto");

        Button objetivos_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        objetivos_toggleTooltip.addClickListener(event -> {
            Tooltip objetivosTooltip = objetivos.getTooltip();
            if (objetivosTooltip != null) {
                objetivosTooltip.setOpened(!objetivosTooltip.isOpened());
            }
        });

        alcance.setLabel("Alcance");
        alcance.getElement().setAttribute("aria-label", "Introduzca el alcance");
        alcance.setValue(solicitud.getAlcance());
        alcance.setTooltipText("Grupo de personas a las que beneficiará el proyecto");

        Button alcance_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        alcance_toggleTooltip.addClickListener(event -> {
            Tooltip alcanceTooltip = alcance.getTooltip();
            if (alcanceTooltip != null) {
                alcanceTooltip.setOpened(!alcanceTooltip.isOpened());
            }
        });


        normativa.setLabel("Normativa");
        normativa.getElement().setAttribute("aria-label", "Normativa");
        normativa.setValue(solicitud.getNormativa());
        normativa.setTooltipText("Código que debe cumplir la solicitud");

        Button normativa_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        normativa_toggleTooltip.addClickListener(event -> {
            Tooltip normativaTooltip = normativa.getTooltip();
            if (normativaTooltip != null) {
                normativaTooltip.setOpened(!normativaTooltip.isOpened());
            }
        });

        avaladores.setLabel("Avalador");
        List<Usuario> avalador = usuarioService.getAvaladores();
        avaladores.setItems(avalador);
        avaladores.getElement().setAttribute("aria-label", "Avalador");
        avaladores.setValue(solicitud.getAvalador());
        avaladores.setItemLabelGenerator(Usuario::getNombre);

        fecha_puesta.setLabel("Fecha Máxima de Puesta en Marcha");
        fecha_puesta.setPlaceholder("Seleccione Fecha");
        fecha_puesta.getElement().setAttribute("aria-label", "Seleccione Fecha");
        fecha_puesta.setAutoOpen(false);
        fecha_puesta.setHelperText("Fecha limite de puesta en marcha del proyecto");
        fecha_puesta.setValue(solicitud.getFecha_puesta().toLocalDate());
        fecha_puesta.setTooltipText("Fecha limite en la que el proyecto se tiene que poner en marcha");

        Button fecha_puesta_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        fecha_puesta_toggleTooltip.addClickListener(event -> {
            Tooltip fecha_puestaTooltip = fecha_puesta.getTooltip();
            if (fecha_puestaTooltip != null) {
                fecha_puestaTooltip.setOpened(!fecha_puestaTooltip.isOpened());
            }
        });

        //no esta ni memoria ni avalador

        contenido.add(titulo,nombre,interesados,objetivos,alcance,normativa,fecha_puesta, avaladores);

        Button save = new Button("Guardar", event -> {
            try {
                // Llamar al servicio para actualizar la solicitud
                solicitudService.update_solicitud(
                        solicitud.getId(), // ID de la solicitud
                        titulo.getValue(), // Nuevos valores del formulario
                        nombre.getValue(),
                        fecha_puesta.getValue().atStartOfDay(),
                        interesados.getValue(),
                        objetivos.getValue(),
                        alcance.getValue(),
                        normativa.getValue(),
                        avaladores.getValue()
                );

                // Mostrar un mensaje de confirmación
                common.showSuccessNotification("Solicitud actualizada con exito");

                // Cerrar el diálogo y refrescar la tabla de solicitudes
                dialog.close();
                crearTabla();
            } catch (Exception e) {
                // Manejo de errores
                common.showErrorNotification("Error al actualizar la solicitud: " + e.getMessage());
            }
        });

        Button cancel = new Button("Cancelar",click-> dialog.close());
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setAlignItems(FlexComponent.Alignment.END);
        buttons.setWidthFull();
        VerticalLayout layout = new VerticalLayout(contenido,buttons);
        layout.setSpacing(true);
        layout.setPadding(true);

        dialog.add(tituloSpan,layout);
        dialog.open();

    }


}
