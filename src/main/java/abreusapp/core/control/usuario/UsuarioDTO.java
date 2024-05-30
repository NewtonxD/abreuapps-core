package abreusapp.core.control.usuario;

public record UsuarioDTO(
    Integer id,
    String usr,
    String mail,
    boolean act,
    boolean pwd_chg,
    Integer persona_id
) {}
