package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.utils.TemplateServ;
import abreuapps.core.control.transporte.LocVehiculoServ;
import abreuapps.core.control.transporte.Vehiculo;
import abreuapps.core.control.transporte.VehiculoServ;
import abreuapps.core.control.usuario.AccesoServ;

import java.util.HashMap;
import java.util.Map;

import abreuapps.core.control.utils.DateUtils;
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
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/vhl")
public class VehiculosCntr {

    private final AccesoServ AccesoServicio;

    private final VehiculoServ VehiculoServicio;

    private final DatoServ DatosServicio;

    private final LocVehiculoServ LocVehiculoServicio;

    private final DateUtils FechaUtils;

    private final TemplateServ TemplateServicio;


//----------------------------------------------------------------------------//
//------------------ENDPOINTS VEHICULOS---------------------------------------//
//----------------------------------------------------------------------------//

    @PostMapping("/save")
    public String GuardarVehiculo(
            Model model,
            Vehiculo vehiculo,
            @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {

        if (!AccesoServicio.verificarPermisos("trp_vehiculo_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        TemplateServicio.cargarDatosPagina("trp_vehiculo_consulta", model);

        var resultados = VehiculoServicio.guardar(vehiculo, fechaActualizacion);
        model.addAttribute("status", resultados.get(0));
        model.addAttribute("msg", resultados.get(1));

        return "fragments/trp_vehiculo_consulta";

    }
//----------------------------------------------------------------------------//

    @PostMapping("/update")
    public String ActualizarVehiculo(
            Model model,
            @RequestParam("placa") String placa
    ) {

        if (!AccesoServicio.verificarPermisos("trp_vehiculo_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        var vehiculo = VehiculoServicio.obtener(placa);

        if (!vehiculo.isPresent())
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("vehiculo", vehiculo.get());
        model.addAttribute("marca", DatosServicio.consultarPorGrupo("Marca"));
        model.addAttribute("last_loc", LocVehiculoServicio.tieneUltimaLoc(placa));
        model.addAttribute("tipo_vehiculo", DatosServicio.consultarPorGrupo("Tipo Vehiculo"));
        model.addAttribute("estado", DatosServicio.consultarPorGrupo("Estados Vehiculo"));
        model.addAttribute("color", DatosServicio.consultarPorGrupo("Colores"));
        model.addAttribute("modelo", DatosServicio.consultarPorGrupo(vehiculo.get().getMarca().getDato()));
        model.addAttribute("dateUtils",FechaUtils);
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario("trp_vehiculo_registro"));

        return "fragments/trp_vehiculo_registro";
    }

//----------------------------------------------------------------------------//

    @PostMapping(value = "/get-modelos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerModelosVehiculosPorMarca(
            @RequestParam("Marca") String marca
    ) {
        if (!AccesoServicio.verificarPermisos("trp_vehiculo_registro"))
            return ResponseEntity.ok(null);

        var Marca = DatosServicio.obtener(marca);

        return ResponseEntity.ok(
                ! Marca.isPresent()?
                        null:
                        DatosServicio.consultarPorGrupo(Marca.get().getDato())
        );
    }

//----------------------------------------------------------------------------//

    @PostMapping(value = "/getLastLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerUltimaLocTransporte(
            @RequestParam("placa") String placa
    ) {

        if (!AccesoServicio.verificarPermisos("trp_vehiculo_registro") )
            return ResponseEntity.ok(null);

        Map<String, Object> respuesta = new HashMap<>();
        var lastLoc = LocVehiculoServicio.consultarUltimaLocVehiculo(placa);

        if (lastLoc.isPresent()) {
            respuesta.put("placa", placa);
            respuesta.put("lon", lastLoc.get().getLongitud());
            respuesta.put("lat", lastLoc.get().getLatitud());
            respuesta.put("fecha", FechaUtils.DateToFormato1(lastLoc.get().getFecha_registro()));
        }

        return ResponseEntity.ok(respuesta);
    }

}
