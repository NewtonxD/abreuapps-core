package abreusapp.core.control.transporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */
@Repository
public interface DataRepo extends JpaRepository<Vehiculo, String>{
    
    @Query(value="SELECT * FROM transport.get_nearest_pda(?2,?1)",nativeQuery = true)
    Object[] findParadaMasCercana(Double Latitud,Double Longitud);
}