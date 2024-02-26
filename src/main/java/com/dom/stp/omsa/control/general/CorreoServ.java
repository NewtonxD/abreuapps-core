/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;

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

