package abreusapp.core.control.usuario;

import java.io.Serializable;

public record UsuarioDTO(
    Integer id,
    String usr,
    String mail,
    boolean act,
    boolean pwd_chg,
    Integer persona_id
) implements Serializable {}
