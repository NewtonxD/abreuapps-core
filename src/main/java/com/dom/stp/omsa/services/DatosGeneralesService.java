/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.services;

import com.dom.stp.omsa.entities.Dato;
import com.dom.stp.omsa.repositorys.DatosGeneralesRepository;
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
public class DatosGeneralesService {
    
    @Autowired
    private DatosGeneralesRepository dtrepo;
    
    public Dato guardar(Dato gd, Integer idUsuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        return dtrepo.save(gd);
    }
    
    public List<Dato> consultar(){
        return dtrepo.findAll();
    }
    
    public Optional<Dato> obtener(String dato){
        return dtrepo.findById(dato);
    }
}

