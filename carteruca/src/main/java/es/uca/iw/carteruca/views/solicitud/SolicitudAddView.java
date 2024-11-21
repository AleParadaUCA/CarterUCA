package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Agregar Solicitud")
@Route(value = "/solicitudes/agregar-solicitud", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante"})
public class SolicitudAddView extends Composite<VerticalLayout> {

    private final TextField titulo = new TextField();
    private final TextField nombre = new TextField();   //nombre corto del proyecto
    private final TextField interesados = new TextField();
    private final TextField objetivos = new TextField();
    private final TextField alcance = new TextField();
    //private final MultiSelectComboBox<String> normativa = new MultiSelectComboBox<>("Normativa");  //multiple hay que disponerlo
    private final ComboBox<String> normativa = new ComboBox<>();
    private final DatePicker fecha_puesta = new DatePicker();
    private final MemoryBuffer memoria = new MemoryBuffer();
    private final ComboBox<String> avalador = new ComboBox<>();

    public SolicitudAddView() {

        // Alineación del layout principal para centrar el contenido
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        // Layout principal
        VerticalLayout principal = new VerticalLayout();
        principal.setWidth("100%");
        principal.setMaxWidth("800px"); // Limitar el ancho del formulario para mejorar su presentación

        // Título
        H2 h2 = new H2("Agregar Solicitud");
        h2.getStyle().set("text-align", "center");  // Alineamos el título al centro

        // Formulario
        FormLayout form = new FormLayout();
        form.setWidthFull(); // El formulario ocupará todo el ancho disponible

	    //Configuracion de los diferentes componentes del formulario
        titulo.setId("Titulo");
        titulo.setLabel("Titulo");

        nombre.setId("Nombre");
        nombre.setLabel("Nombre Corto del Proyecto");

        interesados.setId("Interesados");
        interesados.setLabel("Interesados del Proyecto");

        objetivos.setId("Objetivos");
        objetivos.setLabel("Objetivos del Proyecto");

        alcance.setId("Alcance");
        alcance.setLabel("Alcance");

        normativa.setLabel("Normativa");
        normativa.setPlaceholder("Seleccione Normativa:");
        //funcion para todos los roles

        avalador.setLabel("Avalador");
        avalador.setPlaceholder("Seleccione Avalador:");
        //funcion que tenga todos los avaladores que hay por su rol


        fecha_puesta.setLabel("Fecha");
        fecha_puesta.setPlaceholder("Seleccione Fecha");
        fecha_puesta.setAutoOpen(false);

        Upload upload = new Upload(memoria);
        upload.setAcceptedFileTypes("application/pdf");

        upload.addSucceededListener(event -> {
            Notification.show("Archivo cargado: " + event.getFileName());
        });

        // Botón para iniciar el proceso
        Button submitButton = new Button("Enviar archivo", e -> {
            if (memoria.getFileData() != null) {
                // Aquí se pueden manejar los archivos cargados (por ejemplo, guardarlos en el servidor)
                Notification.show("Archivo cargado exitosamente.");
            }
        });


        // Componentes del formulario
        form.add(titulo, nombre, interesados, objetivos, alcance, normativa, avalador, fecha_puesta,normativa, submitButton);

        form.setColspan(submitButton,2);

        // Ajustar la cantidad de columnas para que se vea bien en diferentes tamaños
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("550px", 2),
                new FormLayout.ResponsiveStep("800px", 4)
        );

        //Guardar
        Button guardar = new Button("Guardar", e -> add());
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout guardarLayout = new HorizontalLayout();
        guardarLayout.setWidth("min-content");
        guardarLayout.add(guardar);
        guardarLayout.setAlignItems(FlexComponent.Alignment.CENTER);


        // Botón "Volver"
        Button volver = new Button("Volver");
        volver.setWidth("min-content");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        volver.addClickListener(e -> UI.getCurrent().navigate("/solicitudes"));

        // Layout para el footer con el botón
        HorizontalLayout footer = new HorizontalLayout(volver);
        footer.setWidthFull();
        footer.add(volver);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Agregar el formulario y el botón al layout principal
        principal.add(h2, form);

        // Agregar el layout principal al contenido
        getContent().add(principal,guardar,footer);
    }

    private void add(){}



}