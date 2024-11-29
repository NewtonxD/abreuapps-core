package abreuapps.core.control;

import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.utils.SSEServ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/see")
@CrossOrigin
@RequiredArgsConstructor
public class SSECntr {
    
    private final SSEServ SSEServicio;
    
    private final AccesoServ AccesoServicio;
    
    private static final List<String> REQUIRED_AUTH = new ArrayList<>(Arrays.asList("dtgnr" ,"usrmgr","vhl" ,"pda","rta","pub","vhl_log","prd"));
    
    
//----------------------------------------------------------------------------//
//--------------ENDPOINTS SERVER SIDE EVENTS----------------------------------//
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/{nombre}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter consultarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        if(REQUIRED_AUTH.contains(nombre) && 
            AccesoServicio.getUsuarioLogueado()==null
        ) return null;
        
        return SSEServicio.agregar(clientId,nombre);
    }
//----------------------------------------------------------------------------//
    
    @GetMapping(value = "/{nombre}/close")
    @ResponseBody
    public void cerrarSSE(
        @RequestParam String clientId,
        @PathVariable String nombre
    ) {
        if(
            (REQUIRED_AUTH.contains(nombre) && AccesoServicio.getUsuarioLogueado()!=null)||
            (!REQUIRED_AUTH.contains(nombre))
        ) SSEServicio.cerrar(clientId,nombre);
    }
//----------------------------------------------------------------------------//
    
}
