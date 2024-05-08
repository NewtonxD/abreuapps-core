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
public class ParadaServ {
    
    private final ParadaRepo repo;
    
    public List<Parada> consultarTodo(Integer excluyeParada,Boolean activo){
        return repo.findAllCustom(excluyeParada, activo);
    }
    
    public Parada guardar(Parada gd, Usuario usuario,boolean existe){
        
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
    
    public Optional<Parada> obtener(Integer id){
        if(id==null){
            return Optional.empty();
        }
        return repo.findById(id);
    }
}
