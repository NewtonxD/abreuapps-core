package com.dom.stp.omsa.control.domain.persona;

import com.dom.stp.omsa.control.domain.usuario.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepo extends JpaRepository<Persona, Integer> {

  Optional<Persona> findByCedula(String cedula);
  
  Optional<Persona> findByUsuario(Usuario usuario);
  
  List<Persona> findByUsuarioIsNotNull();

}
