package abreuapps.core.control;

import abreuapps.core.control.general.Dato;
import abreuapps.core.control.general.DatoDTO;
import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.transporte.LocVehiculo;
import abreuapps.core.control.transporte.LocVehiculoServ;
import abreuapps.core.control.transporte.Vehiculo;
import abreuapps.core.control.transporte.VehiculoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.ModelServ;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/vhl")
public class VehiculosCntr {
    
    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;

    private final ModelServ ModeloServicio;
    
    private final VehiculoServ VehiculoServicio;
    
    private final DatoServ DatosServicio;
    
    private final LocVehiculoServ LocVehiculoServicio;
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS VEHICULOS---------------------------------------//
//----------------------------------------------------------------------------//
    
    @PostMapping("/save")
    public String GuardarVehiculo(
        Model model,
        Vehiculo vehiculoCliente,
        @RequestParam(name = "fecha_actualizacionn", 
                        required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        
        String plantillaRespuesta="fragments/trp_vehiculo_consulta :: content-default";
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //INICIO DE VALIDACIONES
        String sinPermisoPlantilla = ModeloServicio.verificarPermisos(
            "trp_vehiculo_registro", model, u );
        
        //USUARIO NO TIENE PERMISOS PARA EJECUTAR ESTA ACCION
        valido = sinPermisoPlantilla.equals("");
        
        
        if(valido){
            
            Optional<Vehiculo> vehiculoBD = VehiculoServicio.obtener(vehiculoCliente.getPlaca());

            if (vehiculoBD.isPresent()) {

                if (! FechaUtils.FechaFormato2.format(
                        vehiculoBD.get().getFecha_actualizacion()
                        ).equals(fechaActualizacionCliente)
                ) {
                    
                    
                    model.addAttribute(
                        "msg",
                        ! ( fechaActualizacionCliente == null || 
                             fechaActualizacionCliente.equals("") ) ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00635" :
                        "No podemos realizar los cambios porque ya este Vehículo se encuentra registrado."
                    );
                    valido = false;
                    
                }

            }
            
            //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
            if(valido){
                
            
                if ( ! ( fechaActualizacionCliente == null || 
                        fechaActualizacionCliente.equals("") )
                ) {
                    vehiculoCliente.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente)
                    );
                }
                
                if (vehiculoBD.isPresent()) {
                    vehiculoCliente.setFecha_registro(vehiculoBD.get().getFecha_registro());
                    vehiculoCliente.setHecho_por(vehiculoBD.get().getHecho_por());
                }

                VehiculoServicio.guardar(vehiculoCliente, u, vehiculoBD.isPresent());
                
                model.addAttribute("msg", "Registro guardado exitosamente!");

                ModeloServicio.load("trp_vehiculo_consulta", model, u.getId());
            }
            
            model.addAttribute("status", valido);
            
            
        }

        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/update")
    public String ActualizarVehiculo(
        HttpServletRequest request,
        Model model,
        @RequestParam("placa") String placa
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/trp_vehiculo_registro :: content-default";
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Vehiculo> vehiculo = VehiculoServicio.obtener(placa);

        if (!vehiculo.isPresent()) {

            //log.error("Error COD: 00637 al editar vehículo. No encontrado ({})",placa);
            plantillaRespuesta = "redirect:/error";
            valido=false;

        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){
            model.addAttribute("vehiculo", vehiculo.get());
            model.addAttribute(
                    "marca",
                    DatosServicio.consultarPorGrupo("Marca") );
            model.addAttribute(
                    "last_loc", 
                    LocVehiculoServicio.tieneUltimaLoc(placa)
            );
            model.addAttribute(
                    "tipo_vehiculo",
                    DatosServicio.consultarPorGrupo("Tipo Vehiculo") );
            model.addAttribute(
                    "estado",
                    DatosServicio.consultarPorGrupo("Estados Vehiculo") );
            model.addAttribute(
                    "color",
                    DatosServicio.consultarPorGrupo("Colores") );
            model.addAttribute(
                    "modelo",
                    DatosServicio.consultarPorGrupo(vehiculo.get().getMarca().getDato() ) );
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                            usuarioLogueado.getId(), "trp_vehiculo_registro" )
            );
        }

        return plantillaRespuesta;
    }
    
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/get-modelos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerListadoPermisosUsuario(
        HttpServletRequest request, 
        @RequestParam("Marca") String marca
    ) {  
        boolean valido;
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla = 
            ModeloServicio.verificarPermisos(
            "trp_vehiculo_registro", null, u 
            );
        
        valido = sinPermisoPlantilla.equals("");
        
        List<DatoDTO> modelos = null; 
        
        if(valido){
            Optional<Dato> Marca = DatosServicio.obtener(marca);

            if(!Marca.isPresent()){
                //log.error("Error COD: 00639 al editar Vehiculo. Marca no encontrada ({})",marca);
                valido=false;
            }
            
            //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
            if(valido) modelos = DatosServicio.consultarPorGrupo(Marca.get().getDato() ); 
            
        }
        
        return new ResponseEntity<>(
                modelos,
                new HttpHeaders(),
        HttpStatus.OK);  
    }
    
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/getLastLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerUltimaLocTransporte(
        @RequestParam("placa") String placa
    ) {  
        boolean valido;
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla = 
            ModeloServicio.verificarPermisos(
            "trp_vehiculo_registro", null, u 
            );
        
        valido = sinPermisoPlantilla.equals("");
        
        Map<String, Object> respuesta= new HashMap<>();
        
        if(valido){
            Optional<LocVehiculo> lastLoc = LocVehiculoServicio.consultarUltimaLocVehiculo(placa); 
        
            //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
            if(lastLoc.isPresent()){
                respuesta.put("placa", placa);
                respuesta.put("lon",lastLoc.get().getLongitud());
                respuesta.put("lat", lastLoc.get().getLatitud());
                respuesta.put("fecha",FechaUtils.DateToFormato1(lastLoc.get().getFecha_registro() ) );
            }
        }
        
        return new ResponseEntity<>(
                respuesta.isEmpty() ? null: respuesta,
                new HttpHeaders(),
                HttpStatus.OK);  
    }
    
}
