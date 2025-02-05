package abreuapps.core.control;

import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.UsuarioServ;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cabreu
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/usrmgr")
public class UsuariosCntr {

    private final AccesoServ AccesoServicio;

    private final DatoServ DatoServicio;

    private final UsuarioServ UsuarioServicio;

//----------------------------------------------------------------------------//
//-------------------------ENDPOINTS USUARIOS---------------------------------//
//----------------------------------------------------------------------------//

    @PostMapping(value = "/save")
    public String GuardarUsuario(
            Model model,
            Usuario usuario,
            @RequestParam("idPersona") Integer idPersona,
            @RequestParam(name = "fecha_actualizacionn", required = false) String fechaActualizacion
    ) {
        if (!AccesoServicio.verificarPermisos("usr_mgr_registro") ||
                !usuario.getId().equals(AccesoServicio.getUsuarioLogueado().getId())
        ) return AccesoServicio.NOT_AUTORIZED_TEMPLATE;

        model.addAttribute("status", UsuarioServicio.guardar(usuario, idPersona, fechaActualizacion, model));
        AccesoServicio.cargarPagina("usr_mgr_principal", model);

        return usuario.getId().equals(AccesoServicio.getUsuarioLogueado().getId()) // mismo usuario logueado que actualizado
                ? "redirect:/main/index" : "fragments/usr_mgr_principal :: content-default";
    }
//----------------------------------------------------------------------------//


    @PostMapping("/update")
    public String ActualizarUsuario(
            Model model,
            String idUsuario
    ) {
        if (!AccesoServicio.verificarPermisos("usr_mgr_registro"))
            return AccesoServicio.NOT_AUTORIZED_TEMPLATE;

        Optional<Usuario> usuario = UsuarioServicio.obtener(idUsuario);

        if (!usuario.isPresent()) return "redirect:/error";

        model.addAttribute("user", usuario.get());
        model.addAttribute("persona", usuario.get().getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo", DatoServicio.consultarPorGrupo("Sexo"));
        model.addAttribute("sangre", DatoServicio.consultarPorGrupo("Tipos Sanguineos"));
        model.addAllAttributes(AccesoServicio.consultarAccesosPantallaUsuario("usr_mgr_registro"));

        return "fragments/usr_mgr_registro :: content-default";
    }
//----------------------------------------------------------------------------//

    @GetMapping("/myupdate")
    public String ActualizarMiUsuario(
            Model model
    ) {

        Usuario usuarioLogueado = AccesoServicio.getUsuarioLogueado();

        model.addAttribute("user", usuarioLogueado);
        model.addAttribute("persona", usuarioLogueado.getPersona());
        model.addAttribute("update", true);
        model.addAttribute("sexo", DatoServicio.consultarPorGrupo("Sexo"));
        model.addAttribute("sangre", DatoServicio.consultarPorGrupo("Tipos Sanguineos"));
        model.addAttribute("usr_mgr_registro", true);
        model.addAttribute("configuracion", true);

        return "fragments/usr_mgr_registro :: content-default";
    }
//----------------------------------------------------------------------------//


    @PostMapping("/vfyUsr")
    @ResponseBody
    public boolean VerificarUsuario(
            @RequestParam("username") String nombreUsuario
    ) {
        return !UsuarioServicio.obtener(nombreUsuario).isPresent();
    }
//----------------------------------------------------------------------------//


    @PostMapping("/vfyMail")
    @ResponseBody
    public boolean VerificarCorreo(
            @RequestParam("correo") String correo
    ) {
        return !UsuarioServicio.obtenerPorCorreo(correo).isPresent();
    }
//----------------------------------------------------------------------------//

    @PostMapping("/vfyPwd")
    @ResponseBody
    public boolean VerificarPassword(
            @RequestParam("pwd") String password
    ) {
        return UsuarioServicio.coincidenPassword(password, AccesoServicio.getUsuarioLogueado().getId());
    }
//----------------------------------------------------------------------------//


    @PostMapping(value = "/closeUsrSess")
    public String CerrarSesionUsuario(
            Model model,
            String nombreUsuario
    ) {

        if (!AccesoServicio.verificarPermisos("usr_mgr_registro"))
            return AccesoServicio.NOT_AUTORIZED_TEMPLATE;

        UsuarioServicio.cerrarSesion(nombreUsuario);
        model.addAttribute("status", true);
        model.addAttribute("msg", "Sesión Cerrada Exitosamente!");
        AccesoServicio.cargarPagina("usr_mgr_principal", model);
        return "fragments/usr_mgr_principal :: content-default";

    }
//----------------------------------------------------------------------------//

    @PostMapping(value = "/resetPwd")
    public String ResetearContraseña(
            Model model,
            String nombreUsuario
    ) {
        if (!AccesoServicio.verificarPermisos("usr_mgr_registro"))
            return AccesoServicio.NOT_AUTORIZED_TEMPLATE;

        Optional<Usuario> usuarioBD = UsuarioServicio.obtener(nombreUsuario);

        if (!usuarioBD.isPresent()) return "redirect:/error";

        UsuarioServicio.cerrarSesion(nombreUsuario);
        UsuarioServicio.cambiarPassword(usuarioBD.get(), UsuarioServicio.generarPassword(), true);
        model.addAttribute("status", true);
        model.addAttribute("msg", "Contraseña Reseteada Exitosamente! Comuniquese con el usuario para que revise su correo.");
        AccesoServicio.cargarPagina("usr_mgr_principal", model);
        return "fragments/usr_mgr_principal :: content-default";

    }

}
