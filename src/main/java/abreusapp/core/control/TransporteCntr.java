/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import abreusapp.core.control.general.Dato;
import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.general.GrupoDato;
import abreusapp.core.control.general.GrupoDatoServ;
import abreusapp.core.control.transporte.LocVehiculo;
import abreusapp.core.control.transporte.LocVehiculoServ;
import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoServ;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.utils.DateUtils;
import abreusapp.core.control.utils.ModelServ;
import abreusapp.core.control.utils.TransportTokenServ;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    private final LocVehiculoServ LocVehiculoServicio;
    
    private final TransportTokenServ TrpTokenServ;
    
    private final PasswordEncoder passwordEncoder;
    
    private static final String PWD_HASH="$2a$10$FD.HVab6z8H3Tba.hw.SvukdeJDfZ5aIIzCN87AL7T2SSAJqoi8Bq";
    
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
    
    @PostMapping(value="/vhl/getLastLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerUltimaLocTransporte(
            HttpServletRequest request, 
            @RequestParam("placa") String placa
    ) {  
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        String verificarPermisos = ModeloServicio.verificarPermisos("trp_vehiculo_registro", null, u);
        if (! verificarPermisos.equals("")) return null;
        
        LocVehiculo lastLoc = LocVehiculoServicio.consultarUltimaLocVehiculo(placa); 
        
        Map<String, Object> respuesta= new HashMap<>();
        if(lastLoc!=null){
            respuesta.put("placa", placa);
            respuesta.put("lon",lastLoc.getLongitud());
            respuesta.put("lat", lastLoc.getLatitud());
            respuesta.put("fecha",lastLoc.getFecha_registro());
        }
        return new ResponseEntity<>(
                lastLoc!=null?respuesta:null,
                new HttpHeaders(),
                HttpStatus.OK);  
    }
    
//----------------------------------------------------------------------------//
//----------------------------API TRANSPORTE----------------------------------//
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/API/trp/verifyData", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> VerificarInformacionTransporte(
            HttpServletRequest request, 
            @RequestBody Map<String, String> req
    ) {  
        boolean valido=false;
        
        String mensaje="";
        String token="";
        String placa=req.get("placa");
        String pwd=req.get("password");
        if(passwordEncoder.matches(pwd, PWD_HASH)){
            
            if(! placa.equals("") ){
                Optional<Vehiculo> v = VehiculoServicio.obtener(placa);

                if(v.isPresent()){

                    if (! v.get().isActivo() )
                        mensaje = "Vehiculo Inactivo! Verifique Placa...";

                    if (! v.get().getEstado().getDato().equals("Estacionado") ) 
                        mensaje = "Debe presionar detener para poder Iniciar!";

                }else mensaje = "Vehiculo no existe!";

                if(mensaje.equals("")) {
                    
                    token=TrpTokenServ.generateToken();
                    valido=true;
                    mensaje = "Transporte en Camino! Iniciando Servicio...";
                    
                    if(!"".equals(token)){
                        Vehiculo h=v.get();
                        h.setEstado(DatosServicio.obtener("En Camino").get());
                        h.setToken(token);
                        VehiculoServicio.guardar(h, null, true);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("U", h);
                        map.put("date", FechaUtils.FechaFormato1.format(new Date()));
                        SSEControlador.publicar("vhl", map);
                    }
                }

            }else mensaje="Placa no pudo ser procesada...";
        
        }else mensaje="Token invalido, intentelo nuevamente...";
        
        Map<String, Object> respuesta= new HashMap<>();
        respuesta.put("isValid", valido);
        respuesta.put("message", mensaje);
        respuesta.put("token", token);
        
        return respuesta;  
    } 
    
    @PostMapping(value="/API/trp/sendData", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> RegistrarInformacionTransporte(
            HttpServletRequest request, 
            @RequestBody Map<String, String> req
    ) {  
        boolean valido=true;
        
        String mensaje = "";
        
        String placa   = req.get("placa");
        Double lat     = Double.valueOf(req.get("lat"));
        Double lon     = Double.valueOf(req.get("lon"));
        String token   = req.get("token");
       
        
        if(!(placa.isBlank() || lat.isNaN() || lon.isNaN())){
        
            Vehiculo v=VehiculoServicio.obtener(placa).get();
            
            if(token.equals(v.getToken())){
                
                LocVehiculo lv = new LocVehiculo();

                lv.setLatitud(lat);
                lv.setLongitud(lon);
                lv.setPlaca(v);

                LocVehiculoServicio.guardar(lv);
                
            }
        }
        
        Map<String, Object> respuesta= new HashMap<>();
        respuesta.put("isValid", valido);
        respuesta.put("message", mensaje);
        
        return respuesta;  
        
    }
    
    @PostMapping(value="/API/trp/changeStatus", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> CambiarEstadoTransporte(
            HttpServletRequest request, 
            @RequestBody Map<String, String> req
    ) {  
        boolean valido=false;
        
        String mensaje="";
        
        String placa  =  req.get("placa");
        String estado =  req.get("estado");
        String pwd    =  req.get("password");
        
        if(passwordEncoder.matches(pwd, PWD_HASH)){
        
            if(! placa.equals("") ){
                Optional<Vehiculo> v = VehiculoServicio.obtener(placa);

                if(v.isPresent()){

                    if (! v.get().isActivo() )
                        mensaje = "Vehiculo Inactivo! Verifique Placa...";
                    else {

                        mensaje = "Transporte "+estado+"! ";
                        valido=true;
                        Vehiculo h=v.get();
                        h.setToken("");
                        h.setEstado(DatosServicio.obtener(estado).get());
                        VehiculoServicio.guardar(h, null, true);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("U", h);
                        map.put("date", FechaUtils.FechaFormato1.format(new Date()));
                        SSEControlador.publicar("vhl", map);
                    }

                }else mensaje = "Vehiculo no existe!";

            }else mensaje="Placa no pudo ser procesada...";
        
        }else mensaje="Token invalido, intentelo nuevamente...";
        
        Map<String, Object> respuesta= new HashMap<>();
        respuesta.put("isValid", valido);
        respuesta.put("message", mensaje);
        
        return respuesta;  
    } 
    
    
}
