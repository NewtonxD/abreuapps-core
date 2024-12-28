package abreuapps.core.control.transporte;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */


@Service
@RequiredArgsConstructor
public class LogVehiculoServ {
    
    private final LogVehiculoRepo repo;
    
    @Cacheable("LogVehiculo")
    public List<LogVehiculo> consultar(int limit){
        return repo.customFindAll(limit);
    }
    
}
