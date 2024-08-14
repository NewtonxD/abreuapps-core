package abreuapps.core.control;

import abreuapps.core.control.transporte.Parada;
import abreuapps.core.control.transporte.ParadaServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.ModelServ;
import java.text.ParseException;
import java.util.HashMap;
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
@RequestMapping("/pda")
public class ParadasCntr {
    
    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;

    private final ModelServ ModeloServicio;
    
    private final ParadaServ ParadaServicio;
        
//----------------------------------------------------------------------------//
//------------------ENDPOINTS PARADAS-----------------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarParada(
        Model model,
        Parada paradaCliente,
        @RequestParam(name = "fecha_actualizacionn", 
                        required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        
        String plantillaRespuesta="fragments/trp_paradas_consulta :: content-default";
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //INICIO DE VALIDACIONES
        String sinPermisoPlantilla = ModeloServicio.verificarPermisos(
            "trp_paradas_consulta", model, u );
        
        //USUARIO NO TIENE PERMISOS PARA EJECUTAR ESTA ACCION
        valido = sinPermisoPlantilla.equals("");
        
        
        if(valido){
            
            Optional<Parada> paradaDB = ParadaServicio.obtener(paradaCliente.getId());

            if (paradaDB.isPresent()) {

                if (! FechaUtils.FechaFormato2.format(
                        paradaDB.get().getFecha_actualizacion()
                        ).equals(fechaActualizacionCliente)
                ) {
                    
                    model.addAttribute(
                        "msg",
                        ! ( fechaActualizacionCliente == null || 
                             fechaActualizacionCliente.equals("") ) ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00656" :
                        "No podemos realizar los cambios porque ya esta Parada se encuentra registrado."
                    );
                    valido = false;
                    
                }

            }
            
            //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
            if(valido){
                
            
                if ( ! ( fechaActualizacionCliente == null || 
                        fechaActualizacionCliente.equals("") )
                ) {
                    paradaCliente.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente)
                    );
                }
                
                if (paradaDB.isPresent()) {
                    paradaCliente.setFecha_registro(paradaDB.get().getFecha_registro());
                    paradaCliente.setHecho_por(paradaDB.get().getHecho_por());
                }

                ParadaServicio.guardar(paradaCliente, u, paradaDB.isPresent());
                model.addAttribute("msg", "Registro guardado exitosamente!");

                ModeloServicio.load("trp_paradas_consulta", model, u.getId());
            }
            
            model.addAttribute("status", valido);
        }

        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }    
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarParada(
        Model model,
        @RequestParam("idParada") Integer idParada
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/trp_paradas_registro :: content-default";
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Parada> parada = ParadaServicio.obtener(idParada);

        if (!parada.isPresent()) {

            //log.error("Error COD: 00637 al editar parada. No encontrado ({})",idParada);
            plantillaRespuesta = "redirect:/error";
            valido=false;

        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){
            model.addAttribute("parada", parada.get());
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                            usuarioLogueado.getId(), "trp_paradas_registro" )
            );
        }

        return plantillaRespuesta;
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/getLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerLocParada(
        @RequestParam("idParada") Integer idParada
    ) {  
        boolean valido;
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //VERIFICAMOS PERMISOS PARA ESTA ACCION
        String sinPermisoPlantilla = 
            ModeloServicio.verificarPermisos(
            "trp_paradas_registro", null, u 
            );
        
        valido = sinPermisoPlantilla.equals("");
        
        Map<String, Object> respuesta= new HashMap<>();
        
        if(valido){
            Optional<Parada> LocParada = ParadaServicio.obtener(idParada); 
            
            respuesta.put("paradas",ParadaServicio.consultarTodo( 
                idParada , 
                true
            ));
        
            //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
            if(LocParada.isPresent()){
                respuesta.put("lon",LocParada.get().getLongitud());
                respuesta.put("lat", LocParada.get().getLatitud());
            }
        }
        
        return new ResponseEntity<>(
                respuesta.isEmpty() ? null: respuesta,
                new HttpHeaders(),
                HttpStatus.OK);  
    }
    
    
}
