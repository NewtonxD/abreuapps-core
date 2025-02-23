package abreuapps.core.control;

import abreuapps.core.control.general.TemplateServ;
import abreuapps.core.control.transporte.Parada;
import abreuapps.core.control.transporte.ParadaServ;
import abreuapps.core.control.usuario.AccesoServ;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
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

    private final AccesoServ AccesoServicio;
    
    private final ParadaServ ParadaServicio;

    private final TemplateServ TemplateServicio;
        
//----------------------------------------------------------------------------//
//------------------ENDPOINTS PARADAS-----------------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarParada(
        Model model,
        Parada parada,
        @RequestParam(name = "fecha_actualizacionn", 
                        required = false) String fechaActualizacion
    ) {
        
        if (!AccesoServicio.verificarPermisos("trp_paradas_consulta"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        var respuesta = ParadaServicio.guardar(parada,fechaActualizacion);
        model.addAttribute("status", (Boolean)respuesta.get(0));
        model.addAttribute("msg", respuesta.get(1));

        TemplateServicio.cargarDatosPagina("trp_paradas_consulta", model);

        return "fragments/trp_paradas_consulta";
    }    
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarParada(
        Model model,
        @RequestParam("idParada") Integer idParada
    ) {

        if (! AccesoServicio.verificarPermisos("trp_paradas_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        var parada = ParadaServicio.obtener(idParada);
        if (!parada.isPresent())
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("parada", parada.get());
        model.addAllAttributes(
                AccesoServicio.consultarAccesosPantallaUsuario("trp_paradas_registro")
        );

        return "fragments/trp_paradas_registro";
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/getLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerLocParada(
        @RequestParam("idParada") Integer idParada
    ) {  
        if(!AccesoServicio.verificarPermisos("trp_paradas_registro"))
            return ResponseEntity.ok(null);
        
        Map<String, Object> respuesta= new HashMap<>();

        respuesta.put(
                "paradas",
                ParadaServicio.consultarTodo( idParada ,true)
        );

        var LocParada = ParadaServicio.obtener(idParada);
        if(LocParada.isPresent()){
            respuesta.put("lon",LocParada.get().getLongitud());
            respuesta.put("lat", LocParada.get().getLatitud());
        }

        return ResponseEntity.ok(respuesta);
    }
    
    
}
