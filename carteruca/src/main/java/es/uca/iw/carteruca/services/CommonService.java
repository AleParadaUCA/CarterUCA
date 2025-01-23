package es.uca.iw.carteruca.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import es.uca.iw.carteruca.views.common.common;

@Service
public class CommonService {


    /**
     * Guarda múltiples archivos en un directorio específico.
     *
     * @param buffer el buffer que contiene los archivos cargados.
     * @param id identificador que se usa para determinar la ruta del directorio destino.
     * @return una lista con las rutas de los archivos guardados.
     */
    public static List<String> guardarFile(MultiFileMemoryBuffer buffer, String id) {

        String targetDirPath = "/home/ubuntu/archivos/cartera" + id;

        List<String> filePaths = new ArrayList<>();

            buffer.getFiles().forEach(fileName -> {
            try (InputStream inputStream = buffer.getInputStream(fileName)) {

                File targetDir = new File(targetDirPath);
                if (!targetDir.exists()) {
                    //crear directorio si no existe
                    targetDir.mkdirs();
                }
                // Define the target file
                File targetFile = new File(targetDir, fileName);

                //Cambiar Nombre para archivos repetidos
                if (targetFile.exists()) {

                    String newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    newFileName += "_" + System.currentTimeMillis() + ".pdf";
                    targetFile = new File(targetDir, newFileName);
                }

                // Write the uploaded file to the target directory
                try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                    IOUtils.copy(inputStream, outputStream);
                    filePaths.add(targetFile.getPath());
                } catch (IOException e) {
                    common.showErrorNotification("Error al guardar el archivo: " + e.getMessage());
                }
            } catch (IOException e) {
                common.showErrorNotification("Error al leer el archivo: " + e.getMessage());
            }
        });
        return filePaths;
    }

    /**
     * Genera un componente de anclaje para descargar un archivo.
     *
     * @param filePath la ruta del archivo que se descargará.
     * @param buttonText el texto que se mostrará en el botón de descarga.
     * @return un objeto {@link Anchor} configurado para descargar el archivo.
     */
    public static Anchor descargarFile(String filePath, String buttonText) {
        Button downloadButton = new Button(buttonText);
        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String[] parts = filePath.split("/");
        String fileName = parts[parts.length - 1];

        StreamResource resource = new StreamResource(fileName, () -> {
            try {
                return new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                return null;
            }
        });

        Anchor anchor = new Anchor(resource, "");
        anchor.getElement().setAttribute("download", true);
        anchor.add(downloadButton);

        return anchor;
    }

    /**
     * Elimina un archivo especificado por su ruta.
     *
     * @param filePath la ruta del archivo que se eliminará.
     */
    public static void eliminarFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.deleteIfExists(path);
            } else {
                System.out.println("El fichero no existe: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
