package abreusapp.core.control.general;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface DatoRepo extends JpaRepository<Dato, String>  {
    @Query(value="SELECT new abreusapp.core.control.general.DatoDTO("
            + "d.dato,"
            + "d.dato_padre,"
            + "d.descripcion,"
            + "d.activo"
            + ") FROM Dato d WHERE CASE WHEN ?2=false THEN coalesce(?1,'')=coalesce(d.dato_padre,'') ELSE true END "
    )
    List<DatoDTO> customFindAll(String datoPadre,Boolean TodosLosDatos);
}
