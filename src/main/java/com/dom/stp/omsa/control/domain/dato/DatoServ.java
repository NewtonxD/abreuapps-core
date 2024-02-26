/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.dato;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  
 * Servicio para manipular y consultar los datos generales.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Service
@RequiredArgsConstructor
public class DatoServ {
    
    private final DatoRepo repo;
    
    public Dato guardar(Dato gd, Integer idUsuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        return repo.save(gd);
    }
    
    public List<Dato> consultar(){
        return repo.findAll();
    }
    
    public List<Dato> consultarPorGrupo(String grupo){
        return repo.findByGrupoDato(grupo);
    }
    
    public Optional<Dato> obtener(String dato){
        return repo.findById(dato);
    }
}

