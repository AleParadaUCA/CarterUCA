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
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import es.uca.iw.carteruca.views.common.common;

@Service
public class CommonService {

//    private String bucketName = "carterucarficheros";
//    private String region = "EE.UU. Este (Norte de Virginia) us-east-1";
//    private String accessKey = "carterucaficheros";
//    private String secretKey = "";
//    private AmazonS3 s3Client = null;
//
//    public void init() {
//        s3Client = AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//    }

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
    
//    @Profile("prod")
//    public List<String> guardarFileProd(MultiFileMemoryBuffer buffer, String targetDirPath) {
//        List<String> filePaths = new ArrayList<>();
//
//        init();
//
//        buffer.getFiles().forEach(fileName -> {
//            try (InputStream inputStream = buffer.getInputStream(fileName)) {
//                String s3Key = targetDirPath + "/" + fileName;
//                s3Client.putObject(new PutObjectRequest(bucketName, s3Key, inputStream, null));
//                filePaths.add(s3Key);
//            } catch (IOException e) {
//                common.showErrorNotification("Error al leer el archivo: " + e.getMessage());
//            }
//        });
//        return filePaths;
//    }
//
//    @Profile("prod")
//    public Anchor descargarFileProd(String filePath, String buttonText) {
//
//        init();
//
//        Button downloadButton = new Button(buttonText);
//        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//
//        String[] parts = filePath.split("/");
//        String fileName = parts[parts.length - 1];
//
//        StreamResource resource = new StreamResource(fileName, () -> {
//            try {
//                S3Object s3Object = s3Client.getObject(bucketName, filePath);
//                return s3Object.getObjectContent();
//            } catch (Exception e) {
//                return null;
//            }
//        });
//
//        Anchor anchor = new Anchor(resource, "");
//        anchor.getElement().setAttribute("download", true);
//        anchor.add(downloadButton);
//
//        return anchor;
//    }
}
