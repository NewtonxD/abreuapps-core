/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control;

import com.dom.stp.omsa.control.domain.dato.DatoServ;
import com.dom.stp.omsa.control.domain.dato.GrupoDato;
import com.dom.stp.omsa.control.domain.usuario.Usuario;
import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.general.ModelServ;
import com.dom.stp.omsa.control.domain.dato.GrupoDatoServ;
import com.dom.stp.omsa.control.domain.usuario.Persona;
import com.dom.stp.omsa.control.domain.usuario.PersonaServ;
import com.dom.stp.omsa.control.domain.usuario.UsuarioServ;
import com.dom.stp.omsa.control.general.DateUtils;
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
public class UsuariosCntr {

    
    @Autowired
    DateUtils FechaUtils;

    @Autowired
    GrupoDatoServ gdserv;
    
    @Autowired
    DatoServ dtserv;
    
    @Autowired
    AccesoServ accserv;
    
    @Autowired
    ModelServ dmService;
    
    @Autowired
    PersonaServ PersonaServicio;
    
    @Autowired
    UsuarioServ UsuarioServicio;
    
    @Autowired
    SSECntr seeCnt;

    @PostMapping("/save")
    public String GuardarUsuario(
            HttpServletRequest request, 
            Model model, 
            Usuario user,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(model.getAttribute("usr_mgr_registro")==null || (! (Boolean)model.getAttribute("usr_mgr_registro"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/usr_mgr_principal :: content-default";
        }
        
        HashMap<String, Object> map = new HashMap<>();
        
        if (dateInput != null && !dateInput.equals("")) {
            
            user.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));
            
        }
        
        Optional<Usuario> usuario1 = UsuarioServicio.obtenerPorId(user.getId());
        
        boolean ext = false, ss = true;
        
        if (usuario1.isPresent()) {
            
            ext = true;
            
            if (!FechaUtils.FechaFormato2.format(usuario1.get().getFecha_actualizacion()).equals(dateInput)) {
                
                ss = false;
                
            } else {
                
                user.setFecha_registro(usuario1.get().getFecha_registro());
                user.setHecho_por(usuario1.get().getHecho_por());
                
            }
            
        }

        if (ss) {
            
            //Usuario d = UsuarioServicio.guardar(usuario, u.getId(), ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", user);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));
            
        } else {
            
            model.addAttribute("status", false);
            model.addAttribute("msg", "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535");
            
        }
        
        if(!map.isEmpty())
            seeCnt.publicar("usrmgr", map);

        dmService.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }

    @PostMapping("/update")
    public String ActualizarUsuario(
            HttpServletRequest request, 
            Model model, 
            String idUsuario
    ) {  
        
        Usuario u =(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Usuario> us=UsuarioServicio.obtener(idUsuario); 

        if(us.isEmpty()){

            log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ("+idUsuario+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }
        
        
        model.addAttribute("user",us.get());
        model.addAttribute("persona",us.get().getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo",dtserv.consultarPorGrupo("sexo"));
        model.addAttribute("sangre",dtserv.consultarPorGrupo("Tipos Sanguineos"));
        model.addAllAttributes(accserv.consultarAccesosPantallaUsuario(u.getId(), "usr_mgr_registro"));

        return "fragments/usr_mgr_registro :: content-default";  
    }

    

}
