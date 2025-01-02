package es.uca.iw.carteruca.services;

import es.uca.iw.carteruca.models.Usuario;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailFakeService implements EmailInterfaceService {

    @Override
    public boolean enviarCorreoRegistro(Usuario usuario) {
        String subject = "Bienvenido";
        String body = "Debe activar su cuenta. "
                + "Vaya a http://localhost:8080/useractivation "
                + "e introduzca su correo y el siguiente código: "
                + usuario.getCodigoRegistro();

        try {
            System.out.println("De: app (testing)");
            System.out.println("Para: " + usuario.getEmail());
            System.out.println("Asunto: " + subject);
            System.out.println("Cuerpo: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Simulación de envío de correo completada!");
            return true;
            
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}