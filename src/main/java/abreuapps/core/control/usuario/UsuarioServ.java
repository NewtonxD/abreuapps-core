package abreuapps.core.control.usuario;

import abreuapps.core.control.utils.CorreoServ;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    
    private final CorreoServ correoServicio;
    
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
        
        correoServicio.enviarMensajeSimple(
                u.getCorreo(),
                "Sistema: Su contraseña fue actualizada", 
                "Su nueva contraseña es "+Contraseña+" . Al ingresar podra colocar una nueva contraseña.");
        u.setPassword(passwordEncoder.encode(Contraseña));
        u.setCambiarPassword(true);
        guardar(u,u,true);
    }
    
    public boolean coincidenPassword(String Contraseña,int IdUsuario){
        
        return passwordEncoder.matches(
            Contraseña, 
            repo.findById(IdUsuario).get().getPassword()
        );
    }
    
    @Transactional
    @CacheEvict(value={"Usuarios","Usuario"},allEntries = true)
    public void guardar(Usuario gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(usuario.getId());
        }else{
            String nuevaContraseña=generarPassword();
            correoServicio.enviarMensajeSimple(
                gd.getCorreo(),
                "Sistema: Su contraseña fue actualizada", 
                "Su nueva contraseña es "+nuevaContraseña+" . Al ingresar podra colocar una nueva contraseña.");
            
            gd.setCambiarPassword(true);
            gd.setPassword(passwordEncoder.encode(nuevaContraseña));
            gd.setHecho_por(usuario.getId());
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
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
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof UserDetails userDetails) {
                if (userDetails.getUsername().equals(usuario)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }
}
