/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.Dato;
import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.general.GrupoDato;
import abreusapp.core.control.general.GrupoDatoServ;
import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoServ;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.utils.DateUtils;
import abreusapp.core.control.utils.ModelServ;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class TransporteCntr {
    
    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;

    private final ModelServ ModeloServicio;

    private final SSECntr SSEControlador;
    
    private final VehiculoServ VehiculoServicio;
    
    private final GrupoDatoServ GrupoServicio;
    
    private final DatoServ DatosServicio;
    
    
//----------------------------------------------------------------------------//
//----------------------------VEHICULOS---------------------------------------//
//----------------------------------------------------------------------------//
    
    @PostMapping("/vhl/save")
    public String GuardarVehiculo(
            HttpServletRequest request,
            Model model,
            Vehiculo vhl,
            @RequestParam(name = "fecha_actualizacionn", required = false) String dateInput
    ) throws ParseException {

        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos= ModeloServicio.verificarPermisos("trp_vehiculo_registro", model, u);
        if (! verificarPermisos.equals("")) return verificarPermisos;

        HashMap<String, Object> map = new HashMap<>();

        if (dateInput != null && !dateInput.equals("")) {

            vhl.setFecha_actualizacion(FechaUtils.Formato2ToDate(dateInput));

        }

        Optional<Vehiculo> vhl1 = VehiculoServicio.obtener(vhl.getPlaca());

        boolean ext = false, ss = true;

        if (vhl1.isPresent()) {

            ext = true;

            if (!FechaUtils.FechaFormato2.format(vhl1.get().getFecha_actualizacion()).equals(dateInput)) {

                ss = false;

            } else {

                vhl.setFecha_registro(vhl1.get().getFecha_registro());
                vhl.setHecho_por(vhl1.get().getHecho_por());

            }

        }

        if (ss) {

            Vehiculo d = VehiculoServicio.guardar(vhl, u, ext);
            model.addAttribute("status", true);
            model.addAttribute("msg", "Registro guardado exitosamente!");
            map.put(ext ? "U" : "I", d);
            map.put("date", FechaUtils.FechaFormato1.format(new Date()));

        } else {

            model.addAttribute("status", false);
            model.addAttribute(
                    "msg", 
                     ( dateInput!=null ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00635" :
                        "No podemos realizar los cambios porque ya este Vehículo se encuentra registrado."
                     )
            );

        }
        
        ModeloServicio.load("trp_vehiculo_consulta", model, u.getId());

        if (!map.isEmpty()) {
            SSEControlador.publicar("vhl", map);
        }

        return "fragments/trp_vehiculo_consulta :: content-default";

    }
//----------------------------------------------------------------------------//
    
    @PostMapping("/vhl/update")
    public String ActualizarVehiculo(
            HttpServletRequest request,
            Model model,
            @RequestParam("placa") String Placa
    ) {

        Usuario u = ModeloServicio.getUsuarioLogueado();

        Optional<Vehiculo> g = VehiculoServicio.obtener(Placa);

        if (!g.isPresent()) {

            log.error("Error COD: 00637 al editar vehículo.");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());

            return "redirect:/error";

        }

        model.addAttribute("vehiculo", g.get());
        model.addAttribute(
                "marca",
                DatosServicio.consultarPorGrupo(
                        GrupoServicio.obtener("Marca").get() 
                )
        );
        model.addAttribute(
                "tipo_vehiculo",
                DatosServicio.consultarPorGrupo(
                        GrupoServicio.obtener("Tipo Vehiculo").get() 
                )
        );
        model.addAttribute(
                "estado",
                DatosServicio.consultarPorGrupo(
                        GrupoServicio.obtener("Estados Vehiculo").get() 
                )
        );
        model.addAttribute(
                "color",
                DatosServicio.consultarPorGrupo(
                        GrupoServicio.obtener("Colores").get() 
                )
        );
        model.addAttribute(
                "modelo",
                DatosServicio.consultarPorGrupo(
                        GrupoServicio.obtener(g.get().getMarca().getDato() ).get() 
                )
        );
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario(u.getId(), "trp_vehiculo_registro"));

        return "fragments/trp_vehiculo_registro :: content-default";
    }
    
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/vhl/get-modelos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerListadoPermisosUsuario(
            HttpServletRequest request, 
            @RequestParam("Marca") String marca
    ) {  
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos = ModeloServicio.verificarPermisos("trp_vehiculo_registro", null, u);
        if (! verificarPermisos.equals("")) return null;
        
        Optional<GrupoDato> g = GrupoServicio.obtener(marca);
        
        if(!g.isPresent()){

            log.error("Error COD: 00639 al editar Vehiculo. Marca no encontrada ("+marca+")");
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            
            return null;

        }
        
        List<Dato> modelos = DatosServicio.consultarPorGrupo(g.get()); 
        
        return new ResponseEntity<>(
                modelos,
                new HttpHeaders(),
                HttpStatus.OK);  
    }

//----------------------------------------------------------------------------//
    
}
