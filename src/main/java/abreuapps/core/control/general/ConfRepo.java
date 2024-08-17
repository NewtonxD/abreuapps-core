package abreuapps.core.control.general;

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
public interface ConfRepo extends JpaRepository<Conf, String> {
    @Query(value="SELECT new abreuapps.core.control.general.ConfDTO("
            + "c.codigo,"
            + "c.descripcion,"
            + "c.valor"
            + ") FROM Conf c")
    List<ConfDTO> customFindAll();
    
    @Query(value="SELECT c.valor FROM Conf c WHERE c.codigo=?1")
    Optional<String>customFind(String codigo);
}
