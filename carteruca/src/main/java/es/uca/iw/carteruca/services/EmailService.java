package es.uca.iw.carteruca.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uca.iw.carteruca.models.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
@EnableAsync
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String defaultMail;

    @Value("${server.port}")
    private int serverPort;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Obtiene la URL pública del servidor.
     *
     * @return la URL pública del servidor, incluyendo la dirección IP pública y el endpoint `/useractivation`.
     * @throws IOException si ocurre un error al realizar la solicitud HTTP para obtener la IP pública.
     * @throws InterruptedException si el subproceso que realiza la solicitud HTTP es interrumpido.
     */
    private String getServerUrl() throws IOException, InterruptedException {

    // Get the public IP address
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://checkip.amazonaws.com"))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String publicIp = response.body().trim(); // Ensure no extra characters

    // Generate the server URL
    String serverUrl = "http://";
    serverUrl += publicIp + "/useractivation";
    return serverUrl;

    }

    /**
     * Envía un correo de registro para activar la cuenta de un usuario.
     * Este metodo se ejecuta de forma asíncrona.
     *
     * @param user el usuario al que se enviará el correo de registro.
     * @return un {@link CompletableFuture} que indica si el correo fue enviado con éxito (true) o no (false).
     * @throws IOException si ocurre un error al construir la URL del servidor.
     * @throws InterruptedException si el subproceso que construye la URL del servidor es interrumpido.
     */
    @Async
    public CompletableFuture<Boolean> enviarCorreoRegistro(Usuario user) throws IOException, InterruptedException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Validar Correo";
        String body = "Bienvenido " + user.getNombre()
                + ".\n\nPara activar tu cuenta de CarterUCA entra en: " + getServerUrl()
                + "\nTu código secreto es: " + user.getCodigoRegistro()
                + "\n\nSaludos,\nEl equipo de Carteruca.";

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            logger.info("Correo de notificación no se ha podido enviar");
            return CompletableFuture.completedFuture(false);
        }

        logger.info("Correo de notificación enviado ");
        return CompletableFuture.completedFuture(true);
    }

    /**
     * Envía un correo electrónico con un asunto y cuerpo especificados a un destinatario.
     * Este metodo se ejecuta de forma asíncrona.
     *
     * @param to la dirección de correo del destinatario.
     * @param subject el asunto del correo.
     * @param body el cuerpo del correo.
     * @return un {@link CompletableFuture} que indica si el correo fue enviado con éxito (true) o no (false).
     */
    @Async
    public CompletableFuture<Boolean> enviarCorreo(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setFrom(defaultMail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            logger.info("Error al enviar correo de notificación");
            return CompletableFuture.completedFuture(false);
        }

        logger.info("Correo de notificación enviado ");
        return CompletableFuture.completedFuture(true);
    }
}
