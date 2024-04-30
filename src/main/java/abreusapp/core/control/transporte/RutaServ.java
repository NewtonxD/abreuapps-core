/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.transporte;

import abreusapp.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Transactional
@Service
@RequiredArgsConstructor
public class RutaServ {
    
    private RutaRepo repo;
    
    public List<Ruta> consultar(){
        return repo.findAll();
    }
    
    public List<Ruta> consultarActivo(){
        return repo.findByActivo(true);
    }
    
    public Ruta guardar(Ruta gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            if(usuario!=null)
                gd.setActualizado_por(usuario);
        }else{
            if(usuario!=null)
                gd.setHecho_por(usuario);
            
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        
        return repo.save(gd);
    }
    
    public Optional<Ruta> obtener(String Ruta){
        return repo.findById(Ruta);
    }
    
}
