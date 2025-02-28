package abreuapps.core.control;

import abreuapps.core.control.general.Dato;
import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.utils.TemplateServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.utils.DateUtils;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author cabreu
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/dtgnr")
public class DatosCntr {
    
    private final DateUtils FechaUtils;

    private final TemplateServ TemplateServicio;
    
    private final AccesoServ AccesoServicio;
    
    private final DatoServ DatoServicio;
    
//----------------------------------------------------------------------------//
//---------------------------ENDPOINTS DATOS----------------------------------//
//----------------------------------------------------------------------------//
    @PostMapping("/save")
    public String GuardarDatoGeneral(
        Model model, 
        Dato dtgnr,
        @RequestParam(value = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {

        if(!AccesoServicio.verificarPermisos("dat_gen_registro_datos"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        var respuesta=DatoServicio.guardar(dtgnr, fechaActualizacion);
        model.addAttribute("status", respuesta.get(0));
        model.addAttribute("msg", respuesta.get(1));

        TemplateServicio.cargarDatosPagina("dat_gen_consulta_datos", model);

        return "fragments/dat_gen_consulta_datos";
    }
//----------------------------------------------------------------------------//

    @PostMapping("/update")
    public String ActualizarDatosGenerales(
        Model model,
        String idDato
    ) {

        if(! AccesoServicio.verificarPermisos("dat_gen_registro_datos"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        Optional<Dato> datoDB = DatoServicio.obtener(idDato);

        if (! datoDB.isPresent())
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("dato", datoDB.get());
        model.addAttribute("update", true);
        model.addAttribute("dateUtils",FechaUtils);
        model.addAttribute("grupos", DatoServicio.consultarPorGrupo(null));
        model.addAllAttributes(
                AccesoServicio.consultarAccesosPantallaUsuario("dat_gen_registro_datos")
        );

        return "fragments/dat_gen_registro_datos";
    }

    @PostMapping("/emp/save")
    public String GuardarDatoEmpresa(
        Model model, 
        Dato dtgnr,
        @RequestParam(value = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {
        
        if(!AccesoServicio.verificarPermisos("dat_gen_registro_empresa"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;
        
        var respuesta=DatoServicio.guardar(dtgnr, fechaActualizacion);
        model.addAttribute("status", respuesta.get(0));
        model.addAttribute("msg", respuesta.get(1));

        TemplateServicio.cargarDatosPagina("dat_gen_consulta_empresa", model);

        return "fragments/dat_gen_consulta_empresa";
    }
//----------------------------------------------------------------------------//

    @PostMapping("/emp/update")
    public String ActualizarDatosEmpresa(
        Model model,
        String idDato
    ) {

        if(! AccesoServicio.verificarPermisos("dat_gen_registro_empresa"))
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        Optional<Dato> datoDB = DatoServicio.obtener(idDato);

        if (! datoDB.isPresent())
            return TemplateServicio.NOT_FOUND_TEMPLATE;

        model.addAttribute("dato", datoDB.get());
        model.addAttribute("update", true);
        model.addAttribute("dateUtils", FechaUtils);
        model.addAllAttributes( AccesoServicio.consultarAccesosPantallaUsuario("dat_gen_registro_empresa"));

        return "fragments/dat_gen_registro_empresa";

    }

}
