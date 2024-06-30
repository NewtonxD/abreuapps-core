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
    
    @Query(
        value = "select tl.*,vhl.* "
                + " from transport.trp_loc tl inner join transport.vhl vhl on tl.placa_pl=vhl.pl "
                + " where tl.placa_pl = :placa order by tl.id desc limit 1 ",
        nativeQuery = true
    )
    Optional<LocVehiculo> findLastByPlaca(String placa);
    
    @Query(
        value="SELECT * FROM transport.get_lastloc_vhl_data()",
        nativeQuery=true
    )
    List<Object[]> findUltimoEnCamino();
}
