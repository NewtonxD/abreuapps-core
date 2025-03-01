package abreuapps.core.control.usuario;

import abreuapps.core.control.general.PersonaServ;
import abreuapps.core.control.utils.CorreoServ;
import abreuapps.core.control.utils.DateUtils;
import jakarta.transaction.Transactional;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */


@Service
@RequiredArgsConstructor
public class UsuarioServ {
    
    private final UsuarioRepo repo;
    
    private final PasswordEncoder passwordEncoder;

    private final DateUtils FechaUtils;

    private final PersonaServ PersonaServicio;
    
    private final CorreoServ CorreoServicio;
    
    private final  SessionRegistry sessionRegistry;
    
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
    
    public boolean coincidenPassword(String Contraseña,int IdUsuario){
        
        return passwordEncoder.matches(
            Contraseña, 
            repo.findById(IdUsuario).get().getPassword()
        );
    }

    @Transactional
    @CacheEvict(value={"Usuarios","Usuario"},allEntries = true)
    public List<Object> guardar(Usuario usuario, Integer idPersona, String fechaActualizacion){

        if(usuario.equals(null))
            return List.of( false,
                "El usuario no puede ser guardado. Por favor, inténtalo otra vez. COD: 00537"
            );

        var usuarioBD = obtenerPorId(usuario.getId());

        if (usuarioBD.isPresent()) {

            if (! FechaUtils.FechaFormato2
                    .format(usuarioBD.get().getFecha_actualizacion())
                    .equals(fechaActualizacion)
            ) return List.of( false,
                ! fechaActualizacion.isEmpty() ?
                        "Alguien ha realizado cambios en la información. Inténtentelo nuevamente. COD: 00535" :
                        "Este usuario ya existe!. Verifique e intentelo nuevamente."
            );


            usuario.setFecha_registro(usuarioBD.get().getFecha_registro());
            usuario.setHecho_por(usuarioBD.get().getHecho_por());
            usuario.setPassword(usuarioBD.get().getPassword());
            usuario.setPersona(usuarioBD.get().getPersona());

        }else{

            String nuevaContraseña=generarPassword();
            CorreoServicio.enviarMensajeSimple(
                    usuario.getCorreo(),
                    "Sistema: credenciales de su nueva cuenta",
                    "El usuario de su cuenta es: "+usuario.getUsername()+" y su contraseña es "+nuevaContraseña+" .");

            usuario.setCambiarPassword(true);
            usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
            usuario.setHecho_por(usuario.getId());
            usuario.setFecha_registro(new Date());

        }

        if(idPersona!=0)
            usuario.setPersona(PersonaServicio.obtenerPorId(idPersona).get());
        else
            return List.of( false,
                    "La información personal no pudo ser guardada. Por favor, inténtalo otra vez. COD: 00536"
            );


        cerrarSesion(usuario.getUsername());
        usuario.setFecha_actualizacion(new Date());
        repo.save(usuario);

        return List.of( true,
            "Registro guardado exitosamente!"
        );
    }
    
    @Cacheable("Usuarios")
    public List<UsuarioDTO> consultar(){  
        return repo.customFindAll(null);
    }
    
    @Cacheable("Usuario")
    public Optional<Usuario> obtener(String usuario){
        return repo.findByUsername(usuario);
    }
    
    @Cacheable("Usuario")
    public Optional<Usuario> obtenerPorCorreo(String correo){
        return repo.findByCorreo(correo);
    }
    
    @Cacheable("Usuario")
    public Optional<Usuario> obtenerPorId(Integer id){
        if(id==null || id==0) Optional.empty();
        return repo.findById(id);
    }
    
    public void cerrarSesion(String usuario){
        sessionRegistry
                .getAllPrincipals()
                .stream()
                .filter(principal -> principal instanceof UserDetails)
                .map(principal -> (UserDetails) principal)
                .filter(userDetails -> userDetails.getUsername().equals(usuario))
                .forEach(userDetails ->
                        sessionRegistry
                                .getAllSessions(userDetails, false)
                                .forEach(SessionInformation::expireNow)
                );
    }
}
