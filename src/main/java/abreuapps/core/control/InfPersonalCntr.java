package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.general.PersonaServ;
import abreuapps.core.control.usuario.AccesoServ;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class InfPersonalCntr {
    
    private final PersonaServ PersonaServicio;
    
    private final DatoServ DatoServicio;
    
    private final AccesoServ AccesoServicio;
    

//----------------------------------------------------------------------------//
//------------------ENDPOINTS INFORMACION PERSONAL----------------------------//
//----------------------------------------------------------------------------//    
    @PostMapping(value = "/infppl/save")
    @ResponseBody
    public int GuardarPersona(
        Model model, 
        Persona persona,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {
        if(!AccesoServicio.verificarPermisos("usr_mgr_registro"))
            return 0;

        var personaGuardada = PersonaServicio.guardar(persona, fechaActualizacion);
        return personaGuardada.equals(null) ? 0 : personaGuardada.getId();

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
        model.addAttribute("sexo", DatoServicio.consultarPorGrupo("Sexo") );
        model.addAttribute("sangre",DatoServicio.consultarPorGrupo("Tipos Sanguineos") );
        model.addAttribute("persona", personaBD);
        model.addAttribute("update",update);
        return "shared/info_personal";
    }
//----------------------------------------------------------------------------//  
    
}
