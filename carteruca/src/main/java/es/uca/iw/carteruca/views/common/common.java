package es.uca.iw.carteruca.views.common;

import com.vaadin.flow.component.Composite;
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
import es.uca.iw.carteruca.models.usuario.Centro;
import es.uca.iw.carteruca.services.CentroService;

public class common {
    public static Div createSquare(String text, VaadinIcon iconType) {
        Div square = new Div();

        HorizontalLayout content = new HorizontalLayout();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Icon icon = iconType.create();
        icon.setSize("40px");
        icon.getStyle().set("color", "hsl(214, 33%, 38%)");

        Span label = new Span(text);
        label.getStyle().set("color","black");
        label.getStyle().set("font-size", "24px");

        content.add(icon, label);
        content.getStyle().set("align-items", "center");
        content.getStyle().set("justify-content", "flex-start");

        // Estilos del cuadrado
        square.getStyle().set("width", "400px");
        square.getStyle().set("height", "100px");
        square.getStyle().set("background-color", "#ffffff");
        square.getStyle().set("border", "1px solid #000");
        square.getStyle().set("display", "flex");
        square.getStyle().set("align-items", "center");
        square.getStyle().set("justify-content", "center");
        square.getStyle().set("cursor", "pointer");
        square.getStyle().set("margin-left", "50px");
        square.getStyle().set("margin-top", "20px");

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

    // Método estático para abrir el diázlogo de crear/editar
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


}
