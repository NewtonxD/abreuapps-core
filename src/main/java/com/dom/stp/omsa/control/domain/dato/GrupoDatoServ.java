/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.dato;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
public class GrupoDatoServ {
    
    @Autowired
    private GrupoDatoRepo gdrepo;
    
    public GrupoDato guardar(GrupoDato gd, Integer idUsuario,boolean existe){
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date(System.currentTimeMillis()));
        }
        gd.setFecha_actualizacion(new Date(System.currentTimeMillis()));
        return gdrepo.save(gd);
    }
    
    public List<GrupoDato> consultar(){
        return gdrepo.findAll();
    }
    
    public Optional<GrupoDato> obtener(String grupo){
        return gdrepo.findById(grupo);
    }
}
