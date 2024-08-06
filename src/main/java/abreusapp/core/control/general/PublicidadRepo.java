package abreusapp.core.control.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface PublicidadRepo extends JpaRepository<Publicidad, Long> {    
}
