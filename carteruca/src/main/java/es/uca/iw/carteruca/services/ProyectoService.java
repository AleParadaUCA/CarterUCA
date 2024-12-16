package es.uca.iw.carteruca.services;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import es.uca.iw.carteruca.models.Proyecto;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoRepository repository;
    private final SolicitudService solicitudService;

    @Autowired
    public ProyectoService(ProyectoRepository repository, SolicitudService solicitudService) {
        this.repository = repository;
        this.solicitudService = solicitudService;
    }

    public void guardar(Float puntuacion, Float porcentaje, MultiFileMemoryBuffer buffer, Solicitud solicitud) {

        List<String> documento = new ArrayList<>();

        buffer.getFiles().forEach(fileName -> {
            try (InputStream inputStream = buffer.getInputStream(fileName)) {
                // Define el directorio objetivo
                File targetDir = new File("../archivos");
                if (!targetDir.exists()) {
                    targetDir.mkdirs(); // Crea el directorio si no existe
                }

                // Define el archivo objetivo
                File targetFile = new File(targetDir, fileName);

                // Escribe el archivo subido al directorio objetivo
                try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                    org.apache.commons.io.IOUtils.copy(inputStream, outputStream);

                    // Agrega la ruta del archivo procesado a la lista
                    documento.add(targetFile.getPath());
                } catch (IOException e) {
                    Notification.show("Error al guardar el archivo: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            } catch (IOException e) {
                Notification.show("Error al leer el archivo: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });


        Proyecto proyecto = new Proyecto();

        proyecto.setPuntuacion(puntuacion);
        proyecto.setPorcentaje(porcentaje);
        proyecto.setEspecificacion_tecnica(documento.get(0));
        proyecto.setPresupuesto(documento.get(1));
        proyecto.setSolicitud(solicitud);

        repository.save(proyecto);
    }
}
