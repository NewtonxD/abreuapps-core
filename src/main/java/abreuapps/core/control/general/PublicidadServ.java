package abreuapps.core.control.general;

import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.RecursoServ;
import jakarta.transaction.Transactional;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class PublicidadServ {
    
    private final PublicidadRepo repo;
    
    private final RecursoServ ResourcesServicio;
    
    private final ConfServ ConfiguracionServicio;
    
    @Cacheable(value="Publicidades")
    public List<PublicidadDTO> consultar(){
        return repo.customFindAll();
    }
    
    @Transactional
    @CacheEvict(value={"Publicidad","Publicidades","PublicidadArchivos"}, allEntries = true)
    public void guardar(Publicidad gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(usuario);
        }else{
            gd.setHecho_por(usuario);
            gd.setFecha_registro(new Date());
            gd.setConteo_clic(0);
            gd.setConteo_view(0);
        }
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
    }
    
    public Optional<Publicidad> obtener(Long id){
        return repo.findById(id);
    }
    
    @Async
    @Transactional
    public void IncrementarVistas(Long id){
        repo.customIncreaseViewsById(id);
    }
    
    @Async
    @Transactional
    public void IncrementarClics(Long id){
        repo.customIncreaseClickById(id);
    }
    
    @Transactional
    public void procesarPublicidadFinalizada(){
        repo.stopAllOutDated();
    }
    
    @Transactional
    public void procesarEstadisticas(){
        repo.saveStatictics();
    }
    
    @Async
    @Transactional
    public void aumentarVisitas(){
        repo.addClientVisit();
    }
    
    public Integer getTotalViewsHoy(){
        return repo.consultarTotalViewsHoy();
    }
    
    @Cacheable(value="Publicidad")
    public PublicidadDTO obtenerUltimo(){
        return repo.customFindCurrent();
    }
    
    @Cacheable(value="PublicidadArchivos")
    public Map<String, Object> obtenerArchivoPublicidad(String ruta){
        return ResourcesServicio.obtenerArchivo(ruta);
    }
}
