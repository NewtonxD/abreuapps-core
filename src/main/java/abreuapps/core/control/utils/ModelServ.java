package abreuapps.core.control.utils;

import abreuapps.core.control.general.ConfServ;
import abreuapps.core.control.general.DatoDTO;
import abreuapps.core.control.general.DatoServ;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.transporte.Parada;
import abreuapps.core.control.transporte.ParadaServ;
import abreuapps.core.control.transporte.Ruta;
import abreuapps.core.control.transporte.RutaServ;
import abreuapps.core.control.transporte.Vehiculo;
import abreuapps.core.control.transporte.VehiculoServ;
import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.usuario.UsuarioServ;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *  
 * Servicio para manejar las pantallas usando 1 solo endpoint.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Service
@Slf4j
public class ModelServ {
    
    private final Map<String,Runnable> actions=new ConcurrentHashMap<>();
    
    private Model dataModel=null;
    
    private Integer userId=null;
    
    private final AccesoServ AccesoServicio;
    
    public ModelServ(
            DatoServ DatoServicio,
            AccesoServ AccesoServicio,
            UsuarioServ UsuarioServicio,
            ConfServ ConfServicio,
            VehiculoServ VehiculoServicio,
            ParadaServ ParadaServicio,
            RutaServ RutaServicio
    ){
        this.AccesoServicio = AccesoServicio;
        initActions( DatoServicio,UsuarioServicio, ConfServicio, VehiculoServicio, ParadaServicio, RutaServicio);
    }

    private void initActions(
            DatoServ DatoServicio,
            UsuarioServ UsuarioServicio,
            ConfServ ConfServicio,
            VehiculoServ VehiculoServicio,
            ParadaServ ParadaServicio,
            RutaServ RutaServicio
    ) {

        this.actions.put("dat_gen_consulta_datos", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("datos", DatoServicio.consultar());
            } catch (Exception e) {
                log.error("Error ejecutando 'dat_gen_consulta_datos'", e);
            }
        });

        this.actions.put("dat_gen_registro_datos", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("grupos", DatoServicio.consultarPorGrupo(null));
                dataModel.addAttribute("update", false);
            } catch (Exception e) {
                log.error("Error ejecutando 'dat_gen_registro_datos'", e);
            }
        });

        this.actions.put("usr_mgr_principal", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "usr_mgr_principal");
                dataModel.addAttribute("usuarios", UsuarioServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'usr_mgr_principal'", e);
            }
        });

        this.actions.put("usr_mgr_registro", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "usr_mgr_registro");
                dataModel.addAttribute("update", false);
                Persona p = new Persona();
                Usuario u = new Usuario();
                u.setPersona(p);
                dataModel.addAttribute("user", u);
                dataModel.addAttribute("persona", p);
                dataModel.addAttribute("sangre", DatoServicio.consultarPorGrupo("Tipos Sanguineos"));
                dataModel.addAttribute("sexo", DatoServicio.consultarPorGrupo("Sexo"));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'usr_mgr_registro'", e);
            }
        });

        this.actions.put("sys_configuracion", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "sys_configuracion");
                dataModel.addAttribute("conf", ConfServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'sys_configuracion'", e);
            }
        });

        this.actions.put("trp_vehiculo_consulta", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_vehiculo_consulta");
                dataModel.addAttribute("vehiculos", VehiculoServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_vehiculo_consulta'", e);
            }
        });

        this.actions.put("trp_vehiculo_registro", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_vehiculo_registro");
                Vehiculo p = new Vehiculo();
                dataModel.addAttribute("vehiculo", p);
                List<DatoDTO> marcas = DatoServicio.consultarPorGrupo("Marca");
                dataModel.addAttribute("marca", marcas);
                dataModel.addAttribute("tipo_vehiculo", DatoServicio.consultarPorGrupo("Tipo Vehiculo"));
                dataModel.addAttribute("estado", DatoServicio.consultarPorGrupo("Estados Vehiculo"));
                dataModel.addAttribute("color", DatoServicio.consultarPorGrupo("Colores"));
                dataModel.addAttribute("modelo",DatoServicio.consultarPorGrupo(marcas.get(0).dat() ));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_vehiculo_registro'", e);
            }
        });

        this.actions.put("trp_paradas_consulta", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_paradas_consulta");
                dataModel.addAttribute("paradas", ParadaServicio.consultarTodo(null, null));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_paradas_consulta'", e);
            }
        });

        this.actions.put("trp_paradas_registro", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_paradas_registro");
                Parada p = new Parada();
                dataModel.addAttribute("parada", p);
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_paradas_registro'", e);
            }
        });

        this.actions.put("trp_rutas_consulta", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_rutas_consulta");
                dataModel.addAttribute("rutas", RutaServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_rutas_consulta'", e);
            }
        });

        this.actions.put("trp_rutas_registro", () -> {
            try {
                Map<String, Object> acc = AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_rutas_registro");
                Ruta r = new Ruta();
                dataModel.addAttribute("ruta", r);
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_rutas_registro'", e);
            }
        });
    }
    
    public void load(String idPage,Model model,Integer idUser){
        this.dataModel=model;
        this.userId=idUser;
        
        actions.getOrDefault(idPage, () -> log.warn("Acción no encontrada: " + idPage)).run();
        
    }
    
    public Usuario getUsuarioLogueado(){
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    public String verificarPermisos(String permiso,Model modelo,Usuario usuario){
        Map<String,Object> m=AccesoServicio.consultarAccesosPantallaUsuario(usuario.getId(),permiso);
        String respuesta="";
        if (m.get(permiso) == null || (!(Boolean) m.get(permiso))) {
            if (modelo !=null){
                modelo.addAttribute("status", false);
                modelo.addAttribute("msg", "No tiene permisos para realizar esta acción!");
            }
            respuesta="fragments/"+permiso+" :: content-default";
        }
        return respuesta;
    }
}
