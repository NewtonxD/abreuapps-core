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

    private final DateUtils FechaUtils;

    private final PersonaServ PersonaServicio;



    @Transactional
    @CacheEvict(value={"Usuarios","Usuario"},allEntries = true)
    public List<Object> guardar(Usuario usuario, Integer idPersona, String fechaActualizacion){
        boolean nuevoUsuario=false;
        if(usuario.equals(null))
            return List.of( false,
                "El usuario no puede ser guardado. Por favor, inténtalo otra vez. COD: 00537",
                    nuevoUsuario
            );

        var usuarioBD = obtenerPorId(usuario.getId());

        if (usuarioBD.isPresent()) {

            if (! FechaUtils.FechaFormato2
                    .format(usuarioBD.get().getFecha_actualizacion())
                    .equals(fechaActualizacion)
            ) return List.of( false,
                ! fechaActualizacion.isEmpty() ?
                        "Alguien ha realizado cambios en la información. Inténtentelo nuevamente. COD: 00535" :
                        "Este usuario ya existe!. Verifique e intentelo nuevamente.",
                    nuevoUsuario
            );


            usuario.setFecha_registro(usuarioBD.get().getFecha_registro());
            usuario.setHecho_por(usuarioBD.get().getHecho_por());
            usuario.setPassword(usuarioBD.get().getPassword());
            usuario.setPersona(usuarioBD.get().getPersona());

        }else{
            nuevoUsuario=true;
            usuario.setHecho_por(usuario.getId());
            usuario.setFecha_registro(new Date());

        }

        if(idPersona!=0)
            usuario.setPersona(PersonaServicio.obtenerPorId(idPersona).get());
        else
            return List.of( false,
                    "La información personal no pudo ser guardada. Por favor, inténtalo otra vez. COD: 00536",
                    nuevoUsuario
            );

        usuario.setFecha_actualizacion(new Date());
        var usuarioDB = repo.save(usuario);

        return List.of( true,
            "Registro guardado exitosamente!",
                nuevoUsuario,
                usuarioDB
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

}
