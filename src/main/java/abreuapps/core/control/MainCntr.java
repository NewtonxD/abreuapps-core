package abreuapps.core.control;

import abreuapps.core.control.general.ConfServ;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.transporte.LogVehiculoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.UsuarioServ;
import abreuapps.core.control.utils.ModelServ;
import abreuapps.core.control.utils.SSEServ;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Newton
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainCntr {

    private final ModelServ DataModelServicio;

    private final AccesoServ AccesosServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    private final PasswordEncoder passwordEncoder;
    
    private final LogVehiculoServ LogVehiculoServicio;
    
    private final PublicidadServ PublicidadServicio;
    
    private final SSEServ SSEServicio;
    
    private final ConfServ confServ;
    
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS BASICOS SISTEMA---------------------------------//
//----------------------------------------------------------------------------//
    @RequestMapping({"/", "/index"})
    public String MainPage(
        Model model
    ) {
        Usuario u = DataModelServicio.getUsuarioLogueado();
        
        if (!UsuarioServicio.obtener(
                u.getUsername()
            ).get().isCredentialsNonExpired()
        ) return "redirect:/main/changePwd";
        
        model.addAttribute("vhl_log",LogVehiculoServicio.consultar(100));
        model.addAttribute("total_views_today",PublicidadServicio.getTotalViewsHoy());
        model.addAttribute("total_active_views",SSEServicio.obtenerTotalClientesActivos());
        model.addAttribute("datos_personales",u.getPersona());
        model.addAllAttributes(confServ.consultarConfMap());
        model.addAllAttributes(AccesosServicio.consultarAccesosMenuUsuario(u.getId()));
        
        return "index";
    }
//----------------------------------------------------------------------------//
    @RequestMapping("/leaflet.js.map")
    public String LeatLeaftJsMap() {        
        return "redirect:/content/js/lib/leaflet.js.map";
    }
//----------------------------------------------------------------------------//  
    @RequestMapping("/leaflet-geoman.js.map")
    public String GeomanJsMap(){
        return "redirect:/content/js/lib/leaflet-geoman.js.map";
    }
//----------------------------------------------------------------------------//  

    @RequestMapping(value = "/content-page/", method = RequestMethod.POST)
    public String loadContetPage(
        Model model,
        @RequestParam("id") String idPage
    ) {

        Usuario u = DataModelServicio.getUsuarioLogueado();
        DataModelServicio.load(idPage, model, u.getId());

        return "fragments/" + idPage + " :: content-default";
    }
//----------------------------------------------------------------------------//
    
    @RequestMapping("/changePwd")
    public String changePasswordExpired(
        Model model
    ) {
        Usuario userSession = DataModelServicio.getUsuarioLogueado();
        Usuario userBd = UsuarioServicio.obtener(userSession.getUsername()).get();
        userBd.setPassword("");
        model.addAttribute("usuario", userBd);
        
        if(userBd.isCredentialsNonExpired())
            return "password  :: content-default";
        else 
            return "password";
           
    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/changeMyPwdNow")
    @ResponseBody
    public Map<String,String> changePasswordExpired(
        @RequestParam(name = "actualPassword",required = false) String oldPwd,
        @RequestParam("newPassword") String newPwd
    ) {
        oldPwd = oldPwd==null ? "" : oldPwd ;
        Usuario userSession = DataModelServicio.getUsuarioLogueado();
        Usuario userBd = UsuarioServicio.obtener(userSession.getUsername()).get();
        Map<String, String> respuesta= new HashMap<>();
        
        //si credenciales no estan expiradas verificar old pass
        if(userBd.isCredentialsNonExpired() && 
                !passwordEncoder.matches(
                        oldPwd, 
                        userBd.getPassword()
                )
          ) {
            respuesta.put("status", "warning");
            respuesta.put("msg", "Contraseña anterior incorrecta!");
            return respuesta; //contraseña vieja no matchea
        }
        
        
        userBd.setCambiarPassword(false);
        userBd.setPassword(passwordEncoder.encode(newPwd));
        UsuarioServicio.guardar(userBd, UsuarioServicio.obtenerPorId(1).get() , true);
        respuesta.put("status", "success");
        respuesta.put("msg", "Contraseña fue guardada exitosamente! En breve lo redirigiremos.");
        return respuesta;
        
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/saveConf")
    public String GuardarConfiguracion(
        Model model,
        @RequestParam Map<String,String> data
    ) {
        
        Usuario u= DataModelServicio.getUsuarioLogueado();
        
        String verificarPermisos= DataModelServicio.verificarPermisos("sys_configuracion", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;
        
        confServ.GuardarTodosMap(data, u);
        model.addAttribute("status", true);
        model.addAttribute("msg", "Configuración guardada exitosamente!");
           
        
        DataModelServicio.load("sys_configuracion", model, u.getId());
        
        return "fragments/sys_configuracion :: content-default";

    }
    
}
