package abreuapps.core.control.transporte;

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
public class LocVehiculoServ {
    
    private final LocVehiculoRepo repo;
    
    @Transactional
    @CacheEvict({"LV","PI"})
    public LocVehiculo guardar(LocVehiculo Loc){
        Loc.setFecha_registro(new Date());
        return repo.save(Loc);
    }
    
    @Cacheable("LV")
    public List<Object[]> consultarDatosTransporteEnCamino(){
        return repo.findUltimoEnCamino();
    }
    
    public Optional<LocVehiculo> consultarUltimaLocVehiculo(String placa){
        return repo.findLastByPlaca(placa);
    }
    
    public boolean tieneUltimaLoc(String placa){
        return consultarUltimaLocVehiculo(placa)!=null;
    }
    
}
