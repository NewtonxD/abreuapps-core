package abreuapps.core.control.general;

import abreuapps.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
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
import org.springframework.core.io.FileSystemResource;
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
    
    @Cacheable(value="Publicidad")
    public PublicidadDTO obtenerUltimo(){
        return repo.customFindCurrent();
    }
    
    @Cacheable(value="PublicidadArchivos")
    public Map<String,Object> obtenerArchivoPublicidad(PublicidadDTO publicidad,String tipo) throws IOException{
        String archivo = tipo.equals("sm") ? publicidad.sm_img_vid() : publicidad.lg_img_vid();
        
        if( archivo.equals("") ) 
            archivo = tipo.equals("lg") ? publicidad.sm_img_vid() : publicidad.lg_img_vid();
        
        Path path = Paths.get(archivo);
        if (! Files.exists(path)) {
            return null;
        }
        String contentType=Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        Map<String, Object> m = new HashMap<>();
        m.put("archivo", new FileSystemResource(path).getContentAsByteArray());
        m.put("media-type",MediaType.parseMediaType(contentType));
        return m;
    }
}
