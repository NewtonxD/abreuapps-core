package abreuapps.core.control;

import abreuapps.core.control.general.TemplateServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.UsuarioServ;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/acc")
public class PermisosCntr {

    private final AccesoServ AccesoServicio;
    
    private final UsuarioServ UsuarioServicio;

    private final TemplateServ TemplateServicio;

//----------------------------------------------------------------------------//
    @PostMapping("/access")
    public String ActualizarPermisosUsuario(
        Model model, 
        String idUsuario
    ) {

        if(! AccesoServicio.verificarPermisos("usr_mgr_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        Optional<Usuario> usuarioBD = UsuarioServicio.obtener(idUsuario);

        if( ! usuarioBD.isPresent() )
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("usuario",usuarioBD.get());
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario("usr_mgr_registro"));

        return "fragments/usr_mgr_permisos";
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/get-access", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerListadoPermisosUsuario(
        @RequestParam("idUsuario") String nombreUsuario
    ) {  
        if(! AccesoServicio.verificarPermisos("usr_mgr_registro"))
            return ResponseEntity.notFound().build();

        Optional<Usuario> usuarioBD=UsuarioServicio.obtener(nombreUsuario);

        if(!usuarioBD.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                AccesoServicio.ListadoAccesosUsuarioEditar( usuarioBD.get().getId() )
        );
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/save-acc")
    public String GuardarPermisosUsuario(
        Model model,
        @RequestParam Map<String,String> data
    ) {
        
        if(!AccesoServicio.verificarPermisos("usr_mgr_registro" ))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        Optional<Usuario> usuarioBD=UsuarioServicio.obtener(data.getOrDefault("idUsuario",""));

        if(!usuarioBD.isPresent()){
            model.addAttribute("status",false);
            model.addAttribute("msg",
                    "No pudimos encontrar al usuario. Por favor, inténtalo otra vez. COD: 00545"
            );
        }else {
            UsuarioServicio.cerrarSesion(usuarioBD.get().getUsername());
            AccesoServicio.GuardarTodosMap(data, usuarioBD.get());
            model.addAttribute("msg", "Permisos guardados exitosamente!");
            model.addAttribute("status", true);
        }
        TemplateServicio.cargarDatosPagina("usr_mgr_principal",model);
        return "fragments/usr_mgr_principal";
    }
//----------------------------------------------------------------------------//
}
