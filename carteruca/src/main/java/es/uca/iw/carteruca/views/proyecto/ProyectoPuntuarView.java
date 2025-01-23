package es.uca.iw.carteruca.views.proyecto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.carteruca.models.Criterio;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.CriterioService;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Puntuar Proyectos")
@Route(value = "/proyectos/puntuar", layout = MainLayout.class)
@RolesAllowed("CIO")
public class ProyectoPuntuarView extends Composite<VerticalLayout> {

    private final CriterioService criterioService;
    private final ProyectoService proyectoService;
    private final Usuario currentUser;

    private final Grid<Criterio> criterio_tabla = new Grid<>(Criterio.class);
    private final Grid<Proyecto> proyecto_tabla = new Grid<>(Proyecto.class);

    @Autowired
    public ProyectoPuntuarView(CriterioService criterioService,
                               ProyectoService proyectoService,
                               AuthenticatedUser authenticatedUser) {
        this.criterioService = criterioService;
        this.proyectoService = proyectoService;
        this.currentUser = authenticatedUser.get().get();

        common.creartituloComposite("Puntuar Proyectos", this);

        crearTabla();

        getContent().add(common.botones_proyecto());
    }

    private void crearTabla() {

        proyecto_tabla.setEmptyStateText("No hay proyectos que puntuar");
        proyecto_tabla.setWidthFull();
        proyecto_tabla.setHeight("400px");

        proyecto_tabla.removeAllColumns();

        proyecto_tabla.addColumn(proyecto -> proyecto.getSolicitud().getTitulo()).setHeader("Titulo");
        proyecto_tabla.addColumn(Proyecto::getDirector_de_proyecto).setHeader("Director de Proyecto");

        // Handling potential null value for jefe
        proyecto_tabla.addColumn(proyecto -> {
            Usuario jefe = proyecto.getJefe();
            return (jefe != null) ? jefe.getNombre() : "No asignado";
        }).setHeader("Jefe");

        proyecto_tabla.addColumn(common.createToggleDetailsRenderer(proyecto_tabla));
        proyecto_tabla.setItemDetailsRenderer(common.createStaticDetailsRendererProyecto());
        proyecto_tabla.setDetailsVisibleOnClick(true);

        proyecto_tabla.addComponentColumn(proyecto -> {
            Button evaluar = new Button("Evaluar");
            evaluar.getElement().setAttribute("aria-label", "Evaluar");
            evaluar.addClickListener(e -> DialogCriterio(proyecto));
            evaluar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return evaluar;
        });

        List<Proyecto> proyectos = proyectoService.getProyectosSinPuntuacion();
        proyecto_tabla.setItems(proyectos);

        ListDataProvider<Proyecto> dataProvider = new ListDataProvider<>(proyectos);
        proyecto_tabla.setDataProvider(dataProvider);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar...");
        searchField.setWidth("50%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener(event -> {
           String search = searchField.getValue();
           dataProvider.setFilter(proyecto -> {
               String titulo = proyecto.getSolicitud().getTitulo().toLowerCase();
               return titulo.contains(search);
           });
        });

        getContent().add(searchField, proyecto_tabla);
    }

    private void DialogCriterio(Proyecto proyecto) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(proyecto.getSolicitud().getTitulo());

        List<Criterio> criterios = criterioService.getAllCriterios();

        // Listas para almacenar IDs de criterios y puntuaciones
        List<Long> idsCriterios = new ArrayList<>();
        List<Float> puntuaciones = new ArrayList<>(Collections.nCopies(criterios.size(), null));

        // Llenar la lista de IDs de criterios
        for (Criterio criterio : criterios) {
            idsCriterios.add(criterio.getId());
        }

        // Grid de criterios
        Grid<Criterio> gridCriterios = new Grid<>(Criterio.class, false);
        gridCriterios.addColumn(Criterio::getDescripcion).setHeader("Descripción");

        // Añadir columna para NumberField (puntuación)
        gridCriterios.addComponentColumn(criterio -> {
            NumberField numberField = new NumberField();
            numberField.setMin(0);
            numberField.setMax(10);
            numberField.setValue(null);
            numberField.setWidth("100%");
            numberField.setPlaceholder("Puntuar");
            numberField.addValueChangeListener(event -> {
                Float valor = event.getValue() != null ? event.getValue().floatValue() : null;
                if (valor != null) {
                    int index = criterios.indexOf(criterio);
                    if (index != -1) {
                        puntuaciones.set(index, valor);
                    }
                }
            });
            return numberField;
        }).setHeader("Puntuación");

        gridCriterios.setItems(criterios);

        // Botón guardar
        Button guardarButton = new Button("Guardar", e -> {
            try {
                if (puntuaciones.contains(null)) {
                    common.showErrorNotification("Debe completar todas las puntuaciones.");
                    return;
                }

                proyectoService.guardarPuntuaciones(proyecto, idsCriterios, puntuaciones);
                common.showSuccessNotification("Puntuaciones guardadas correctamente.");

                // Actualizar la tabla de proyectos
                List<Proyecto> proyectosActualizados = proyectoService.getProyectosSinPuntuacion();
                proyecto_tabla.setItems(proyectosActualizados);

                dialog.close();
            } catch (Exception ex) {
                // Manejar excepciones
                common.showErrorNotification("Error al guardar las puntuaciones. Intente de nuevo." + ex);

            }
        });

        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelarButton = new Button("Cancelar", e -> dialog.close());
        cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button volverButton = new Button("Volver", e -> dialog.close());
        volverButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botones = new HorizontalLayout(guardarButton, cancelarButton);
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        botones.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout botonesLayout = new HorizontalLayout(volverButton, botones);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Justificar "Volver" a la izquierda y los demás a la derecha
        botonesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente

        dialog.add(gridCriterios, botonesLayout);
        dialog.setWidthFull();
        dialog.open();
    }

}
