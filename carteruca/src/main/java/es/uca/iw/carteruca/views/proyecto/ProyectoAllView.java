package es.uca.iw.carteruca.views.proyecto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.carteruca.models.Cartera;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;

@PageTitle("Proyectos")
@Route(value = "/proyectos", layout = MainLayout.class)
@AnonymousAllowed

public class ProyectoAllView extends Composite<VerticalLayout>  implements BeforeEnterObserver  {

    private final CarteraService carteraService;
    private final ProyectoService proyectoService;

    private final Accordion carteras = new Accordion();
    private String searchValue = null; 

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Obtener el valor del parámetro search
        searchValue = event.getLocation().getQueryParameters()
                .getParameters()
                .getOrDefault("search", List.of(""))
                .stream()
                .findFirst()
                .orElse(null);
        this.carteras.getElement().removeAllChildren();
        loadCarteras();
    }

    @Autowired
    public ProyectoAllView(CarteraService carteraService, ProyectoService proyectoService, AuthenticatedUser authenticatedUser) {

        this.carteraService = carteraService;
        this.proyectoService = proyectoService;

        getContent().setSizeFull();
        getContent().setPadding(true);
        getContent().setSpacing(true);

        common.creartitulo("Proyectos", this);

        carteras.setSizeFull();
        // loadCarteras();
        getContent().add(carteras);
        getContent().add(common.boton_dinamico(authenticatedUser.get().orElse(null)));
    }

    private void loadCarteras() {
        // Obtener todas las carteras del servicio
        List<Cartera> cartera_lista = carteraService.getAllCarteras();

        for (Cartera cartera : cartera_lista) {
            // Layout para mostrar los proyectos
            VerticalLayout proyectosLayout = new VerticalLayout();
            proyectosLayout.setSpacing(true);
            proyectosLayout.setPadding(false);

            // Obtener proyectos finalizados con todos los campos completos para la cartera actual
            List<Proyecto> listaDeProyectos = proyectoService.getProyectosFinalizadosPorCartera(cartera.getId());

            // Filtrar proyectos si hay un valor de búsqueda
            if (searchValue != null && !searchValue.isEmpty()) {
                listaDeProyectos = listaDeProyectos.stream()
                        .filter(proyecto -> proyecto.getSolicitud().getNombre().equalsIgnoreCase(searchValue))
                        .toList();
            }

            if (listaDeProyectos.isEmpty()) {
                proyectosLayout.add(new Span("No hay proyectos para esta cartera."));
            } else {
                // Crear un Grid para mostrar los proyectos
                Grid<Proyecto> proyect = new Grid<>(Proyecto.class, false);
                proyect.addClassName("responsive-grid");

                // Columna de nombre del proyecto con enlace a diálogo
                proyect.addComponentColumn(proyecto -> {
                    Span nombreSpan = new Span(proyecto.getSolicitud().getNombre());
                    nombreSpan.getElement().setAttribute("style", "cursor: pointer; color: var(--lumo-primary-color);"); // Estilo para que se vea como enlace
                    nombreSpan.addClickListener(event -> abrirDialogoSolicitud(proyecto)); // Evento de clic que abre el diálogo
                    return nombreSpan;
                }).setHeader("Nombre del Proyecto");

                // Columna de horas con Badge de colores contrastantes
                proyect.addComponentColumn(proyecto -> {
                    Span badge = new Span(String.valueOf(proyecto.getHoras()));
                    float horas = proyecto.getHoras();
                    String color;
                    if (horas < 20) {
                        color = "#4caf50"; // Verde para pocas horas
                    } else if (horas < 50) {
                        color = "#ffc107"; // Amarillo para horas moderadas
                    } else {
                        color = "#f44336"; // Rojo para muchas horas
                    }
                    common.applyBadgeStyles(badge, color);
                    return badge;
                }).setHeader("Horas");

                // Columna de presupuesto con Badge de colores contrastantes
                proyect.addComponentColumn(proyecto -> {
                    Span badge = new Span(String.valueOf(proyecto.getPresupuesto_valor()));
                    float presupuesto = proyecto.getPresupuesto_valor();
                    String color;
                    if (presupuesto < 50000) {
                        color = "#4caf50"; // Verde para presupuestos bajos
                    } else if (presupuesto < 100000) {
                        color = "#ffc107"; // Amarillo para presupuestos medianos
                    } else {
                        color = "#f44336"; // Rojo para presupuestos altos
                    }
                    common.applyBadgeStyles(badge, color);
                    return badge;
                }).setHeader("Presupuesto");

                // Asignar los proyectos al Grid
                proyect.setItems(listaDeProyectos);

                // Añadir el Grid al layout de proyectos
                proyectosLayout.add(proyect);
            }

            // Añadir la sección de la cartera al Accordion
            carteras.add(cartera.getNombre(), proyectosLayout);
        }
    }

    private void abrirDialogoSolicitud(Proyecto proyecto) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Detalles");

        // Crear el formulario con dos columnas
        FormLayout formLayout = new FormLayout();

        // Crear los campos de texto
        TextField tituloField = new TextField("Título");
        tituloField.setValue(proyecto.getSolicitud().getTitulo());
        tituloField.setReadOnly(true);

        TextField solicitanteField = new TextField("Solicitante");
        solicitanteField.setValue(proyecto.getSolicitud().getSolicitante().getNombre());
        solicitanteField.setReadOnly(true);

        TextField promotorField = new TextField("Promotor");
        promotorField.setValue(proyecto.getSolicitud().getAvalador().getNombre());
        promotorField.setReadOnly(true);

        TextField interesadosField = new TextField("Interesados");
        interesadosField.setValue(proyecto.getSolicitud().getInteresados());
        interesadosField.setReadOnly(true);

        TextField alineamientoField = new TextField("Alineamiento");
        alineamientoField.setValue(proyecto.getSolicitud().getAlineamiento());
        alineamientoField.setReadOnly(true);

        TextField alcanceField = new TextField("Alcance");
        alcanceField.setValue(proyecto.getSolicitud().getAlcance());
        alcanceField.setReadOnly(true);

        TextField normativaField = new TextField("Normativa");
        normativaField.setValue(proyecto.getSolicitud().getNormativa());
        normativaField.setReadOnly(true);

        // Configurar el diseño del formulario para mostrar pares en la misma línea
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 2) // Dos columnas en todas las pantallas
        );

        // Añadir los campos al formulario, con alineamiento ocupando toda la fila
        formLayout.add(tituloField, solicitanteField);
        formLayout.add(promotorField, interesadosField);
        formLayout.add(alineamientoField, 2); // Campo "Alineamiento" ocupa ambas columnas
        formLayout.add(alcanceField, normativaField);

        // Ajustar el ancho del formulario
        formLayout.setWidth("500px");

        // Botón de volver
        Button volverButton = new Button("Volver", click -> dialog.close());
        volverButton.getStyle().set("margin-top", "10px");

        // Disposición vertical para compactar el formulario y el botón
        VerticalLayout contentLayout = new VerticalLayout(formLayout, volverButton);
        contentLayout.setSpacing(false);
        contentLayout.setPadding(false);
        contentLayout.setAlignItems(FlexComponent.Alignment.END); // Alinear el botón al final del formulario

        // Añadir el contenido al diálogo
        dialog.add(contentLayout);

        // Abrir el diálogo
        dialog.open();
    }



}


