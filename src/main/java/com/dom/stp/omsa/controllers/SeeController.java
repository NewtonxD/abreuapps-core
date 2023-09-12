/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.controllers;

import com.dom.stp.omsa.services.SeeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * @author cabreu
 */

@Controller
@Slf4j
@RequestMapping("/see")
@CrossOrigin
public class SeeController {
    
    @Autowired
    private SeeService seeServ;
    
    private final CopyOnWriteArrayList<SseEmitter> dtGnrEmitters = new CopyOnWriteArrayList<>();

    private final CopyOnWriteArrayList<SseEmitter> dtGrpEmitters = new CopyOnWriteArrayList<>();
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private HashMap<String, Object> Datos;
    
    public SeeController(){
        this.actions.put("dtgnr", ()->{
                seeServ.emitir(dtGnrEmitters, Datos);
            }
        );
        
        this.actions.put("dtgrp", ()->{
                seeServ.emitir(dtGrpEmitters, Datos);
            }
        );
    }
    
    @GetMapping(value = "/dtgnr", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarDatosGenerales(
            HttpServletRequest request
    ) {
        return seeServ.agregar(dtGnrEmitters);
    }

    @GetMapping(value = "/dtgrp", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarGrupoDato(
            HttpServletRequest request
    ) {
        return seeServ.agregar(dtGrpEmitters);
    }
    
    public void publicar(String nombre,HashMap<String, Object> datos){
        this.Datos=datos;
        actions.get(nombre).run();
    }
    
    
}
