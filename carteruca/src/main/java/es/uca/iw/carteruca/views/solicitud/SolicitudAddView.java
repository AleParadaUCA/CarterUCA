package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

    private H1 title = new H1();
    private TextField titulo = new TextField();
    private TextField intersesados = new TextField();
    private TextField alimiamiento  = new TextField();
    private TextField alcance = new TextField();
    private TextField normativa = new TextField();
    private MemoryBuffer memoria = new MemoryBuffer();

    public SolicitudAddView() {

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        title.setText("Agregar Solicitud");

        getContent().add(title);

        FormLayout formulario = new FormLayout();
        formulario.setWidth("100%");

        titulo.setId("Titulo");
        intersesados.setId("Intersesados");
        alimiamiento.setId("Alimiamiento");
        alcance.setId("Alcance");
        normativa.setId("Normativa");


        titulo.setLabel("Titulo");
        titulo.setPlaceholder("Indique un titulo suficientemente descriptivo");
        intersesados.setLabel("Intersesados");
        intersesados.setPlaceholder("Indique el grupo de personas que desean llevar a cabo el proyecto");
        alimiamiento.setLabel("Alimiamiento");
        alimiamiento.setPlaceholder("Indique el objetivo del proyecto");
        alcance.setLabel("Alcance");
        alcance.setPlaceholder("Indique el grupo de personas a las que beneficiará el proyecto");
        normativa.setLabel("Normativa");
        normativa.setPlaceholder("Indique el codigo de la normativa de aplicación");


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

        formulario.add(titulo, intersesados, alimiamiento, normativa, submitButton);
        getContent().add(formulario);








    }
}
