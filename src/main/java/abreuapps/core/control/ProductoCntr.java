package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.TemplateServ;
import abreuapps.core.control.inventario.Producto;
import abreuapps.core.control.inventario.ProductoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.utils.RecursoServ;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/prd")
public class ProductoCntr {

    private final TemplateServ TemplateServicio;

    private final AccesoServ AccesoServicio;
    
    private final ProductoServ ProductoServicio;
    
    private final DatoServ DatoServicio;
    
    private final RecursoServ ResourcesServicio;
    
//--------------------------------------------------------------------------------//
//------------------ENDPOINTS PRODUCTO------------------------------------------//
//--------------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarProducto(
        Model model,
        Producto producto,
        @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {
        if(AccesoServicio.verificarPermisos("inv_producto_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        TemplateServicio.cargarDatosPagina("inv_producto_consulta", model);

        var resultados = ProductoServicio.guardar(producto,fechaActualizacion);
        model.addAttribute("status", resultados.get(0));
        model.addAttribute("msg", resultados.get(1));

        return "fragments/inv_producto_consulta";
    }
    
    //----------------------------------------------------------------------------//
    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("archivo") MultipartFile file) {
        return ResourcesServicio.subirArchivo(file);
    }    
    
    //--------------------------------------------------------------------------
    @GetMapping(value="/archivo/{nombre}")
    public ResponseEntity<Resource> consultarArchivoActualPublicidad(
            @PathVariable("nombre") String nombre
    ){
        var archivo = ResourcesServicio.obtenerArchivo(URLDecoder.decode(nombre, StandardCharsets.UTF_8) );
        return (! archivo.equals(null)) ?
                ResponseEntity.ok()
                        .contentType( (MediaType) archivo.get("media-type") )
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + ((Resource)archivo.get("body")).getFilename() + "\"")
                        .body((Resource)archivo.get("body")) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarPublicidad(
        Model model,
        @RequestParam("idProducto") Long idProducto
    ) {
        if(! AccesoServicio.verificarPermisos("inv_producto_registro"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        var ProductoDB = ProductoServicio.obtener(idProducto);

        if (!ProductoDB.isPresent())
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("producto", ProductoDB.get());
        model.addAllAttributes( AccesoServicio.consultarAccesosPantallaUsuario("inv_producto_registro"));
        model.addAttribute("categorias", DatoServicio.consultarPorGrupo("Categoria Producto"));

        return "fragments/inv_producto_registro";
    }
//----------------------------------------------------------------------------//
    
}
