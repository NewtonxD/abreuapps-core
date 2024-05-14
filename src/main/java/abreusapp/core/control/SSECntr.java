/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.utils.SSEServ;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    public SSECntr(SSEServ SSEServicio) {
        this.SSEServicio = SSEServicio;
    }
    
    private final Map<String,SseEmitter> dtGnrEmitters = new ConcurrentHashMap<>();

    private final Map<String,SseEmitter> dtGrpEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> usrMgrEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> vhlEmitters = new ConcurrentHashMap<>();
        
    private final Map<String,SseEmitter> pdaEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> rtaEmitters = new ConcurrentHashMap<>();

    private Map<String,SseEmitter> obtenerEmitter(String nombre){
        return switch (nombre) {
            case "dtgnr" -> dtGnrEmitters;
            case "dtgrp" -> dtGrpEmitters;
            case "usrmgr" -> usrMgrEmitters;
            case "vhl" -> vhlEmitters;
            case "pda" -> pdaEmitters;
            case "rta" -> rtaEmitters;
            case "" -> null;
            default -> null;
        };
    }
    
    public void publicar(String nombre,HashMap<String, Object> Datos){
        SSEServicio.emitir(obtenerEmitter(nombre),Datos);
    }
    
    
//----------------------------------------------------------------------------//
//--------------ENDPOINTS SERVER SIDE EVENTS----------------------------------//
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/{nombre}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        return SSEServicio.agregar(clientId,obtenerEmitter(nombre));
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/{nombre}/close")
    @ResponseBody
    public void cerrarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        Map<String,SseEmitter> emitter=obtenerEmitter(nombre);
        if(emitter!=null){
            if (emitter.get(clientId)!=null){
                emitter.get(clientId).complete();
                emitter.remove(clientId);
            }
        }
    }
//----------------------------------------------------------------------------//
    
}
