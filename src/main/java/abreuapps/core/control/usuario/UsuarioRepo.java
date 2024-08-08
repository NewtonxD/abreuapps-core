package abreuapps.core.control.usuario;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {
    
    @Query(value = "SELECT new abreuapps.core.control.usuario.UsuarioDTO(" +
           "u.id, " +
           "u.username, " +
           "u.correo, " +
           "u.activo, " +
           "u.cambiarPassword, " +
           "u.persona.id) FROM Usuario u WHERE CASE WHEN ?1<>null THEN u.activo=?1 ELSE true END")
    List<UsuarioDTO> customFindAll(Boolean activo);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByUsername(String username);

}
