package abreusapp.core.control.usuario;

import abreusapp.core.control.general.Dato;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */
@Repository
public interface AccesoRepo extends JpaRepository<Acceso, Integer>{
  Optional<Acceso> findByPantalla(Dato pantalla);
}
