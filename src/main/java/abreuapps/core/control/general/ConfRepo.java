package abreuapps.core.control.general;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */
@Repository
public interface ConfRepo extends JpaRepository<Conf, String> {
    @Query(value="SELECT new abreuapps.core.control.general.ConfDTO("
            + "c.codigo,"
            + "c.descripcion,"
            + "c.valor"
            + ") FROM Conf c")
    List<ConfDTO> customFindAll();
}
