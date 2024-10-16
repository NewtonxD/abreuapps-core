package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.inventario.Producto;
import abreuapps.core.control.inventario.ProductoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.utils.RecursoServ;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;
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
    
    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;
    
    private final ProductoServ ProductoServicio;
    
    private final DatoServ DatoServicio;
    
    private final RecursoServ ResourcesServicio;
    
//--------------------------------------------------------------------------------//
//------------------ENDPOINTS PUBLICIDAD------------------------------------------//
//--------------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarPublicidad(
        Model model,
        Producto producto,
        @RequestParam(name = "fecha_actualizacionn", 
                        required = false) String fechaActualizacionCliente
    ) throws ParseException {

        boolean valido;
        
        String plantillaRespuesta="fragments/inv_producto_consulta :: content-default";
        
        Usuario u = AccesoServicio.getUsuarioLogueado();
        
        //INICIO DE VALIDACIONES
        String sinPermisoPlantilla = AccesoServicio.verificarPermisos(
            "inv_producto_consulta", model, u );
        
        //USUARIO NO TIENE PERMISOS PARA EJECUTAR ESTA ACCION
        valido = sinPermisoPlantilla.equals("");
        
        if(valido){
            
            Optional<Producto> ProductoDB = ProductoServicio.obtener(producto.getId()!=null ? producto.getId() : 0 );

            if (ProductoDB.isPresent()) {
                

                if (! FechaUtils.FechaFormato2.format(
                        ProductoDB.get().getFecha_actualizacion()
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
                    producto.setFecha_actualizacion(
                        FechaUtils.Formato2ToDate(fechaActualizacionCliente)
                    );
                }
                
                if (ProductoDB.isPresent()) {
                    producto.setFecha_registro(ProductoDB.get().getFecha_registro());
                    producto.setHecho_por(ProductoDB.get().getHecho_por());
                }

                ProductoServicio.guardar(producto, u, ProductoDB.isPresent());
                model.addAttribute("msg", "Registro guardado exitosamente!");

                AccesoServicio.cargarPagina("inv_producto_consulta", model, u.getId());
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
    
    //--------------------------------------------------------------------------
    @GetMapping(value="/archivo/{nombre}")
    public ResponseEntity<Resource> consultarArchivoActualPublicidad(@PathVariable("nombre") String nombre ){
        Map<String,Object> archivo=ProductoServicio.obtenerArchivoProducto(URLDecoder.decode(nombre, StandardCharsets.UTF_8) );
        if(archivo!=null)
            return ResponseEntity.ok()
                        .contentType( (MediaType) archivo.get("media-type") )
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + ((Resource)archivo.get("body")).getFilename() + "\"")
                        .body((Resource)archivo.get("body"));
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
//----------------------------------------------------------------------------//
    @PostMapping("/update")
    public String ActualizarPublicidad(
        Model model,
        @RequestParam("idProducto") Integer idProducto
    ) {
        
        boolean valido=true;
        String plantillaRespuesta="fragments/inv_producto_registro :: content-default";
        
        Usuario usuarioLogueado = AccesoServicio.getUsuarioLogueado();

        Optional<Producto> ProductoDB = ProductoServicio.obtener(idProducto);

        if (!ProductoDB.isPresent()) {

            //log.error("Error COD: 00637 al editar Publicidad. No encontrado ({})",Publicidad);
            plantillaRespuesta = "redirect:/error";
            valido=false;

        }
        
        //SI TODAS LAS ANTERIORES SON VALIDAS PROCEDEMOS
        if(valido){
            model.addAttribute("producto", ProductoDB.get());
            
            model.addAllAttributes(
                    AccesoServicio.consultarAccesosPantallaUsuario(
                            usuarioLogueado.getId(), "inv_producto_registro" )
            );
            
            model.addAttribute("categorias", DatoServicio.consultarPorGrupo("Categoria Producto"));
        }

        return plantillaRespuesta;
    }
//----------------------------------------------------------------------------//
    
}
