package abreuapps.core.control;

import abreuapps.core.control.general.Dato;
import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
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
    
    private final AccesoServ AccesoServicio;
    
    private final DatoServ DatoServicio;
    
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
        Usuario usuarioLogueado = AccesoServicio.getUsuarioLogueado();
        
        String sinPermisoPlantilla = AccesoServicio.verificarPermisos(
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
            AccesoServicio.cargarPagina(
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
        
        Usuario usuarioLogueado = AccesoServicio.getUsuarioLogueado();

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
    @PostMapping("/emp/save")
    public String GuardarDatoEmpresa(
        Model model, 
        Dato dtgnr,
        @RequestParam(value = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        String plantillaRespuesta = "fragments/dat_gen_consulta_empresa :: content-default";
        Usuario usuarioLogueado = AccesoServicio.getUsuarioLogueado();
        
        String sinPermisoPlantilla = AccesoServicio.verificarPermisos(
                "dat_gen_registro_empresa", model, usuarioLogueado );
        
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            
            if(dtgnr==null){
                model.addAttribute(
                        "msg",
                        "La información de la empresa no puede ser guardada. Por favor, inténtalo otra vez. COD: 00562");
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
            AccesoServicio.cargarPagina(
                    "dat_gen_consulta_empresa", model, usuarioLogueado.getId() );

        
        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;
    }
//----------------------------------------------------------------------------//

    @PostMapping("/emp/update")
    public String ActualizarDatosEmpresa(
        Model model,
        String idDato
    ) {

        String plantillaRespuesta="fragments/dat_gen_registro_empresa :: content-default";
        boolean valido = true;
        
        Usuario usuarioLogueado = AccesoServicio.getUsuarioLogueado();

        Optional<Dato> datoDB = DatoServicio.obtener(idDato);

        if (! datoDB.isPresent()) {

            //log.error("Error COD: 00535 al editar datos generales, ({}) no existe.",idDato);
            valido=false;
            plantillaRespuesta="redirect:/error";

        }
        
        if(valido){
            model.addAttribute("dato", datoDB.get());
            model.addAttribute("update", true);
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                    usuarioLogueado.getId(), "dat_gen_registro_empresa" ) 
            );
        }

        return plantillaRespuesta;

    }

}
