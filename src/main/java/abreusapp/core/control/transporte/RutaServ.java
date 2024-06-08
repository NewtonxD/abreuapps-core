package abreusapp.core.control.transporte;

import abreusapp.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class RutaServ {
    
    private final RutaRepo repo;
    
    @Cacheable("Rutas")
    public List<RutaDTO> consultar(){
        return repo.customFindAll(false);
    }
    
    @Cacheable("Rutas")
    public List<RutaDTO> consultarActivo(){
        return repo.customFindAll(true);
    }
    
    @Transactional
    @CacheEvict(value="Rutas",allEntries = true)
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
