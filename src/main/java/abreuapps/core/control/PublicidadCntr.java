package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.Publicidad;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.RecursoServ;
import abreuapps.core.control.utils.ReporteServ;
import abreuapps.core.control.utils.TipoReporte;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    private final PublicidadServ PublicidadServicio;
    
    private final DatoServ DatoServicio;
    
    private final RecursoServ ResourcesServicio;
    
    private final ReporteServ ReporteServicio;
    
//--------------------------------------------------------------------------------//
//------------------ENDPOINTS PUBLICIDAD------------------------------------------//
//--------------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarPublicidad(
        Model model,
        Publicidad publicidad,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacionCliente
    ) {
        
        String plantillaRespuesta="fragments/pub_publicidad_consulta :: content-default";
        
        Usuario u = AccesoServicio.getUsuarioLogueado();
        
        //INICIO DE VALIDACIONES
        String sinPermisoPlantilla = AccesoServicio.verificarPermisos(
            "pub_publicidad_consulta", model);
        
        //USUARIO NO TIENE PERMISOS PARA EJECUTAR ESTA ACCION
        boolean valido = sinPermisoPlantilla.equals("");

        if(valido){
            
            Optional<Publicidad> publicidadDB = PublicidadServicio.obtener(publicidad.getId()!=null ? publicidad.getId() : 0 );

            if (publicidadDB.isPresent()) {
                
                publicidad.setConteo_clic(publicidadDB.get().getConteo_clic());
                publicidad.setConteo_view(publicidadDB.get().getConteo_view());

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

                AccesoServicio.cargarPagina("pub_publicidad_consulta", model);
            }
            
            model.addAttribute("status", valido);
        }

        return sinPermisoPlantilla.equals("") ? plantillaRespuesta : sinPermisoPlantilla;

    }
    
    //----------------------------------------------------------------------------//
    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("archivo") MultipartFile file) {
        return ResourcesServicio.subirArchivo(file);
    }    
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarPublicidad(
        Model model,
        @RequestParam("idPublicidad") Long idPublicidad
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/pub_publicidad_registro :: content-default";

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
                    AccesoServicio.consultarAccesosPantallaUsuario("pub_publicidad_registro" )
            );
            
            model.addAttribute("empresas", DatoServicio.consultarPorGrupo("Empresas"));
        }

        return plantillaRespuesta;
    }
//----------------------------------------------------------------------------//
    
    /*@GetMapping("/report/pub_gen")
    public ResponseEntity<Resource> employeeJasperReport24(@RequestParam("fileType") String fileType){

        TipoReporte report = new TipoReporte(fileType);
        ByteArrayResource resource = new ByteArrayResource(ReporteServicio.employeeJasperReportInBytes(fileType)); 
        String fileName = "Reporte_General_Publicidad_" + LocalDateTime.now() + report.getExtension();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        
    }*/
}
