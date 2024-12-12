package es.uca.iw.carteruca.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import es.uca.iw.carteruca.models.Estado;
import es.uca.iw.carteruca.models.Solicitud;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.repository.SolicitudRepository;

@Service
public class SolicitudService {
    private final SolicitudRepository repository;

    @Autowired
    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }

//    public void guardar(String titulo, String nombre, LocalDateTime fechaPuesta, String interesados, String alineamiento, String alcance, String normativa, MultiFileMemoryBuffer buffer, Usuario avalador, String presupuesto, Usuario solictante) {
    public void guardar(String titulo, String nombre, LocalDateTime fechaPuesta, String interesados, String alineamiento, String alcance, String normativa, MultiFileMemoryBuffer buffer, Usuario avalador, Usuario solictante) {
        AtomicReference<String> memoria = new AtomicReference<>("");

        //Faltan commprobaciones..

        buffer.getFiles().forEach(fileName -> {
        try (InputStream inputStream = buffer.getInputStream(fileName)) {
            // Define the target directory
            File targetDir = new File("../archivos");
            if (!targetDir.exists()) {
                targetDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Define the target file
            File targetFile = new File(targetDir, fileName);

            // Write the uploaded file to the target directory
            try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                org.apache.commons.io.IOUtils.copy(inputStream, outputStream);
                memoria.set(targetFile.getPath());
            } catch (IOException e) {
                Notification.show("Error al guardar el archivo: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        } catch (IOException e) {
            Notification.show("Error al leer el archivo: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    });
        System.out.println("name "+ avalador.getId());
        Solicitud solicitud = new Solicitud();
        solicitud.setTitulo(titulo);
        solicitud.setNombre(nombre);
        solicitud.setFecha_solicitud(LocalDateTime.now());
        solicitud.setFecha_puesta(fechaPuesta);
        solicitud.setInteresados(interesados);
        solicitud.setAlineamiento(alineamiento);
        solicitud.setAlcance(alcance);
        solicitud.setNormativa(normativa);
        solicitud.setMemoria(memoria.get()); //path
        //solicitud.setImportancia_promotor(importanciaPromotor); //esto que es?
        //solicitud.setPresupuesto(presupuesto);
        //solicitud.setEspecificacion_tecnica(especificacionTecnica);
        solicitud.setEstado(Estado.EN_TRAMITE);
        //puntuacion se quita
        //porcentaje se quita
        solicitud.setSolicitante(solictante);
        solicitud.setAvalador(avalador);
        //cartera

        repository.save(solicitud);
    }
}
