package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class PublicidadServ {
    
    private final PublicidadRepo repo;
    
    @Transactional
    public void guardar(Publicidad gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(usuario);
        }else{
            gd.setHecho_por(usuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
    }
    
    public Optional<Publicidad> obtener(Long id){
        return repo.findById(id);
    }
}
