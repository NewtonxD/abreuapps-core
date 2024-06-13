/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.Dato;
import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.utils.DateUtils;
import abreusapp.core.control.utils.ModelServ;
import java.text.ParseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/dtgnr")
public class DatosCntr {
    
    private final DateUtils FechaUtils;
    
    private final ModelServ ModeloServicio;
    
    private final DatoServ DatoServicio;
    
    private final AccesoServ AccesoServicio;
    
//----------------------------------------------------------------------------//
//---------------------------ENDPOINTS DATOS----------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/save")
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
                    
                    DatoServicio.guardar(dtgnr, usuarioLogueado, datoBD.isPresent());
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

    @PostMapping("/update")
    public String ActualizarDatosGenerales(
        Model model,
        String idDato
    ) {

        String plantillaRespuesta="fragments/dat_gen_registro_datos :: content-default";
        boolean valido = true;
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Dato> datoDB = DatoServicio.obtener(idDato);

        if (! datoDB.isPresent()) {

            //log.error("Error COD: 00535 al editar datos generales, ({}) no existe.",idDato);
            valido=false;
            plantillaRespuesta="redirect:/error";

        }
        
        if(valido){
            model.addAttribute("dato", datoDB.get());
            model.addAttribute("update", true);
            model.addAttribute("grupos", DatoServicio.consultarPorGrupo(null));
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                    usuarioLogueado.getId(), "dat_gen_registro_datos" ) 
            );
        }

        return plantillaRespuesta;

    }

}
