package es.uca.iw.carteruca.views.perfil;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.uca.iw.carteruca.models.usuario.Usuario;
import es.uca.iw.carteruca.services.UsuarioService;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.layout.MainLayout;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Perfil")
@Route(value = "/perfil", layout = MainLayout.class)
@RolesAllowed({"Admin", "CIO", "Promotor", "Solicitante", "OTP"})
public class PerfilView extends Composite<VerticalLayout> {
    private final UsuarioService usuarioService;
    private Usuario currentUser;

    @Autowired
    public PerfilView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        // Obtener el usuario actual de la sesión
        this.currentUser = fetchCurrentUser();

        if (currentUser == null) {
            // Si no se encuentra un usuario, redirigir al login
            getUI().ifPresent(ui -> ui.navigate("login"));
            Notification.show("Sesión expirada o no autenticado. Vuelve a iniciar sesión.", 3000, Notification.Position.MIDDLE);
            return; // No se debe cargar la vista si la sesión está expirada
        }

        // Construcción de la vista
        createHeader();
        createProfileForm();
        createFooter();
    }


    private Usuario fetchCurrentUser() {
        // Obtener el nombre de usuario de la sesión
        Object usernameAttribute = VaadinSession.getCurrent().getAttribute("username");

        if (usernameAttribute == null) {
            // Mostrar mensaje si no se encuentra el nombre de usuario en la sesión
            System.out.println("No se encontró el atributo 'username' en la sesión.");
            Notification.show("Sesión expirada o no autenticado. Vuelve a iniciar sesión.", 3000, Notification.Position.MIDDLE);
            return null; // Si no hay sesión, devolver null
        }

        // Convertir el atributo a cadena
        String username = usernameAttribute.toString();
        System.out.println("Usuario encontrado en la sesión: " + username);

        // Obtener el usuario desde el servicio
        return usuarioService.getUserByUsername(username);
    }


    private void createHeader() {
        H2 header = new H2("Datos Personales");

        Avatar avatar = new Avatar(currentUser.getNombre() + " " + currentUser.getApellidos());
        avatar.setImage(currentUser.getFotoPerfil()); // Configura la URL de la imagen si está disponible
        avatar.getElement().getStyle().set("width", "100px").set("height", "100px");

        HorizontalLayout headerLayout = new HorizontalLayout(avatar, header);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(headerLayout);
    }

    private void createProfileForm() {
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre");
        nombreField.setValue(currentUser.getNombre());
        nombreField.setReadOnly(true);

        TextField apellidosField = new TextField("Apellidos");
        apellidosField.setValue(currentUser.getApellidos());
        apellidosField.setReadOnly(true);

        TextField emailField = new TextField("Email");
        emailField.setValue(currentUser.getEmail());
        emailField.setReadOnly(true);

        TextField usernameField = new TextField("Usuario");
        usernameField.setValue(currentUser.getUsername());
        usernameField.setReadOnly(true);

        TextField rolField = new TextField("Rol");
        rolField.setValue(currentUser.getRol().name());
        rolField.setReadOnly(true);

        formLayout.add(nombreField, apellidosField, emailField, usernameField, rolField);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        getContent().add(formLayout);
    }

    private void createFooter() {
        Button volverButton = new Button("Volver");
        volverButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        volverButton.addClickListener(e -> volver());

        HorizontalLayout footerLayout = new HorizontalLayout(volverButton);
        footerLayout.setWidthFull();
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        getContent().add(footerLayout);
    }

    private void volver() {
        // Redirigir según el rol del usuario
        if (currentUser.getRol().name().equals("Admin")) {
            getUI().ifPresent(ui -> ui.navigate(HomeAdminView.class));
        } else {
            getUI().ifPresent(ui -> ui.navigate(HomeSolicitanteView.class));
        }
    }
}


