package es.uca.iw.carteruca.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;  // Usando Paragraph en lugar de P
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;

@PageTitle("CarterUCA")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    public HomeView() {
        VerticalLayout container = new VerticalLayout();
        container.addClassName("responsive-container");

        // Crear el mensaje de bienvenida con estilo
        HorizontalLayout welcomeLayout = new HorizontalLayout();
        welcomeLayout.setWidthFull();
        welcomeLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        welcomeLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H1 welcomeMessage = new H1("¡Bienvenido a CarterUCA!");
        welcomeMessage.getStyle()
                .set("font-size", "72px") // Texto más grande
                .set("color", "#007BFF") // Texto en azul
                .set("margin", "20px 0") // Espaciado arriba y abajo
                .set("text-align", "center");


        // Agregar el mensaje al HorizontalLayout
        welcomeLayout.add(welcomeMessage);
        getContent().add(welcomeLayout);

        HorizontalLayout descriptionLayout = new HorizontalLayout();
        descriptionLayout.setWidthFull();
        descriptionLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        descriptionLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Crear el texto descriptivo debajo del título usando Paragraph
        Paragraph descriptionText = new Paragraph("Aquí encontrarás toda la información sobre proyectos y cartera para gestionar de manera efectiva.");
        descriptionText.getStyle()
                .set("font-size", "18px") // Texto más pequeño
                .set("color", "#555555") // Color gris para el texto
                .set("text-align", "center") // Centrado del texto
                .set("margin", "10px 0") // Espaciado entre el título y la descripción
                .set("font-style", "italic"); // Cursiva
        descriptionLayout.add(descriptionText);

        // Agregar el texto descriptivo debajo del título
        getContent().add(descriptionLayout);

        HorizontalLayout cuadroLayout = new HorizontalLayout();
        cuadroLayout.setWidthFull();
        cuadroLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cuadroLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Crear cuadros con funcionalidad de navegación
        Div proyectos = common.createSquare("Proyectos", VaadinIcon.FILE_TEXT);
        proyectos.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("/proyectos")));

        Div cartera = common.createSquare("Cartera", VaadinIcon.CLIPBOARD);
        cartera.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("cartera")));

        cuadroLayout.add(proyectos,cartera);

        // Agregar los cuadros al contenedor
        container.add(cuadroLayout);

        // Agregar el contenedor al contenido principal
        getContent().add(container);
    }
}
