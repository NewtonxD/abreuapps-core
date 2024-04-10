/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.ConfServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.UsuarioServ;
import abreusapp.core.control.utils.ModelServ;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Newton
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class MainCntr {

    private final ModelServ DataModelServicio;

    private final AccesoServ AccesosServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    private final PasswordEncoder passwordEncoder;
    
    private final ConfServ confServ;
    
    
//----------------------------------------------------------------------------//
//--------------------------MAIN----------------------------------------------//
//----------------------------------------------------------------------------//
    @RequestMapping({"/main/", "/main/index"})
    public String MainPage(
        HttpServletRequest request,
        Model model
    ) {
        Usuario u = DataModelServicio.getUsuarioLogueado();
        
        if (!UsuarioServicio.obtener(
                u.getUsername()
            ).get().isCredentialsNonExpired()
        ) return "redirect:/main/changePwd";
        
        model.addAttribute("datos_personales",u.getPersona());
        model.addAllAttributes(confServ.consultarConfMap());
        model.addAllAttributes(AccesosServicio.consultarAccesosMenuUsuario(u.getId()));
        
        return "index";
    }
//----------------------------------------------------------------------------//

    @RequestMapping(value = "/main/content-page/", method = RequestMethod.POST)
    public String loadContetPage(
            HttpServletRequest request,
            Model model,
            @RequestParam("id") String idPage
    ) {

        Usuario u = DataModelServicio.getUsuarioLogueado();
        DataModelServicio.load(idPage, model, u.getId());

        return "fragments/" + idPage + " :: content-default";
    }
//----------------------------------------------------------------------------//
    
    @RequestMapping("/main/changePwd")
    public String changePasswordExpired(
            HttpServletRequest request,
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
    
    @PostMapping("/main/changeMyPwdNow")
    @ResponseBody
    public Map<String,String> changePasswordExpired(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "actualPassword",required = false) String oldPass,
            @RequestParam("newPassword") String newPass
    ) {
        oldPass = oldPass==null ? "" : oldPass ;
        Usuario userSession = DataModelServicio.getUsuarioLogueado();
        Usuario userBd = UsuarioServicio.obtener(userSession.getUsername()).get();
        Map<String, String> respuesta= new HashMap<>();
        
        //si credenciales no estan expiradas verificar old pass
        if(userBd.isCredentialsNonExpired() && 
                !passwordEncoder.matches(
                        oldPass, 
                        userBd.getPassword()
                )
          ) {
            respuesta.put("status", "warning");
            respuesta.put("msg", "Contraseña anterior incorrecta!");
            return respuesta; //contraseña vieja no matchea
        }
        
        
        userBd.setCambiarPassword(false);
        userBd.setPassword(passwordEncoder.encode(newPass));
        UsuarioServicio.guardar(userBd, UsuarioServicio.obtenerPorId(1).get() , true);
        respuesta.put("status", "success");
        respuesta.put("msg", "Contraseña fue guardada exitosamente! En breve lo redirigiremos.");
        return respuesta;
        
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/main/saveConf")
    public String GuardarConfiguracion(
            HttpServletRequest request, 
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
//----------------------------------------------------------------------------//
//------------------------- AUTH----------------------------------------------//
//----------------------------------------------------------------------------//
    @GetMapping("/auth/login")
    public String Login(
            HttpServletRequest request,
            @RequestParam(name = "invalidSession", required = false,defaultValue = "false") boolean invalidSession,
            @RequestParam(name = "logout", required = false,defaultValue = "false") boolean logout,
            Model model
    ) {
        
        if((SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken))
            return "redirect:/main/index";
        
        if(invalidSession)
            model.addAttribute("error_msg","Su sesión expiró. Ingrese sus credenciales nuevamente.");
        
        if(logout)
            model.addAttribute("success_msg","Sesión cerrada exitosamente!");
        
        return "login";
    }
//----------------------------------------------------------------------------//
    
    @GetMapping("/")
    public String redirectLogin(HttpServletRequest request,Model model
    ){
        return "redirect:/auth/login";
    }
//----------------------------------------------------------------------------//
}
