/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    public void emitir(Map<String,SseEmitter> emitters,HashMap<String, Object> Datos){
        
        for (Map.Entry<String,SseEmitter> val : emitters.entrySet()) {
            try {
                val.getValue().send(Datos);
            } catch (IOException e) {
                val.getValue().completeWithError(e);
                emitters.remove(val.getKey());
            }
        }
    }
    
    public SseEmitter agregar(String nombre,Map<String,SseEmitter> emitters){
        long timeout = 7200000;
        SseEmitter emitter = new SseEmitter(timeout);
        emitter.onCompletion(() -> emitters.remove(nombre));
        emitter.onTimeout(() -> emitter.complete());
        emitters.put(nombre,emitter);
        return emitter ;
    }
}
