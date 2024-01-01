/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control;

import com.dom.stp.omsa.control.domain.dato.Dato;
import com.dom.stp.omsa.control.domain.dato.GrupoDato;
import com.dom.stp.omsa.control.domain.usuario.Usuario;
import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.general.ModelServ;
import com.dom.stp.omsa.control.domain.dato.DatoServ;
import com.dom.stp.omsa.control.domain.dato.GrupoDatoServ;
import com.dom.stp.omsa.control.general.DateUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author cabreu
 */
@Controller
@Slf4j
public class DatosCntr {

    @Autowired
    DateUtils FechaUtils;

    @Autowired
    GrupoDatoServ GrupoServicio;

    @Autowired
    AccesoServ AccesoServicio;

    @Autowired
    ModelServ ModeloServicio;

    @Autowired
    DatoServ DatoServicio;

    @Autowired
    SSECntr SSEControlador;
    
    
    /*----Grupos de Datos-----*/

    @PostMapping("/dtgrp/save")
    public String GuardarGrupoDato(
            HttpServletRequest request,
            Model model,
            GrupoDato grpdt,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {

        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"dat_gen_consulta_grupos");

        if (m.get("dat_gen_registro_grupos") == null || (!(Boolean) m.get("dat_gen_registro_grupos"))) {
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/dat_gen_consulta_grupos :: content-default";
        }

        HashMap<String, Object> map = new HashMap<>();

        if (dateInput != null && !dateInput.equals("")) {

            grpdt.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));

        }

        Optional<GrupoDato> grupo = GrupoServicio.obtener(grpdt.getGrupoDato());

        boolean ext = false, ss = true;

        if (grupo.isPresent()) {

            ext = true;

            if (!FechaUtils.FechaFormato2.format(grupo.get().getFecha_actualizacion()).equals(dateInput)) {

                ss = false;

            } else {

                grpdt.setFecha_registro(grupo.get().getFecha_registro());
                grpdt.setHecho_por(grupo.get().getHecho_por());

            }

        }

        if (ss) {

            GrupoDato d = GrupoServicio.guardar(grpdt, u.getId(), ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));

        } else {

            model.addAttribute("status", false);
            model.addAttribute("msg", "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535");

        }
        
        ModeloServicio.load("dat_gen_consulta_grupos", model, u.getId());

        if (!map.isEmpty()) {
            SSEControlador.publicar("dtgrp", map);
        }

        return "fragments/dat_gen_consulta_grupos :: content-default";

    }
    
    
    @PostMapping("/dtgrp/update")
    public String ActualizarGrupo(
            HttpServletRequest request,
            Model model,
            String idGrupo
    ) {

        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<GrupoDato> g = GrupoServicio.obtener(idGrupo);

        if (g.isEmpty()) {

            log.error("Error COD: 00537 al editar grupos de datos.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());

            return "redirect:/error";

        }

        model.addAttribute("grupo", g.get());
        model.addAttribute("update", true);
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "dat_gen_registro_grupos"));

        return "fragments/dat_gen_registro_grupos :: content-default";
    }
    
    /*----------------------------*/
    
    
    /*----Datos Generales-----*/

    @PostMapping("/dtgnr/save")
    public String GuardarDatoGeneral(
            HttpServletRequest request,
            Model model, Dato dtgnr,
            @RequestParam(value = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {

        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"dat_gen_consulta_datos");

        if (m.get("dat_gen_registro_datos") == null || (!(Boolean) m.get("dat_gen_registro_datos"))) {
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/dat_gen_consulta_datos :: content-default";
        }

        HashMap<String, Object> map = new HashMap<>();

        if (dateInput != null && !dateInput.equals("")) {

            dtgnr.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));

        }

        Optional<Dato> dato = DatoServicio.obtener(dtgnr.getDato());
        boolean ext = false, ss = true;

        if (dato.isPresent()) {

            ext = true;

            if (!FechaUtils.FechaFormato2.format(dato.get().getFecha_actualizacion()).equals(dateInput)) {

                ss = false;

            } else {

                dtgnr.setFecha_registro(dato.get().getFecha_registro());
                dtgnr.setHecho_por(dato.get().getHecho_por());

            }

        }

        if (ss) {

            Dato d = DatoServicio.guardar(dtgnr, u.getId(), ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));

        } else {

            model.addAttribute("status", false);
            model.addAttribute("msg", "Hubo un inconveniente al actualizar el registro. Parece que alguien más ha realizado cambios en la información. Por favor, inténtalo otra vez. COD: 00535");

        }
        
        ModeloServicio.load("dat_gen_consulta_datos", model, u.getId());

        if (!map.isEmpty()) {
            SSEControlador.publicar("dtgnr", map);
        }

        return "fragments/dat_gen_consulta_datos :: content-default";

    }

    @PostMapping("/dtgnr/update")
    public String ActualizarDatosGenerales(
            HttpServletRequest request,
            Model model,
            String idDato
    ) {

        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Dato> d = DatoServicio.obtener(idDato);

        if (d.isEmpty()) {

            log.error("Error COD: 00535 al editar datos generales.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            return "redirect:/error";

        }

        model.addAttribute("dato", d.get());
        model.addAttribute("update", true);
        model.addAttribute("grupos", GrupoServicio.consultar());
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "dat_gen_registro_datos"));

        return "fragments/dat_gen_registro_datos :: content-default";

    }
    
    /*----------------------------*/
    

}
