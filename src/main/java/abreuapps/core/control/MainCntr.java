package abreuapps.core.control;

import abreuapps.core.control.general.ConfServ;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.utils.TemplateServ;
import abreuapps.core.control.transporte.LogVehiculoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.UsuarioServ;
import abreuapps.core.control.utils.SSEServ;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

    private final AccesoServ AccesosServicio;

    private final TemplateServ TemplateServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    private final LogVehiculoServ LogVehiculoServicio;
    
    private final PublicidadServ PublicidadServicio;
    
    private final SSEServ SSEServicio;
    
    private final ConfServ confServ;
    
    private final ConfServ ConfiguracionServicio;
    
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS BASICOS SISTEMA---------------------------------//
//----------------------------------------------------------------------------//
    @RequestMapping({"/", "/index"})
    public String MainPage(
        Model model
    ) {
        var u =UsuarioServicio.obtener(AccesosServicio.getUsuarioLogueado().getUsername());
        if (!u.isPresent())
            return "redirect:/auth/logout";

        if (!u.get().isCredentialsNonExpired())
            return "redirect:/main/changePwd";
        
        model.addAttribute("app_nombre",ConfiguracionServicio.consultar("appnombre"));
        model.addAttribute("vhl_log",LogVehiculoServicio.consultar(100));
        model.addAttribute("today_views",PublicidadServicio.getTotalViewsHoy());
        model.addAttribute("active_views",SSEServicio.obtenerTotalClientesActivos());
        model.addAttribute("datos_personales",u.get().getPersona());
        model.addAttribute("conf",confServ.consultarConfMap());
        model.addAttribute("permisos",AccesosServicio.consultarAccesosMenuUsuario());
        model.addAttribute("server_ip",ConfiguracionServicio.consultar("serverip"));
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
        TemplateServicio.cargarDatosPagina(idPage, model);
        return "fragments/" + idPage ;
    }
//----------------------------------------------------------------------------//
    
    @RequestMapping("/changePwd")
    public String changePasswordExpired(
        Model model
    ) {
        var usuario = UsuarioServicio
                .obtener(AccesosServicio.getUsuarioLogueado().getUsername())
                .get();

        usuario.setPassword("");
        model.addAttribute("usuario", usuario);

        return "password";
           
    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/changeMyPwdNow")
    @ResponseBody
    public Map<String,String> changePasswordExpired(
        @RequestParam(name = "actualPassword",required = false) String AnteriorPassword,
        @RequestParam("newPassword") String NuevaPassword
    ) {
        AnteriorPassword = AnteriorPassword.equals(null) ? "" : AnteriorPassword ;
        var usuario = UsuarioServicio.obtener(AccesosServicio.getUsuarioLogueado().getUsername()).get();

        Map<String, String> respuesta= new HashMap<>();
        
        //si credenciales no estan expiradas verificar old pass
        if(usuario.isCredentialsNonExpired() &&
                ! UsuarioServicio.coincidenPassword(
                        AnteriorPassword, 
                        usuario.getId()
                )
          ) {
            respuesta.put("status", "warning");
            respuesta.put("msg", "Contraseña anterior incorrecta!");
            return respuesta; //contraseña vieja no matchea
        }
        
        usuario.setCambiarPassword(false);
        UsuarioServicio.cambiarPassword(usuario,NuevaPassword,false);
        
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
        
        if(!AccesosServicio.verificarPermisos("sys_configuracion"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;
        
        confServ.GuardarTodosMap(data, AccesosServicio.getUsuarioLogueado());
        model.addAttribute("status", true);
        model.addAttribute("msg", "Configuración guardada exitosamente!");

        TemplateServicio.cargarDatosPagina("sys_configuracion", model);
        
        return "fragments/sys_configuracion";

    }
    
}
