package es.uca.iw.carteruca.views.proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.services.ProyectoService;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Reasignar Jefe")
@Route(value = "proyectos/reasignar-jefe", layout = MainLayout.class)
@RolesAllowed("OTP")

public class ProyectoReasignarView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final UsuarioService usuarioService;

    private final Grid<Proyecto> proyectos_tabla = new Grid<>(Proyecto.class);
    private final ComboBox<Usuario> otp = new ComboBox<>();

    @Autowired
    public ProyectoReasignarView(ProyectoService proyectoService,
                                 UsuarioService usuarioService) {
        this.proyectoService = proyectoService;
        this.usuarioService = usuarioService;

        common.creartituloComposite("Reasignar Jefe",this);

        crearTabla();

        getContent().add(common.botones_proyecto());

    }

    private void crearTabla() {

        proyectos_tabla.setEmptyStateText("No hay proyectos a los que reasignar Jefe");
        proyectos_tabla.setWidthFull();
        proyectos_tabla.setHeight("400px");

        proyectos_tabla.removeAllColumns();

        proyectos_tabla.addColumn(proyecto -> proyecto.getSolicitud().getTitulo()).setHeader("Titulo");

        proyectos_tabla.addComponentColumn(proyecto -> {
            Button actualizar = new Button("Reasignar Jefe");
            actualizar.addClickListener(event -> reasignarJefe(proyecto));
            actualizar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return actualizar;
        });

        List<Proyecto> lista = proyectoService.getProyectosSinJefeConDirector();
        proyectos_tabla.setItems(lista);
        getContent().add(proyectos_tabla);
    }

    private void reasignarJefe(Proyecto proyecto) {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Reasignar Jefe");

        FormLayout contenido = new FormLayout();

        otp.setLabel("Seleccionar nuevo jefe");
        List<Usuario> usuariosOTP = usuarioService.getOTP();
        otp.setItems(usuariosOTP);
        otp.setItemLabelGenerator(Usuario::getNombre);
        otp.setRequired(true);

        contenido.add(otp);
        contenido.setColspan(otp, 3);


        Button guardarButton = new Button("Guardar", event -> {
            Usuario nuevoJefe = otp.getValue();
            if (nuevoJefe != null) {
                proyecto.setJefe(nuevoJefe);
                proyectoService.cambiarPorcentaje(proyecto);
                common.showSuccessNotification("Jefe reasignado correctamente");
                dialog.close();
                // Actualizar la tabla de proyectos si es necesario
                actualizarTabla();
            }
        });
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelarButton = new Button("Cancelar", event -> dialog.close());
        cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout botones = new HorizontalLayout(guardarButton, cancelarButton);
        botones.setSpacing(true);
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        botones.setAlignItems(FlexComponent.Alignment.END);

        Button volverButton = new Button("Volver", event -> dialog.close());
        volverButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botonesLayout = new HorizontalLayout(volverButton, botones);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Justificar "Volver" a la izquierda y los demás a la derecha
        botonesLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Alinear verticalmente


        VerticalLayout dialogLayout = new VerticalLayout(contenido, botonesLayout);
        dialogLayout.setWidthFull();
        dialog.add(dialogLayout);
        dialog.open();

    }

    private void actualizarTabla() {
        List<Proyecto> listaActualizada = proyectoService.getProyectosSinJefeConDirector();
        proyectos_tabla.setItems(listaActualizada);
        proyectos_tabla.getDataProvider().refreshAll();
    }
}
