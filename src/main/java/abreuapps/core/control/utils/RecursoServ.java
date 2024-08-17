package abreuapps.core.control.utils;

import abreuapps.core.control.general.ConfServ;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecursoServ {
    
    private final ConfServ ConfiguracionServicio;
    
    //-------------------------------------------------------------------------//
    @Cacheable("Tiles")
    public byte[] getTilesBytes(int zoom, int x, int y) {
        String tileKey = String.format("%d/%d/%d", zoom, x, y);
        String tilePath = String.format("%s/%s.webp", ConfiguracionServicio.consultar("maptilesdir"), tileKey);
        Path path = Paths.get(tilePath);
        if (!Files.exists(path)) {
            path = Paths.get(ConfiguracionServicio.consultar("maptilesdir")+"/default_tile.webp");
        }
        try {
            return new FileSystemResource(path).getContentAsByteArray();
        } catch (IOException ex) {
            Logger.getLogger(RecursoServ.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //------------------------------------------------------------------------//
    public String uploadFile(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newFilename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) 
                + timestamp + originalFilename.substring(originalFilename.lastIndexOf("."));
        newFilename = newFilename.replaceAll("[^a-zA-Z0-9.]", "");
        Path path = Paths.get(ConfiguracionServicio.consultar("maptilesdir"), newFilename);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException ex) {
            newFilename="";
            Logger.getLogger(RecursoServ.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newFilename;
    }
    
}
