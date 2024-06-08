package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  
 * Servicio para manipular y consultar los datos personales.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Service
@RequiredArgsConstructor
public class PersonaServ {
    
    private final PersonaRepo repo;
    
    @Transactional
    public Persona guardar(Persona gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(usuario);
        }else{
            gd.setHecho_por(usuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        
        return repo.save(gd);
    }
    
    public Optional<Persona> obtenerPorId(Integer id){
        if(id==null || id==0) Optional.empty();
        return repo.findById(id);
    }
    
    public Optional<Persona> obtenerPorCedula(String cedula){
        return repo.findByCedula(cedula);
    }
    
    public List<Persona> consultar(){
        return repo.findAll();
    }
    
}
