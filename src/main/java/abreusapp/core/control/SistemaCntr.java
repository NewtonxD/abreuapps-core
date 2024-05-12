/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.Dato;
import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.general.GrupoDato;
import abreusapp.core.control.general.GrupoDatoServ;
import abreusapp.core.control.general.PersonaServ;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.usuario.UsuarioServ;
import abreusapp.core.control.utils.DateUtils;
import abreusapp.core.control.utils.ModelServ;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author cabreu
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class SistemaCntr {

    private final DateUtils FechaUtils;

    private final GrupoDatoServ GrupoServicio;

    private final AccesoServ AccesoServicio;

    private final ModelServ ModeloServicio;

    private final DatoServ DatoServicio;
    
    private final PersonaServ PersonaServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    
//----------------------------------------------------------------------------//
//-------------------ENDPOINTS GRUPO DE DATOS---------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/dtgrp/save")
    public String GuardarGrupoDato(
        Model model,
        GrupoDato grpdt,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        String plantillaRespuesta = "fragments/dat_gen_consulta_grupos :: content-default";
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();
        
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "dat_gen_registro_grupos", model, usuarioLogueado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){

            Optional<GrupoDato> grupoBD = GrupoServicio.obtener(grpdt.getGrupo());
            
            if (grupoBD.isPresent() &&
                !FechaUtils.FechaFormato2.format(
                grupoBD.get().getFecha_actualizacion() )
                .equals(fechaActualizacionCliente)
            ) {
                valido = false;
                model.addAttribute(
                        "msg", 
                         ( !(fechaActualizacionCliente == null || 
                            fechaActualizacionCliente.equals("") )  ? 
                            "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                            "No podemos realizar los cambios porque ya este Grupo se encuentra registrado."
                         )
                );
            }

            if (valido) {

                if ( !(fechaActualizacionCliente == null || 
                    fechaActualizacionCliente.equals("") ) 
                ) grpdt.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente) );

                if(grupoBD.isPresent()){
                    grpdt.setFecha_registro(grupoBD.get().getFecha_registro());
                    grpdt.setHecho_por(grupoBD.get().getHecho_por());
                }

                GrupoServicio.guardar(grpdt, usuarioLogueado, grupoBD.isPresent());
                model.addAttribute("msg", "Registro guardado exitosamente!");
                
            } 
            
            model.addAttribute("status", valido);
        }
        
        if(sinPermisoPlantilla.equals(""))
            ModeloServicio.load(
                    "dat_gen_consulta_grupos", model, usuarioLogueado.getId() );

        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/dtgrp/update")
    public String ActualizarGrupo(
        Model model,
        String idGrupo
    ) {

        boolean valido=true;
        String plantillaRespuesta = "fragments/dat_gen_registro_grupos :: content-default";
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<GrupoDato> grupoDB = GrupoServicio.obtener(idGrupo);

        if (! grupoDB.isPresent()) {
            
            log.error("Error COD: 00537 al editar grupos de datos, ({}) no existe.",idGrupo);
            plantillaRespuesta = "redirect:/error";
            valido = false;
            
        }
        
        if(valido){
            
            model.addAttribute("grupo", grupoDB.get());
            model.addAttribute("update", true);
            model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(
                    usuarioLogueado.getId(), "dat_gen_registro_grupos") );
            
        }
        
        return plantillaRespuesta;
    }

//----------------------------------------------------------------------------//
//---------------------------ENDPOINTS DATOS----------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/dtgnr/save")
    public String GuardarDatoGeneral(
        Model model, 
        Dato dtgnr,
        @RequestParam(value = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        String plantillaRespuesta = "fragments/dat_gen_consulta_datos :: content-default";
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();
        
        String sinPermisoPlantilla = ModeloServicio.verificarPermisos(
                "dat_gen_registro_datos", model, usuarioLogueado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            
            if(dtgnr==null){
                model.addAttribute(
                        "msg",
                        "La información del dato no puede ser guardada. Por favor, inténtalo otra vez. COD: 00562");
                valido=false;
            }
            
            if(valido){
                
                Optional<Dato> datoBD = DatoServicio.obtener(dtgnr.getDato());
                
                if ( datoBD.isPresent() && 
                        ! FechaUtils.FechaFormato2.format(datoBD.get().getFecha_actualizacion())
                        .equals(fechaActualizacionCliente) 
                ) {
                    valido = false;
                    model.addAttribute(
                            "msg", 
                             ( !(fechaActualizacionCliente==null || fechaActualizacionCliente.equals("")) ? 
                                "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                                "No podemos realizar los cambios porque ya este Dato se encuentra registrado."
                             )
                    );
                }
                
                //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
                if (valido) {

                    if (! (fechaActualizacionCliente == null || 
                            fechaActualizacionCliente.equals("") )
                    ) dtgnr.setFecha_actualizacion(
                            FechaUtils.Formato2ToDate(fechaActualizacionCliente)
                    );

                    if(datoBD.isPresent()){
                        dtgnr.setFecha_registro(datoBD.get().getFecha_registro());
                        dtgnr.setHecho_por(datoBD.get().getHecho_por());
                    }
                    
                    Dato datoNuevo = DatoServicio.guardar(dtgnr, usuarioLogueado, datoBD.isPresent());
                    model.addAttribute("msg", "Registro guardado exitosamente!");
                    
                } 
                
            }
            
            model.addAttribute("status", valido);
            
        }
        
        if(sinPermisoPlantilla.equals(""))
            ModeloServicio.load(
                    "dat_gen_consulta_datos", model, usuarioLogueado.getId() );

        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;
    }
//----------------------------------------------------------------------------//

    @PostMapping("/dtgnr/update")
    public String ActualizarDatosGenerales(
        Model model,
        String idDato
    ) {

        String plantillaRespuesta="fragments/dat_gen_registro_datos :: content-default";
        boolean valido = true;
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Dato> datoDB = DatoServicio.obtener(idDato);

        if (! datoDB.isPresent()) {

            log.error("Error COD: 00535 al editar datos generales, ({}) no existe.",idDato);
            valido=false;
            plantillaRespuesta="redirect:/error";

        }
        
        if(valido){
            model.addAttribute("dato", datoDB.get());
            model.addAttribute("update", true);
            model.addAttribute("grupos", GrupoServicio.consultar());
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                    usuarioLogueado.getId(), "dat_gen_registro_datos" ) 
            );
        }

        return plantillaRespuesta;

    }
    
//----------------------------------------------------------------------------//
//-------------------------ENDPOINTS USUARIOS---------------------------------//
//----------------------------------------------------------------------------//

    @PostMapping(value="/usrmgr/save")
    public String GuardarUsuario(
        Model model, 
        Usuario usuario,
        @RequestParam("idPersona") Integer idPersona,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) throws ParseException {
        
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        boolean valido;
        
        Usuario usuarioLogueado= ModeloServicio.getUsuarioLogueado();
        
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
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
            ModeloServicio.load("usr_mgr_principal", model, usuarioLogueado.getId());
        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping("/usrmgr/update")
    public String ActualizarUsuario( 
        Model model, 
        String idUsuario
    ) {  
        
        String plantillaRespuesta="fragments/usr_mgr_registro :: content-default";
        boolean valido=true;
        
        Usuario usuarioLogueado =ModeloServicio.getUsuarioLogueado();
        Optional<Usuario> usuarioBD=UsuarioServicio.obtener(idUsuario); 

        if(!usuarioBD.isPresent()){
            log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ({})",idUsuario);
            plantillaRespuesta= "redirect:/error";
            valido=false;

        }
        
        //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
        if(valido){
            model.addAttribute("user",usuarioBD.get());
            model.addAttribute("persona",usuarioBD.get().getPersona());
            model.addAttribute("update", true);
            model.addAttribute("sexo",DatoServicio.consultarPorGrupo(
                    GrupoServicio.obtener("Sexo").get() )
            );
            model.addAttribute("sangre",DatoServicio.consultarPorGrupo(
                    GrupoServicio.obtener("Tipos Sanguineos").get() )
            );
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
    
    @GetMapping("/usrmgr/myupdate")
    public String ActualizarMiUsuario(
        Model model
    ) {  
        boolean valido=true;
        String plantillaRespuesta="fragments/usr_mgr_registro :: content-default";
        
        Usuario usuarioLogueado =ModeloServicio.getUsuarioLogueado();
        Optional<Usuario> usuarioBD=UsuarioServicio.obtener(usuarioLogueado.getUsername()); 
 
        if(! usuarioBD.isPresent() ){
            log.error("Error COD: 00539 al editar mi Usuario. Mi Usuario no encontrado ({})",usuarioLogueado.getUsername());
            plantillaRespuesta= "redirect:/error";
            valido=false;
        }
        
        //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
        if(valido){
            model.addAttribute("user",usuarioBD.get());
            model.addAttribute("persona",usuarioBD.get().getPersona());
            model.addAttribute("update", true);
            model.addAttribute("sexo",DatoServicio.consultarPorGrupo(
                    GrupoServicio.obtener("Sexo").get() ) 
            );
            model.addAttribute("sangre",DatoServicio.consultarPorGrupo(
                    GrupoServicio.obtener("Tipos Sanguineos").get() )
            );
            model.addAttribute("usr_mgr_registro",true);
            model.addAttribute("configuracion",true);
        }
        
        return plantillaRespuesta;  
    }
//----------------------------------------------------------------------------//

    
    @PostMapping("/usrmgr/vfyUsr")
    @ResponseBody
    public boolean VerificarUsuario(
        @RequestParam("username") String nombreUsuario
    ){
       return ! UsuarioServicio.obtener(nombreUsuario).isPresent();
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping("/usrmgr/vfyMail")
    @ResponseBody
    public boolean VerificarCorreo(
        @RequestParam("correo") String correo
    ){
        return ! UsuarioServicio.obtenerPorCorreo(correo).isPresent();
    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/usrmgr/vfyPwd")
    @ResponseBody
    public boolean VerificarPassword(
        @RequestParam("pwd") String password
    ){
       Usuario usuarioLogeado=ModeloServicio.getUsuarioLogueado();
       return UsuarioServicio.coincidenContraseña(password,usuarioLogeado.getId());
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping(value="/usrmgr/closeUsrSess")
    public String CerrarSesionUsuario(
        Model model, 
        String nombreUsuario
    ) throws ParseException {
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        
        Usuario usuarioLogeado=ModeloServicio.getUsuarioLogueado();
        
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
        if(valido){
            
            UsuarioServicio.cerrarSesion(nombreUsuario);

            model.addAttribute("status", valido);
            model.addAttribute("msg", "Sesión Cerrada Exitosamente!");

            ModeloServicio.load("usr_mgr_principal", model, usuarioLogeado.getId());
            
        }
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }
//----------------------------------------------------------------------------//        
    
    @PostMapping(value="/usrmgr/resetPwd")
    public String ResetearContraseña(
        Model model, 
        String nombreUsuario
    ) throws ParseException {
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        
        Usuario usuarioLogeado = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            
            Optional<Usuario> usuarioBD=UsuarioServicio.obtener(nombreUsuario); 

            if( ! usuarioBD.isPresent() ){
                log.error("Error COD: 00537 al resetear Password. Usuario no encontrado ({})",nombreUsuario);
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

                ModeloServicio.load("usr_mgr_principal", model, usuarioLogeado.getId());
            }
        }
        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }
//----------------------------------------------------------------------------//   
    
    @PostMapping("/usrmgr/access")
    public String PermisosUsuario(
        Model model, 
        String idUsuario
    ) {  
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_permisos :: content-default";
        
        Usuario usuarioLogeado = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            
            Optional<Usuario> usuarioBD=UsuarioServicio.obtener(idUsuario); 

            if( ! usuarioBD.isPresent() ){
                log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ({})",idUsuario);
                plantillaRespuesta = "redirect:/error";
                valido=false;
            }
            
            
            //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
            if(valido) model.addAttribute("usuario",usuarioBD.get());
            
        }
        
        model.addAllAttributes(
                AccesoServicio.consultarAccesosPantallaUsuario(
                        usuarioLogeado.getId(), "usr_mgr_registro" )
        );
        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;  
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/usrmgr/get-access", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerListadoPermisosUsuario(
        @RequestParam("idUsuario") String nombreUsuario
    ) {  
        boolean valido;
        
        Usuario usuarioLogeado = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", null, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        
        List<Object[]> permisosUsuario = null;
        
        if(valido){
            Optional<Usuario> usuarioBD=UsuarioServicio.obtener(nombreUsuario); 

            if(!usuarioBD.isPresent()){
                log.error("Error COD: 00539 al editar Usuario. Usuario no encontrado ({})",nombreUsuario);
                valido=false;
            }
        
            
            //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
            if(valido) permisosUsuario = AccesoServicio.ListadoAccesosUsuarioEditar(
                    usuarioBD.get().getId() );
            
        }
        
        return new ResponseEntity<>(
            permisosUsuario,
            new HttpHeaders(),
            valido ? HttpStatus.OK: HttpStatus.NOT_FOUND
        );  
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/usrmgr/save-acc")
    public String GuardarPermisosUsuario(
        Model model,
        @RequestParam Map<String,String> data
    ) {
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_principal :: content-default";
        
        Usuario usuarioLogeado= ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            Optional<Usuario> usuarioBD=UsuarioServicio.obtener(data.getOrDefault("idUsuario",""));

            if(!usuarioBD.isPresent()){
                valido=false;
                model.addAttribute("msg", 
                        "No pudimos encontrar al usuario. Por favor, inténtalo otra vez. COD: 00545"
                );
            }
            
            //PROCEDEMOS SI TODOS LOS DATOS SON VALIDOS
            if(valido){
                UsuarioServicio.cerrarSesion(usuarioBD.get().getUsername());
                AccesoServicio.GuardarTodosMap(data, usuarioBD.get());
                model.addAttribute("msg", "Permisos guardados exitosamente!");

                ModeloServicio.load("usr_mgr_principal", model, usuarioLogeado.getId());

            }
            
            model.addAttribute("status", valido);
        }
        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;
    }
//----------------------------------------------------------------------------//
}
