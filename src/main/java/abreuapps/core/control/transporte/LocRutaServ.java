package abreuapps.core.control.transporte;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class LocRutaServ {
    
    private final LocRutaRepo repo;
    
    @Cacheable(value="RutasLoc")
    public List<LocRutaDTO> consultar(String PorRuta,Boolean ActivoRuta){
        return repo.customFindAll(PorRuta,ActivoRuta);
    }
    
    @Transactional
    public void borrarPorRuta(Ruta ruta){
        repo.deleteAllByRuta(ruta);
    }
    
    @Transactional
    @CacheEvict(value={"RutasLoc","RutasInfo"},allEntries = true)
    public void guardarTodos(List<LocRuta> gd){
        repo.saveAll(gd);
    }
    
    public List<LocRuta> generarLista(String lista_cadena, Ruta ruta ){
        /*List<LocRuta> points = new ArrayList<>();
        String[] coordinatePairs = lista_cadena.split("],\\[");

        for (String pair : coordinatePairs) {
            pair = pair.replace("[", "").replace("]", "");
            String[] latLng = pair.split(", ");
            double latitude = Double.parseDouble(latLng[1]);
            double longitude = Double.parseDouble(latLng[0]);
            
            points.add(new LocRuta(null,ruta,latitude, longitude));
        }*/

        return Arrays.stream(lista_cadena.split("],\\["))
                .map(pair -> pair.replace("[", "").replace("]", ""))
                .map(pair -> {
                    String[] latLng = pair.split(", ");
                    double latitude = Double.parseDouble(latLng[1]);
                    double longitude = Double.parseDouble(latLng[0]);
                    return new LocRuta(null, ruta, latitude, longitude);
                })
                .collect(Collectors.toList());
    }
    
}

