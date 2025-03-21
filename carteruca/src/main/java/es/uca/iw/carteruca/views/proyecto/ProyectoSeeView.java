package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Consultar Proyectos")
@Route(value = "/consultar-proyectos", layout = MainLayout.class)
@RolesAllowed({"Solicitante", "CIO", "OTP", "Promotor"})

public class ProyectoSeeView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;
    private final ProyectoService proyectoService;

    private final Usuario currentUser;

    private final Grid<Proyecto> proyectos_tabla = new Grid<>(Proyecto.class);

    public ProyectoSeeView(AuthenticatedUser authenticatedUser,
                           ProyectoService proyectoService) {
        this.authenticatedUser = authenticatedUser;
        this.proyectoService = proyectoService;
        this.currentUser = authenticatedUser.get().get();

        common.creartituloComposite("Consultar Proyectos",this);

        crearTabla();

        getContent().add(common.boton_dinamico(currentUser));
    }

    private void crearTabla() {
        proyectos_tabla.setEmptyStateText("No hay proyectos");
        proyectos_tabla.setWidthFull();
        proyectos_tabla.setHeight("400px");
        proyectos_tabla.removeAllColumns();

        proyectos_tabla.addColumn(proyecto -> proyecto.getSolicitud().getTitulo()).setHeader("Titulo");

        proyectos_tabla.addColumn(proyecto -> proyecto.getSolicitud().getCartera().getNombre()).setHeader("Cartera");

        proyectos_tabla.addColumn(common.createToggleDetailsRenderer(proyectos_tabla));
        proyectos_tabla.setItemDetailsRenderer(createStaticDetailsRendererSee());
        proyectos_tabla.setDetailsVisibleOnClick(true);

        List<Proyecto> proyectoList = proyectoService.findAllByEstadoAndSolicitante(currentUser);
        proyectos_tabla.setItems(proyectoList);

        ListDataProvider<Proyecto> dataProvider = new ListDataProvider<>(proyectoList);
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

    /**
     * Crea un {@link ComponentRenderer} que renderiza los detalles de un proyecto en un {@link FormLayout}.
     * Muestra campos con la información del proyecto como presupuesto, horas, jefe de proyecto, técnicos asignados,
     * director de proyecto, cartera del proyecto y el porcentaje de avance en el proyecto junto con una barra de progreso.
     * Los campos son de solo lectura y se organizan en columnas para una mejor presentación.
     *
     * @return {@link ComponentRenderer} que renderiza un contenedor {@link Div} con la información del proyecto.
     */
    private ComponentRenderer<Div, Proyecto> createStaticDetailsRendererSee() {
        return new ComponentRenderer<>(proyecto -> {
            FormLayout formLayout = new FormLayout();

            // Campo: Presupuesto
            NumberField presupuesto = new NumberField("Presupuesto");
            presupuesto.setValue(proyecto.getPresupuesto_valor().doubleValue());
            presupuesto.setReadOnly(true);

            // Campo: Horas
            NumberField horas = new NumberField("Horas");
            horas.setValue(Double.valueOf(proyecto.getHoras()));
            horas.setReadOnly(true);

            // Campo: Jefe de Proyecto
            TextField jefe = new TextField("Jefe de Proyecto");
            jefe.setValue(proyecto.getJefe().getNombre());
            jefe.setReadOnly(true);

            IntegerField n_tecnicosField = new IntegerField("Número de Tecnicos Asignados");
            n_tecnicosField.setValue(proyecto.getTecnicos_Asignados());
            n_tecnicosField.setReadOnly(true);

            // Campo: Director de Proyecto
            TextField director = new TextField("Director de Proyecto");
            director.setValue(proyecto.getDirector_de_proyecto());
            director.setReadOnly(true);

            TextField cartera = new TextField("Cartera del Proyecto");
            cartera.setValue(proyecto.getSolicitud().getCartera().getNombre());
            cartera.setReadOnly(true);

            // Campo: Porcentaje y ProgressBar
            Span porcentaje = new Span("Porcentaje");
            porcentaje.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-right", "10px"); // Separación a la derecha

            ProgressBar porcentaje_bar = new ProgressBar();
            porcentaje_bar.setValue(proyecto.getPorcentaje() / 100);
            porcentaje_bar.setWidth("30%"); // ProgressBar más pequeño

            // Contenedor para Porcentaje y ProgressBar
            Div porcentajeLayout = new Div(porcentaje, porcentaje_bar);
            porcentajeLayout.getStyle().set("display", "flex").set("align-items", "center");

            // Añadir campos al FormLayout
            formLayout.add(presupuesto, horas, n_tecnicosField, jefe, director, cartera, porcentajeLayout);

            // Configurar distribución en columnas
            formLayout.setColspan(presupuesto, 1);
            formLayout.setColspan(horas, 1);
            formLayout.setColspan(n_tecnicosField, 1);
            formLayout.setColspan(jefe, 1);
            formLayout.setColspan(director, 1);
            formLayout.setColspan(porcentajeLayout, 2); // Abarca dos columnas

            // Crear el layout final para los detalles
            Div detailsLayout = new Div();
            detailsLayout.add(formLayout);

            return detailsLayout;
        });
    }

}
