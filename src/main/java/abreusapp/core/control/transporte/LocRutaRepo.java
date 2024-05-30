package abreusapp.core.control.transporte;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface LocRutaRepo  extends JpaRepository<LocRuta, Long> {
    
    @Query(value="SELECT new abreusapp.core.control.transporte.LocRutaDTO("
            + "r.id,"
            + "r.ruta.ruta,"
            + "r.longitud,"
            + "r.latitud"
            + ") FROM LocRuta r WHERE CASE WHEN ?1 != null THEN r.ruta.ruta=?1 ELSE true END")
    List<LocRutaDTO> customFindAll(String ruta);
    
    void deleteAllByRuta(Ruta ruta);
}


