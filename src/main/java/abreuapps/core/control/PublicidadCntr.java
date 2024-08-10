package abreuapps.core.control;

import abreuapps.core.control.general.Publicidad;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.ModelServ;
import java.text.ParseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/pub")
public class PublicidadCntr {
    
    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;

    private final ModelServ ModeloServicio;
    
    private final PublicidadServ PublicidadServicio;
    
    @Value("${abreuapps.core.files.directory}")
    private String FILE_DIRECTORY; 
    
    //----------------------------------------------------------------------------//
//------------------ENDPOINTS PARADAS-----------------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarParada(
        Model model,
        Publicidad publicidad,
        @RequestParam(name="archivo", 
                        required = false) MultipartFile archivo,
        @RequestParam(name = "fecha_actualizacionn", 
                        required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        
        String plantillaRespuesta="fragments/pub_publicidad_consulta :: content-default";
        
        Usuario u = ModeloServicio.getUsuarioLogueado();
        
        //INICIO DE VALIDACIONES
        String sinPermisoPlantilla = ModeloServicio.verificarPermisos(
            "pub_publicidad_consulta", model, u );
        
        //USUARIO NO TIENE PERMISOS PARA EJECUTAR ESTA ACCION
        valido = sinPermisoPlantilla.equals("");
        
        /*
        
            if (file.isEmpty()) {
                return "File is empty";
            }

            try {
                // Generate a new file name with timestamp to avoid duplication
                String originalFilename = file.getOriginalFilename();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String newFilename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) 
                        + "_" + timestamp + originalFilename.substring(originalFilename.lastIndexOf("."));

                // Create path for the new file
                Path path = Paths.get(UPLOAD_DIR, newFilename);

                // Move file to the specific folder
                Files.copy(file.getInputStream(), path);

                // Save the file path in a variable
                String savedFilePath = path.toString();

                return "File uploaded successfully: " + savedFilePath;

            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to upload file";
            }
        
        */
        
        if(valido){
            
            Optional<Publicidad> publicidadDB = PublicidadServicio.obtener(publicidad.getId());

            if (publicidadDB.isPresent()) {

                if (! FechaUtils.FechaFormato2.format(
                        publicidadDB.get().getFecha_actualizacion()
                        ).equals(fechaActualizacionCliente)
                ) {
                    
                    model.addAttribute(
                        "msg",
                        ! ( fechaActualizacionCliente == null || 
                             fechaActualizacionCliente.equals("") ) ? 
                        "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00686" :
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
                    publicidad.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente)
                    );
                }
                
                if (publicidadDB.isPresent()) {
                    publicidad.setFecha_registro(publicidadDB.get().getFecha_registro());
                    publicidad.setHecho_por(publicidadDB.get().getHecho_por());
                }

                PublicidadServicio.guardar(publicidad, u, publicidadDB.isPresent());
                model.addAttribute("msg", "Registro guardado exitosamente!");

                ModeloServicio.load("pub_publicidad_consulta", model, u.getId());
            }
            
            model.addAttribute("status", valido);
        }

        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }    
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarParada(
        Model model,
        @RequestParam("idPublicidad") Long idPublicidad
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/pub_publicidad_registro :: content-default";
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Publicidad> publicidadDB = PublicidadServicio.obtener(idPublicidad);

        if (!publicidadDB.isPresent()) {

            //log.error("Error COD: 00637 al editar parada. No encontrado ({})",idParada);
            plantillaRespuesta = "redirect:/error";
            valido=false;

        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){
            model.addAttribute("publicidad", publicidadDB.get());
            
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                            usuarioLogueado.getId(), "pub_publicidad_registro" )
            );
        }

        return plantillaRespuesta;
    }
//----------------------------------------------------------------------------//
}
