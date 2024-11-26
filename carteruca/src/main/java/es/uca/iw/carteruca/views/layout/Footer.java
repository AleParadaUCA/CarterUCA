package es.uca.iw.carteruca.views.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Footer extends Composite<HorizontalLayout> {

    public Footer() {
        //Poner estilo del footer.
        getStyle()
                .setBackground("#384850")
                .set("color", "white")
                .set("width", "100%")
                .set("text-align", "center")
                .set("padding", "10px 0");

        // 1ª Columna: Logo con icono
        Image footerLogo = new Image("layout/logoFooterUCA_05.png", "Logo UCA");
        footerLogo.setHeight("90px");
        Div logoContainer = new Div(footerLogo);
        logoContainer.getStyle()
                .set("border-right", "2px solid white")
                .set("margin-left", "30px")
                .set("margin-right", "30px")
                .set("width", "20%")
                .set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center");

        // 2ª Columna: Dirección con texto más pequeño
        Icon locationIcon = new Icon(VaadinIcon.MAP_MARKER);
        locationIcon.getStyle().set("color", "white");

        Div addressContainer = new Div(
                locationIcon,
                new Div(new Text("Centro Cultural Reina Sofía")),
                new Div(new Text("C/ Paseo Carlos III, nº 9")),
                new Div(new Text("11003, Cádiz")),
                new Div(new Text("CÁDIZ"))
        );
        addressContainer.getStyle()
                .set("font-size", "0.85rem")
                .set("border-right", "2px solid white")
                .set("padding", "0 10px")
                .set("width", "20%")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("justify-content", "center")
                .set("align-items", "center");

        // 3ª Columna: Enlaces de la UCA

        VerticalLayout linksColumn = new VerticalLayout(
                createLink("https://www.uca.es/aviso-legal", "Aviso legal"),
                createLink("https://www.uca.es/accesibilidad", "Accesibilidad"),
                createLink("https://www.uca.es/sitemap/", "Mapa del Sitio"),
                createLink("https://www.uca.es/cookies", "Cookies")
        );
        linksColumn.getStyle()
                .set("font-size", "0.85rem")
                .set("border-right", "2px solid white")
                .set("width", "10%")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("justify-content", "center")
                .set("align-items", "center");

        linksColumn.setAlignItems(FlexComponent.Alignment.CENTER); // Centra el contenido verticalmente
        linksColumn.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centra el contenido horizontalmente



        // 4ª Columna: Redes sociales sin borde derecho
        HorizontalLayout socialLinks = new HorizontalLayout(
                createSocialIcon("https://www.facebook.com/universidaddecadiz/", VaadinIcon.FACEBOOK, "Facebook"),
                createSocialIcon("https://twitter.com/univcadiz", VaadinIcon.TWITTER, "Twitter"),
                createSocialIcon("https://www.instagram.com/univcadiz/", "/layout/instagram.svg", "Instagram"),
                createSocialIcon("http://www.youtube.com/user/videosUCA", VaadinIcon.YOUTUBE, "YouTube")
        );
        socialLinks.getStyle()
                .set("padding", "0 10px")
                .set("margin-right", "20px")
                .set("width", "20%")
                .set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center");


        // Layout del footer
        HorizontalLayout footerLayout = new HorizontalLayout(logoContainer, addressContainer, linksColumn, socialLinks);
        footerLayout.setWidthFull();
        footerLayout.setSpacing(true);
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        footerLayout.getStyle().set("gap", "10px");
        getStyle().set("margin-top", "auto");

        getContent().add(footerLayout);
    }

    private Span createLink(String url, String name) {
        Span link = new Span(name);
        link.getStyle().set("color", "white").set("cursor", "pointer");
        link.addClickListener(e -> {
            UI.getCurrent().getPage().setLocation(url);
        });
        return link;
    }

        private Anchor createSocialIcon(String url, VaadinIcon icon, String altText) {
        Icon socialIcon = new Icon(icon);
        socialIcon.setSize("24px");
        socialIcon.getStyle().set("color", "white");
        socialIcon.getStyle().set("vertical-align", "middle"); // Asegura la alineación vertical
        Anchor anchor = new Anchor(url, socialIcon);
        anchor.setTarget("_blank");
        anchor.getElement().setAttribute("aria-label", altText);
        return anchor;
        }

        private Component createSocialIcon(String url, String iconPath, String altText) {
        Image icon = new Image(iconPath, altText);
        icon.setWidth("28px");
        icon.setHeight("28px");
        icon.getStyle().set("vertical-align", "middle"); // Asegura la alineación vertical
        Anchor anchor = new Anchor(url, icon);
        anchor.setTarget("_blank");
        anchor.getElement().setAttribute("aria-label", altText);
        return anchor;
        }
}
