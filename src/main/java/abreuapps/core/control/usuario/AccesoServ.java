package abreuapps.core.control.usuario;

import abreuapps.core.control.general.ConfServ;
import abreuapps.core.control.general.Dato;
import abreuapps.core.control.general.DatoDTO;
import abreuapps.core.control.general.DatoRepo;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.general.Publicidad;
import abreuapps.core.control.general.PublicidadServ;
import abreuapps.core.control.inventario.Producto;
import abreuapps.core.control.inventario.ProductoServ;
import abreuapps.core.control.transporte.Parada;
import abreuapps.core.control.transporte.ParadaServ;
import abreuapps.core.control.transporte.Ruta;
import abreuapps.core.control.transporte.RutaServ;
import abreuapps.core.control.transporte.Vehiculo;
import abreuapps.core.control.transporte.VehiculoServ;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import static java.util.Map.entry;

/**
 *
 * @author cabreu
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AccesoServ {

    private final UsuarioServ UsuarioServicio;
    private final ConfServ ConfServicio;
    private final VehiculoServ VehiculoServicio;
    private final ParadaServ ParadaServicio;
    private final RutaServ RutaServicio;
    private final PublicidadServ PublicidadServicio;
    private final  ProductoServ ProductoServicio;
    private final AccesoUsuarioRepo AccesoUsuarioRepo; //repo
    private final AccesoRepo AccesoRepo;
    private final DatoRepo DatoRepo;
    public final String NOT_AUTORIZED_TEMPLATE="redirect:/error";
    
    private Model dataModel=null;

    private final Map<String,Runnable> actions = Map.ofEntries(
            entry("dat_gen_consulta_datos", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("dat_gen_consulta_datos");
                    dataModel.addAllAttributes(acc);
                    dataModel.addAttribute("datos", DatoRepo.customFindAll(null,true));
                } catch (Exception e) {
                    log.error("Error ejecutando 'dat_gen_consulta_datos'", e);
                }
            }),
            entry("dat_gen_registro_datos", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("dat_gen_registro_datos");
                    dataModel.addAllAttributes(acc);
                    dataModel.addAttribute("grupos", DatoRepo.customFindAll(null,false));
                    dataModel.addAttribute("update", false);
                } catch (Exception e) {
                    log.error("Error ejecutando 'dat_gen_registro_datos'", e);
                }
            }),
            entry("dat_gen_consulta_empresa", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("dat_gen_consulta_empresa");
                    dataModel.addAllAttributes(acc);
                    dataModel.addAttribute("datos", DatoRepo.customFindAll("Empresas",false));
                } catch (Exception e) {
                    log.error("Error ejecutando 'dat_gen_consulta_empresa'", e);
                }
            }),
            entry("dat_gen_registro_empresa", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("dat_gen_registro_empresa");
                    dataModel.addAllAttributes(acc);
                    dataModel.addAttribute("update", false);
                } catch (Exception e) {
                    log.error("Error ejecutando 'dat_gen_registro_empresa'", e);
                }
            }),
            entry("usr_mgr_principal", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("usr_mgr_principal");
                    dataModel.addAttribute("usuarios", UsuarioServicio.consultar());
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'usr_mgr_principal'", e);
                }
            }),
            entry("usr_mgr_registro", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("usr_mgr_registro");
                    dataModel.addAttribute("update", false);
                    Persona p = new Persona();
                    Usuario u = new Usuario();
                    u.setPersona(p);
                    dataModel.addAttribute("user", u);
                    dataModel.addAttribute("persona", p);
                    dataModel.addAttribute("sangre", DatoRepo.customFindAll("Tipos Sanguineos",false));
                    dataModel.addAttribute("sexo", DatoRepo.customFindAll("Sexo",false));
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'usr_mgr_registro'", e);
                }
            }),
            entry("sys_configuracion", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("sys_configuracion");
                    dataModel.addAttribute("conf", ConfServicio.consultar());
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'sys_configuracion'", e);
                }
            }),
            entry("trp_vehiculo_registro", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("trp_vehiculo_registro");
                    Vehiculo p = new Vehiculo();
                    dataModel.addAttribute("vehiculo", p);
                    List<DatoDTO> marcas = DatoRepo.customFindAll("Marca",false);
                    dataModel.addAttribute("marca", marcas);
                    dataModel.addAttribute("tipo_vehiculo", DatoRepo.customFindAll("Tipo Vehiculo",false));
                    dataModel.addAttribute("estado", DatoRepo.customFindAll("Estados Vehiculo",false));
                    dataModel.addAttribute("color", DatoRepo.customFindAll("Colores",false));
                    dataModel.addAttribute("modelo",DatoRepo.customFindAll(marcas.get(0).dat(),false ));
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'trp_vehiculo_registro'", e);
                }
            }),
            entry("trp_vehiculo_consulta", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("trp_vehiculo_consulta");
                    dataModel.addAttribute("vehiculos", VehiculoServicio.consultar());
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'trp_vehiculo_consulta'", e);
                }
            }),
            entry("trp_paradas_consulta", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("trp_paradas_consulta");
                    dataModel.addAttribute("paradas", ParadaServicio.consultarTodo(null, null));
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'trp_paradas_consulta'", e);
                }
            }),
            entry("trp_paradas_registro", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("trp_paradas_registro");
                    Parada p = new Parada();
                    dataModel.addAttribute("parada", p);
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'trp_paradas_registro'", e);
                }
            }),
            entry("trp_rutas_consulta", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("trp_rutas_consulta");
                    dataModel.addAttribute("rutas", RutaServicio.consultar());
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'trp_rutas_consulta'", e);
                }
            }),
            entry("trp_rutas_registro", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("trp_rutas_registro");
                    Ruta r = new Ruta();
                    dataModel.addAttribute("ruta", r);
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'trp_rutas_registro'", e);
                }
            }),
            entry("pub_publicidad_consulta", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("pub_publicidad_consulta");
                    dataModel.addAttribute("publicidades", PublicidadServicio.consultar());
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'pub_publicidad_consulta'", e);
                }
            }),
            entry("pub_publicidad_registro", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("pub_publicidad_registro");
                    Publicidad r = new Publicidad();
                    dataModel.addAttribute("publicidad", r);
                    dataModel.addAttribute("empresas", DatoRepo.customFindAll("Empresas",false));
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'pub_publicidad_registro'", e);
                }
            }),
            entry("inv_producto_consulta", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("inv_producto_consulta");
                    dataModel.addAttribute("productos", ProductoServicio.consultar());
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'inv_producto_consulta'", e);
                }
            }),
            entry("inv_producto_registro", () -> {
                try {
                    Map<String, Object> acc = consultarAccesosPantallaUsuario("inv_producto_registro");
                    Producto r = new Producto();
                    dataModel.addAttribute("producto", r);
                    dataModel.addAttribute("categorias", DatoRepo.customFindAll("Categoria Producto",false));
                    dataModel.addAllAttributes(acc);
                } catch (Exception e) {
                    log.error("Error ejecutando 'inv_producto_registro'", e);
                }
            })
    );
    
    @Cacheable("PermisosMenu")
    public Map<String, Boolean> consultarAccesosMenuUsuario(){
        List<Object[]> results=AccesoUsuarioRepo.ListadoMenuUsuario(getUsuarioLogueado().getId());
        Map<String, Boolean> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Boolean valor = (Boolean) result[1];
            convert.put(nombre, valor);
        }
        
        return convert;
    }
    
    @Cacheable("PermisosPantalla")
    public Map<String, Object> consultarAccesosPantallaUsuario(String pantalla){
        List<Object[]> results=AccesoUsuarioRepo.ListadoAccesosPantallaUsuario(getUsuarioLogueado().getId(),pantalla);
        Map<String, Object> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Object valor = result[1];
            convert.put(nombre, valor);
        }
        
        return convert;
    }
    
    public List<Object[]> ListadoAccesosUsuarioEditar(int idUsuario){
        return AccesoUsuarioRepo.ListadoAccesosUsuarioEditar(idUsuario);
    }
    
      
    @Transactional  
    @CacheEvict(value ={"PermisosPantalla","PermisosMenu"}, allEntries = true)
    public void GuardarTodosMap( Map<String,String> accesos, Usuario usuario){
        List<AccesoUsuario> listaAccesoNuevo = new ArrayList<>();
        List<AccesoUsuario> listaAccesoEdicion = new ArrayList<>();
        for (String key : accesos.keySet()) {
            //si el acceso existe crear acceso y agregar
            Optional<Dato> dato=DatoRepo.findById(key);
            if(dato.isPresent()){
                Optional<Acceso> acc=AccesoRepo.findByPantalla(dato.get());
                
                if(acc.isPresent()){
                    
                    Optional<AccesoUsuario> accUsr= AccesoUsuarioRepo.findAllByUsuarioAndAcceso(
                        usuario,
                        acc.get()
                    );
                    
                    if(accUsr.isPresent()){
                        //editando
                        AccesoUsuario AccUsr=accUsr.get();
                        AccUsr.setActivo(true);
                        AccUsr.setValor(
                            accesos.get(key).equals("on")?"true":(accesos.get(key).equals("off")?"false":accesos.get(key))
                        );
                        listaAccesoEdicion.add(AccUsr);
                    }else{
                        //nuevo
                        listaAccesoNuevo.add(
                            new AccesoUsuario(
                                null,
                                accesos.get(key).equals("on")?"true":(accesos.get(key).equals("off")?"false":accesos.get(key)),
                                true,
                                usuario,
                                acc.get()
                            )
                        );
                        
                    }
                    
                }
            }
        }

        AccesoUsuarioRepo.saveAll(listaAccesoEdicion);
        AccesoUsuarioRepo.saveAll(listaAccesoNuevo);
    }
    
    public void cargarPagina( String idPage, Model model ){
        this.dataModel=model;
        actions.getOrDefault(idPage, () -> log.warn("Acción no encontrada: " + idPage)).run();
    }
    
    public Usuario getUsuarioLogueado(){
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean verificarPermisos( String permiso ){
        Map<String,Object> m=consultarAccesosPantallaUsuario(permiso);
        return m.containsKey(permiso) ? (Boolean) m.get(permiso) : false;
    }
    
}
