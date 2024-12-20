package es.uca.iw.carteruca.views.common;



import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.services.CentroService;
import es.uca.iw.carteruca.views.avalar.AvalarMainView;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import es.uca.iw.carteruca.views.home.HomeView;

public class common {
    public static Div createSquare(String text, VaadinIcon iconType) {
        Div square = new Div();

        // Crear contenido
        HorizontalLayout content = new HorizontalLayout();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

        Icon icon = iconType.create();
        icon.setSize("40px");
        icon.getStyle().set("color", "hsl(214, 33%, 38%)");

        Span label = new Span(text);
        label.getStyle().set("color", "black");
        label.getStyle().set("font-size", "24px");

        content.add(icon, label);
        content.getStyle().set("align-items", "center");
        content.getStyle().set("justify-content", "flex-start");

        // Estilos del cuadrado
        square.getStyle().set("background-color", "#ffffff");
        square.getStyle().set("border", "1px solid #000");
        square.getStyle().set("display", "flex");
        square.getStyle().set("align-items", "center");
        square.getStyle().set("justify-content", "center");
        square.getStyle().set("cursor", "pointer");
        square.getStyle().set("margin", "10px");
        square.getStyle().set("transition", "all 0.3s ease-in-out");

        // Estilos responsivos
        square.getStyle().set("width", "100%"); // Ancho completo por defecto
        square.getStyle().set("max-width", "400px"); // Limitar tamaño en pantallas grandes
        square.getStyle().set("height", "100px");

        // Agregar el contenido al cuadrado
        square.add(content);

        return square;
    }


    public static void creartitulo(String title, Composite<VerticalLayout> composite) {
        // Crear el título
        H2 titulo = new H2(title);
        titulo.getElement().setAttribute("aria-label", title);

        // Agregar el título al contenido del Composite
        composite.getContent().add(titulo);
    }

    public static void crearTitulo(String title, VerticalLayout layout) {
        H2 titulo = new H2(title);
        titulo.getElement().setAttribute("aria-label", title);
        layout.add(titulo);
    }

    // Metodo estático para abrir el diálogo de crear/editar
    public static void openDialog(String title, String nombreCentro, String acronimoCentro, Centro centro, boolean isEdit,
                                  CentroService centroService, Runnable updateGrid) {
        Dialog dialog = new Dialog();
        TextField nombre = new TextField("Nombre del Centro", nombreCentro);
        nombre.getElement().setAttribute("aria-label", "Nombre del Centro");
        TextField acronimo = new TextField("Acrónimo del Centro", acronimoCentro);
        acronimo.getElement().setAttribute("aria-label", "Acrónimo del Centro");

        // Crear los botones
        Button guardar = new Button("Guardar", event -> {
            if (!nombre.getValue().trim().isEmpty() && !acronimo.getValue().trim().isEmpty()) {
                if (isEdit) {
                    // Si es edición, actualizamos el centro
                    centro.setNombre(nombre.getValue().trim());
                    centro.setAcronimo(acronimo.getValue().trim());
                    centroService.updateCentro(centro); // Actualizar el centro
                    showSuccessNotification("Centro modificado con éxito");
                } else {
                    // Si es creación, creamos un nuevo centro
                    Centro nuevoCentro = new Centro();
                    nuevoCentro.setNombre(nombre.getValue().trim());
                    nuevoCentro.setAcronimo(acronimo.getValue().trim());
                    centroService.addCentro(nuevoCentro); // Guardar el centro
                    showSuccessNotification("Centro añadido con éxito");
                }
                updateGrid.run(); // Refrescar la tabla
                dialog.close();
            } else {
                Notification.show("Todos los campos son obligatorios", 2000, Notification.Position.BOTTOM_START);
            }
        });
        guardar.getElement().setAttribute("aria-label", "Guardar");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        cancelar.getElement().setAttribute("aria-label", "Cancelar");

        // Layout para los botones
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar);
        VerticalLayout dialogLayout = new VerticalLayout(nombre, acronimo, botones);
        dialogLayout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);

        dialog.add(dialogLayout);
        dialog.open();
    }

    public static void showSuccessNotification(String message) {
        Notification successNotification = new Notification(message, 2000, Notification.Position.MIDDLE);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.open();
    }
    public static void showErrorNotification(String message) {
        Notification errorNotification = new Notification(message, 4000, Notification.Position.MIDDLE);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    public static String getColorFromName(String name) {
        // Generar un valor de color hexadecimal basado en el hash del nombre
        int hash = name.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static HorizontalLayout botones_Admin(){

        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);


        Button volver = new Button("Volver", event -> {
            // Redirigir al usuario a la vista HomeAdminView
            UI.getCurrent().navigate("/home-admin");
        });
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volver.getElement().setAttribute("aria-label", "Volver");

        botones.add(volver);
        return botones;
    }

    public static HorizontalLayout botones_Registrado(){
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button volver = new Button("Volver", event -> {
            UI.getCurrent().navigate("/home");
        });

        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        botones.add(volver);
        return botones;
    }

    public static HorizontalLayout botones_solicitud(){
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button volver = new Button("Volver", event -> {
            UI.getCurrent().navigate("/solicitudes");
        });

        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        botones.add(volver);
        return botones;
    }

    public static HorizontalLayout boton_dinamico(Usuario user){
        HorizontalLayout botones = new HorizontalLayout();

        Button volverButton = new Button("Volver");

        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volverButton.getElement().setAttribute("aria-label", "Volver");

        volverButton.addClickListener(event -> {
        if(user == null){
            UI.getCurrent().navigate(HomeView.class);
        } else if(user.getRol().name().equals("Admin")){
            UI.getCurrent().navigate(HomeAdminView.class);
            } else {
                UI.getCurrent().navigate(HomeSolicitanteView.class);
            }
        });
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        botones.add(volverButton);
        return botones;
    }

    public static Span createBadgeForEstado(Estado estado) {
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
            case EN_TRAMITE:
                badge.getStyle().set("background-color", "violet"); // Amarillo
                badge.getStyle().set("color", "#000000");           // Texto negro
                break;
            case EN_TRAMITE_AVALADO:
                badge.getStyle().set("background-color", "orange");
                badge.getStyle().set("color", "#ff0000");
                break;
            case TERMINADO:
                badge.getStyle().set("background-color", "blue");
                badge.getStyle().set("color", "#ffffff");
                break;
            case CANCELADO:
                badge.getStyle().set("background-color", "#f44336"); // Rojo coral para cancelado
                badge.getStyle().set("color", "#ffffff");           // Texto blanco
                break;
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

    public static HorizontalLayout boton_avalar(){
        HorizontalLayout boton = new HorizontalLayout();

        boton.setWidthFull();
        boton.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button volver = new Button("Volver");
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volver.getElement().setAttribute("aria-label", "Volver");
        volver.addClickListener(event -> UI.getCurrent().navigate(AvalarMainView.class));

        boton.add(volver);
        return boton;

    }


}
