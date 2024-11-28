package es.uca.iw.carteruca.views.registro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uca.iw.carteruca.models.usuario.Rol;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.home.HomeView;
import es.uca.iw.carteruca.views.layout.MainLayout;

@AnonymousAllowed
@PageTitle("Registro")
@Route(value = "/registro", layout = MainLayout.class)
public class RegistroView extends Composite<VerticalLayout> {


    private final TextField nombre = new TextField();
    private final TextField apellidos = new TextField();
    private final TextField usuario = new TextField();
    private final EmailField email = new EmailField();
    private final PasswordField contraseña = new PasswordField();
    private final PasswordField repetir_contraseña = new PasswordField();
    private final UsuarioService userService;
    private ComboBox<String> centro = new ComboBox<>();
    //private ComboBox<Centro> centro = new ComboBox<>();
    //private final BeanValidationBinder<User> binder;
    Button guardar = new Button("Guardar");
    Button volver = new Button("Volver");

    //private final BeanValidationBinder;
    public RegistroView(UsuarioService userService) {

        this.userService = userService;

        // Configuración del layout principal
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        // Layout de columna principal
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");

        // Título
        H2 h2 = new H2("Registro");
        h2.setWidth("100%");

        // Formulario
        FormLayout formLayout2Col = new FormLayout();
        formLayout2Col.setWidth("100%");
        nombre.setId("Nombre");
        apellidos.setId("Apellidos");
        usuario.setId("Usuario");
        email.setId("Email");
        contraseña.setId("Contraseña");
        repetir_contraseña.setId("Repetir Contraseña");
        centro.setId("Centro");


        contraseña.setMinLength(6);
        contraseña.setMaxLength(20);
        contraseña.setHelperText("6-20 letras y numeros");

        repetir_contraseña.setMinLength(6);
        repetir_contraseña.setMaxLength(20);

        nombre.setLabel("Nombre");
        nombre.getElement().setAttribute("aria-label", "Introduzca su nombre");
        apellidos.setLabel("Apellidos");
        apellidos.getElement().setAttribute("aria-label", "Introduzca su apellido");
        usuario.setLabel("Usuario");
        usuario.getElement().setAttribute("aria-label", "Introduzca su usuario");
        email.setLabel("Email");
        email.getElement().setAttribute("aria-label", "Introduzca su email");
        contraseña.setLabel("Contraseña");
        contraseña.getElement().setAttribute("aria-label", "Introduzca su contraseña");
        repetir_contraseña.setLabel("Repetir Contraseña");
        repetir_contraseña.getElement().setAttribute("aria-label", "Repita la contraseña");
        centro.setLabel("Centro");
        centro.getElement().setAttribute("aria-label", "Introduzca el centro");

        centro.setWidth("100px");
        /*
        centro.setItemLabelGenerator(Centro::getNombre);
        centro.setClearButtonVisible(true);
        centro.setPlaceholder("Elige el centro");
        List<Centro> centros_disponibles = userService.findAllRoles();
        centro.setItems(centros_disponibles);
         */
        //centros(centro);

        // Inicializa el binder
        /*
        binder = new BeanValidationBinder<>(User.class);  // Crear el binder
        binder.bindInstanceFields(this); // Enlazar los campos del formulario con la entidad User
        binder.setBean(new usuario()); // Asignar un nuevo objeto User al binder
         */

        // Checkbox para términos y condiciones
        Checkbox checkbox = new Checkbox("He podido leer y entiendo la Política de Privacidad y Cookies");
        checkbox.getElement().setAttribute("aria-label","He podido leer y entiendo la Política de Provacidad y Cookies");

        // Agregar campos al formulario
        formLayout2Col.add(nombre, apellidos, usuario, email, contraseña, repetir_contraseña);
        //formLayout2Col.add(nombre, apellidos, usuario, email, centro, contraseña, repetir_contraseña);

        formLayout2Col.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2),
                new FormLayout.ResponsiveStep("800px", 4) // Aquí establece la cantidad de columnas necesarias
        );

        formLayout2Col.setColspan(nombre, 1);
        formLayout2Col.setColspan(apellidos, 1);
        formLayout2Col.setColspan(usuario, 1);
        formLayout2Col.setColspan(email, 1);
        formLayout2Col.setColspan(centro, 2); // Centro ocupa toda la fila en este caso
        formLayout2Col.setColspan(contraseña, 1);
        formLayout2Col.setColspan(repetir_contraseña, 1);



        // Layout para botones
        guardar.setWidth("min-content");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.setEnabled(false);
        guardar.getElement().setAttribute("aria-label", "Guardar");

        volver.setWidth("min-content");
        volver.addClickListener(e -> UI.getCurrent().navigate(HomeView.class));
        volver.getElement().setAttribute("aria-label", "Volver");

        Runnable actualizarEstadoBoton = () -> {
            boolean contraseñasCoinciden = contraseña.getValue().equals(repetir_contraseña.getValue());
            boolean checkboxMarcado = checkbox.getValue();
            guardar.setEnabled(contraseñasCoinciden && checkboxMarcado);
        };

        // Validación de contraseñas
        repetir_contraseña.addValueChangeListener(e -> {
            if (!contraseña.getValue().equals(repetir_contraseña.getValue())) {
                repetir_contraseña.setInvalid(true);
                repetir_contraseña.setErrorMessage("Las contraseñas no coinciden");
            } else{
                repetir_contraseña.setInvalid(false);
            }
            actualizarEstadoBoton.run();
        });

        // Validación del checkbox
        checkbox.addValueChangeListener(e -> actualizarEstadoBoton.run());

        // Acción para el botón "Guardar"
        guardar.addClickListener(event -> {
            if (contraseña.getValue().equals(repetir_contraseña.getValue())) {
                Notification.show("Registro exitoso");
            } else {
                Notification.show("Las contraseñas no coinciden", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout buttonRow = new HorizontalLayout(volver,guardar);
        buttonRow.setWidth("100%");
        buttonRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Layout de fila principal
        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.add(buttonRow);

        // Agregar componentes al layout principal
        layoutColumn2.add(h2, formLayout2Col, checkbox, layoutRow);
        getContent().add(layoutColumn2);
    }

}
