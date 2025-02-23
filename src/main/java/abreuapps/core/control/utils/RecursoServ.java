package abreuapps.core.control.utils;

import abreuapps.core.control.general.ConfServ;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Map.entry;

@Service
@RequiredArgsConstructor
public class RecursoServ {
    
    private final ConfServ ConfiguracionServicio;

    @Cacheable("Tiles")
    public Resource getTiles(int zoom, int x, int y) {
        Path path = Paths.get(
                String.format("%s/%s.webp", ConfiguracionServicio.consultar("maptilesdir"), String.format("%d/%d/%d", zoom, x, y))
        );

        if (!Files.exists(path))
            path = Paths.get(ConfiguracionServicio.consultar("maptilesdir")+"/default_tile.webp");

        return new FileSystemResource(path);
    }
    //------------------------------------------------------------------------//
    public String subirArchivo(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newFilename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) 
                + timestamp + originalFilename.substring(originalFilename.lastIndexOf("."));
        newFilename = newFilename.replaceAll("[^a-zA-Z0-9.]", "");
        Path path = Paths.get(ConfiguracionServicio.consultar("filesdir"), newFilename);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException ex) {
            newFilename="";
            Logger.getLogger(RecursoServ.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newFilename;
    }
    
    public Map<String, Object> obtenerArchivo(String archivo){
        try {
            archivo = archivo.replaceAll("[^a-zA-Z0-9.]", "");
            Resource recurso = new UrlResource(
                    Paths
                            .get( ConfiguracionServicio.consultar("filesdir") )
                            .toAbsolutePath()
                            .normalize()
                            .resolve(archivo)
                            .normalize()
                            .toUri()
            );
            if (!recurso.exists())
                return null;

            String contentType = archivo.endsWith(".mp4") ? "video/mp4" : "image/"+archivo.substring(archivo.lastIndexOf("."));
            if( contentType.equals("image/jpg") )
                contentType="image/jpeg";

            return Map.ofEntries(
                    entry("body", recurso),
                    entry("media-type",MediaType.parseMediaType(contentType))
            );

        }catch(MalformedURLException ex){
                return null;
        }
    }
    
}
