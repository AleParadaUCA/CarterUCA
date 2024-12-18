package es.uca.iw.carteruca.services;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import es.uca.iw.carteruca.views.common.common;

public class CommonService {
    public static List<String> guardarFile(MultiFileMemoryBuffer buffer, String targetDirPath) {
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

    public static Anchor descargarFile(String filePath, String buttonText) {
        Button downloadButton = new Button(buttonText);
        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String[] parts = filePath.split("\\\\");
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
