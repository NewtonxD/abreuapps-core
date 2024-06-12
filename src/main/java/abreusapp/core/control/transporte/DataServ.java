package abreusapp.core.control.transporte;

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
public class DataServ {
    
    private final DataRepo repo;
    
    @Cacheable("PMC")
    public Object[] getParadaMasCercana(Double Latitud,Double Longitud){
        return repo.findParadaMasCercana(Latitud,Longitud);
    }
    
    @Cacheable("PI")
    public List<Object[]> getParadaInfo(Integer idParada){
        return repo.findParadaInfo(idParada);
    }
}
