package abreusapp.core.control.usuario;

import abreusapp.core.control.general.Persona;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByCorreo(String correo);
  
  Optional<Usuario> findByUsername(String username);
  
  Optional<Usuario> findByPersona(Persona persona);

}
