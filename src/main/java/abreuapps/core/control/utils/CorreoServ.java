package abreuapps.core.control.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class CorreoServ {
    
    private final  JavaMailSender emailSender;
    
    @Async
    public void enviarMensajeSimple(String Para, String Titulo, String Texto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(Para);
        message.setSubject(Titulo);
        message.setText(Texto);
        emailSender.send(message);
    }
}

