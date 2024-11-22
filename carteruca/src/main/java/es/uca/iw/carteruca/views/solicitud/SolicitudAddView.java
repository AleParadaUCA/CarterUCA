package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.solicitud.Normativa;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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
    private final ComboBox<Normativa> normativa = new ComboBox<>();
    private final DatePicker fecha_puesta = new DatePicker();
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
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

        /*
        List<Normativa> normativas = List.of(
                new Normativa("CLOFMFJOVO"),
                new Normativa("KMCKSCLKCM")
        );
        //Para que salga el nombre
        normativa.setItems(normativas);
        normativa.setItemLabelGenerator(Normativa::getNombre);

         */

        avalador.setLabel("Avalador");
        avalador.setPlaceholder("Seleccione Avalador:");
        //funcion que tenga todos los avaladores que hay por su rol


        fecha_puesta.setLabel("Fecha");
        fecha_puesta.setPlaceholder("Seleccione Fecha");
        fecha_puesta.setAutoOpen(false);
        fecha_puesta.setHelperText("Fecha limite de puesta en marcha");

        Span memoria = new Span("Memoria");
        memoria.getStyle()
                .set("font-size", "14px") // Tamaño de fuente
                .set("font-weight", "600") // Negrita
                .set("color", "grey") // Color del texto
                .set("margin-bottom", "8px"); // Espaciado inferior

        Upload upload = new Upload(buffer);
        upload.setDropLabel(new com.vaadin.flow.component.html.Span("Arrastra tus archivos aquí o haz clic para cargar"));
        upload.setMaxFiles(1); // Límite opcional del número de archivos
        upload.setAcceptedFileTypes(".pdf", ".word"); // Acepta archivos específicos opcionalmente

        // Listener para manejar el evento de subida exitosa
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            // Procesar el archivo
            processFile(inputStream, fileName);
        });

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        // Componentes del formulario
        form.add(titulo, nombre, interesados, objetivos, alcance, normativa, avalador, fecha_puesta,normativa, memoria, upload);

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

        // Verificar si todos los campos están llenos
        HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<?, ?>> changeListener = event -> {
            boolean allFilled = !titulo.isEmpty() &&
                    !nombre.isEmpty() &&
                    !interesados.isEmpty() &&
                    !objetivos.isEmpty() &&
                    !alcance.isEmpty() &&
                    normativa.getValue() != null &&
                    avalador.getValue() != null &&
                    fecha_puesta.getValue() != null;
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
            add();  // Llamar al método add() si el usuario acepta
            confirmDialog.close();  // Cerrar el diálogo
        });

        // Botón "Cancelar"
        Button cancelButton = new Button("Cancelar", event -> {
            confirmDialog.close();  // Solo cierra el diálogo sin realizar ninguna acción
        });

        // Agregar botones al pie del diálogo
        confirmDialog.getFooter().add(acceptButton, cancelButton);

        // Mostrar el diálogo
        confirmDialog.open();
    }

    private void add(){

        Notification.show("Solicitud guardada exitosamente.");
    }

    private void processFile(InputStream inputStream, String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            System.out.println("Contenido del archivo " + fileName + ":");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Procesamiento o impresión de las líneas
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}