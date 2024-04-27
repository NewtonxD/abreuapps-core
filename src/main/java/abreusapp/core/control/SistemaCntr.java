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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
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

    private final SSECntr SSEControlador;
    
    private final PersonaServ PersonaServicio;
    
    private final UsuarioServ UsuarioServicio;
    
    
//----------------------------------------------------------------------------//
//----------------------------GRUPO DE DATOS----------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/dtgrp/save")
    public String GuardarGrupoDato(
        Model model,
        GrupoDato grpdt,
        @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {

        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("dat_gen_registro_grupos", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;

        HashMap<String, Object> map = new HashMap<>();

        if (dateInput != null && !dateInput.equals("")) {

            grpdt.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));

        }

        Optional<GrupoDato> grupo = GrupoServicio.obtener(grpdt.getGrupo());

        boolean ext = false, ss = true;

        if (grupo.isPresent()) {

            ext = true;

            if (!FechaUtils.FechaFormato2.format(grupo.get().getFecha_actualizacion()).equals(dateInput)) {

                ss = false;

            } else {

                grpdt.setFecha_registro(grupo.get().getFecha_registro());
                grpdt.setHecho_por(grupo.get().getHecho_por());

            }

        }

        if (ss) {

            GrupoDato d = GrupoServicio.guardar(grpdt, u, ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));

        } else {

            model.addAttribute("status", false);
            model.addAttribute(
                    "msg", 
                     ( dateInput!=null ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                        "No podemos realizar los cambios porque ya este Grupo se encuentra registrado."
                     )
            );

        }
        
        ModeloServicio.load("dat_gen_consulta_grupos", model, u.getId());

        if (!map.isEmpty()) {
            SSEControlador.publicar("dtgrp", map);
        }

        return "fragments/dat_gen_consulta_grupos :: content-default";

    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/dtgrp/update")
    public String ActualizarGrupo(
        HttpServletRequest request,
        Model model,
        String idGrupo
    ) {

        Usuario u = ModeloServicio.getUsuarioLogueado();

        Optional<GrupoDato> g = GrupoServicio.obtener(idGrupo);

        if (!g.isPresent()) {

            log.error("Error COD: 00537 al editar grupos de datos.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());

            return "redirect:/error";

        }

        model.addAttribute("grupo", g.get());
        model.addAttribute("update", true);
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "dat_gen_registro_grupos"));

        return "fragments/dat_gen_registro_grupos :: content-default";
    }

//----------------------------------------------------------------------------//
//-------------------------------------DATOS----------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/dtgnr/save")
    public String GuardarDatoGeneral(
        Model model, 
        Dato dtgnr,
        @RequestParam(value = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {

        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("dat_gen_registro_datos", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;

        HashMap<String, Object> map = new HashMap<>();

        if (dateInput != null && !dateInput.equals("")) {

            dtgnr.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));

        }

        Optional<Dato> dato = DatoServicio.obtener(dtgnr.getDato());
        boolean ext = false, ss = true;

        if (dato.isPresent()) {

            ext = true;

            if (!FechaUtils.FechaFormato2.format(dato.get().getFecha_actualizacion()).equals(dateInput)) {

                ss = false;

            } else {

                dtgnr.setFecha_registro(dato.get().getFecha_registro());
                dtgnr.setHecho_por(dato.get().getHecho_por());

            }

        }

        if (ss) {

            Dato d = DatoServicio.guardar(dtgnr, u, ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));

        } else {

            model.addAttribute("status", false);
            model.addAttribute(
                    "msg", 
                     ( dateInput!=null ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                        "No podemos realizar los cambios porque ya este Dato se encuentra registrado."
                     )
            );
        }
        
        ModeloServicio.load("dat_gen_consulta_datos", model, u.getId());

        if (!map.isEmpty()) {
            SSEControlador.publicar("dtgnr", map);
        }

        return "fragments/dat_gen_consulta_datos :: content-default";

    }
//----------------------------------------------------------------------------//

    @PostMapping("/dtgnr/update")
    public String ActualizarDatosGenerales(
        HttpServletRequest request,
        Model model,
        String idDato
    ) {

        Usuario u = ModeloServicio.getUsuarioLogueado();

        Optional<Dato> d = DatoServicio.obtener(idDato);

        if (!d.isPresent()) {

            log.error("Error COD: 00535 al editar datos generales.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            return "redirect:/error";

        }

        model.addAttribute("dato", d.get());
        model.addAttribute("update", true);
        model.addAttribute("grupos", GrupoServicio.consultar());
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "dat_gen_registro_datos"));

        return "fragments/dat_gen_registro_datos :: content-default";

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
        
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos("usr_mgr_registro", model, usuarioLogueado);
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

                    Usuario d = UsuarioServicio.guardar(usuario, usuarioLogueado, usuarioBD.isPresent());
                    model.addAttribute("msg", "Registro guardado exitosamente!");

                    HashMap<String, Object> map = new HashMap<>();
                    map.put(usuarioBD.isPresent() ? "U" : "I", d);
                    map.put("date", FechaUtils.FechaFormato1.format(new Date()));
                    SSEControlador.publicar("usrmgr", map);

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
        
        valido = ! sinPermisoPlantilla.equals("");
        
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
        
        valido = ! sinPermisoPlantilla.equals("");
        
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
        String nombreUsuario
    ) {  
        
        boolean valido;
        String plantillaRespuesta="fragments/usr_mgr_permisos :: content-default";
        
        Usuario usuarioLogeado = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogeado );
        
        valido = ! sinPermisoPlantilla.equals("");
        
        if(valido){
            
            Optional<Usuario> usuarioBD=UsuarioServicio.obtener(nombreUsuario); 

            if( ! usuarioBD.isPresent() ){
                log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ({})",nombreUsuario);
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
        
        valido = ! sinPermisoPlantilla.equals("");
        
        
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
        
        valido = ! sinPermisoPlantilla.equals("");
        
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
