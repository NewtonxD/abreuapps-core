/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.general.GrupoDatoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.utils.ModelServ;
import abreusapp.core.control.general.PersonaServ;
import abreusapp.core.control.usuario.UsuarioServ;
import abreusapp.core.control.utils.DateUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author cabreu
 */
@Controller
@RequestMapping("/usrmgr")
@Slf4j
@RequiredArgsConstructor
public class UsuariosCntr {

    private final DateUtils FechaUtils;
    
    private final DatoServ dtserv;
    
    private final GrupoDatoServ GrupoServicio;
    
    private final AccesoServ AccesoServicio;
    
    private final ModelServ dmService;
    
    private final PersonaServ PersonaServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    private final SSECntr seeCnt;

    @PostMapping(value="/save")
    public String GuardarUsuario(
            HttpServletRequest request, 
            Model model, 
            Usuario usuario,
            @RequestParam("idPersona") Integer idPersona,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/usr_mgr_principal :: content-default";
        }
        
        
        HashMap<String, Object> map = new HashMap<>();
        
        if (dateInput != null && !dateInput.equals("")) {
            
            usuario.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));
            
        }
        
        Optional<Usuario> usuario_existe = UsuarioServicio.obtenerPorId(usuario.getId()==null?0:usuario.getId());
        
        boolean ext = false, ss = true;
        
        if (usuario_existe.isPresent()) {
            
            ext = true;
            
            if (!FechaUtils.FechaFormato2.format(usuario_existe.get().getFecha_actualizacion()).equals(dateInput)) {
                
                ss = false;
                
            } else {
                
                usuario.setFecha_registro(usuario_existe.get().getFecha_registro());
                usuario.setHecho_por(usuario_existe.get().getHecho_por());
                usuario.setPassword(usuario_existe.get().getPassword());
                usuario.setPersona(usuario_existe.get().getPersona());
            }
            
        }else{
            
            if(idPersona==0)
                ss=false;
            else
                usuario.setPersona(PersonaServicio.obtenerPorId(idPersona).get());
            
            
        }

        if (ss) {
            if(! usuario.getUsername().equals(u.getUsername()) )
                UsuarioServicio.cerrarSesion(usuario.getUsername());
            
            Usuario d = UsuarioServicio.guardar(usuario, u, ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));
            
        } else {
            
            model.addAttribute("status", false);
            model.addAttribute("msg", "Al parecer alguien hubo un inconveniente con la transacción. Por favor, inténtalo otra vez. COD: 00535");
            
        }
        
        if(!map.isEmpty()) seeCnt.publicar("usrmgr", map);

        dmService.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }
    
    
    @PostMapping("/update")
    public String ActualizarUsuario(
            HttpServletRequest request, 
            Model model, 
            String idUsuario
    ) {  
        
        Usuario u =(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Usuario> us=UsuarioServicio.obtener(idUsuario); 

        if(!us.isPresent()){

            log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ("+idUsuario+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }
        
        model.addAttribute("user",us.get());
        model.addAttribute("persona",us.get().getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo",dtserv.consultarPorGrupo(GrupoServicio.obtener("Sexo").get() ));
        model.addAttribute("sangre",dtserv.consultarPorGrupo(GrupoServicio.obtener("Tipos Sanguineos").get() ));
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "usr_mgr_registro"));

        return "fragments/usr_mgr_registro :: content-default";  
    }
    
    @GetMapping("/myupdate")
    public String ActualizarMiUsuario(
            HttpServletRequest request, 
            Model model
    ) {  
        
        Usuario u =(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Usuario> us=UsuarioServicio.obtener(u.getUsername()); 

        if(!us.isPresent()){

            log.error("Error COD: 00539 al editar mi Usuario. Mi Usuario no encontrado ("+u.getUsername()+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }
        
        model.addAttribute("user",us.get());
        model.addAttribute("persona",us.get().getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo",dtserv.consultarPorGrupo(GrupoServicio.obtener("Sexo").get() ));
        model.addAttribute("sangre",dtserv.consultarPorGrupo(GrupoServicio.obtener("Tipos Sanguineos").get() ));
        model.addAttribute("usr_mgr_registro",true);
        model.addAttribute("configuracion",true);
        return "fragments/usr_mgr_registro :: content-default";  
    }

    
    @PostMapping("/vfyUsr")
    @ResponseBody
    public boolean VerificarUsuario(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("username") String usuario
    ){
       return ! UsuarioServicio.obtener(usuario).isPresent();
    }
    
    
    @PostMapping("/vfyMail")
    @ResponseBody
    public boolean VerificarCorreo(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("correo") String correo
    ){
        return ! UsuarioServicio.obtenerPorCorreo(correo).isPresent();
    }
    
    @PostMapping("/vfyPwd")
    @ResponseBody
    public boolean VerificarPassword(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("pwd") String password
    ){
       Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return UsuarioServicio.coincidenContraseña(password,u.getId());
    }
    
    
    @PostMapping(value="/closeUsrSess")
    public String CerrarSesionUsuario(
            HttpServletRequest request, 
            Model model, 
            String usuario
    ) throws ParseException {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/usr_mgr_principal :: content-default";
        }
        
        UsuarioServicio.cerrarSesion(usuario);
        
        model.addAttribute("status", true);
        model.addAttribute("msg", "Sesión Cerrada Exitosamente!");

        dmService.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }        
    
    @PostMapping(value="/resetPwd")
    public String ResetearContraseña(
            HttpServletRequest request, 
            Model model, 
            String usuario
    ) throws ParseException {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/usr_mgr_principal :: content-default";
        }
        
        Usuario us=UsuarioServicio.obtener(usuario).get();
        UsuarioServicio.cerrarSesion(usuario);
        UsuarioServicio.cambiarPassword(us,UsuarioServicio.generarPassword(),true);
 
        model.addAttribute("status", true);
        model.addAttribute("msg", "Contraseña Reseteada Exitosamente! Comuniquese con el usuario para que revise su correo.");

        dmService.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }   
    
    @PostMapping("/access")
    public String PermisosUsuario(
            HttpServletRequest request, 
            Model model, 
            String idUsuario
    ) {  
        
        Usuario u =(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/usr_mgr_principal :: content-default";
        }
        
        Optional<Usuario> us=UsuarioServicio.obtener(idUsuario); 

        if(!us.isPresent()){

            log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ("+idUsuario+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }
        
        model.addAttribute("usuario",us.get());
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "usr_mgr_registro"));
        return "fragments/usr_mgr_permisos :: content-default";  
    }
    
    @PostMapping(value="/get-access", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerListadoPermisosUsuario(
            HttpServletRequest request, 
            @RequestBody String idUsuario
    ) {  
        
        Usuario u =(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            return null;
        }
        
        Optional<Usuario> us=UsuarioServicio.obtener(idUsuario.replace("idUsuario=", "")); 

        if(!us.isPresent()){

            log.error("Error COD: 00539 al editar Usuario. Usuario no encontrado ("+idUsuario+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return null;

        }
        
        return new ResponseEntity<>(
                AccesoServicio.ListadoAccesosUsuarioEditar(us.get().getId()),
                new HttpHeaders(),
                HttpStatus.OK);  
    }
    
    @PostMapping(value="/save-acc")
    public String GuardarPermisosUsuario(
            HttpServletRequest request, 
            Model model,
            @RequestParam Map<String,String> data
    ) {
        
        Usuario u=(Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(u.getId(),"usr_mgr_registro");
 
        if(m.get("usr_mgr_registro")==null || (! (Boolean)m.get("usr_mgr_registro"))
        ){
            model.addAttribute("status", false);
            model.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            return "fragments/usr_mgr_principal :: content-default";
        }
        
        Optional<Usuario> usuario=UsuarioServicio.obtener(data.get("idUsuario"));
        if(usuario.isPresent()){
            UsuarioServicio.cerrarSesion(usuario.get().getUsername());
            AccesoServicio.GuardarTodosMap(data, usuario.get());
            model.addAttribute("status", true);
            model.addAttribute("msg", "Permisos guardados exitosamente!");
           
        } else {
            
            model.addAttribute("status", false);
            model.addAttribute("msg", "Al parecer alguien hubo un inconveniente con la transacción. Por favor, inténtalo otra vez. COD: 00545");
            
        }
        
        dmService.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }
    
}
