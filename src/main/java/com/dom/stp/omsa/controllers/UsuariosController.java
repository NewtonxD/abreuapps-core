/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.controllers;

import com.dom.stp.omsa.entities.GrupoDatos;
import com.dom.stp.omsa.entities.Usuario;
import com.dom.stp.omsa.services.AccesosService;
import com.dom.stp.omsa.services.DataModelService;
import com.dom.stp.omsa.services.GrupoDatosService;
import com.dom.stp.omsa.utils.DateUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author cabreu
 */
@Controller
@RequestMapping("/usrmgr")
@Slf4j
public class UsuariosController {

    
    @Autowired
    private DateUtils dtUtils;

    @Autowired
    private GrupoDatosService gdserv;
    
    @Autowired
    private AccesosService accserv;
    
    @Autowired
    private DataModelService dmService;
    
    @Autowired
    private SeeController seeCnt;

    @PostMapping("/save")
    public String GuardarGrupoDato(
            HttpServletRequest request, 
            Model model, 
            GrupoDatos grpdt,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(model.getAttribute("dat_gen_registro_grupos")==null || (! (Boolean)model.getAttribute("dat_gen_registro_grupos"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/dat_gen_consulta_grupos :: content-default";
        }
        
        HashMap<String, Object> map = new HashMap<>();
        
        if (dateInput != null && !dateInput.equals("")) {
            
            grpdt.setFecha_actualizacion(dtUtils.Formato2ToDate(dateInput));
            
        }
        
        Optional<GrupoDatos> grupo = gdserv.obtener(grpdt.getGrupo_dato());
        
        boolean ext = false, ss = true;
        
        if (grupo.isPresent()) {
            
            ext = true;
            
            if (!dtUtils.FechaFormato2.format(grupo.get().getFecha_actualizacion()).equals(dateInput)) {
                
                ss = false;
                
            } else {
                
                grpdt.setFecha_registro(grupo.get().getFecha_registro());
                grpdt.setHecho_por(grupo.get().getHecho_por());
                
            }
            
        }

        if (ss) {
            
            GrupoDatos d = gdserv.guardar(grpdt, u.getId(), ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", dtUtils.FechaFormato1.format(new Date()));
            
        } else {
            
            model.addAttribute("status", false);
            model.addAttribute("msg", "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535");
            
        }
        
        if(!map.isEmpty())
            seeCnt.publicar("dtgrp", map);

        dmService.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }

    @PostMapping("/update")
    public String ActualizarGrupo(
            HttpServletRequest request, 
            Model model, 
            String idGrupo
    ) {  
        
        Usuario u =(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         
        Optional<GrupoDatos> g = gdserv.obtener(idGrupo);

        if(g.isEmpty()){

            log.error("Error COD: 00537 al editar grupos de datos.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }

        model.addAttribute("grupo", g.get());
        model.addAttribute("update", true);
        model.addAllAttributes(accserv.consultarAccesosPantallaUsuario(u.getId(), "usr_mgr_registro"));

        return "fragments/usr_mgr_registro :: content-default";  
    }

    

}
