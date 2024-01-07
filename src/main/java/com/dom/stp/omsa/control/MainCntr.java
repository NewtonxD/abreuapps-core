/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control;

import com.dom.stp.omsa.control.domain.usuario.PersonaServ;
import com.dom.stp.omsa.control.domain.usuario.Usuario;
import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.general.ModelServ;
import jakarta.servlet.http.HttpServletRequest;
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
public class MainCntr {

    @Autowired
    ModelServ DataModelServicio;

    @Autowired
    AccesoServ AccesosServicio;
    
    @Autowired
    PersonaServ PersonaServicio;

    @RequestMapping({"/", "index"})
    public String MainPage(
            HttpServletRequest request,
            Model model
    ) {
        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("datos_personales",u.getPersona());
        model.addAllAttributes(AccesosServicio.consultarAccesosMenuUsuario(u.getId()));

        return "index";
    }

    @RequestMapping(value = "/content-page/", method = RequestMethod.POST)
    public String loadContetPage(
            HttpServletRequest request,
            Model model,
            @RequestParam("id") String idPage
    ) {

        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DataModelServicio.load(idPage, model, u.getId());

        return "fragments/" + idPage + " :: content-default";
    }

}
