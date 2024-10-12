package abreuapps.core.control.transporte;

import abreuapps.core.control.usuario.Usuario;
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
public class ParadaServ {
    
    private final ParadaRepo repo;
    
    @Cacheable("Paradas")
    public List<ParadaDTO> consultarTodo(Integer excluyeParada,Boolean activo){
        return repo.findAllCustom(excluyeParada, activo);
    }
    
    @Transactional
    @CacheEvict(value={"Paradas","RutasInfo"},allEntries = true)
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
    
    @Cacheable("PMC")
    public Object[] getParadaMasCercana(Double Latitud,Double Longitud){
        return repo.findParadaMasCercana(Latitud,Longitud);
    }
    
    @Cacheable("PI")
    public List<Object[]> getParadaInfo(Integer idParada){
        return repo.findParadaInfo(idParada);
    }
    
    public Optional<Parada> obtener(Integer id){
        if(id==null){
            return Optional.empty();
        }
        return repo.findById(id);
    }
}
