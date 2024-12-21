package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Configurar Proyectos")
@Route(value = "/configurar-proyectos", layout = MainLayout.class)
@RolesAllowed("OTP")
public class ProyectoConfigureView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final AuthenticatedUser authenticatedUser;
    private final UsuarioService usuarioService;
    private Usuario currentUser;

    private final ComboBox<Usuario> otp = new ComboBox<>();
    private final TextField director = new TextField();
    private final NumberField n_horasField = new NumberField();

    private final Grid<Proyecto> proyectos_tabla = new Grid<>(Proyecto.class);

    @Autowired
    public ProyectoConfigureView(ProyectoService proyectoService,
                                 AuthenticatedUser authenticatedUser,
                                 UsuarioService usuarioService) {
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;
        this.usuarioService = usuarioService;
        this.currentUser = authenticatedUser.get().get();;

        common.creartitulo("Configurar Proyectos",this);

        crearTabla();

        common.boton_dinamico(currentUser);

    }

    private void crearTabla() {

        proyectos_tabla.setEmptyStateText("No hay proyectos que necesiten configurarse");
        proyectos_tabla.setWidthFull();
        proyectos_tabla.setHeight("400px");

        proyectos_tabla.removeAllColumns();

        proyectos_tabla.addColumn(proyecto ->
                proyecto.getSolicitud().getTitulo()).setHeader("Título de la Solicitud");

        proyectos_tabla.addColumn(common.createToggleDetailsRenderer(proyectos_tabla));
        proyectos_tabla.setItemDetailsRenderer(common.createStaticDetailsRendererProyecto());
        proyectos_tabla.setDetailsVisibleOnClick(true);

        proyectos_tabla.addComponentColumn(proyecto -> {
            Button configureButton = new Button("Configurar Proyecto");
            configureButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            configureButton.addClickListener(e -> DialogConfigurar(proyecto));
            return configureButton;
        });
        //refrescarTabla();

        List<Proyecto> lista = proyectoService.getProyectosSinConfigurar();
        proyectos_tabla.setItems(lista);
        getContent().add(proyectos_tabla);
    }

    private void DialogConfigurar(Proyecto proyecto) {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Configurar Proyecto");
        dialog.setWidthFull();

        FormLayout formulario = new FormLayout();

        director.setLabel("Director del Proyecto");
        director.getElement().setAttribute("aria-label", "Director del Proyecto");
        director.setTooltipText("Director que debe dirigir el proyecto");

        Button director_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        director_toggleTooltip.addClickListener(event -> {
            Tooltip directorTooltip = director.getTooltip();
            if (directorTooltip != null) {
                directorTooltip.setOpened(!directorTooltip.isOpened());
            }
        });
        director.setRequiredIndicatorVisible(true);

        otp.setLabel("Jefe de Proyecto");
        otp.getElement().setAttribute("aria-label", "Jefe de Proyecto");
        List<Usuario> user_otp = usuarioService.getOTP();
        otp.setItems(user_otp);
        otp.setItemLabelGenerator(usuario -> usuario.getNombre());
        otp.setRequiredIndicatorVisible(true);

        // Se obtiene el total de horas usadas en la cartera
        float totalHorasCartera = proyectoService.sumarHorasByCarteraAndEstado(proyecto.getSolicitud().getCartera().getId());

        // Definimos los campos para las horas, validando el máximo permitido
        n_horasField.setLabel("Número de Horas");
        n_horasField.setMin(1.0);
        float horasMaximas = proyecto.getSolicitud().getCartera().getN_horas();
        n_horasField.setMax(horasMaximas - totalHorasCartera);  // El máximo de horas será lo que queda disponible en la cartera
        n_horasField.getElement().setAttribute("aria-label", "Numero de Horas");
        n_horasField.setTooltipText("Número de Horas disponibles para este proyecto");
        Button n_horas_toggleTooltip = new Button("Mostrar/Ocultar Tooltip");
        n_horas_toggleTooltip.addClickListener(event -> {
            Tooltip n_horasTooltip = n_horasField.getTooltip();
            if (n_horasTooltip != null) {
                n_horasTooltip.setOpened(!n_horasTooltip.isOpened());
            }
        });
        n_horasField.setRequiredIndicatorVisible(true);

        // Configuramos el botón de "Guardar" para verificar las horas
        Button guardarButton = new Button("Guardar", event -> {
            float horasNuevas = n_horasField.getValue().floatValue();
            // Si las horas ingresadas exceden el límite, mostramos un mensaje de error
            if (totalHorasCartera + horasNuevas > horasMaximas) {
                common.showErrorNotification("El número de horas excede el límite permitido en la cartera.");
            } else {
                try {
                    // Si la validación es exitosa, asignamos los valores al proyecto
                    proyecto.setDirector_de_proyecto(director.getValue());

                    Usuario jefeSeleccionado = otp.getValue();
                    if (jefeSeleccionado != null) {
                        proyecto.setJefe(jefeSeleccionado);
                    }

                    // Asignamos las horas al proyecto
                    proyecto.setHoras(horasNuevas);

                    // Llamamos al servicio para actualizar el proyecto
                    proyectoService.updateProyecto(proyecto);

                    // Mostrar notificación de éxito
                    common.showSuccessNotification("Proyecto configurado correctamente.");
                    dialog.close();

                    // **Actualizar el Grid después de guardar**
                    List<Proyecto> listaActualizada = proyectoService.getProyectosSinConfigurar();  // Obtener proyectos actualizados
                    proyectos_tabla.setItems(listaActualizada);  // Actualizar la vista del Grid

                } catch (Exception ex) {
                    common.showErrorNotification("Ocurrió un error inesperado: " + ex.getMessage());
                }
            }
        });

        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout boton = new HorizontalLayout(guardarButton);
        boton.setSizeFull();
        boton.setSpacing(true);
        boton.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        boton.setAlignItems(FlexComponent.Alignment.CENTER);

        formulario.add(director, otp, n_horasField);

        VerticalLayout contenido = new VerticalLayout(formulario, boton);
        contenido.setSizeFull();
        contenido.setSpacing(true);

        dialog.add(contenido);
        dialog.setWidthFull();
        dialog.open();
    }


}
