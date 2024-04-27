/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.general.GrupoDatoServ;
import abreusapp.core.control.general.Persona;
import abreusapp.core.control.general.PersonaServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.utils.DateUtils;
import abreusapp.core.control.utils.ModelServ;
import java.text.ParseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class NominaCntr {
    
    private final PersonaServ PersonaServicio;
    
    private final DateUtils FechaUtils;
    
    private final DatoServ dtserv;
    
    private final GrupoDatoServ GrupoServicio;
    
    private final ModelServ ModeloServicio;
    

//----------------------------------------------------------------------------//
//------------------ENDPOINTS INFORMACION PERSONAL----------------------------//
//----------------------------------------------------------------------------//    
    @PostMapping("/infppl/save")
    @ResponseBody
    public int GuardarPersona(
        Model model, 
        Persona persona,
        @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {
        
        Usuario u=ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("usr_mgr_registro", model, u);
        if (! verificarPermisos.equals("")) return 0;
        
        if (dateInput != null && !dateInput.equals("")) {
            
            persona.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));
            
        }
        
        Optional<Persona> persona_existe = PersonaServicio.obtenerPorId(persona.getId()==null?0:persona.getId());
        
        boolean ext = false, ss = true;
        
        if (persona_existe.isPresent()) {
            
            ext = true;
            
            if (!FechaUtils.FechaFormato2.format(persona_existe.get().getFecha_actualizacion()).equals(dateInput)) {
                
                ss = false;
                
            } else {
                
                persona.setFecha_registro(persona_existe.get().getFecha_registro());
                persona.setHecho_por(persona_existe.get().getHecho_por());
            
            }
            
        }
        
        Persona d = null;
        if (ss) {
            
            d = PersonaServicio.guardar(persona, u, ext);
            
        }
        
        return d!=null?d.getId():0;

    }
//----------------------------------------------------------------------------//  
    
    @PostMapping("/infppl/vfyCedInUsr")
    @ResponseBody
    public boolean VerificarCedula(
        @RequestParam("cedula") String cedula
    ){
       Optional<Persona> p=PersonaServicio.obtenerPorCedula(cedula);
       return ! p.isPresent();
    }
//----------------------------------------------------------------------------//  
    
    
    @PostMapping("/infppl/getAllInfCed")
    public String ObtenerInfoPorCedula(
        Model model, 
        @RequestParam("cedula") String cedula,
        @RequestParam("update") boolean update
    ){
        Persona p=PersonaServicio.obtenerPorCedula(cedula).orElse(new Persona());
        p.setCedula(cedula);
        model.addAttribute("sexo",
                dtserv.consultarPorGrupo(
                GrupoServicio.obtener("Sexo").get() )
        );
        model.addAttribute("sangre",
                dtserv.consultarPorGrupo(
                GrupoServicio.obtener("Tipos Sanguineos").get() )
        );
        model.addAttribute("persona", p);
        model.addAttribute("update",update);
        return "fragments/usr_mgr_registro :: info-dinamica-personal";
    }
//----------------------------------------------------------------------------//  
    
}
