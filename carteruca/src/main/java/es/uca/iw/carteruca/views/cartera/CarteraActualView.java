package es.uca.iw.carteruca.views.cartera;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.carteruca.security.AuthenticatedUser;
import es.uca.iw.carteruca.services.CarteraService;
import es.uca.iw.carteruca.views.common.common;
import es.uca.iw.carteruca.views.layout.MainLayout;

@PageTitle("Cartera Actual")
@Route(value ="/cartera", layout = MainLayout.class)
@AnonymousAllowed
public class CarteraActualView extends Composite<VerticalLayout> {

    private final CarteraService carteraService;

    @Autowired
    public CarteraActualView(CarteraService carteraService, AuthenticatedUser authenticatedUser) {
        this.carteraService = carteraService;

        Icon icono = new Icon(VaadinIcon.CLIPBOARD);
        icono.getStyle().set("font-size", "25px");
        H2 titulo = new H2("Cartera Actual");

        HorizontalLayout header = new HorizontalLayout(titulo, icono);
        getContent().add(header);

        loadCarteraActual();

        getContent().add(common.boton_dinamico(authenticatedUser.get().orElse(null)));
    }

    private void loadCarteraActual() {
        carteraService.getCarteraActual().ifPresentOrElse(cartera -> {
            // Crear el FormLayout
            FormLayout formLayout = new FormLayout();
            formLayout.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1), // Una columna para pantallas pequeñas
                    new FormLayout.ResponsiveStep("600px", 2) // Dos columnas para pantallas más grandes
            );

            // Crear y configurar los campos
            TextField nombreField = new TextField("Nombre");
            nombreField.setValue(cartera.getNombre());
            nombreField.getElement().setAttribute("aria-label", "Nombre");
            nombreField.setReadOnly(true);

            TextField fechaInicioField = new TextField("Fecha de Inicio de la Cartera");
            fechaInicioField.setValue(cartera.getFecha_inicio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            fechaInicioField.getElement().setAttribute("aria-label", "Fecha Inicio de la Cartera");
            fechaInicioField.setReadOnly(true);

            TextField fechaFinField = new TextField("Fecha de Fin de la Cartera");
            fechaFinField.setValue(cartera.getFecha_fin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            fechaFinField.getElement().setAttribute("aria-label", "Fecha Fin de la Cartera");
            fechaFinField.setReadOnly(true);

            TextField fechaAperturaSolicitudField = new TextField("Fecha Apertura Solicitud");
            fechaAperturaSolicitudField.setValue(cartera.getFecha_apertura_solicitud().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            fechaAperturaSolicitudField.getElement().setAttribute("aria-label", "Fecha Apertura Solicitud");
            fechaAperturaSolicitudField.setReadOnly(true);

            TextField fechaCierreSolicitudField = new TextField("Fecha Cierre Solicitud");
            fechaCierreSolicitudField.setValue(cartera.getFecha_cierre_solicitud().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            fechaCierreSolicitudField.getElement().setAttribute("aria-label", "Fecha Cierre Solicitud");
            fechaCierreSolicitudField.setReadOnly(true);

            TextField nHorasField = new TextField("Número de Horas");
            nHorasField.setValue(String.valueOf(cartera.getN_horas()));
            nHorasField.getElement().setAttribute("aria-label", "Número de Horas");
            nHorasField.setReadOnly(true);

            TextField nMaxTecnicosField = new TextField("Número Máximo de Técnicos");
            nMaxTecnicosField.setValue(String.valueOf(cartera.getN_max_tecnicos()));
            nMaxTecnicosField.getElement().setAttribute("aria-label", "Número Máximo de Técnicos");
            nMaxTecnicosField.setReadOnly(true);

            TextField presupuestoTotalField = new TextField("Presupuesto Total");
            presupuestoTotalField.setValue(String.valueOf(cartera.getPresupuesto_total()));
            presupuestoTotalField.getElement().setAttribute("aria-label", "Presupuesto Total");
            presupuestoTotalField.setReadOnly(true);

            // Agregar los campos al FormLayout
            formLayout.add(
                    nombreField,
                    nHorasField,
                    fechaInicioField,
                    fechaFinField,
                    fechaAperturaSolicitudField,
                    fechaCierreSolicitudField,
                    nMaxTecnicosField,
                    presupuestoTotalField
            );

            // Agregar el FormLayout al contenido principal
            getContent().add(formLayout);
        }, () -> {
            // Acción en caso de que no haya cartera actual
            getContent().add(new Text("No hay cartera actual disponible."));
        });
    }

}
