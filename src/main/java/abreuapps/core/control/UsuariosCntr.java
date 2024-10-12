package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.PersonaServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.UsuarioServ;
import abreuapps.core.control.utils.DateUtils;
import java.text.ParseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/usrmgr")
public class UsuariosCntr {

    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;

    private final DatoServ DatoServicio;
    
    private final PersonaServ PersonaServicio;
    
    private final UsuarioServ UsuarioServicio;
    

    
//----------------------------------------------------------------------------//
//-------------------------ENDPOINTS USUARIOS---------------------------------//
//----------------------------------------------------------------------------//

    @PostMapping(value="/save")
    public String GuardarUsuario(
        Model model, 
        Usuario usuario,
        @RequestParam("idPersona") Integer idPersona,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) throws ParseException {
        
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        boolean valido;
        
        Usuario usuarioLogueado= AccesoServicio.getUsuarioLogueado();
        
        String sinPermisoPlantilla= AccesoServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogueado );
        
        valido =  sinPermisoPlantilla.equals("");
            
        if(valido){
            
            
            if(usuario==null) {
                valido=false;
                model.addAttribute(
                        "msg",
                        "La información del usuario no puede ser guardada. Por favor, inténtalo otra vez. COD: 00537"     
                );
            }
            
            if(valido){
                
                Optional<Usuario> usuarioBD = UsuarioServicio.obtenerPorId(usuario.getId());

                if (usuarioBD.isPresent()) {

                    if (!FechaUtils.FechaFormato2.format(
                            usuarioBD.get().getFecha_actualizacion()
                            ).equals(fechaActualizacionCliente)
                    ) {

                        valido = false;
                        model.addAttribute(
                           "msg", 
                           ( ! ( fechaActualizacionCliente==null || 
                                   fechaActualizacionCliente.equals("") 
                               ) ? 
                              "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                              "No podemos realizar los cambios porque ya este Usuario se encuentra registrado."
                           )
                       );
                    }

                }else if(idPersona==0) {
                    valido=false;
                    model.addAttribute(
                           "msg",
                           "La información personal no pudo ser guardada. Por favor, inténtalo otra vez. COD: 00536"     
                    );
                }
                
                //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
                if (valido) {

                    if (! (fechaActualizacionCliente == null || 
                        fechaActualizacionCliente.equals("") )
                    ) usuario.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente )
                    );

                    if(idPersona!=0) usuario.setPersona(PersonaServicio.obtenerPorId(idPersona).get());


                    if(usuarioBD.isPresent()){
                        usuario.setFecha_registro(usuarioBD.get().getFecha_registro());
                        usuario.setHecho_por(usuarioBD.get().getHecho_por());
                        usuario.setPassword(usuarioBD.get().getPassword());
                        usuario.setPersona(usuarioBD.get().getPersona());
                    }

                    if(! usuario.getUsername().equals(usuarioLogueado.getUsername()) )
                        UsuarioServicio.cerrarSesion(usuario.getUsername());

                    UsuarioServicio.guardar(usuario, usuarioLogueado, usuarioBD.isPresent());
                    model.addAttribute("msg", "Registro guardado exitosamente!");

                } 
                
            }
            
            model.addAttribute("status", valido);
            
        }
        
        if( sinPermisoPlantilla.equals("") )
            AccesoServicio.cargarPagina("usr_mgr_principal", model, usuarioLogueado.getId());
        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping("/update")
    public String ActualizarUsuario( 
        Model model, 
        String idUsuario
    ) {  
        
        String plantillaRespuesta="fragments/usr_mgr_registro :: content-default";
        boolean valido=true;
        
        Usuario usuarioLogueado =AccesoServicio.getUsuarioLogueado();
        Optional<Usuario> usuarioBD=UsuarioServicio.obtener(idUsuario); 

        if(!usuarioBD.isPresent()){
            //log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ({})",idUsuario);
            plantillaRespuesta= "redirect:/error";
            valido=false;

        }
        
        //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
        if(valido){
            model.addAttribute("user",usuarioBD.get());
            model.addAttribute("persona",usuarioBD.get().getPersona());
            model.addAttribute("update", true);
            model.addAttribute("sexo",DatoServicio.consultarPorGrupo("Sexo") );
            model.addAttribute("sangre",DatoServicio.consultarPorGrupo("Tipos Sanguineos") );
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                            usuarioLogueado.getId(), 
                            "usr_mgr_registro"
                    )
            );
        }

        return plantillaRespuesta;  
    }
//----------------------------------------------------------------------------//
    
    @GetMapping("/myupdate")
    public String ActualizarMiUsuario(
        Model model
    ) {  
        boolean valido=true;
        String plantillaRespuesta="fragments/usr_mgr_registro :: content-default";
        
        Usuario usuarioLogueado =AccesoServicio.getUsuarioLogueado();
        Optional<Usuario> usuarioBD=UsuarioServicio.obtener(usuarioLogueado.getUsername()); 
 
        if(! usuarioBD.isPresent() ){
            //log.error("Error COD: 00539 al editar mi Usuario. Mi Usuario no encontrado ({})",usuarioLogueado.getUsername());
            plantillaRespuesta= "redirect:/error";
            valido=false;
        }
        
        //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
        if(valido){
            model.addAttribute("user",usuarioBD.get());
            model.addAttribute("persona",usuarioBD.get().getPersona());
            model.addAttribute("update", true);
            model.addAttribute("sexo",DatoServicio.consultarPorGrupo("Sexo") );
            model.addAttribute("sangre",DatoServicio.consultarPorGrupo("Tipos Sanguineos") );
            model.addAttribute("usr_mgr_registro",true);
            model.addAttribute("configuracion",true);
        }
        
        return plantillaRespuesta;  
    }
//----------------------------------------------------------------------------//

    
    @PostMapping("/vfyUsr")
    @ResponseBody
    public boolean VerificarUsuario(
        @RequestParam("username") String nombreUsuario
    ){
       return ! UsuarioServicio.obtener(nombreUsuario).isPresent();
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping("/vfyMail")
    @ResponseBody
    public boolean VerificarCorreo(
        @RequestParam("correo") String correo
    ){
        return ! UsuarioServicio.obtenerPorCorreo(correo).isPresent();
    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/vfyPwd")
    @ResponseBody
    public boolean VerificarPassword(
        @RequestParam("pwd") String password
    ){
       Usuario usuarioLogeado=AccesoServicio.getUsuarioLogueado();
       return UsuarioServicio.coincidenPassword(password,usuarioLogeado.getId());
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping(value="/closeUsrSess")
    public String CerrarSesionUsuario(
        Model model, 
        String nombreUsuario
    ) throws ParseException {
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        
        Usuario usuarioLogeado=AccesoServicio.getUsuarioLogueado();
        
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= AccesoServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
        if(valido){
            
            UsuarioServicio.cerrarSesion(nombreUsuario);

            model.addAttribute("status", valido);
            model.addAttribute("msg", "Sesión Cerrada Exitosamente!");

            AccesoServicio.cargarPagina("usr_mgr_principal", model, usuarioLogeado.getId());
            
        }
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }
//----------------------------------------------------------------------------//        
    
    @PostMapping(value="/resetPwd")
    public String ResetearContraseña(
        Model model, 
        String nombreUsuario
    ) throws ParseException {
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        
        Usuario usuarioLogeado = AccesoServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= AccesoServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            
            Optional<Usuario> usuarioBD=UsuarioServicio.obtener(nombreUsuario); 

            if( ! usuarioBD.isPresent() ){
                //log.error("Error COD: 00537 al resetear Password. Usuario no encontrado ({})",nombreUsuario);
                plantillaRespuesta = "redirect:/error";
                valido=false;
            }
            
            //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
            if(valido){
                UsuarioServicio.cerrarSesion(nombreUsuario);
                UsuarioServicio.cambiarPassword(usuarioBD.get(),UsuarioServicio.generarPassword(),true);

                model.addAttribute("status", valido);
                model.addAttribute(
                        "msg", 
                        "Contraseña Reseteada Exitosamente! Comuniquese con el usuario para que revise su correo."
                );

                AccesoServicio.cargarPagina("usr_mgr_principal", model, usuarioLogeado.getId());
            }
        }
        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }

}
