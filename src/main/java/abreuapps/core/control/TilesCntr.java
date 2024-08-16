package abreuapps.core.control;

import abreuapps.core.control.utils.RecursoServ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cabreu
 */

@RestController
@RequiredArgsConstructor
public class TilesCntr {
    
    private final RecursoServ ResourcesServicio;

//----------------------------------------------------------------------------//
//------------------ENDPOINTS TILES MAPA--------------------------------------//
//----------------------------------------------------------------------------//

    @GetMapping(value = "/API/tiles/{zoom}/{x}/{y}", produces = "image/webp")
    public ResponseEntity<byte[]> getMapTile(@PathVariable int zoom, @PathVariable int x, @PathVariable int y)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("image","webp"));
        return new ResponseEntity<>(ResourcesServicio.getTilesBytes(zoom, x, y), headers, HttpStatus.OK);
    }

//----------------------------------------------------------------------------//    
    
}
