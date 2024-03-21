/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.stp.control.domain.dato;

import abreusapp.core.stp.control.domain.usuario.Usuario;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  
 * Servicio para manipular y consultar los grupos de datos.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Service
@RequiredArgsConstructor
public class GrupoDatoServ {
    
    private final GrupoDatoRepo repo;
    
    public GrupoDato guardar(GrupoDato gd, Usuario usuario,boolean existe){
        if(existe){ 
            gd.setActualizado_por(usuario);
        }else{
            gd.setHecho_por(usuario);
            gd.setFecha_registro(new Date(System.currentTimeMillis()));
        }
        gd.setFecha_actualizacion(new Date(System.currentTimeMillis()));
        return repo.save(gd);
    }
    
    public List<GrupoDato> consultar(){
        return repo.findAll();
    }
    
    public Optional<GrupoDato> obtener(String grupo){
        return repo.findById(grupo);
    }
}
