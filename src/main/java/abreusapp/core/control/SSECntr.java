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
    
    private final Map<String,SseEmitter> vhlEmitters = new ConcurrentHashMap<>();
        
    private final Map<String,SseEmitter> pdaEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,SseEmitter> rtaEmitters = new ConcurrentHashMap<>();
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private HashMap<String, Object> Datos;
    
    public SSECntr(SSEServ SSEServicio){
        this.SSEServicio = SSEServicio; 
        
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
        
        this.actions.put("vhl", ()->{
                this.SSEServicio.emitir(vhlEmitters, Datos);
            }
        );
        
        this.actions.put("pda", ()->{
                this.SSEServicio.emitir(pdaEmitters, Datos);
            }
        );
        
        this.actions.put("rta", ()->{
                this.SSEServicio.emitir(rtaEmitters, Datos);
            }
        );
    }
    
    public void publicar(String nombre,HashMap<String, Object> datos){
        this.Datos=datos;
        actions.get(nombre).run();
    }
    
    
//----------------------------------------------------------------------------//
//--------------ENDPOINTS SERVER SIDE EVENTS----------------------------------//
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/dtgnr", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarDatosGenerales(
        @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,dtGnrEmitters);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/dtgnr/close")
    @ResponseBody
    public void cerrarSSEDatosGenerales(
        @RequestParam String clientId
    ) {
        if (dtGnrEmitters.get(clientId)!=null){
            dtGnrEmitters.get(clientId).complete();
            dtGnrEmitters.remove(clientId);
        }
    }
//----------------------------------------------------------------------------//

    @GetMapping(value = "/dtgrp", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarGrupoDato(
        @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,dtGrpEmitters);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/dtgrp/close")
    @ResponseBody
    public void cerrarSSEGrupoDato(
        @RequestParam String clientId
    ) {
        if (dtGrpEmitters.get(clientId)!=null){
            dtGrpEmitters.get(clientId).complete();
            dtGrpEmitters.remove(clientId);
        }
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value="/usrmgr", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarUsuarios(
        @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,usrMgrEmitters);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/usrmgr/close")
    @ResponseBody
    public void cerrarSSEUsuarios(
        @RequestParam String clientId
    ) {
        if (usrMgrEmitters.get(clientId)!=null){
            usrMgrEmitters.get(clientId).complete();
            usrMgrEmitters.remove(clientId);
        }
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value="/vhl", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarVehiculos(
        @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,vhlEmitters);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/vhl/close")
    @ResponseBody
    public void cerrarSSEVehiculos(
        @RequestParam String clientId
    ) {
        if (vhlEmitters.get(clientId)!=null){
            vhlEmitters.get(clientId).complete();
            vhlEmitters.remove(clientId);
        }
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value="/pda", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarParada(
        @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,pdaEmitters);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/pda/close")
    @ResponseBody
    public void cerrarSSEParada(
        @RequestParam String clientId
    ) {
        if (pdaEmitters.get(clientId)!=null){
            pdaEmitters.get(clientId).complete();
            pdaEmitters.remove(clientId);
        }
    }
    
    
//----------------------------------------------------------------------------//
    
    @GetMapping(value="/rta", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarRuta(
        @RequestParam String clientId
    ) {
        return SSEServicio.agregar(clientId,rtaEmitters);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/rta/close")
    @ResponseBody
    public void cerrarSSERuta(
        @RequestParam String clientId
    ) {
        if (rtaEmitters.get(clientId)!=null){
            rtaEmitters.get(clientId).complete();
            rtaEmitters.remove(clientId);
        }
    }
//----------------------------------------------------------------------------// 
    
    
}
