/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.services;

import com.dom.stp.omsa.entities.GrupoDatos;
import com.dom.stp.omsa.repositorys.GrupoDatosRepository;
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
public class GrupoDatosService {
    
    @Autowired
    private GrupoDatosRepository gdrepo;
    
    public GrupoDatos guardar(GrupoDatos gd, Integer idUsuario,boolean existe){
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date(System.currentTimeMillis()));
        }
        gd.setFecha_actualizacion(new Date(System.currentTimeMillis()));
        return gdrepo.save(gd);
    }
    
    public List<GrupoDatos> consultar(){
        return gdrepo.findAll();
    }
    
    public Optional<GrupoDatos> obtener(String grupo){
        return gdrepo.findById(grupo);
    }
}
