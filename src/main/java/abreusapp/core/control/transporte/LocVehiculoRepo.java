package abreusapp.core.control.transporte;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface LocVehiculoRepo extends JpaRepository<LocVehiculo, Long>{
        
    @Query(value="SELECT new abreusapp.core.control.transporte.LocVehiculoDTO("
            + "l.id,"
            + "l.placa.placa,"
            + "l.latitud,"
            + "l.longitud,"
            + "l.fecha_registro"
            + ") FROM LocVehiculo l WHERE "
            + " CASE WHEN coalesce(?1,'')<>'' THEN l.placa.placa=?1 ELSE true END "
            + " ORDER BY l.fecha_registro DESC"
            + " LIMIT ?2")
    List<LocVehiculoDTO> customFindAll(String placa,int limit);
    
    @Query(
        value = "select tl.*,vhl.* "
                + " from transport.trp_loc tl inner join transport.vhl vhl on tl.placa_pl=vhl.pl "
                + " where tl.placa_pl = :placa order by tl.id desc limit 1 ",
        nativeQuery = true
    )
    Optional<LocVehiculo> findLastByPlaca(String placa);
}
