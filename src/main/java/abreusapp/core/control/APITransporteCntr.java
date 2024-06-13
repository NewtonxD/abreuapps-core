package abreusapp.core.control;

import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.transporte.DataServ;
import abreusapp.core.control.transporte.LocRutaServ;
import abreusapp.core.control.transporte.LocVehiculo;
import abreusapp.core.control.transporte.LocVehiculoServ;
import abreusapp.core.control.transporte.ParadaServ;
import abreusapp.core.control.transporte.RutaDTO;
import abreusapp.core.control.transporte.RutaServ;
import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoServ;
import abreusapp.core.control.utils.TransportTokenServ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    private final DataServ DataServicio;
    
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
        boolean valido=true;
        
        String mensaje="";
        String token="";
        String placa=req.getOrDefault("placa","");
        String ruta=req.getOrDefault("ruta","");
        String pwd=req.getOrDefault("password","");

        //INICIO DE LAS VALIDACIONES
        if(placa.equals("") ){
            mensaje="Placa no pudo ser procesada...";
            valido=false;
        }
        
        if(ruta.equals("") ){
            mensaje="Ruta invalida...";
            valido=false;
        }

        if(valido && !passwordEncoder.matches(
                pwd, 
                PWD_HASH )
        ){
            mensaje="Pwd invalida, intentelo nuevamente...";
            valido=false;
        }
            
        Optional<Vehiculo> v = VehiculoServicio.obtener(placa);

        if(! v.isPresent()){
            mensaje = "Vehiculo no existe!";
            valido=false;
        } 
        
        if(valido){
            if (! v.get().isActivo() ){
                mensaje = "Vehiculo Inactivo! Verifique Placa...";
                valido=false;
            }

            if (! v.get().getEstado().getDato().equals("Estacionado") ) {
                mensaje = "Debe Detener el vehiculo para poder Iniciar!";
                valido=false;
            }
        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido) {

            token= TransportTokenServ.generateToken();
            mensaje = "Transporte en Camino! Iniciando Servicio...";
            Vehiculo h=v.get();
            h.setEstado(DatosServicio.obtener("En Camino").get());
            h.setRuta(RutaServicio.obtener(ruta).get());
            h.setToken(token);
            VehiculoServicio.guardar(h, null, true);
            
        }
        
        Map<String, Object> respuesta= new HashMap<>();
        respuesta.put("isValid", valido);
        respuesta.put("message", mensaje);
        respuesta.put("token", token);
        
        return respuesta;  
    } 
//----------------------------------------------------------------------------//
    
    @ResponseBody
    @PostMapping(value="/trp/sendData",
                    produces = MediaType.APPLICATION_JSON_VALUE, 
                    consumes=MediaType.APPLICATION_JSON_VALUE )
    public Map<String, Object> RegistrarInformacionTransporte(
        @RequestBody Map<String, String> req
    ) {  
        boolean valido=true;
        
        String mensaje = "";
        String placa   = req.getOrDefault("placa","");
        Double lat     = Double.valueOf(req.get("lat"));
        Double lon     = Double.valueOf(req.get("lon"));
        String token   = req.getOrDefault("token","");
       
        
        //INICIO DE VALIDACIONES
        if(!(placa.isBlank() || lat.isNaN() || lon.isNaN() )){
            mensaje = "Datos invalidos! intentelo de nuevo.";
            valido=false;
        }
        
        Optional<Vehiculo> v=VehiculoServicio.obtener(placa);
        if(valido && !v.isPresent() ){
            mensaje = "Vehiculo no existe!";
            valido=false;
        }
        

        if(valido){
            if(! token.equals(v.get().getToken())){
                mensaje = "Token invalido!";
                valido=false;
            }   
        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){
            LocVehiculo lv = new LocVehiculo();
            lv.setLatitud(lat);
            lv.setLongitud(lon);
            lv.setPlaca(v.get());
            lv.setRuta(v.get().getRuta());
            LocVehiculoServicio.guardar(lv);
        }
        
        Map<String, Object> respuesta= new HashMap<>();
        respuesta.put("isValid", valido);
        respuesta.put("message", mensaje);
        
        return respuesta;  
        
    }
//----------------------------------------------------------------------------//
    @ResponseBody
    @PostMapping(value="/trp/changeStatus",
                    produces = MediaType.APPLICATION_JSON_VALUE, 
                    consumes=MediaType.APPLICATION_JSON_VALUE )
    public Map<String, Object> CambiarEstadoTransporte(
        @RequestBody Map<String, String> req
    ) {  
        boolean valido=true;
        String mensaje="";
        
        String placa  =  req.getOrDefault("placa","");
        String estado =  req.getOrDefault("estado","");
        String pwd    =  req.getOrDefault("password","");
        
        
        //INICIO DE VALIDACIONES
        if( placa.equals("")){
            mensaje="Placa no pudo ser procesada...";
            valido=false;
        }
        
        if(valido && !passwordEncoder.matches(
                pwd, 
                PWD_HASH )
        ){
            mensaje="Token invalido, intentelo nuevamente...";
            valido=false;
        } 
            
        Optional<Vehiculo> v = VehiculoServicio.obtener(placa);
            
        if(valido && !v.isPresent() ){
            mensaje = "Vehiculo no existe!";
            valido=false;
        }


        if (valido){
            if(!v.get().isActivo()){
                mensaje = "Vehiculo Inactivo! Verifique Placa...";
                valido=false;
            }
        } 
        
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){

            mensaje = "Transporte "+estado+"! ";
            Vehiculo h=v.get();
            h.setToken("");
            h.setEstado(DatosServicio.obtener(estado).get());
            if(! estado.equals("En Camino") && ! estado.equals("En Parada")){
                h.setRuta(null);
            }
            VehiculoServicio.guardar(h, null, true);
            
        }
        
        Map<String, Object> respuesta= new HashMap<>();
        respuesta.put("isValid", valido);
        respuesta.put("message", mensaje);
        
        return respuesta;  
    } 
//----------------------------------------------------------------------------//
    @ResponseBody
    @GetMapping(value="/trp/getRutas")
    public Map<String, Object> ObtenerRutasActivas(
    ) {  
        Map<String, Object> respuesta= new HashMap<>();
        List<RutaDTO> Rutas=RutaServicio.consultarActivo();
        List<String> NombreRutas=new ArrayList<>();
        for(RutaDTO r : Rutas){
            NombreRutas.add(r.rta());
        }
        respuesta.put("rutas", NombreRutas);                
        return respuesta;  
    }
    
//----------------------------------------------------------------------------//
    @ResponseBody
    @GetMapping(value="/trp/getStatic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity ObtenerLocStaticAPI(
    ) {  

        Map<String, Object> respuesta= new HashMap<>();

        respuesta.put("rutasLoc", LocRutaServicio.consultar(
                null,true) 
        );
        
        respuesta.put("paradas",ParadaServicio.consultarTodo( 
            0 , true)
        );

        return new ResponseEntity<>(
                respuesta.isEmpty() ? null: respuesta,
                new HttpHeaders(),
                HttpStatus.OK);  
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
        
        if (!"".equals(tipo)){
            switch (tipo) {
                //--------------------------------------------------------------
                case "rta" -> {
                    // informacion de la ruta:
                    // 1- vehiculos activos con esta ruta (clic desde el cliente)
                    // 2- distancia total de la ruta 
                    // 3- hacia donde va
                    // 4- (si esta disponible la localizacion) parada mas cercana a tu ubicacion 
                    // (id, descripcion, distancia) (clic de la parada en el cliente)
                    
                    
                    // en el cliente hara un fitbound de la ruta y se volvera de color opacidad 1
                    // se ocultaran todos los objetos que no esten asociados a esta ruta (otras rutas, paradas y vehiculos)
                    // cuando pierda el foco el popup volvera a su color normal y todo aparecera nuevamente
                }
                //--------------------------------------------------------------
                case "pda" -> {
                    Integer id = Integer.valueOf(req.getOrDefault("id",""));
                    respuesta.put("pdaInfo",DataServicio.getParadaInfo(id));
                }
                //--------------------------------------------------------------
                case "vhl" -> {
                    // informacion del vehiculo:
                    // 1- informaciones basicas del vehiculo incluyendo la ruta (clic de la ruta en el cliente)
                    // 2- velocidad
                    // 3- proxima parada (id, descripcion, distancia, tiempo ETA) (clic de la parada en el cliente)
                    
                    // en el cliente se perseguira la ubicacion del vehiculo y se actualizaran estos datos
                }
                //--------------------------------------------------------------
                case "myloc" -> {
                    Double lat=Double.valueOf(req.getOrDefault("lat","0"));
                    Double lon=Double.valueOf(req.getOrDefault("lon","0"));
                    respuesta.put("locInfo",DataServicio.getParadaMasCercana(lat,lon)); 
                }
                //--------------------------------------------------------------
                default -> {
                    // do nothing
                }
                //--------------------------------------------------------------
            }

        }

        return new ResponseEntity<>(
                respuesta,
                new HttpHeaders(),
                HttpStatus.OK);  
    }
    
}
