package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.general.PersonaServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import java.text.ParseException;
import java.util.Optional;
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
    
    private final DateUtils FechaUtils;
    
    private final DatoServ dtserv;
    
    private final AccesoServ AccesoServicio;
    

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
        Usuario usuarioLogueado=AccesoServicio.getUsuarioLogueado();
        
        String verificarPermisos= AccesoServicio.verificarPermisos(
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
                dtserv.consultarPorGrupo("Sexo") );
        model.addAttribute("sangre",
                dtserv.consultarPorGrupo("Tipos Sanguineos") );
        model.addAttribute("persona", personaBD);
        model.addAttribute("update",update);
        return "fragments/usr_mgr_registro :: info-dinamica-personal";
    }
//----------------------------------------------------------------------------//  
    
}
