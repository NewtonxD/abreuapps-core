package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.Publicidad;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.utils.TemplateServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.RecursoServ;
import abreuapps.core.control.utils.ReporteServ;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/pub")
public class PublicidadCntr {

    private final AccesoServ AccesoServicio;
    
    private final PublicidadServ PublicidadServicio;

    private final DateUtils FechaUtils;
    
    private final DatoServ DatoServicio;
    
    private final RecursoServ ResourcesServicio;
    
    private final ReporteServ ReporteServicio;

    private final TemplateServ TemplateServicio;
    
//--------------------------------------------------------------------------------//
//------------------ENDPOINTS PUBLICIDAD------------------------------------------//
//--------------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarPublicidad(
        Model model,
        Publicidad publicidad,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {

        if(! AccesoServicio.verificarPermisos("pub_publicidad_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;


        TemplateServicio.cargarDatosPagina("pub_publicidad_consulta", model);

        var resultados = PublicidadServicio.guardar(publicidad,fechaActualizacion);
        model.addAttribute("status", resultados.get(0));
        model.addAttribute("msg", resultados.get(1));

        return "fragments/pub_publicidad_consulta";

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

        if(! AccesoServicio.verificarPermisos("pub_publicidad_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        Optional<Publicidad> publicidadDB = PublicidadServicio.obtener(idPublicidad);

        if (!publicidadDB.isPresent())
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("dateUtils",FechaUtils);
        model.addAttribute("publicidad", publicidadDB.get());
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario("pub_publicidad_registro" ));
        model.addAttribute("empresas", DatoServicio.consultarPorGrupo("Empresas"));


        return "fragments/pub_publicidad_registro";
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
