package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.Publicidad;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.ModelServ;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    
    private final DatoServ DatoServicio;
    
    @Value("${abreuapps.core.files.directory}")
    private String FILE_DIRECTORY; 
    
//--------------------------------------------------------------------------------//
//------------------ENDPOINTS PUBLICIDAD------------------------------------------//
//--------------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarPublicidad(
        Model model,
        Publicidad publicidad,
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
        
        if(valido){
            
            Optional<Publicidad> publicidadDB = PublicidadServicio.obtener(publicidad.getId()!=null ? publicidad.getId() : 0 );

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
    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("archivo") MultipartFile file) {
        
        if (file.isEmpty()) {
            return "";
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String newFilename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) 
                    + "_" + timestamp + originalFilename.substring(originalFilename.lastIndexOf("."));

            Path path = Paths.get(FILE_DIRECTORY, newFilename);
            Files.copy(file.getInputStream(), path);

            return newFilename;

        } catch (IOException e) {
            return "";
        }
    }    
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarPublicidad(
        Model model,
        @RequestParam("idPublicidad") Long idPublicidad
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/pub_publicidad_registro :: content-default";
        
        Usuario usuarioLogueado = ModeloServicio.getUsuarioLogueado();

        Optional<Publicidad> publicidadDB = PublicidadServicio.obtener(idPublicidad);

        if (!publicidadDB.isPresent()) {

            //log.error("Error COD: 00637 al editar Publicidad. No encontrado ({})",Publicidad);
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
            
            model.addAttribute("empresas", DatoServicio.consultarPorGrupo("Empresas"));
        }

        return plantillaRespuesta;
    }
//----------------------------------------------------------------------------//
}
