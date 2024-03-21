/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SSEServ {
    
    @Async
    public void emitir(CopyOnWriteArrayList<SseEmitter> emitters,HashMap<String, Object> Datos){
        emitters.forEach(emitter -> {
            try {
                emitter.send(Datos);
            } catch (IOException e) {
                log.error("SSEServ: Error al emitir "+e.getMessage());
                emitter.complete();
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
