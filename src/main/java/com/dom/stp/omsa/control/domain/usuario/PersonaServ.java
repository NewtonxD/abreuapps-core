/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  
 * Servicio para manipular y consultar los datos personales.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Transactional
@Service
public class PersonaServ {
    
    @Autowired
    PersonaRepo repo;
    
    public Persona guardar(Persona gd, Integer idUsuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        
        return repo.save(gd);
    }
    
    public List<Persona> consultarUsuarios(){
        return repo.findByUsuarioIsNotNull();
    }
    
    public List<Persona> consultar(){
        return repo.findAll();
    }
    
    public Optional<Persona> obtenerPorUsuario(Usuario usuario){
        return repo.findByUsuario(usuario);
    }
}