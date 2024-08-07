package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class PublicidadServ {
    
    private final PublicidadRepo repo;
    
    private Long PUBLICIDAD_ACTUAL;
    
    @Transactional
    @CacheEvict(value={"Publicidad"}, allEntries = true)
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
    
    public void IncrementarActual(){
        repo.customIncreaseById(PUBLICIDAD_ACTUAL);
    }
    
    @Cacheable(value="Publicidad")
    public PublicidadDTO obtenerUltimo(){
        PublicidadDTO d=repo.customFindAll();
        PUBLICIDAD_ACTUAL=d.id();
        return d;
    }
}
