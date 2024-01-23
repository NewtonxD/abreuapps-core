package com.dom.stp.omsa.control.domain.usuario;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByCorreo(String correo);
  
  Optional<Usuario> findByUsername(String username);

}
