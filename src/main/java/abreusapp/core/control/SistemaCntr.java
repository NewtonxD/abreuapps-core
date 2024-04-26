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
            HttpServletRequest request,
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
            HttpServletRequest request,
            Model model, Dato dtgnr,
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
//-------------------------------------USUARIOS-------------------------------//
//----------------------------------------------------------------------------//

    @PostMapping(value="/usrmgr/save")
    public String GuardarUsuario(
            HttpServletRequest request, 
            Model model, 
            Usuario usuario,
            @RequestParam("idPersona") Integer idPersona,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {
        
        Usuario u= ModeloServicio.getUsuarioLogueado();
        
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;
        
        
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
            model.addAttribute(
                    "msg", 
                     ( dateInput!=null ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                        "No podemos realizar los cambios porque ya este Grupo se encuentra registrado."
                     )
            );
        }
        
        if(!map.isEmpty()) SSEControlador.publicar("usrmgr", map);

        ModeloServicio.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping("/usrmgr/update")
    public String ActualizarUsuario(
            HttpServletRequest request, 
            Model model, 
            String idUsuario
    ) {  
        
        Usuario u =ModeloServicio.getUsuarioLogueado();
        Optional<Usuario> us=UsuarioServicio.obtener(idUsuario); 

        if(!us.isPresent()){

            log.error("Error COD: 00537 al editar Usuario. Usuario no encontrado ("+idUsuario+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }
        
        model.addAttribute("user",us.get());
        model.addAttribute("persona",us.get().getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo",DatoServicio.consultarPorGrupo(GrupoServicio.obtener("Sexo").get() ));
        model.addAttribute("sangre",DatoServicio.consultarPorGrupo(GrupoServicio.obtener("Tipos Sanguineos").get() ));
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "usr_mgr_registro"));

        return "fragments/usr_mgr_registro :: content-default";  
    }
//----------------------------------------------------------------------------//
    
    @GetMapping("/usrmgr/myupdate")
    public String ActualizarMiUsuario(
            HttpServletRequest request, 
            Model model
    ) {  
        
        Usuario u =ModeloServicio.getUsuarioLogueado();
        Optional<Usuario> us=UsuarioServicio.obtener(u.getUsername()); 

        if(!us.isPresent()){

            log.error("Error COD: 00539 al editar mi Usuario. Mi Usuario no encontrado ("+u.getUsername()+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return "redirect:/error";

        }
        
        model.addAttribute("user",us.get());
        model.addAttribute("persona",us.get().getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo",DatoServicio.consultarPorGrupo(GrupoServicio.obtener("Sexo").get() ));
        model.addAttribute("sangre",DatoServicio.consultarPorGrupo(GrupoServicio.obtener("Tipos Sanguineos").get() ));
        model.addAttribute("usr_mgr_registro",true);
        model.addAttribute("configuracion",true);
        return "fragments/usr_mgr_registro :: content-default";  
    }
//----------------------------------------------------------------------------//

    
    @PostMapping("/usrmgr/vfyUsr")
    @ResponseBody
    public boolean VerificarUsuario(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("username") String usuario
    ){
       return ! UsuarioServicio.obtener(usuario).isPresent();
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping("/usrmgr/vfyMail")
    @ResponseBody
    public boolean VerificarCorreo(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("correo") String correo
    ){
        return ! UsuarioServicio.obtenerPorCorreo(correo).isPresent();
    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/usrmgr/vfyPwd")
    @ResponseBody
    public boolean VerificarPassword(
            HttpServletRequest request, 
            Model model, 
            @RequestParam("pwd") String password
    ){
       Usuario u=ModeloServicio.getUsuarioLogueado();
       return UsuarioServicio.coincidenContraseña(password,u.getId());
    }
//----------------------------------------------------------------------------//
    
    
    @PostMapping(value="/usrmgr/closeUsrSess")
    public String CerrarSesionUsuario(
            HttpServletRequest request, 
            Model model, 
            String usuario
    ) throws ParseException {
        
        Usuario u=ModeloServicio.getUsuarioLogueado();
        
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;
        
        UsuarioServicio.cerrarSesion(usuario);
        
        model.addAttribute("status", true);
        model.addAttribute("msg", "Sesión Cerrada Exitosamente!");

        ModeloServicio.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }
//----------------------------------------------------------------------------//        
    
    @PostMapping(value="/usrmgr/resetPwd")
    public String ResetearContraseña(
            HttpServletRequest request, 
            Model model, 
            String usuario
    ) throws ParseException {
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;
        
        Usuario us=UsuarioServicio.obtener(usuario).get();
        UsuarioServicio.cerrarSesion(usuario);
        UsuarioServicio.cambiarPassword(us,UsuarioServicio.generarPassword(),true);
 
        model.addAttribute("status", true);
        model.addAttribute("msg", "Contraseña Reseteada Exitosamente! Comuniquese con el usuario para que revise su correo.");

        ModeloServicio.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }
//----------------------------------------------------------------------------//   
    
    @PostMapping("/usrmgr/access")
    public String PermisosUsuario(
            HttpServletRequest request, 
            Model model, 
            String idUsuario
    ) {  
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;
        
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
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/usrmgr/get-access", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerListadoPermisosUsuario(
            HttpServletRequest request, 
            @RequestParam("idUsuario") String idUsuario
    ) {  
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", null, u);
        if (! verificarPermisos.equals("")) return null;
        
        Optional<Usuario> us=UsuarioServicio.obtener(idUsuario); 

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
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/usrmgr/save-acc")
    public String GuardarPermisosUsuario(
            HttpServletRequest request, 
            Model model,
            @RequestParam Map<String,String> data
    ) {
        
        Usuario u= ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;
        
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
        
        ModeloServicio.load("usr_mgr_principal", model, u.getId());
        
        return "fragments/usr_mgr_principal :: content-default";

    }
//----------------------------------------------------------------------------//
}
