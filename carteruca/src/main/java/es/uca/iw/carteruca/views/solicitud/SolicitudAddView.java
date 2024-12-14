package es.uca.iw.carteruca.views.solicitud;

import java.time.LocalDateTime;
import java.util.List;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Agregar Solicitud")
@Route(value = "/solicitudes/agregar-solicitud", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante", "OTP"})
public class SolicitudAddView extends Composite<VerticalLayout> {

    private final TextField titulo = new TextField();
    private final TextField nombre = new TextField();   //nombre corto del proyecto
    private final TextField interesados = new TextField();
    private final TextField objetivos = new TextField();
    private final TextField alcance = new TextField();
    private final TextField normativa = new TextField();
    private final DatePicker fecha_puesta = new DatePicker();
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final ComboBox<Usuario> avalador = new ComboBox<>();
    private final SolicitudService solicitudService;
    private final AuthenticatedUser authenticatedUser;
    private final Cartera carteraActual;

    public SolicitudAddView(AuthenticatedUser authenticatedUser, SolicitudService solicitudService, UsuarioService usuarioService, CarteraService carteraService) {
        this.authenticatedUser = authenticatedUser;
        this.solicitudService = solicitudService;
        this.carteraActual = carteraService.getCarteraActual().orElse(null);

        if (carteraActual == null) {
            common.showErrorNotification("No hay Cartera disponible");
            UI.getCurrent().access(() -> UI.getCurrent().navigate("/home"));
            return;
        }

        if ( LocalDateTime.now().isAfter(carteraActual.getFecha_cierre_solicitud().toLocalDate().atStartOfDay())){
            common.showErrorNotification("El plazo de solicitud está cerrado.");
            UI.getCurrent().access(() -> UI.getCurrent().navigate("/home") );
            return;
        }

        // Alineación del layout principal para centrar el contenido
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        // Layout principal
        VerticalLayout principal = new VerticalLayout();
        common.crearTitulo("Agregar Solicitud",principal);
        principal.setWidth("100%");
        principal.setMaxWidth("800px"); // Limitar el ancho del formulario para mejorar su presentación


        // Formulario
        FormLayout form = new FormLayout();
        form.setWidthFull(); // El formulario ocupará todo el ancho disponible

        //Configuración de los diferentes componentes del formulario
        titulo.setId("Titulo");
        titulo.setLabel("Titulo");
        titulo.getElement().setAttribute("aria-label", "Introduzca el titulo");

        nombre.setId("Nombre");
        nombre.setLabel("Nombre Corto del Proyecto");
        nombre.getElement().setAttribute("aria-label", "Introduzca el nombre Corto del Proyecto");

        interesados.setId("Interesados");
        interesados.setLabel("Interesados del Proyecto");
        interesados.getElement().setAttribute("aria-label", "Introduzca los interesados");

        objetivos.setId("Objetivos");
        objetivos.setLabel("Objetivos del Proyecto");
        objetivos.getElement().setAttribute("aria-label", "Introduzca los objetivos");

        alcance.setId("Alcance");
        alcance.setLabel("Alcance");
        alcance.getElement().setAttribute("aria-label", "Introduzca el alcance");

        normativa.setId("Normativa");
        normativa.setLabel("Normativa");
        normativa.getElement().setAttribute("aria-label", "Normativa");

        avalador.setId("Avalador");
        avalador.setLabel("Avalador");
        avalador.setPlaceholder("Seleccione Avalador");
        avalador.getElement().setAttribute("aria-label", "Seleccione al avalador");

        List<Usuario> avaladores = usuarioService.getAvaladores();
        avalador.setItems(avaladores);
        avalador.setItemLabelGenerator(Usuario::getNombre);

        // Configuración del Binder
        BeanValidationBinder<Solicitud> binder = new BeanValidationBinder<>(Solicitud.class);
        binder.forField(avalador)
                .asRequired("Debe seleccionar un Avalador")
                .bind(Solicitud::getAvalador, Solicitud::setAvalador);


        fecha_puesta.setLabel("Fecha");
        fecha_puesta.setPlaceholder("Seleccione Fecha");
        fecha_puesta.getElement().setAttribute("aria-label", "Seleccione Fecha");
        fecha_puesta.setAutoOpen(false);
        fecha_puesta.setHelperText("Fecha limite de puesta en marcha del proyecto");

        Span memoria = new Span("Memoria(20MB)");
        memoria.getElement().setAttribute("aria-label", "Adjunte la memoria del proyecto");
        memoria.getStyle()
                .set("font-size", "14px") // Tamaño de fuente
                .set("font-weight", "600") // Negrita
                .set("color", "grey") // Color del texto
                .set("margin-bottom", "8px"); // Espaciado inferior

        Upload upload = new Upload(buffer);
        upload.setDropLabel(new com.vaadin.flow.component.html.Span("Arrastra tus archivos aquí o haz clic para cargar"));
        upload.setMaxFiles(1); // Límite opcional del número de archivos
        upload.setAcceptedFileTypes(".pdf", ".word"); // Acepta archivos específicos opcionalmente
        upload.setMaxFileSize(20 * 1024 * 1024); // Tamaño máximo de archivo en bytes (20 MB)

        // Componentes del formulario
        form.add(titulo, nombre, interesados, objetivos, alcance, normativa, avalador, fecha_puesta, memoria, upload);

        form.setColspan(memoria,2);
        form.setColspan(upload,2);
        // Ajustar la cantidad de columnas para que se vea bien en diferentes tamaños
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("550px", 2),
                new FormLayout.ResponsiveStep("800px", 4)
        );

        //Guardar
        Button guardar = new Button("Guardar", e -> showConfirmDialog());
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.setEnabled(false); // Comenzamos con el botón deshabilitado
        guardar.getElement().setAttribute("aria-label", "Guardar");

        // Verificar si todos los campos están llenos
        HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<?, ?>> changeListener = event -> {
            boolean allFilled = !titulo.isEmpty() &&
                    !nombre.isEmpty() &&
                    !interesados.isEmpty() &&
                    !objetivos.isEmpty() &&
                    !alcance.isEmpty() &&
                    !normativa.isEmpty() &&
                    avalador.getValue() != null &&
                    fecha_puesta.getValue() != null &&
                    !buffer.getFiles().isEmpty();

            guardar.setEnabled(allFilled);
        };

        // Agregar el listener a cada campo
        titulo.addValueChangeListener(changeListener);
        nombre.addValueChangeListener(changeListener);
        interesados.addValueChangeListener(changeListener);
        objetivos.addValueChangeListener(changeListener);
        alcance.addValueChangeListener(changeListener);
        normativa.addValueChangeListener(changeListener);
        avalador.addValueChangeListener(changeListener);
        fecha_puesta.addValueChangeListener(changeListener);
        upload.addSucceededListener(event -> changeListener.valueChanged(null));

        HorizontalLayout guardarLayout = new HorizontalLayout();
        guardarLayout.setWidth("min-content");
        guardarLayout.add(guardar);
        guardarLayout.setAlignItems(FlexComponent.Alignment.CENTER);



        // Agregar el formulario y el botón al layout principal
        principal.add(form);

        // Agregar el layout principal al contenido
        getContent().add(principal,guardar,common.botones_solicitud());
    }

    private void showConfirmDialog() {
        // Crear el cuadro de diálogo de confirmación
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirmación");
        confirmDialog.setWidth("400px");

        // Mensaje de confirmación
        VerticalLayout content = new VerticalLayout();
        content.add("¿Estás seguro de que deseas guardar esta solicitud?");
        confirmDialog.add(content);

        // Botón "Aceptar"
        Button acceptButton = new Button("Aceptar", event -> {
            guardar();  // Llamar al método guardar() si el usuario acepta
            confirmDialog.close();  // Cerrar el diálogo
        });
        acceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botón "Cancelar"
        Button cancelButton = new Button("Cancelar", event -> {
            confirmDialog.close();  // Solo cierra el diálogo sin realizar ninguna acción
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Agregar botones al pie del diálogo
        confirmDialog.getFooter().add(acceptButton, cancelButton);

        // Mostrar el diálogo
        confirmDialog.open();
    }

    private void guardar(){

        solicitudService.guardar(
                titulo.getValue(),
                nombre.getValue(),
                fecha_puesta.getValue().atStartOfDay(),
                interesados.getValue(),
                objetivos.getValue(),
                alcance.getValue(),
                normativa.getValue(),
                buffer,
                avalador.getValue(),
                authenticatedUser.get().get(),
                carteraActual
        );
        common.showSuccessNotification("Solicitud guardada exitosamente.");
    }
}