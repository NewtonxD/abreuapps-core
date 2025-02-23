package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.transporte.LocRutaServ;
import abreuapps.core.control.transporte.LocVehiculo;
import abreuapps.core.control.transporte.LocVehiculoServ;
import abreuapps.core.control.transporte.ParadaServ;
import abreuapps.core.control.transporte.RutaDTO;
import abreuapps.core.control.transporte.RutaServ;
import abreuapps.core.control.transporte.Vehiculo;
import abreuapps.core.control.transporte.VehiculoServ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static java.util.Map.entry;

/**
 *
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/API")
public class APITransporteCntr {
    
    private final VehiculoServ VehiculoServicio;
    
    private final DatoServ DatosServicio;
    
    private final ParadaServ ParadaServicio;
    
    private final LocVehiculoServ LocVehiculoServicio;
        
    private final RutaServ RutaServicio;
    
    private final LocRutaServ LocRutaServicio;
    
    private final PasswordEncoder passwordEncoder;
    
    private static final String PWD_HASH="$2a$10$FD.HVab6z8H3Tba.hw.SvukdeJDfZ5aIIzCN87AL7T2SSAJqoi8Bq";


//----------------------------------------------------------------------------//
//------------------ENDPOINTS API TRANSPORTE----------------------------------//
//----------------------------------------------------------------------------//
    @ResponseBody
    @PostMapping(value="/trp/verifyData", 
            produces = MediaType.APPLICATION_JSON_VALUE, 
            consumes=MediaType.APPLICATION_JSON_VALUE )
    public Map<String, Object> VerificarInformacionTransporte(
        @RequestBody Map<String, String> req
    ) {
        String token="";
        String placa=req.getOrDefault("placa","");
        String ruta=req.getOrDefault("ruta","");
        String pwd=req.getOrDefault("password","");

        //INICIO DE LAS VALIDACIONES
        if(placa.equals("") )
            return Map.ofEntries(
                entry("message","Placa no pudo ser procesada"),
                entry("isValid",false), entry("token",token)
            );
        
        if(ruta.equals("") )
            return Map.ofEntries(
                entry("message","Ruta invalida"),
                entry("isValid",false), entry("token",token)
            );

        if(!passwordEncoder.matches(pwd, PWD_HASH ))
            return Map.ofEntries(
                entry("message","Contraseña invalida, intentelo otra vez"),
                entry("isValid",false), entry("token",token)
            );
            
        var vehiculoDB = VehiculoServicio.obtener(placa);

        if(! vehiculoDB.isPresent())
            return Map.ofEntries(
                entry("message","Vehiculo no existe!"),
                entry("isValid",false), entry("token",token)
            );

        if (! vehiculoDB.get().isActivo() )
            return Map.ofEntries(
                    entry("message","Vehiculo inactivo! verifique placa"),
                    entry("isValid",false), entry("token",token)
            );

        if (! (vehiculoDB.get().getEstado().getDato().equals("Estacionado") || vehiculoDB.get().getEstado().getDato().equals("Averiado")) )
            return Map.ofEntries(
                    entry("message","Presione detener y luego iniciar"),
                    entry("isValid",false), entry("token",token)
            );


        token = VehiculoServicio.generateToken();
        var vehiculo=vehiculoDB.get();
        vehiculo.setEstado(DatosServicio.obtener("En Camino").get());
        vehiculo.setRuta(RutaServicio.obtener(ruta).get());
        vehiculo.setToken(token);
        VehiculoServicio.guardarAPI(vehiculo);

        return Map.ofEntries(
                entry("message","Transporte en Camino! Iniciando Servicio..."),
                entry("isValid",true), entry("token",token)
        );
    } 
//----------------------------------------------------------------------------//
    
    @ResponseBody
    @PostMapping(value="/trp/sendData",
                    produces = MediaType.APPLICATION_JSON_VALUE, 
                    consumes=MediaType.APPLICATION_JSON_VALUE )
    public Map<String, Object> RegistrarInformacionTransporte(
        @RequestBody Map<String, String> req
    ) {
        String placa   = req.getOrDefault("placa","");
        Double lat     = Double.valueOf(req.get("lat"));
        Double lon     = Double.valueOf(req.get("lon"));
        String token   = req.getOrDefault("token","");
        
        //INICIO DE VALIDACIONES
        if((placa.isBlank() || lat.isNaN() || lon.isNaN() ))
            return Map.ofEntries(
                    entry("message","Datos invalidos! intentelo de nuevo."),
                    entry("isValid",false)
            );

        Optional<Vehiculo> v=VehiculoServicio.obtener(placa);

        if(!v.isPresent() )
            return Map.ofEntries(
                    entry("message","Vehiculo no existe!"),
                    entry("isValid",false)
            );
        

        if(! token.equals(v.get().getToken()))
            return Map.ofEntries(
                    entry("message","Token invalido!"),
                    entry("isValid",false)
            );


        LocVehiculo lv = new LocVehiculo();
        lv.setLatitud(lat);
        lv.setLongitud(lon);
        lv.setPlaca(v.get());
        lv.setRuta(v.get().getRuta());
        LocVehiculoServicio.guardar(lv);

        return Map.ofEntries(
                entry("message",""),
                entry("isValid",true)
        );
        
    }
//----------------------------------------------------------------------------//
    @ResponseBody
    @PostMapping(value="/trp/changeStatus",
                    produces = MediaType.APPLICATION_JSON_VALUE, 
                    consumes=MediaType.APPLICATION_JSON_VALUE )
    public Map<String, Object> CambiarEstadoTransporte(
        @RequestBody Map<String, String> req
    ) {
        String placa  =  req.getOrDefault("placa","");
        String estado =  req.getOrDefault("estado","");
        String pwd    =  req.getOrDefault("password","");

        //INICIO DE VALIDACIONES
        if( placa.equals(""))
            return Map.ofEntries(
                    entry("message","Placa no pudo ser procesada..."),
                    entry("isValid",false)
            );

        if(! passwordEncoder.matches(pwd,PWD_HASH))
            return Map.ofEntries(
                    entry("message","Token invalido, intentelo nuevamente..."),
                    entry("isValid",false)
            );

            
        Optional<Vehiculo> vehiculoBD = VehiculoServicio.obtener(placa);
            
        if(!vehiculoBD.isPresent() )
            return Map.ofEntries(
                    entry("message","Vehiculo no existe!"),
                    entry("isValid",false)
            );

        if(!vehiculoBD.get().isActivo())
            return Map.ofEntries(
                    entry("message","Vehiculo Inactivo! Verifique Placa..."),
                    entry("isValid",false)
            );

        Vehiculo vehiculo=vehiculoBD.get();
        vehiculo.setToken("");
        vehiculo.setEstado(DatosServicio.obtener(estado).get());
        if(! estado.equals("En Camino") && ! estado.equals("En Parada")){
            vehiculo.setRuta(null);
        }
        VehiculoServicio.guardarAPI(vehiculo);

        return Map.ofEntries(
                entry("message","Transporte "+estado+"! "),
                entry("isValid",true)
        );
    } 
//----------------------------------------------------------------------------//
    @ResponseBody
    @GetMapping(value="/trp/getRutas")
    public Map<String, Object> ObtenerRutasActivas(
    ) { //rutas activas por nombre
        return Map.ofEntries(
                entry("rutas",
                        RutaServicio.consultarActivo()
                            .stream()
                            .map(RutaDTO::rta)
                            .collect(Collectors.toList())
                )
        );
    }
    
//----------------------------------------------------------------------------//
    @ResponseBody
    @GetMapping(value="/trp/getStatic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity ObtenerLocStaticAPI() {
        return ResponseEntity.ok(//respuesta
                Map.ofEntries(
                        entry("rutasInfo", RutaServicio.consultarInfo() ),
                        entry("rutasLoc", LocRutaServicio.consultar(null,true) ),
                        entry("paradas",ParadaServicio.consultarTodo(0 , true) ),
                        entry("vehiculosLoc", LocVehiculoServicio.consultarDatosTransporteEnCamino() )
                )
        );
    }
    
    @ResponseBody
    @PostMapping(value="/trp/getInfoObject",
                    produces = MediaType.APPLICATION_JSON_VALUE, 
                    consumes=MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity ObtenerInfoObjeto(
        @RequestBody Map<String, String> req
    ) {
        String tipo  =  req.getOrDefault("type","");
        Map<String, Object> respuesta= new HashMap<>();

        switch (tipo) {
            //--------------------------------------------------------------
           /* case "rta" -> {
                // informacion de la ruta:
                // 1- vehiculos activos con esta ruta (clic desde el cliente)
                // 2- distancia total de la ruta
                // 3- hacia donde va
            }*/
            //--------------------------------------------------------------
            case "pda" -> {
                Integer id = Integer.valueOf(req.getOrDefault("id",""));
                respuesta.put("pdaInfo",ParadaServicio.getParadaInfo(id));
            }
            //--------------------------------------------------------------
            case "myloc" -> {
                Double lat=Double.valueOf(req.getOrDefault("lat","0"));
                Double lon=Double.valueOf(req.getOrDefault("lon","0"));
                respuesta.put("locInfo",ParadaServicio.getParadaMasCercana(lat,lon));
            }
            //--------------------------------------------------------------
            default -> {
                // do nothing
            }
            //--------------------------------------------------------------
        }

        return ResponseEntity.ok(respuesta);
    }
    
}
