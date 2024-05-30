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
public interface RutaRepo extends JpaRepository<Ruta, String> {
    
    @Query(value="SELECT new abreusapp.core.control.transporte.RutaDTO("
            + "r.ruta,"
            + "r.localizacion_inicial,"
            + "r.localizacion_final,"
            + "r.activo"
            + ") FROM Ruta r WHERE CASE WHEN ?1!=null THEN r.activo=?1 ELSE true END")
    List<RutaDTO> customFindAll(Boolean activo);
}
