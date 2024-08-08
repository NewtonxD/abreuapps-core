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
public interface LogVehiculoRepo extends JpaRepository<LogVehiculo, Long>{
    @Query(value="SELECT * FROM transport.vhl_log lv ORDER BY lv.reg_dt DESC LIMIT :limit",nativeQuery = true)
    List<LogVehiculo> customFindAll(int limit);
}
