package abreusapp.core.control.utils;


import abreusapp.core.control.transporte.LocVehiculoServ;
import abreusapp.core.control.transporte.ParadaServ;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SSEServ {
    
    private final Map<String,SseEmitter> dtGnrEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> usrMgrEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> vhlEmitters = new ConcurrentHashMap<>();
        
    private final Map<String,SseEmitter> pdaEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> rtaEmitters = new ConcurrentHashMap<>();
    
    //--------------------------------------------------------------------------
    private final Map<String,SseEmitter> trpInfoEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> pdaInfoEmitters = new ConcurrentHashMap<>();
    
    private final ParadaServ ParadaServicio;
    
    private final LocVehiculoServ LocServicio;
    //--------------------------------------------------------------------------
    
    private Map<String,SseEmitter> obtenerEmitter(String nombre){
        return switch (nombre) {
            case "dtgnr" -> dtGnrEmitters;
            case "usrmgr" -> usrMgrEmitters;
            case "vhl" -> vhlEmitters;
            case "pda" -> pdaEmitters;
            case "rta" -> rtaEmitters;
                
            case "trpInfo" -> trpInfoEmitters;
            case "pdaInfo" -> pdaInfoEmitters;
            case "" -> null;
            default -> null;
        };
    }
    //--------------------------------------------------------------------------
    
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
    public void publicarParadaInfo(){        
        if(pdaInfoEmitters!=null){
            for (Map.Entry<String,SseEmitter> val : pdaInfoEmitters.entrySet()) {
                Integer idParada=Integer.valueOf(val.getKey().split("-")[2]);
                try {
                    val.getValue().send(ParadaServicio.getParadaInfo(idParada));
                } catch (IOException e) {
                    val.getValue().completeWithError(e);
                    pdaInfoEmitters.remove(val.getKey());
                }
            }
        }
    }
    
    @Async
    public void publicarTransporteInfo(){
        if(trpInfoEmitters!=null){
            for (Map.Entry<String,SseEmitter> val : trpInfoEmitters.entrySet()) {
                try {
                    val.getValue().send(LocServicio.consultarDatosTransporteEnCamino());
                } catch (IOException e) {
                    val.getValue().completeWithError(e);
                    trpInfoEmitters.remove(val.getKey());
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
