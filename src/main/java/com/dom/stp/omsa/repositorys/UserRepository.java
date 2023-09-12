package com.dom.stp.omsa.repositorys;

import com.dom.stp.omsa.entities.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByCorreo(String correo);
  
  Optional<Usuario> findByUsuario(String usuario);

}
