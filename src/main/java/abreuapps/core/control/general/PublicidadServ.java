package abreuapps.core.control.general;

import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.RecursoServ;
import jakarta.transaction.Transactional;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    
    private final AccesoServ AccesoServicio;

    private final DateUtils FechaUtils;
    
    @Cacheable(value="Publicidades")
    public List<PublicidadDTO> consultar(){
        return repo.customFindAll();
    }
    
    @Transactional
    @CacheEvict(value={"Publicidad","Publicidades","PublicidadArchivos"}, allEntries = true)
    public List<Object> guardar(Publicidad publicidad, String fechaActualizacion){

        Optional<Publicidad> publicidadDB = obtener(! publicidad.getId().equals(null) ? publicidad.getId() : 0 );
        List<Object> resultados = new ArrayList<>();
        Usuario usuario = AccesoServicio.getUsuarioLogueado();

        if (publicidadDB.isPresent()) {

            if (! FechaUtils.FechaFormato2
                    .format(publicidadDB.get().getFecha_actualizacion())
                    .equals(fechaActualizacion)
            ) {
                resultados.add(false);
                resultados.add(
                        ! ( fechaActualizacion == null ||
                                fechaActualizacion.equals("") ) ?
                                "Alguien ha realizado cambios en la información. Inténtalo nuevamente. COD: 00686" :
                                "Esta Publicidad ya existe!. Verifique e intentelo nuevamente."
                );
                return resultados;
            }

            publicidad.setFecha_registro(publicidadDB.get().getFecha_registro());
            publicidad.setHecho_por(publicidadDB.get().getHecho_por());
            publicidad.setConteo_clic(publicidadDB.get().getConteo_clic());
            publicidad.setConteo_view(publicidadDB.get().getConteo_view());
            publicidad.setActualizado_por(usuario);

        }else{
            publicidad.setHecho_por(usuario);
            publicidad.setFecha_registro(new Date());
            publicidad.setConteo_clic(0);
            publicidad.setConteo_view(0);
        }


        publicidad.setFecha_actualizacion(new Date());
        repo.save(publicidad);

        resultados.add(true);
        resultados.add( "Registro guardado exitosamente!");
        return resultados;
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
