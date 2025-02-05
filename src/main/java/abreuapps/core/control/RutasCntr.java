package abreuapps.core.control;

import abreuapps.core.control.transporte.LocRuta;
import abreuapps.core.control.transporte.LocRutaServ;
import abreuapps.core.control.transporte.ParadaServ;
import abreuapps.core.control.transporte.Ruta;
import abreuapps.core.control.transporte.RutaServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
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
@RequestMapping("/rta")
public class RutasCntr {

    private final AccesoServ AccesoServicio;
        
    private final RutaServ RutaServicio;
    
    private final LocRutaServ LocRutaServicio;
    
    private final ParadaServ ParadaServicio;
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS RUTAS-------------------------------------------//
//----------------------------------------------------------------------------//   
    @PostMapping("/save")
    public String GuardarRuta(
        Model model,
        Ruta ruta,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacion,
        @RequestParam("data_poly") String data
    ) {

        if(! AccesoServicio.verificarPermisos("trp_rutas_registro"))
            return AccesoServicio.NOT_AUTORIZED_TEMPLATE;

        AccesoServicio.cargarPagina("trp_rutas_consulta", model);
        model.addAttribute("status", RutaServicio.guardar(ruta,data,fechaActualizacion,model));
        return "fragments/trp_rutas_consulta :: content-default";

    }    
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarRuta(
        Model model,
        @RequestParam("idRuta") String idRuta
    ) {
        if(! AccesoServicio.verificarPermisos("trp_rutas_registro"))
            return AccesoServicio.NOT_AUTORIZED_TEMPLATE;

        Optional<Ruta> ruta = RutaServicio.obtener(idRuta);

        if (!ruta.isPresent()) return "redirect:/error";

        model.addAttribute("ruta", ruta.get());
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario("trp_rutas_registro"));

        return "fragments/trp_rutas_registro :: content-default";
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/getLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerLocRuta(
        @RequestParam("idRuta") String idRuta
    ) {
        Map<String, Object> respuesta= new HashMap<>();
        if(AccesoServicio.verificarPermisos("trp_paradas_registro")){

            Optional<Ruta> Ruta = RutaServicio.obtener(idRuta);

            if(Ruta.isPresent())
                respuesta.put("ruta",
                    LocRutaServicio.consultar(Ruta.get().getRuta(),null)
                );
            
            respuesta.put("paradas",ParadaServicio.consultarTodo( null , true));
        
        }
        
        return new ResponseEntity<>(
                respuesta.isEmpty() ? null: respuesta,
                HttpStatus.OK);  
    }
    
}
