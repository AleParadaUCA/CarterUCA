package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.SolicitudService;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Ver Solicitudes")
@Route(value = "/solicitudes/all-solicitudes", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante", "OTP"})
public class SolicitudSeeView extends Composite<VerticalLayout> {

    private final SolicitudService solicitudService;
    private AuthenticatedUser authenticatedUser;
    private Usuario usuario;

    private Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class, false);

    @Autowired
    public SolicitudSeeView(SolicitudService solicitudService, AuthenticatedUser authenticatedUser) {
        this.solicitudService = solicitudService;
        this.authenticatedUser = authenticatedUser;
        this.usuario = authenticatedUser.get().get();

        crearTitulo("Solicitudes");
        crearTabla();  // Descomentado para que se ejecute
        botones();
    }

    private void crearTitulo(String titulo) {
        VerticalLayout title = new VerticalLayout();
        H2 titulo_nombre = new H2(titulo);
        title.add(titulo_nombre);
        title.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().add(title);  // Cambiar add() por getContent().add()
    }

    private void crearTabla() {
        solicitudes.setEmptyStateText("No hay solicitudes");
        solicitudes.setWidthFull();  // Asegurarse de que el grid ocupe todo el ancho disponible
        solicitudes.setHeight("400px");  // Ajustar la altura si es necesario
        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Título");

        // Agregar la columna de Estado con el Badge filtrado por usuario
        solicitudes.addColumn(new ComponentRenderer<>(solicitud -> {
            return createBadgeForEstado(solicitud.getEstado()); // Devuelve el componente Span
        })).setHeader("Estado");

        // Agregar un botón para alternar detalles
        solicitudes.addColumn(createToggleDetailsRenderer(solicitudes)).setHeader("Detalles");

        // Renderizar detalles (detalles ocultos por defecto)
        solicitudes.setItemDetailsRenderer(createStaticDetailsRenderer());

        solicitudes.setDetailsVisibleOnClick(true);

        // Obtener todas las solicitudes asociadas al usuario
        List<Solicitud> solicitudesList = solicitudService.getSolicitudesByUsuario(usuario);
        solicitudes.setItems(solicitudesList); // Establecer los datos al grid
        getContent().add(solicitudes);  // Cambiar add() por getContent().add()
    }

    private Span createBadgeForEstado(Estado estado) {
        Span badge = new Span(estado.name());  // Utiliza name() para obtener el valor del enum como String
        badge.getElement().getThemeList().add("badge"); // Aplicar el tema base de Vaadin

        // Estilo personalizado según el estado
        switch (estado) {
            case ACEPTADO:
                badge.getStyle().set("background-color", "green"); // Verde
                badge.getStyle().set("color", "#ffffff");           // Texto blanco
                break;
            case RECHAZADO:
                badge.getStyle().set("background-color", "#dc3545"); // Rojo
                badge.getStyle().set("color", "#ffffff");           // Texto blanco
                break;
            case EN_TRAMITE_AVALADO:
                badge.getStyle().set("background-color", "violet"); // Amarillo
                badge.getStyle().set("color", "#000000");           // Texto negro
                break;
            case EN_TRAMITE:
                badge.getStyle().set("background-color", "orange");
                badge.getStyle().set("color", "#ff0000");
                break;
            case TERMINADO:
                badge.getStyle().set("background-color", "blue");
                badge.getStyle().set("color", "#ffffff");

            default:
                badge.getStyle().set("background-color", "#6c757d"); // Gris
                badge.getStyle().set("color", "#ffffff");           // Texto blanco
                break;
        }

        // Opcional: personalizar tamaño y bordes del badge
        badge.getStyle().set("padding", "0.25em 0.5em");
        badge.getStyle().set("border-radius", "0.5em");
        badge.getStyle().set("font-size", "0.875rem");
        badge.getStyle().set("font-weight", "bold");

        return badge;
    }

    // Crear el renderizador para alternar los detalles
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
            TextField promotor = new TextField("Avalador");
            promotor.setValue(solicitud.getAvalador() != null && solicitud.getAvalador().getNombre() != null ? solicitud.getAvalador().getNombre() : "No asignado");
            promotor.setReadOnly(true);

            // Fecha de solicitud
            TextField fechaSolicitud = new TextField("Fecha de solicitud");
            fechaSolicitud.setValue(solicitud.getFecha_solicitud() != null ? solicitud.getFecha_solicitud().toString() : "No disponible");
            fechaSolicitud.setReadOnly(true);

            // Añadir los campos al FormLayout
            formLayout.add(titulo, nombre, interesados, alineamiento, alcance, normativa, promotor, fechaSolicitud);

            // Crear el layout final para los detalles
            Div detailsLayout = new Div();
            detailsLayout.add(formLayout);

            return detailsLayout;
        });
    }

    private void botones() {
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        Button volver = new Button("Volver");
        volver.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        volver.addClickListener(e -> UI.getCurrent().navigate(SolicitudesMainView.class));
        botones.add(volver);
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        botones.setVerticalComponentAlignment(FlexComponent.Alignment.END);
        getContent().add(botones);
    }
}


