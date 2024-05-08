/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.usuario;

import abreusapp.core.control.general.Persona;
import abreusapp.core.control.utils.CorreoServ;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */


@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServ {
    
    private final UsuarioRepo repo;
    
    private final PasswordEncoder passwordEncoder;
    
    private final CorreoServ correoServicio;
    
    private final  SessionRegistry sessionRegistry;
    
    private static final String LETTERS = "@#$%!AbCdEfGhIjKlMnOpQrRtUvWxYz-*[]";
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    public String generarPassword() {
        StringBuilder password = new StringBuilder();

        // Generate two random letters
        for (int i = 0; i < 2; i++) {
            char letter = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
            password.append(letter);
        }

        // Generate six random numbers
        for (int i = 0; i < 6; i++) {
            char number = NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length()));
            password.append(number);
        }

        return password.toString();
    }
    
    public Usuario cambiarPassword(Usuario u, String Contraseña, boolean enviaCorreo){
        
        correoServicio.enviarMensajeSimple(
                u.getCorreo(),
                "Sistema: Su contraseña fue actualizada", 
                "Su nueva contraseña es "+Contraseña+" . Al ingresar podra colocar una nueva contraseña.");
        u.setPassword(passwordEncoder.encode(Contraseña));
        u.setCambiarPassword(true);
        return guardar(u,u,true);
    }
    
    public boolean coincidenContraseña(String Contraseña,int IdUsuario){
        
        return passwordEncoder.matches(
            Contraseña, 
            repo.findById(IdUsuario).get().getPassword()
        );
    }
    
    public Usuario guardar(Usuario gd, Usuario usuario,boolean existe){
        
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
        
        return repo.save(gd);
    }
    
    public List<Usuario> consultar(){
        List<Usuario> ul=repo.findAll();        
        return ul;
    }
    
    public Optional<Usuario> obtener(String usuario){
        return repo.findByUsername(usuario);
    }
    
    public Optional<Usuario> obtenerPorCorreo(String correo){
        return repo.findByCorreo(correo);
    }
    
    public Optional<Usuario> obtenerPorPersona(Persona persona){
        return repo.findByPersona(persona);
    }
    
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
