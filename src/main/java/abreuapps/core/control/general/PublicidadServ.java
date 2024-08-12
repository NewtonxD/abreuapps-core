package abreuapps.core.control.general;

import abreuapps.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
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
    
    @Value("${abreuapps.core.files.directory}")
    private String FILE_DIRECTORY; 
    
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
    
    @Cacheable(value="Publicidad")
    public PublicidadDTO obtenerUltimo(){
        return repo.customFindCurrent();
    }
    
    @Cacheable(value="PublicidadArchivos")
    public Map<String, Object> obtenerArchivoPublicidad(String ruta){
        try {
            ruta=ruta.replaceAll("[^a-zA-Z0-9.]", "");
            Path filePath = Paths.get(FILE_DIRECTORY).toAbsolutePath().normalize().resolve(ruta).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return null;
            }
            String contentType = ruta.endsWith(".mp4") ? "video/mp4" : "image/"+ruta.substring(ruta.lastIndexOf("."));
            if(contentType.equals("image/jpg"))contentType="image/jpeg";
            
            Map<String, Object> m = new HashMap<>();
            m.put("body", resource);
            m.put("media-type",MediaType.parseMediaType(contentType));
            return m;
        }catch(MalformedURLException ex){
                return null;
        }
    }
}
