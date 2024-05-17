/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.utils.ModelServ;
import abreusapp.core.control.utils.SSEServ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SSECntr {
    
    private final SSEServ SSEServicio;
    
    private final ModelServ ModeloServicio;
    
    private static final List<String> REQUIRED_AUTH = new ArrayList<>(Arrays.asList("dtgnr" ,"dtgrp","usrmgr","vhl" ,"pda","rta"));
    
    
//----------------------------------------------------------------------------//
//--------------ENDPOINTS SERVER SIDE EVENTS----------------------------------//
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/{nombre}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        if(REQUIRED_AUTH.contains(nombre) && 
            ModeloServicio.getUsuarioLogueado()==null
        ) return null;
        
        return SSEServicio.agregar(clientId,nombre);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/{nombre}/close")
    @ResponseBody
    public void cerrarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        if(
            (REQUIRED_AUTH.contains(nombre) && ModeloServicio.getUsuarioLogueado()!=null)||
            (!REQUIRED_AUTH.contains(nombre))
        ) SSEServicio.cerrar(clientId,nombre);
    }
//----------------------------------------------------------------------------//
    
}
