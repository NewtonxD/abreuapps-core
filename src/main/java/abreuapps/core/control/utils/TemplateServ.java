package abreuapps.core.control.utils;


import abreuapps.core.control.general.*;
import abreuapps.core.control.inventario.Producto;
import abreuapps.core.control.inventario.ProductoServ;
import abreuapps.core.control.transporte.*;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.UsuarioServ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServ {
    private final UsuarioServ UsuarioServicio;
    private final ConfServ ConfServicio;
    private final DateUtils dateUtils;
    private final VehiculoServ VehiculoServicio;
    private final ParadaServ ParadaServicio;
    private final RutaServ RutaServicio;
    private final PublicidadServ PublicidadServicio;
    private final ProductoServ ProductoServicio;
    private final abreuapps.core.control.general.DatoRepo DatoRepo;
    private final AccesoServ AccesoServicio;
    public final String NOT_FOUND_TEMPLATE ="redirect:/error";

    private Model dataModel=null;

    public void cargarDatosPagina(String idPage, Model model ){
        this.dataModel=model;
        Map<String,Runnable> actions = Map.ofEntries(
                entry("dat_gen_consulta_datos", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("dat_gen_consulta_datos");
                        dataModel.addAllAttributes(accesos);
                        dataModel.addAttribute("datos", DatoRepo.customFindAll(null,true));
                    } catch (Exception e) {
                        log.error("Error ejecutando 'dat_gen_consulta_datos'", e);
                    }
                }),
                entry("dat_gen_registro_datos", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("dat_gen_registro_datos");
                        dataModel.addAllAttributes(accesos);
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("grupos", DatoRepo.customFindAll(null,false));
                        dataModel.addAttribute("update", false);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'dat_gen_registro_datos'", e);
                    }
                }),
                entry("dat_gen_consulta_empresa", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("dat_gen_consulta_empresa");
                        dataModel.addAllAttributes(accesos);
                        dataModel.addAttribute("datos", DatoRepo.customFindAll("Empresas",false));
                    } catch (Exception e) {
                        log.error("Error ejecutando 'dat_gen_consulta_empresa'", e);
                    }
                }),
                entry("dat_gen_registro_empresa", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("dat_gen_registro_empresa");
                        dataModel.addAllAttributes(accesos);
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("update", false);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'dat_gen_registro_empresa'", e);
                    }
                }),
                entry("usr_mgr_principal", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("usr_mgr_principal");
                        dataModel.addAttribute("usuarios", UsuarioServicio.consultar());
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'usr_mgr_principal'", e);
                    }
                }),
                entry("usr_mgr_registro", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("usr_mgr_registro");
                        dataModel.addAttribute("update", false);
                        dataModel.addAttribute("configuracion", false);
                        Persona p = new Persona();
                        Usuario u = new Usuario();

                        dataModel.addAttribute("dateUtils",dateUtils);
                        u.setPersona(p);
                        dataModel.addAttribute("user", u);
                        dataModel.addAttribute("persona", p);
                        dataModel.addAttribute("sangre", DatoRepo.customFindAll("Tipos Sanguineos",false));
                        dataModel.addAttribute("sexo", DatoRepo.customFindAll("Sexo",false));
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'usr_mgr_registro'", e);
                    }
                }),
                entry("sys_configuracion", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("sys_configuracion");
                        dataModel.addAttribute("conf", ConfServicio.consultar());
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'sys_configuracion'", e);
                    }
                }),
                entry("trp_vehiculo_registro", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("trp_vehiculo_registro");
                        Vehiculo p = new Vehiculo();
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("vehiculo", p);
                        List<DatoDTO> marcas = DatoRepo.customFindAll("Marca",false);
                        dataModel.addAttribute("marca", marcas);
                        dataModel.addAttribute("tipo_vehiculo", DatoRepo.customFindAll("Tipo Vehiculo",false));
                        dataModel.addAttribute("estado", DatoRepo.customFindAll("Estados Vehiculo",false));
                        dataModel.addAttribute("color", DatoRepo.customFindAll("Colores",false));
                        dataModel.addAttribute("modelo",DatoRepo.customFindAll(marcas.get(0).dat(),false ));
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'trp_vehiculo_registro'", e);
                    }
                }),
                entry("trp_vehiculo_consulta", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("trp_vehiculo_consulta");
                        dataModel.addAttribute("vehiculos", VehiculoServicio.consultar());
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'trp_vehiculo_consulta'", e);
                    }
                }),
                entry("trp_paradas_consulta", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("trp_paradas_consulta");
                        dataModel.addAttribute("paradas", ParadaServicio.consultarTodo(null, null));
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'trp_paradas_consulta'", e);
                    }
                }),
                entry("trp_paradas_registro", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("trp_paradas_registro");
                        Parada p = new Parada();
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("parada", p);
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'trp_paradas_registro'", e);
                    }
                }),
                entry("trp_rutas_consulta", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("trp_rutas_consulta");
                        dataModel.addAttribute("rutas", RutaServicio.consultar());
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'trp_rutas_consulta'", e);
                    }
                }),
                entry("trp_rutas_registro", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("trp_rutas_registro");
                        Ruta r = new Ruta();
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("ruta", r);
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'trp_rutas_registro'", e);
                    }
                }),
                entry("pub_publicidad_consulta", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("pub_publicidad_consulta");
                        dataModel.addAttribute("publicidades", PublicidadServicio.consultar());
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'pub_publicidad_consulta'", e);
                    }
                }),
                entry("pub_publicidad_registro", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("pub_publicidad_registro");
                        Publicidad r = new Publicidad();
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("publicidad", r);
                        dataModel.addAttribute("empresas", DatoRepo.customFindAll("Empresas",false));
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'pub_publicidad_registro'", e);
                    }
                }),
                entry("inv_producto_consulta", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("inv_producto_consulta");
                        dataModel.addAttribute("productos", ProductoServicio.consultar());
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'inv_producto_consulta'", e);
                    }
                }),
                entry("inv_producto_registro", () -> {
                    try {
                        var accesos = AccesoServicio.consultarAccesosPantallaUsuario("inv_producto_registro");
                        Producto r = new Producto();
                        dataModel.addAttribute("dateUtils",dateUtils);
                        dataModel.addAttribute("producto", r);
                        dataModel.addAttribute("categorias", DatoRepo.customFindAll("Categoria Producto",false));
                        dataModel.addAllAttributes(accesos);
                    } catch (Exception e) {
                        log.error("Error ejecutando 'inv_producto_registro'", e);
                    }
                })
        );
        actions.getOrDefault(idPage, () -> log.warn("Acción no encontrada: " + idPage)).run();
    }

}
