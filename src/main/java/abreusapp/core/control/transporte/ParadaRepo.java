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
public interface ParadaRepo extends JpaRepository<Parada,Integer> {
    
    @Query(
        value = "select new abreusapp.core.control.transporte.ParadaDTO("+
                "p.id,"+
                "p.descripción,"+
                "p.longitud,"+
                "p.latitud,"+
                "p.activo"+
                ") from Parada p where  p.id != coalesce(?1,0) and case when ?2!=null then p.activo=?2 else 1=1 end order by p.id desc"
    )
    List<ParadaDTO> findAllCustom(Integer excluyeId,Boolean activo);
    
    @Query(value="SELECT * FROM transport.get_nearest_pda(?2,?1)",nativeQuery = true)
    Object[] findParadaMasCercana(Double Latitud,Double Longitud);
    
    @Query(value="SELECT * FROM transport.get_pda_data(?1)",nativeQuery = true)
    List<Object[]> findParadaInfo(Integer idParada);
    
}
