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
 * @author cabreu
 */


@Transactional
@Service
public class UsuarioServ {
    
    @Autowired
    UsuarioRepo repo;
    
    public Usuario guardar(Usuario gd, Integer idUsuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        
        return repo.save(gd);
    }
    
    public List<Usuario> consultar(){
        return repo.findAll();
    }
    
    public Optional<Usuario> obtener(String usuario){
        return repo.findByUsername(usuario);
    }
    
    public Optional<Usuario> obtenerPorId(Integer id){
        return repo.findById(id);
    }
}
