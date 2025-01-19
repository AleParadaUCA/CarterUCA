package es.uca.iw.carteruca.views.question;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;

@PageTitle("Preguntas Frecuentes")
@Route(value = "/preguntas-frecuentes", layout = MainLayout.class)
@AnonymousAllowed
public class QuestionsView extends Composite<VerticalLayout> {

    public QuestionsView() {
        Accordion preguntas = new Accordion();

        // Adding questions and answers
        preguntas.add("¿Cómo puedo solicitar un proyecto de TI?",
                new Span("Puedes solicitar un proyecto de TI a través de nuestro sistema web llenando el formulario de solicitud y proporcionando los detalles requeridos."));

        preguntas.add("¿Quiénes son los responsables de aprobar las propuestas?",
                new Span("Los promotores de los proyectos son los encargados de avalar las propuestas presentadas, asegurando que estén alineadas con los objetivos estratégicos de la Universidad."));

        preguntas.add("¿Cómo se evalúa la idoneidad técnica de un proyecto?",
                new Span("La Oficina Técnica de Proyectos evalúa la idoneidad técnica utilizando rúbricas parametrizables, considerando las diferentes alternativas y estimaciones de recursos necesarias."));


        preguntas.add("¿Cómo puedo acceder a la información sobre el estado de los proyectos?",
                new Span("Los usuarios que están registrados son los unicos que pueden ver los progresos"));

        preguntas.add("¿Qué usuario debe indicarle a la hora del registro",
                new Span("Debe utilizarse el proporcionado por la universidad"));
        preguntas.add("¿Donde puedo ver la información sobre la cartera actual?",
                new Span("Puede verla en la pestaña de Cartera"));

        getContent().add(preguntas);
        
    }
}
