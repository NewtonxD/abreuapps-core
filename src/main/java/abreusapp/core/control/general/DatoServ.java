package abreusapp.core.control.general;

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
 * Servicio para manipular y consultar los datos generales.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Service
@RequiredArgsConstructor
public class DatoServ {
    
    private final DatoRepo repo;
    
    @Transactional
    @CacheEvict(value="Datos", allEntries = true)
    public void guardar(Dato gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(usuario);
        }else{
            gd.setHecho_por(usuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
    }
    
    @Cacheable("Datos")
    public List<DatoDTO> consultar(){
        return repo.customFindAll(null);
    }
    
    @Cacheable(value="Datos",key="#grupo")
    public List<DatoDTO> consultarPorGrupo(String grupo){
        return repo.customFindAll(grupo);
    }
    
    public Optional<Dato> obtener(String dato){
        return repo.findById(dato);
    }
}

