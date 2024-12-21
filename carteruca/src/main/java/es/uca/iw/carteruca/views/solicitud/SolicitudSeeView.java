package es.uca.iw.carteruca.views.solicitud;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
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
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.services.CommonService;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;

@PageTitle("Ver Solicitudes")
@Route(value = "/solicitudes/all-solicitudes", layout = MainLayout.class)
@RolesAllowed({"Promotor","CIO","Solicitante", "OTP"})
public class SolicitudSeeView extends Composite<VerticalLayout> {

    private final SolicitudService solicitudService;
    private final AuthenticatedUser authenticatedUser;
    private final Usuario usuario;

    private final Grid<Solicitud> solicitudes = new Grid<>(Solicitud.class, false);

    @Autowired
    public SolicitudSeeView(SolicitudService solicitudService, AuthenticatedUser authenticatedUser) {
        this.solicitudService = solicitudService;
        this.authenticatedUser = authenticatedUser;
        this.usuario = authenticatedUser.get().get();

        common.creartitulo("Ver Solicitudes",this);
        crearTabla();
        getContent().add(common.botones_solicitud());
    }

    private void crearTabla() {
        solicitudes.setEmptyStateText("No hay solicitudes");
        solicitudes.setWidthFull();  // Asegurarse de que el grid ocupe todo el ancho disponible
        solicitudes.setHeight("400px");  // Ajustar la altura si es necesario
        solicitudes.addColumn(Solicitud::getTitulo).setHeader("Título");

        solicitudes.addColumn(new ComponentRenderer<>(solicitud -> common.createBadgeForEstado(solicitud.getEstado()))).setHeader("Estado");

        // Agregar un botón para alternar detalles
        solicitudes.addColumn(common.createToggleDetailsRenderer(solicitudes)).setHeader("Detalles");

        // Renderizar detalles (detalles ocultos por defecto)
        solicitudes.setItemDetailsRenderer(common.createStaticDetailsRenderer());

        solicitudes.setDetailsVisibleOnClick(true);

        // Obtener todas las solicitudes asociadas al usuario
        List<Solicitud> solicitudesList = solicitudService.getSolicitudesByUsuario(usuario);
        solicitudes.setItems(solicitudesList); // Establecer los datos al grid
        getContent().add(solicitudes);  // Cambiar add() por getContent().add()
    }

}


