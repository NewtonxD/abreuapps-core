/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    
    private final Map<String,SseEmitter> dtGnrEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> usrMgrEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> vhlEmitters = new ConcurrentHashMap<>();
        
    private final Map<String,SseEmitter> pdaEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> rtaEmitters = new ConcurrentHashMap<>();

    private Map<String,SseEmitter> obtenerEmitter(String nombre){
        return switch (nombre) {
            case "dtgnr" -> dtGnrEmitters;
            case "usrmgr" -> usrMgrEmitters;
            case "vhl" -> vhlEmitters;
            case "pda" -> pdaEmitters;
            case "rta" -> rtaEmitters;
            case "" -> null;
            default -> null;
        };
    }
    
    @Async
    public void publicar(String nombre,HashMap<String, Object> Datos){
        Map<String,SseEmitter> emitters=obtenerEmitter(nombre);
        if(emitters!=null){
            for (Map.Entry<String,SseEmitter> val : emitters.entrySet()) {
                try {
                    val.getValue().send(Datos);
                } catch (IOException e) {
                    val.getValue().completeWithError(e);
                    emitters.remove(val.getKey());
                }
            }
        }
    }
    
    @Async
    public void cerrar(String id, String nombre){
        Map<String,SseEmitter> emitter=obtenerEmitter(nombre);
        if(emitter!=null){
            if (emitter.get(id)!=null){
                emitter.get(id).complete();
                emitter.remove(id);
            }
        }
    }
    
    public SseEmitter agregar(String id,String nombre){
        Map<String,SseEmitter> emitters=obtenerEmitter(nombre);
        if(emitters!=null){
            long timeout = 7200000;
            SseEmitter emitter = new SseEmitter(timeout);
            emitter.onCompletion(() -> emitters.remove(id));
            emitter.onTimeout(() -> emitter.complete());
            emitters.put(id,emitter);
            return emitter ;
        }else return null;
    }
}
