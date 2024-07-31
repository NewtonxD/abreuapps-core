package abreusapp.core.control.transporte;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface VehiculoRepo extends JpaRepository<Vehiculo, String> {
    
    @Query(value="SELECT new abreusapp.core.control.transporte.VehiculoDTO("
            + "v.placa,"
            + "v.marca.dato,"
            + "v.modelo.dato,"
            + "v.ruta.ruta,"
            + "v.capacidad_pasajeros,"
            + "v.anio_fabricacion,"
            + "v.tipo_vehiculo.dato,"
            + "v.estado.dato,"
            + "v.color.dato,"
            + "v.activo,"
            + "v.token"
            + ") FROM Vehiculo v")
    List<VehiculoDTO> customFindAll();
    
    @Modifying
    @Query(value=""
            + " update transport.vhl v set ruta_rta=null,estado_dat='Estacionado',tkn='n/a' " 
            + " where v.estado_dat ='En Camino' and " 
            + " (now()-(select max(reg_dt) from transport.trp_loc tl where v.pl=tl.placa_pl and v.ruta_rta=tl.ruta_rta) )> interval '1 minutes' and "
            + " (now()-v.upd_at )> interval '1 minutes'"
            ,nativeQuery = true)
    void stopAllWithoutActivity();
}
