/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control;

import com.dom.stp.omsa.control.domain.dato.DatoServ;
import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.domain.usuario.Persona;
import com.dom.stp.omsa.control.domain.usuario.PersonaServ;
import com.dom.stp.omsa.control.domain.usuario.Usuario;
import com.dom.stp.omsa.control.domain.usuario.UsuarioServ;
import com.dom.stp.omsa.control.general.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author cabreu
 */

@Controller
@RequestMapping("/infppl")
@Slf4j
public class InfPersonalCntr {
    
    
    @Autowired
    PersonaServ PersonaServicio;
    
    @Autowired
    DateUtils FechaUtils;
    
    @Autowired
    AccesoServ AccesoServicio;
    
    @Autowired
    UsuarioServ UsuarioServicio;
    
    @Autowired
    DatoServ dtserv;
    
    @PostMapping("/save")
    @ResponseBody
    public int GuardarPersona(
            HttpServletRequest request, 
            Model model, 
            Persona persona,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            return 0;
        }
        
        
        HashMap<String, Object> map = new HashMap<>();
        
        if (dateInput != null && !dateInput.equals("")) {
            
            persona.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));
            
        }
        
        Optional<Persona> persona_existe = PersonaServicio.obtenerPorId(persona.getId()==null?0:persona.getId());
        
        boolean ext = false, ss = true;
        
        if (persona_existe.isPresent()) {
            
            ext = true;
            
            if (!FechaUtils.FechaFormato2.format(persona_existe.get().getFecha_actualizacion()).equals(dateInput)) {
                
                ss = false;
                
            } else {
                
                persona.setFecha_registro(persona_existe.get().getFecha_registro());
                persona.setHecho_por(persona_existe.get().getHecho_por());
            
            }
            
        }
        
        Persona d = null;
        if (ss) {
            
            d = PersonaServicio.guardar(persona, u.getId(), ext);
            
        }
        
        return d!=null?d.getId():0;

    }
    
    @PostMapping("/vfyCedInUsr")
    @ResponseBody
    public boolean VerificarCedula(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("cedula") String cedula
    ){
       Optional<Persona> p=PersonaServicio.obtenerPorCedula(cedula);
       if (p.isPresent()) {
           return ! UsuarioServicio.obtenerPorPersona(p.get()).isPresent();
       }
       else return true;
    }
    
    
    @PostMapping("/getAllInfCed")
    public String ObtenerInfoPorCedula(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("cedula") String cedula,
            @RequestParam("update") boolean update
    ){
        Persona p=PersonaServicio.obtenerPorCedula(cedula).orElse(new Persona());
        p.setCedula(cedula);
        model.addAttribute("sexo",dtserv.consultarPorGrupo("sexo"));
        model.addAttribute("sangre",dtserv.consultarPorGrupo("Tipos Sanguineos"));
        model.addAttribute("persona", p);
        model.addAttribute("update",update);
        return "fragments/usr_mgr_registro :: info-dinamica-personal";
    }
    
    
}

