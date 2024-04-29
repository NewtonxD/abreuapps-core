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
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) throws ParseException {
        
        boolean valido;
        int idPersona=0;
        Usuario usuarioLogueado=ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos(
                "usr_mgr_registro", model, usuarioLogueado );
        
        valido = verificarPermisos.equals("");
        
        
        if(valido){
            
            Optional<Persona> personaBD = PersonaServicio.obtenerPorId(persona==null? 0 : persona.getId() );

            if (personaBD.isPresent() && 
                    ! FechaUtils.FechaFormato2.format(
                            personaBD.get().getFecha_actualizacion() )
                    .equals(fechaActualizacionCliente)
            ) valido = false;

            if (valido) {
                if (!(fechaActualizacionCliente == null || fechaActualizacionCliente.equals(""))) 
                    persona.setFecha_actualizacion(FechaUtils.Formato2ToDate(fechaActualizacionCliente));

                if(personaBD.isPresent()){
                    persona.setFecha_registro(personaBD.get().getFecha_registro());
                    persona.setHecho_por(personaBD.get().getHecho_por());
                }

                Persona d = PersonaServicio.guardar(persona, usuarioLogueado, personaBD.isPresent());
                idPersona=d.getId();
            }
            
        }
        
        return idPersona;

    }
//----------------------------------------------------------------------------//  
    
    @PostMapping("/infppl/vfyCedInUsr")
    @ResponseBody
    public boolean VerificarCedula(
        @RequestParam("cedula") String cedula
    ){
       return ! PersonaServicio.obtenerPorCedula(cedula).isPresent();
    }
//----------------------------------------------------------------------------//  
    
    
    @PostMapping("/infppl/getAllInfCed")
    public String ObtenerInfoPorCedula(
        Model model, 
        @RequestParam("cedula") String cedula,
        @RequestParam("update") boolean update
    ){
        Persona personaBD=PersonaServicio.obtenerPorCedula(cedula).orElse(new Persona());
        personaBD.setCedula(cedula);
        model.addAttribute("sexo",
                dtserv.consultarPorGrupo(
                GrupoServicio.obtener("Sexo").get() )
        );
        model.addAttribute("sangre",
                dtserv.consultarPorGrupo(
                GrupoServicio.obtener("Tipos Sanguineos").get() )
        );
        model.addAttribute("persona", personaBD);
        model.addAttribute("update",update);
        return "fragments/usr_mgr_registro :: info-dinamica-personal";
    }
//----------------------------------------------------------------------------//  
    
}
