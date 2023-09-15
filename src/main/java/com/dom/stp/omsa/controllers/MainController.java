/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.controllers;

import com.dom.stp.omsa.entities.AccesosUsuario;
import com.dom.stp.omsa.entities.Usuario;
import com.dom.stp.omsa.services.AccesosService;
import com.dom.stp.omsa.services.DataModelService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Newton
 */

@Controller
@RequestMapping("/main")
@Slf4j
public class MainController {

    @Autowired
    private DataModelService DataModelService;
    
    @Autowired
    private AccesosService AccService;
    
    @RequestMapping({"/","index"})
    public String MainPage(
            HttpServletRequest request,
            Model model
    ){
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user",u);
        model.addAllAttributes(AccService.consultarAccesosMenuUsuario(u.getId()));
        
        
        return "index";
    }
    
    
    @RequestMapping(value="/content-page/",method = RequestMethod.POST)
    public String loadContetPage(
            HttpServletRequest request,
            Model model,
            @RequestParam("id") String idPage
    ){
        
            Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            DataModelService.load(idPage, model,u.getId());
            
            return "fragments/"+idPage+" :: content-default";        
    }
    
    
    
}
