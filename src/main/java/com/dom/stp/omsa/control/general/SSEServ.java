/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;


import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * Servicio para manejar los Server Site Event que refrescan los datos en pantalla.
 * 
 * @author Carlos Abreu Pérez
 */
@Service
public class SSEServ {
    
    @Async
    public void emitir(CopyOnWriteArrayList<SseEmitter> emitters,HashMap<String, Object> Datos){
        emitters.forEach(emitter -> {
            try {
                emitter.send(Datos);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
    
    public SseEmitter agregar(CopyOnWriteArrayList<SseEmitter> emitters){
        long timeout = 900000;
        SseEmitter emitter = new SseEmitter(timeout);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitter.complete());
        emitters.add(emitter);
        return emitter ;
    }
}
