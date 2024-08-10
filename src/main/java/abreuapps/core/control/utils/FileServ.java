package abreuapps.core.control.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
public class FileServ {
    
    
    @Value("${abreuapps.core.map.tiles.directory}")
    private String TILE_DIRECTORY; 
    
    @Cacheable("Tiles")
    public byte[] getTilesBytes(int zoom, int x, int y) throws IOException {
        String tileKey = String.format("%d/%d/%d", zoom, x, y);
        String tilePath = String.format("%s/%s.webp", TILE_DIRECTORY, tileKey);
        Path path = Paths.get(tilePath);
        if (!Files.exists(path)) {
            path = Paths.get(TILE_DIRECTORY+"/default_tile.webp");
        }
        return new FileSystemResource(path).getContentAsByteArray();
    }
    
}
