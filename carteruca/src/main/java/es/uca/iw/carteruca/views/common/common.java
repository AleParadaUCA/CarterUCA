package es.uca.iw.carteruca.views.common;



import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;

import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.services.CommonService;
import es.uca.iw.carteruca.views.avalar.AvalarMainView;
import es.uca.iw.carteruca.views.home.HomeAdminView;
import es.uca.iw.carteruca.views.home.HomeSolicitanteView;
import es.uca.iw.carteruca.views.home.HomeView;
import es.uca.iw.carteruca.views.proyecto.ProyectoMainView;

public class common {

    /**
     * Crea un componente de tipo {@link Div} que representa un cuadrado estilizado con un texto y un icono.
     * Este cuadrado incluye estilos predefinidos para el diseño y la interacción, y es completamente responsivo.
     *
     * @param text     El texto que se mostrará dentro del cuadrado.
     * @param iconType El tipo de icono que se mostrará, especificado como una instancia de {@link VaadinIcon}.
     * @return Un componente {@link Div} que contiene un icono y un texto dentro de un diseño estilizado.
     */
    public static Div createSquare(String text, VaadinIcon iconType) {
        //Crear el contenedor principal del cuadrado
        Div square = new Div();

        // Crear contenido
        HorizontalLayout content = new HorizontalLayout();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

        Icon icon = iconType.create();
        icon.setSize("40px");
        icon.getStyle().set("color", "hsl(214, 33%, 38%)");

        // Crear y configurar el texto
        Span label = new Span(text);
        label.getStyle().set("color", "black");
        label.getStyle().set("font-size", "24px");

        // Añadir el icono y el texto al contenido
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


    /**
     * Crea un título de tipo {@link H2} y lo agrega al contenido de un {@link Composite} de tipo {@link VerticalLayout}.
     * El título también incluye un atributo {@code aria-label} para accesibilidad, con el valor del título.
     *
     * @param title    El texto del título que se mostrará.
     * @param composite El {@link Composite} donde se añadirá el título dentro de su contenido.
     */
    public static void creartituloComposite(String title, Composite<VerticalLayout> composite) {
        // Crear el título
        H2 titulo = new H2(title);
        titulo.getElement().setAttribute("aria-label", title);

        // Agregar el título al contenido del Composite
        composite.getContent().add(titulo);
    }

    /**
     * Crea un título de tipo {@link H2} y lo agrega a un {@link VerticalLayout}.
     * El título también incluye un atributo {@code aria-label} para accesibilidad, con el valor del título.
     *
     * @param title    El texto del título que se mostrará.
     * @param layout   El {@link VerticalLayout} al que se añadirá el título.
     */
    public static void crearTitulo(String title, VerticalLayout layout) {
        // Crear el título
        H2 titulo = new H2(title);
        titulo.getElement().setAttribute("aria-label", title);

        // Agregar el título al layout
        layout.add(titulo);
    }


    /**
     * Muestra una notificación de éxito en el centro de la pantalla durante 2 segundos.
     * La notificación tendrá un estilo que la indica como exitosa (verde).
     *
     * @param message El mensaje que se mostrará en la notificación.
     */
    public static void showSuccessNotification(String message) {
        // Crear la notificación de éxito
        Notification successNotification = new Notification(message, 2000, Notification.Position.MIDDLE);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.open();
    }

    /**
     * Muestra una notificación de error en el centro de la pantalla durante 4 segundos.
     * La notificación tendrá un estilo que la indica como un error (rojo).
     *
     * @param message El mensaje que se mostrará en la notificación.
     */
    public static void showErrorNotification(String message) {
        // Crear la notificación de error
        Notification errorNotification = new Notification(message, 4000, Notification.Position.MIDDLE);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }


    /**
     * Genera un valor de color hexadecimal único basado en el nombre proporcionado.
     * El color se calcula tomando el valor hash del nombre y utilizando sus componentes para generar
     * un color en formato hexadecimal.
     *
     * @param name El nombre del cual se generará el color. Este nombre se usa para crear un valor hash,
     *             el cual se convierte en un color.
     * @return El valor de color en formato hexadecimal, representado como una cadena de texto
     *         (por ejemplo, "#RRGGBB").
     */
    public static String getColorFromName(String name) {
        // Generar un valor de color hexadecimal basado en el hash del nombre
        int hash = name.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return String.format("#%02X%02X%02X", r, g, b);
    }


    /**
     * Crea un conjunto de botones para la vista administrativa, con un botón "Volver" que redirige al usuario
     * a la vista de inicio de administración.
     *
     * @return Un objeto {@link HorizontalLayout} que contiene los botones de la interfaz administrativa,
     *         en este caso, un botón de "Volver" alineado a la derecha.
     */
    public static HorizontalLayout botones_Admin() {
        // Crear un layout horizontal para los botones
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Crear el botón "Volver"
        Button volver = new Button("Volver", event -> {
            // Redirigir al usuario a la vista HomeAdminView
            UI.getCurrent().navigate("/home-admin");
        });
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volver.getElement().setAttribute("aria-label", "Volver");

        // Añadir el botón al layout
        botones.add(volver);

        // Retornar el layout con el botón
        return botones;
    }


    /**
     * Crea un conjunto de botones para un usuario registrado, con un botón "Volver" que redirige al usuario
     * a la vista de inicio.
     *
     * @return Un objeto {@link HorizontalLayout} que contiene los botones de la interfaz de usuario registrada,
     *         en este caso, un botón de "Volver" alineado a la derecha.
     */
    public static HorizontalLayout botones_Registrado() {
        // Crear un layout horizontal para los botones
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Crear el botón "Volver"
        Button volver = new Button("Volver", event -> UI.getCurrent().navigate("/home"));

        // Añadir el estilo del botón
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Añadir el botón al layout
        botones.add(volver);

        // Retornar el layout con el botón
        return botones;
    }


    /**
     * Crea un conjunto de botones para la vista de solicitudes, con un botón "Volver" que redirige al usuario
     * a la vista de lista de solicitudes.
     *
     * @return Un objeto {@link HorizontalLayout} que contiene los botones de la interfaz de solicitudes,
     *         en este caso, un botón de "Volver" alineado a la derecha.
     */
    public static HorizontalLayout botones_solicitud() {
        // Crear un layout horizontal para los botones
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Crear el botón "Volver"
        Button volver = new Button("Volver", event -> UI.getCurrent().navigate("/solicitudes"));

        // Añadir el estilo del botón
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Añadir el botón al layout
        botones.add(volver);

        // Retornar el layout con el botón
        return botones;
    }


    /**
     * Crea un conjunto dinámico de botones con un solo botón "Volver". El comportamiento del botón varía
     * dependiendo del tipo de usuario (si es nulo o el rol del usuario).
     * Si el usuario es nulo, redirige a la vista de inicio general. Si el usuario tiene rol de "Admin",
     * redirige a la vista de administración. Si el usuario tiene rol de "Solicitante", redirige a la vista
     * de solicitante.
     *
     * @param user El usuario cuya información se usa para determinar la redirección del botón. Puede ser
     *             nulo.
     * @return Un objeto {@link HorizontalLayout} que contiene el botón "Volver" con el comportamiento
     *         dinámico basado en el usuario.
     */
    public static HorizontalLayout boton_dinamico(Usuario user) {
        // Crear un layout horizontal para los botones
        HorizontalLayout botones = new HorizontalLayout();

        // Crear el botón "Volver"
        Button volverButton = new Button("Volver");

        // Añadir el estilo del botón
        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volverButton.getElement().setAttribute("aria-label", "Volver");

        // Configurar el comportamiento del botón según el usuario
        volverButton.addClickListener(event -> {
            if (user == null) {
                UI.getCurrent().navigate(HomeView.class); // Si el usuario es nulo, redirigir al inicio
            } else if (user.getRol().name().equals("Admin")) {
                UI.getCurrent().navigate(HomeAdminView.class); // Si es admin, redirigir a la vista admin
            } else {
                UI.getCurrent().navigate(HomeSolicitanteView.class); // Si es solicitante, redirigir a su vista
            }
        });

        // Configurar el layout
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Añadir el botón al layout
        botones.add(volverButton);

        // Retornar el layout con el botón
        return botones;
    }


    /**
     * Crea un distintivo (badge) visual basado en el estado proporcionado. El distintivo tiene diferentes
     * colores de fondo y estilos de texto según el valor del estado.
     *
     * @param estado El valor del enum {@link Estado} que se utiliza para determinar el color y estilo
     *               del distintivo.
     * @return Un {@link Span} que representa el distintivo con el estilo correspondiente al estado.
     */
    public static Span createBadgeForEstado(Estado estado) {
        // Crear un Span con el nombre del estado como texto
        Span badge = new Span(estado.name());
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
                badge.getStyle().set("background-color", "violet"); // Violeta
                badge.getStyle().set("color", "#000000");           // Texto negro
                break;
            case EN_TRAMITE_AVALADO:
                badge.getStyle().set("background-color", "orange"); // Naranja
                badge.getStyle().set("color", "#ff0000");           // Texto rojo
                break;
            case TERMINADO:
                badge.getStyle().set("background-color", "blue");   // Azul
                badge.getStyle().set("color", "#ffffff");           // Texto blanco
                break;
            case CANCELADO:
                badge.getStyle().set("background-color", "#f44336"); // Rojo coral
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


    /**
     * Crea un botón de "Volver" con un diseño horizontal. Al hacer clic en el botón, el usuario
     * es redirigido a la vista {@link AvalarMainView}.
     *
     * @return Un {@link HorizontalLayout} que contiene el botón "Volver".
     */
    public static HorizontalLayout boton_avalar() {
        // Crear el layout horizontal para el botón
        HorizontalLayout boton = new HorizontalLayout();

        // Configuración del layout
        boton.setWidthFull(); // Ancho completo
        boton.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Justificar contenido a la derecha

        // Crear el botón "Volver"
        Button volver = new Button("Volver");
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY); // Aplicar el estilo terciario
        volver.getElement().setAttribute("aria-label", "Volver"); // Agregar atributo accesible

        // Configurar el evento de clic para redirigir a la vista AvalarMainView
        volver.addClickListener(event -> UI.getCurrent().navigate(AvalarMainView.class));

        // Agregar el botón al layout
        boton.add(volver);
        return boton;
    }


    /**
     * Crea un {@link ComponentRenderer} que genera un botón de alternancia para mostrar u ocultar los detalles
     * de un elemento en una {@link Grid}. El botón alterna la visibilidad de los detalles de una fila de la tabla.
     *
     * @param <T> El tipo de los elementos en la {@link Grid}.
     * @param grid La {@link Grid} a la que se le agregarán los botones de alternancia de detalles.
     * @return Un {@link ComponentRenderer} que genera un botón de alternancia para cada elemento de la {@link Grid}.
     */
    public static <T> ComponentRenderer<Button, T> createToggleDetailsRenderer(Grid<T> grid) {
        return new ComponentRenderer<>(item -> {
            // Crear el botón de alternancia
            Button toggleButton = new Button("Detalles", e -> {
                // Verificar el estado actual de los detalles y alternarlo
                boolean visible = grid.isDetailsVisible(item);
                grid.setDetailsVisible(item, !visible);
            });

            // Aplicar un tema terciario al botón
            toggleButton.getElement().setAttribute("theme", "tertiary");

            return toggleButton;
        });
    }

    /**
     * Crea un {@link ComponentRenderer} que genera un diseño estático con los detalles de una solicitud
     * dentro de un {@link FormLayout}. Los detalles incluyen campos como el título de la solicitud, nombre corto,
     * interesados, objetivos, alcance, normativa, avalador, fecha de solicitud, y un botón para descargar la memoria
     * si está disponible.
     *
     * @return Un {@link ComponentRenderer} que muestra los detalles de la solicitud en un layout estático.
     */
    public static ComponentRenderer<Div,Solicitud> createStaticDetailsRenderer() {
        return new ComponentRenderer<>(solicitud -> {
            FormLayout formLayout = new FormLayout();

            // Título de la solicitud
            TextField titulo = new TextField("Título de la solicitud");
            titulo.setValue(solicitud.getTitulo() != null ? solicitud.getTitulo() : "No disponible");
            titulo.setReadOnly(true);

            // Nombre corto del proyecto
            TextField nombre = new TextField("Nombre corto del proyecto");
            nombre.setValue(solicitud.getNombre() != null ? solicitud.getNombre() : "No disponible");
            nombre.setReadOnly(true);

            // Interesados del proyecto
            TextField interesados = new TextField("Interesados del proyecto");
            interesados.setValue(solicitud.getInteresados() != null ? solicitud.getInteresados() : "No disponibles");
            interesados.setReadOnly(true);

            // Objetivos del proyecto
            TextField alineamiento = new TextField("Objetivos del proyecto");
            alineamiento.setValue(solicitud.getAlineamiento() != null ? solicitud.getAlineamiento() : "No disponibles");
            alineamiento.setReadOnly(true);

            // Alcance del proyecto
            TextField alcance = new TextField("Alcance");
            alcance.setValue(solicitud.getAlcance() != null ? solicitud.getAlcance() : "No disponible");
            alcance.setReadOnly(true);

            // Normativa
            TextField normativa = new TextField("Normativa");
            normativa.setValue(solicitud.getNormativa() != null ? solicitud.getNormativa() : "No disponible");
            normativa.setReadOnly(true);

            // Avalador (promotor)
            TextField promotor = new TextField("Avalador");
            promotor.setValue(solicitud.getAvalador() != null && solicitud.getAvalador().getNombre() != null ? solicitud.getAvalador().getNombre() : "No asignado");
            promotor.setReadOnly(true);

            // Fecha de solicitud
            TextField fechaSolicitud = new TextField("Fecha de solicitud");
            fechaSolicitud.setValue(solicitud.getFecha_solicitud() != null ? solicitud.getFecha_solicitud().toString() : "No disponible");
            fechaSolicitud.setReadOnly(true);

            // Añadir los campos al FormLayout
            formLayout.add(titulo, nombre, interesados, alineamiento, alcance, normativa, promotor, fechaSolicitud);

            // Crear el layout final para los detalles
            Div detailsLayout = new Div();
            detailsLayout.add(formLayout);

            Span memoria = new Span("Memoria");
            memoria.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior
            formLayout.add(memoria);
            formLayout.setColspan(memoria, 2);
            // Botón para descargar el archivo de memoria
            String memoriaPath = solicitud.getMemoria();
            if (memoriaPath != null && !memoriaPath.isEmpty()) {
                Anchor downloadAnchor = CommonService.descargarFile(memoriaPath, "Descargar Memoria");
                formLayout.add(downloadAnchor);
                formLayout.setColspan(downloadAnchor, 1);
            } else {

                Button descargarMemoriaButton = new Button("Descargar Memoria");
                descargarMemoriaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                descargarMemoriaButton.addClassName("disabled-button");
                formLayout.add(descargarMemoriaButton);
                descargarMemoriaButton.setEnabled(false);
                formLayout.setColspan(descargarMemoriaButton, 1);
            }
            detailsLayout.add(formLayout);

            return detailsLayout;
        });
    }

    /**
     * Crea un {@link ComponentRenderer} que genera un diseño estático con los detalles de un proyecto
     * dentro de un {@link FormLayout}. Los detalles incluyen información sobre la solicitud asociada
     * al proyecto, como el título, nombre corto, interesados, objetivos, alcance, normativa, solicitante,
     * avalador, fecha de solicitud y un botón para descargar la memoria si está disponible.
     *
     * @return Un {@link ComponentRenderer} que muestra los detalles de un proyecto en un layout estático.
     */
    public static ComponentRenderer<Div,Proyecto> createStaticDetailsRendererProyecto() {
        return new ComponentRenderer<>(proyecto -> {
            FormLayout formLayout = new FormLayout();

            // Título de la solicitud
            TextField titulo = new TextField("Título de la solicitud");
            titulo.setValue(proyecto.getSolicitud().getTitulo() != null ? proyecto.getSolicitud().getTitulo() : "No disponible");
            titulo.setReadOnly(true);

            // Nombre corto del proyecto
            TextField nombre = new TextField("Nombre corto del proyecto");
            nombre.setValue(proyecto.getSolicitud().getNombre() != null ? proyecto.getSolicitud().getNombre() : "No disponible");
            nombre.setReadOnly(true);

            // Interesados del proyecto
            TextField interesados = new TextField("Interesados del proyecto");
            interesados.setValue(proyecto.getSolicitud().getInteresados() != null ? proyecto.getSolicitud().getInteresados() : "No disponibles");
            interesados.setReadOnly(true);

            // Objetivos del proyecto
            TextField alineamiento = new TextField("Objetivos del proyecto");
            alineamiento.setValue(proyecto.getSolicitud().getAlineamiento() != null ? proyecto.getSolicitud().getAlineamiento() : "No disponibles");
            alineamiento.setReadOnly(true);

            // Alcance del proyecto
            TextField alcance = new TextField("Alcance");
            alcance.setValue(proyecto.getSolicitud().getAlcance() != null ? proyecto.getSolicitud().getAlcance() : "No disponible");
            alcance.setReadOnly(true);

            // Normativa
            TextField normativa = new TextField("Normativa");
            normativa.setValue(proyecto.getSolicitud().getNormativa() != null ? proyecto.getSolicitud().getNormativa() : "No disponible");
            normativa.setReadOnly(true);

            //Solicitante
            TextField solicitante = new TextField("Solicitante");
            solicitante.setValue(proyecto.getSolicitud().getSolicitante()!= null ? proyecto.getSolicitud().getSolicitante().getNombre() : "No disponible");
            solicitante.setReadOnly(true);

            // Avalador (promotor)
            TextField promotor = new TextField("Avalador");
            promotor.setValue(proyecto.getSolicitud().getAvalador() != null && proyecto.getSolicitud().getAvalador().getNombre() != null ? proyecto.getSolicitud().getAvalador().getNombre() : "No asignado");
            promotor.setReadOnly(true);

            // Fecha de solicitud
            TextField fechaSolicitud = new TextField("Fecha de solicitud");
            fechaSolicitud.setValue(proyecto.getSolicitud().getFecha_solicitud() != null ? proyecto.getSolicitud().getFecha_solicitud().toString() : "No disponible");
            fechaSolicitud.setReadOnly(true);

            // Añadir los campos al FormLayout
            formLayout.add(titulo, nombre, interesados, alineamiento, alcance, normativa, solicitante, promotor, fechaSolicitud);

            // Crear el layout final para los detalles
            Div detailsLayout = new Div();
            detailsLayout.add(formLayout);

            Span memoria = new Span("Memoria");
            memoria.getStyle()
                    .set("font-size", "14px") // Tamaño de fuente
                    .set("font-weight", "600") // Negrita
                    .set("color", "grey") // Color del texto
                    .set("margin-bottom", "8px"); // Espaciado inferior
            formLayout.add(memoria,1);
            formLayout.setColspan(memoria, 2);

            // Botón para descargar el archivo de memoria
            String memoriaPath = proyecto.getSolicitud().getMemoria();
            VerticalLayout descargar = new VerticalLayout();
            if (memoriaPath != null && !memoriaPath.isEmpty()) {
                Anchor downloadAnchor = CommonService.descargarFile(memoriaPath, "Descargar Memoria");
                descargar.add(downloadAnchor);
            } else {
                Button descargarMemoriaButton = new Button("Descargar Memoria");
                descargarMemoriaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                descargarMemoriaButton.addClassName("disabled-button");
                descargarMemoriaButton.setEnabled(false);
                descargar.add(descargarMemoriaButton);
            }

            formLayout.add(descargar);
            detailsLayout.add(formLayout);



            return detailsLayout;
        });
    }

    /**
     * Crea un {@link HorizontalLayout} que contiene un botón "Volver" en el extremo derecho de la pantalla.
     * Al hacer clic en el botón, el usuario es redirigido a la vista principal del proyecto.
     *
     * @return Un {@link HorizontalLayout} que contiene el botón "Volver".
     */
    public static HorizontalLayout botones_proyecto() {

        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button volver = new Button("Volver", event -> {
            // Redirigir al usuario a la vista HomeAdminView
            UI.getCurrent().navigate(ProyectoMainView.class);
        });
        volver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volver.getElement().setAttribute("aria-label", "Volver");

        botones.add(volver);
        return botones;
    }

    /**
     * Aplica estilos personalizados a un componente {@link Span} para que se presente como una insignia (badge).
     *
     * @param badge El {@link Span} al que se le aplicarán los estilos. Este componente se configurará como una insignia.
     * @param backgroundColor El color de fondo de la insignia, representado en formato de color CSS (por ejemplo, "green", "#ff0000").
     */
    public static void applyBadgeStyles(Span badge, String backgroundColor) {
        badge.getElement().getStyle().set("font-size", "var(--lumo-font-size-m)");
        badge.getElement().getStyle().set("background-color", backgroundColor);
        badge.getElement().getStyle().set("color", "white");
        badge.getElement().getStyle().set("border-radius", "12px");
        badge.getElement().getStyle().set("padding", "2px 6px");
    }

}
