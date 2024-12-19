package es.uca.iw.carteruca.services;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.uca.iw.carteruca.models.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String defaultMail;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @SuppressWarnings("CallToPrintStackTrace") //chorra
    public void enviarCorreo(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setFrom(defaultMail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private String getServerUrl() {
        String serverUrl = "http://";
        serverUrl += InetAddress.getLoopbackAddress().getHostAddress();
        serverUrl += ":" + serverPort + "/";
        return serverUrl;
    }

    // public boolean enviarCorreoRegistro(Usuario user) {

    //     MimeMessage message = emailSender.createMimeMessage();

    //     MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

    //     String subject = "Welcome";
    //     String body = "You should active your account. "
    //             + "Go to " + getServerUrl() + "useractivation "
    //             + "and introduce your mail and the following code: "
    //             + user.getCodigoRegistro();


    //     try {
    //         helper.setFrom(defaultMail);
    //         helper.setTo(user.getEmail());
    //         helper.setSubject(subject);
    //         helper.setText(body);
    //         this.emailSender.send(message);
    //     } catch (MailException | MessagingException ex) {
    //         ex.printStackTrace();
    //         return false;
    //     }

    //     return true;
    // }

    public boolean enviarCorreoRegistro(Usuario usuario) {
        String subject = "Activa tu cuenta";
        String body = "Por favor, activa tu cuenta usando el siguiente enlace: "
                + getServerUrl() + "activate?token=" + usuario.getCodigoRegistro();

        enviarCorreo(usuario.getEmail(), subject, body);
        return true;
    }
}