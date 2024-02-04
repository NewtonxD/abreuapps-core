/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */


@Transactional
@Service
@Slf4j
public class UsuarioServ {
    
    @Autowired
    UsuarioRepo repo;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    public String generarPassword(){

        //password por defecto
        Double instant=new Date().getTime()+(Math.random());
        String p=String.valueOf(instant).split("\\.")[1];
        int len=p.length();
        return p.substring(len-7, len-1);
    }
    
    public Usuario guardar(Usuario gd, Integer idUsuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            String nuevaContraseña=generarPassword();
            //enviar al correo aqui
            log.info("\tContraseña nueva > "+nuevaContraseña);
            gd.setContraseña(passwordEncoder.encode(nuevaContraseña));
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
    
    public Optional<Usuario> obtenerPorCorreo(String correo){
        return repo.findByCorreo(correo);
    }
    
    public Optional<Usuario> obtenerPorPersona(Persona persona){
        return repo.findByPersona(persona);
    }
    
    public Optional<Usuario> obtenerPorId(Integer id){
        return repo.findById(id);
    }
}
