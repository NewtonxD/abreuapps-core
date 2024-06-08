package abreusapp.core.control;

import abreusapp.core.control.general.Dato;
import abreusapp.core.control.general.DatoDTO;
import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.transporte.LocRuta;
import abreusapp.core.control.transporte.LocRutaServ;
import abreusapp.core.control.transporte.LocVehiculo;
import abreusapp.core.control.transporte.LocVehiculoServ;
import abreusapp.core.control.transporte.Parada;
import abreusapp.core.control.transporte.ParadaServ;
import abreusapp.core.control.transporte.Ruta;
import abreusapp.core.control.transporte.RutaDTO;
import abreusapp.core.control.transporte.RutaServ;
import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoServ;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.utils.DateUtils;
import abreusapp.core.control.utils.ModelServ;
import abreusapp.core.control.utils.TransportTokenServ;
import com.google.common.cache.Cache;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
public class TransporteCntr {
    
    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;

    private final ModelServ ModeloServicio;
    
    private final VehiculoServ VehiculoServicio;
    
    private final DatoServ DatosServicio;
    
    private final ParadaServ ParadaServicio;
    
    private final LocVehiculoServ LocVehiculoServicio;
        
    private final RutaServ RutaServicio;
    
    private final LocRutaServ LocRutaServicio;
    
    private final PasswordEncoder passwordEncoder;
    
    private static final String PWD_HASH="$2a$10$FD.HVab6z8H3Tba.hw.SvukdeJDfZ5aIIzCN87AL7T2SSAJqoi8Bq";
    
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS RUTAS-------------------------------------------//
//----------------------------------------------------------------------------//   
    @PostMapping("/rta/save")
    public String GuardarRuta(
        Model model,
        Ruta rutaCliente,
        @RequestParam(name = "fecha_actualizacionn", 
                        required = false) String fechaActualizacionCliente,
        @RequestParam("data_poly") String data
    ) throws ParseException {

        boolean valido;
        
        
        String plantillaRespuesta="fragments/trp_rutas_consulta :: content-default";
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //INICIO DE VALIDACIONES
        String sinPermisoPlantilla = ModeloServicio.verificarPermisos(
            "trp_rutas_consulta", model, u );
        
        //USUARIO NO TIENE PERMISOS PARA EJECUTAR ESTA ACCION
        valido = sinPermisoPlantilla.equals("");
        
        
        if(valido){
            
            Optional<Ruta> rutaDB = RutaServicio.obtener(rutaCliente.getRuta());

            if (rutaDB.isPresent()) {

                if (! FechaUtils.FechaFormato2.format(
                        rutaDB.get().getFecha_actualizacion()
                        ).equals(fechaActualizacionCliente)
                ) {
                    
                    model.addAttribute(
                        "msg",
                        ! ( fechaActualizacionCliente == null || 
                             fechaActualizacionCliente.equals("") ) ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00656" :
                        "No podemos realizar los cambios porque ya esta Ruta se encuentra registrado."
                    );
                    valido = false;
                    
                }

            }
            
            //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
            if(valido){
                
            
                if ( ! ( fechaActualizacionCliente == null || 
                        fechaActualizacionCliente.equals("") )
                ) {
                    rutaCliente.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente)
                    );
                }
                
                if (rutaDB.isPresent()) {
                    rutaCliente.setFecha_registro(rutaDB.get().getFecha_registro());
                    rutaCliente.setHecho_por(rutaDB.get().getHecho_por());
                }

                Ruta d = RutaServicio.guardar(rutaCliente, u, rutaDB.isPresent());
                model.addAttribute("msg", "Registro guardado exitosamente!");
                
                // GUARDAMOS LOC RUTA
                if(!data.equals("")){
                    String cadenaListaLocRuta=data.replace("LatLng(","[").replace(")", "]");
                    List<LocRuta> listaLocRuta = LocRutaServicio.generarLista(cadenaListaLocRuta, d);
                    LocRutaServicio.borrarPorRuta(d);
                    LocRutaServicio.guardarTodos(listaLocRuta);
                }
                ModeloServicio.load("trp_rutas_consulta", model, u.getId());
            }
            
            model.addAttribute("status", valido);
        }

        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }    
//----------------------------------------------------------------------------//
    @PostMapping("/rta/update")
    public String ActualizarRuta(
        HttpServletRequest request,
        Model model,
        @RequestParam("idRuta") String idRuta
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/trp_rutas_registro :: content-default";
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Ruta> ruta = RutaServicio.obtener(idRuta);

        if (!ruta.isPresent()) {

            //log.error("Error COD: 00637 al editar parada. No encontrado ({})",idRuta);
            plantillaRespuesta = "redirect:/error";
            valido=false;

        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){
            model.addAttribute("ruta", ruta.get());
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                            usuarioLogueado.getId(), "trp_rutas_registro" )
            );
        }

        return plantillaRespuesta;
    }
//----------------------------------------------------------------------------//
    
    @PostMapping(value="/rta/getLoc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity ObtenerLocRuta(
        @RequestParam("idRuta") String idRuta
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
            Optional<Ruta> Ruta = RutaServicio.obtener(idRuta); 
            
            if(Ruta.isPresent()){
                respuesta.put("ruta",
                    LocRutaServicio.consultar(Ruta.get().getRuta(),null)) 
                ;
            } 
            
            respuesta.put("paradas",ParadaServicio.consultarTodo( 
                null , 
                true)
            );
        
        }
        
        return new ResponseEntity<>(
                respuesta.isEmpty() ? null: respuesta,
                new HttpHeaders(),
                HttpStatus.OK);  
    }
    
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS PARADAS-----------------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/pda/save")
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
    @PostMapping("/pda/update")
    public String ActualizarParada(
        HttpServletRequest request,
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
    
    @PostMapping(value="/pda/getLoc", produces = MediaType.APPLICATION_JSON_VALUE)
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
    
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS VEHICULOS---------------------------------------//
//----------------------------------------------------------------------------//
    
    @PostMapping("/vhl/save")
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
    
    @PostMapping("/vhl/update")
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
    
    @PostMapping(value="/vhl/get-modelos", produces = MediaType.APPLICATION_JSON_VALUE)
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
    
    @PostMapping(value="/vhl/getLastLoc", produces = MediaType.APPLICATION_JSON_VALUE)
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
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS API TRANSPORTE----------------------------------//
//----------------------------------------------------------------------------//
    @ResponseBody
    @PostMapping(value="/API/trp/verifyData", 
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
    @PostMapping(value="/API/trp/sendData",
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
    @PostMapping(value="/API/trp/changeStatus",
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
    @GetMapping(value="/API/trp/getRutas")
    public Map<String, Object> ObtenerRutasActivas() {  
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
    @GetMapping(value="/API/trp/getStatic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity ObtenerLocStaticAPI(
    ) {  

        Map<String, Object> respuesta= new HashMap<>();



        respuesta.put("rutas", RutaServicio.consultarActivo());
        respuesta.put("rutasLoc", LocRutaServicio.consultar(
                null,null) 
        );
        respuesta.put("paradas",ParadaServicio.consultarTodo( 
            0 , true)
        );

        return new ResponseEntity<>(
                respuesta.isEmpty() ? null: respuesta,
                new HttpHeaders(),
                HttpStatus.OK);  
    }    
    
}
