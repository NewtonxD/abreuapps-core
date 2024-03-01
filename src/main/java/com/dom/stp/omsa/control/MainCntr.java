/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control;

//import com.dom.stp.omsa.control.domain.usuario.PersonaServ;
import com.dom.stp.omsa.control.domain.usuario.Usuario;
import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.domain.usuario.UsuarioServ;
import com.dom.stp.omsa.control.general.ModelServ;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 *
 * @author Newton
 */
@Controller
@RequestMapping("/main")
@Slf4j
@RequiredArgsConstructor
public class MainCntr {

    private final ModelServ DataModelServicio;

    private final AccesoServ AccesosServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    private final PasswordEncoder passwordEncoder;

    
    @RequestMapping({"/", "index"})
    public String MainPage(
        HttpServletRequest request,
        Model model
    ) {
        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!UsuarioServicio.obtener(
                u.getUsername()
            ).get().isCredentialsNonExpired()
        ) return "redirect:/main/changePwd";
        
        model.addAttribute("datos_personales",u.getPersona());
        model.addAllAttributes(AccesosServicio.consultarAccesosMenuUsuario(u.getId()));
        
        return "index";
    }

    @RequestMapping(value = "/content-page/", method = RequestMethod.POST)
    public String loadContetPage(
            HttpServletRequest request,
            Model model,
            @RequestParam("id") String idPage
    ) {

        Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DataModelServicio.load(idPage, model, u.getId());

        return "fragments/" + idPage + " :: content-default";
    }
    
    @RequestMapping("/changePwd")
    public String changePasswordExpired(
            HttpServletRequest request,
            Model model
    ) {
        Usuario userSession = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario userBd = UsuarioServicio.obtener(userSession.getUsername()).get();
        userBd.setContraseña("");
        model.addAttribute("usuario", userBd);
        return "password";
    }
    
    @PostMapping("/changeMyPwdNow")
    @ResponseBody
    public Map<String,String> changePasswordExpired(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "actualPassword",required = false) String oldPass,
            @RequestParam("newPassword") String newPass
    ) {
        oldPass = oldPass==null ? "" : oldPass ;
        Usuario userSession = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
            respuesta.put("msg", "La contraseña anterior que suministro se encuentra incorrecta!");
            return respuesta; //contraseña vieja no matchea
        }
        
        
        userBd.setCambiarPassword(false);
        userBd.setContraseña(passwordEncoder.encode(newPass));
        UsuarioServicio.guardar(userBd, 1, true);
        respuesta.put("status", "success");
        respuesta.put("msg", "Su contraseña fue reestablecida exitosamente! En breves lo redirigiremos.");
        return respuesta;
        
    }

}
