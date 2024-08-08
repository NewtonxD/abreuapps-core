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
public class VehiculoServ {
    
    private final VehiculoRepo repo;
    
    @Cacheable("Vehiculos")
    public List<VehiculoDTO> consultar(){
        return repo.customFindAll();
    }
    
    @Transactional
    public void procesarVehiculoSinActividad(){
        repo.stopAllWithoutActivity();
    }
    
    @Transactional
    @CacheEvict(value={"Vehiculos","RutasInfo","LogVehiculo"},allEntries = true)
    public void guardar(Vehiculo gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            if(usuario!=null)
                gd.setActualizado_por(usuario);
        }else{
            if(usuario!=null)
                gd.setHecho_por(usuario);
            
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
    }
    
    public Optional<Vehiculo> obtener(String Placa){
        return repo.findById(Placa);
    }
}
