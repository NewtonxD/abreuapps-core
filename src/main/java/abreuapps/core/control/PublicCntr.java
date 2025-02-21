package abreuapps.core.control;

import abreuapps.core.control.general.ConfServ;
import abreuapps.core.control.general.PublicidadDTO;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.utils.SSEServ;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * @author cabreu
 */
@Controller
@RequiredArgsConstructor
public class PublicCntr {
    
    private final SSEServ SSEServicio;
    
    private final PublicidadServ PublicidadServicio;
    
    private final ConfServ ConfiguracionServicio;

//----------------------------------------------------------------------------//
//--------------ENDPOINTS PUBLICIDAD PUBLICO----------------------------------//
//----------------------------------------------------------------------------//
    @GetMapping(value="/p/pub/datos")
    @ResponseBody
    public PublicidadDTO consultarDatosActualPublicidad(){
        PublicidadDTO p=PublicidadServicio.obtenerUltimo();
        if(p!=null) PublicidadServicio.IncrementarVistas(p.id());
        return p;
    }
    
//-----------------------------------------------------------------------------//
    @GetMapping(value="/p/pub/archivo/{nombre}")
    public ResponseEntity<Resource> consultarArchivoActualPublicidad(@PathVariable("nombre") String nombre ){
        Map<String,Object> archivo=PublicidadServicio.obtenerArchivoPublicidad(URLDecoder.decode(nombre, StandardCharsets.UTF_8) );

        return (archivo!=null) ?
            ResponseEntity.ok()
                .contentType( (MediaType) archivo.get("media-type") )
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + ((Resource)archivo.get("body")).getFilename() + "\"")
                .body((Resource)archivo.get("body"))
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }
    
//----------------------------------------------------------------------------//
    @GetMapping(value="/p/pub/click/{id}")
    @ResponseBody
    public void aumentarClicksPublicidad(@PathVariable Long id){
        PublicidadServicio.IncrementarClics(id);
    }
    
    
//----------------------------------------------------------------------------//
//--------------ENDPOINTS SERVER SIDE EVENTS----------------------------------//
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/p/see/{nombre}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter solicitarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {        
        return SSEServicio.agregar(clientId,nombre);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/p/see/{nombre}/close")
    @ResponseBody
    public void cerrarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        SSEServicio.cerrar(clientId,nombre);
    }
    
    
    
//----------------------------------------------------------------------------//
//------------------------- AUTH----------------------------------------------//
//----------------------------------------------------------------------------//
    @GetMapping("/auth/login")
    public String Login(
        @RequestParam(name = "invalidSession", required = false,defaultValue = "false") boolean invalidSession,
        @RequestParam(name = "logout", required = false,defaultValue = "false") boolean logout,
        Model model
    ) {
        model.addAttribute("app_nombre",ConfiguracionServicio.consultar("appnombre"));
        
        if((SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken))
            return "redirect:/main/index";
        
        if(invalidSession)
            model.addAttribute("error_msg","Su sesión expiró. Ingrese sus credenciales nuevamente.");
        
        if(logout)
            model.addAttribute("success_msg","Sesión cerrada exitosamente!");
        
        return "login";
    }
//----------------------------------------------------------------------------//
    
    @GetMapping("/")
    public String redirectLogin(Model model){
        model.addAttribute("server_ip",ConfiguracionServicio.consultar("serverip"));
        model.addAttribute("app_provincia",ConfiguracionServicio.consultar("appprovincia"));
        model.addAttribute("base_dir",ConfiguracionServicio.consultar("centraldir"));
        String baseLatLng = ConfiguracionServicio.consultar("centrallatlng");
        model.addAttribute("base_lat",baseLatLng.split(",")[0]);
        model.addAttribute("base_lng",baseLatLng.split(",")[1]);
        return "mapa";
        //return "redirect:/auth/login";
    }    
//----------------------------------------------------------------------------//
    @RequestMapping("/favicon.ico")
    public String Favicon(){
        return "redirect:/content/assets/img/favicon_io/favicon.ico";
    }
//----------------------------------------------------------------------------//
   
}
