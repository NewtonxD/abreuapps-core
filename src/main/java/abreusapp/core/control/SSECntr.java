/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.utils.SSEServ;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * @author cabreu
 */

@Controller
@Slf4j
@RequestMapping("/see")
@CrossOrigin
public class SSECntr {
    
    private final SSEServ SSEServicio;
    
    private final Map<String,SseEmitter> dtGnrEmitters = new ConcurrentHashMap<>();

    private final Map<String,SseEmitter> dtGrpEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> usrMgrEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private HashMap<String, Object> Datos;
    
    public SSECntr(SSEServ SSEServicio){
        this.SSEServicio =SSEServicio; 
        
        this.actions.put("dtgnr", ()->{
                this.SSEServicio.emitir(dtGnrEmitters, Datos);
            }
        );
        
        this.actions.put("dtgrp", ()->{
                this.SSEServicio.emitir(dtGrpEmitters, Datos);
            }
        );
        
        this.actions.put("usrmgr", ()->{
                this.SSEServicio.emitir(usrMgrEmitters, Datos);
            }
        );
    }
    
    @GetMapping(value = "/dtgnr", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarDatosGenerales(
            HttpServletRequest request,
            @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,dtGnrEmitters);
    }
    
    @GetMapping(value = "/dtgnr/close")
    @ResponseBody
    public void cerrarSSEDatosGenerales(
            HttpServletRequest request,
            @RequestParam String clientId
    ) {
        dtGnrEmitters.get(clientId).complete();
        dtGnrEmitters.remove(clientId);
    }

    @GetMapping(value = "/dtgrp", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarGrupoDato(
            HttpServletRequest request,
            @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,dtGrpEmitters);
    }
    
    @GetMapping(value = "/dtgrp/close")
    @ResponseBody
    public void cerrarSSEGrupoDato(
            HttpServletRequest request,
            @RequestParam String clientId
    ) {
        dtGrpEmitters.get(clientId).complete();
        dtGrpEmitters.remove(clientId);
    }
    
    @GetMapping(value="/usrmgr", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarUsuarios(
            HttpServletRequest request,
            @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,usrMgrEmitters);
    }
    
    @GetMapping(value = "/usrmgr/close")
    @ResponseBody
    public void cerrarSSEUsuarios(
            HttpServletRequest request,
            @RequestParam String clientId
    ) {
        usrMgrEmitters.get(clientId).complete();
        usrMgrEmitters.remove(clientId);
    }
    
    public void publicar(String nombre,HashMap<String, Object> datos){
        this.Datos=datos;
        actions.get(nombre).run();
    }
    
    
}
