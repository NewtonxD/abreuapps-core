package abreusapp.core.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
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
public class TilesCntr {
    
    @Value("${abreuapps.core.map.tiles.directory}")
    private String TILE_DIRECTORY; 

//----------------------------------------------------------------------------//
//------------------ENDPOINTS TILES MAPA--------------------------------------//
//----------------------------------------------------------------------------//

    @Cacheable("Tiles")
    @GetMapping(value = "/API/tiles/{zoom}/{x}/{y}", produces = "image/webp")
    public ResponseEntity<byte[]> getMapTile(@PathVariable int zoom, @PathVariable int x, @PathVariable int y) throws IOException {
        String tileKey = String.format("%d/%d/%d", zoom, x, y);
        String tilePath = String.format("%s/%s.webp", TILE_DIRECTORY, tileKey);
        Path path = Paths.get(tilePath);
        if (!Files.exists(path)) {
            path = Paths.get(TILE_DIRECTORY+"/default_tile.webp");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("image","webp"));
        return new ResponseEntity<>(new FileSystemResource(path).getContentAsByteArray(), headers, HttpStatus.OK);
    }

//----------------------------------------------------------------------------//    
    
}
