package abreuapps.core.control.utils;

import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordServ {

    private final UsuarioRepo repo;

    private final CorreoServ CorreoServicio;

    private final PasswordEncoder passwordEncoder;

    private static final String LETTERS = "@#$%!AbCdEfGhIjKlMnOpQrRtUvWxYz-*[]~`|:ñÑ";
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    public String generarPassword() {
        StringBuilder password = new StringBuilder();

        // Generate two random letters
        for (int i = 0; i < 4; i++) {
            password.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
            password.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }

        return password.toString();
    }


    public void cambiarPassword(Usuario u, String Contraseña, boolean enviaCorreo){
        if(enviaCorreo)
            CorreoServicio.enviarMensajeSimple(
                    u.getCorreo(),
                    "Sistema: Su contraseña fue actualizada",
                    "Su nueva contraseña es "+Contraseña+" . Al ingresar podra colocar una nueva contraseña.");

        u.setPassword(passwordEncoder.encode(Contraseña));
        u.setCambiarPassword(true);
        u.setActualizado_por(u.getId());
        u.setFecha_actualizacion(new Date());
        repo.save(u);
    }

    public void generarPasswordNuevaCuenta(Usuario usuario){
        String nuevaContraseña=generarPassword();
        CorreoServicio.enviarMensajeSimple(
                usuario.getCorreo(),
                "Sistema: credenciales de su nueva cuenta",
                "El usuario de su cuenta es: "+usuario.getUsername()+" y su contraseña es "+nuevaContraseña+" .");

        usuario.setCambiarPassword(true);
        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        repo.save(usuario);
    }

    public boolean coincidenPassword(String Contraseña,int IdUsuario){

        return passwordEncoder.matches(
                Contraseña,
                repo.findById(IdUsuario).get().getPassword()
        );
    }
}
