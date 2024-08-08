package abreuapps.core.control.transporte;

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
    
    @Query(value="SELECT new abreuapps.core.control.transporte.LocRutaDTO("
            + "r.id,"
            + "r.ruta.ruta,"
            + "r.longitud,"
            + "r.latitud"
            + ") FROM LocRuta r WHERE CASE WHEN coalesce(?1,'')<>'' THEN r.ruta.ruta=?1 ELSE true END "
            + " AND CASE WHEN ?2<>null THEN r.ruta.activo=?2 ELSE true END ")
    List<LocRutaDTO> customFindAll(String PorRuta,Boolean ActivoRuta);
    
    void deleteAllByRuta(Ruta ruta);
}


