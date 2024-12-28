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
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author cabreu
 */

@Slf4j
@Service
public class AccesoServ {
    
    private final AccesoUsuarioRepo repo;
    
    private final AccesoRepo accRepo;
    
    private final DatoRepo dataRepo;
    
    private final Map<String,Runnable> actions=new ConcurrentHashMap<>();
    
    private Model dataModel=null;
    
    private Integer userId=null;
    
    private Object convertirValor(Object valor) {
        if (valor instanceof String stringValue) {
            
            if(!stringValue.contains(",")){
                
                if (stringValue.equalsIgnoreCase("on") ||stringValue.equalsIgnoreCase("off") ||stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")) {
                    return Boolean.valueOf(stringValue);
                } else if (stringValue.matches("-?\\d+")) {

                    if (stringValue.contains(".")) 
                        return Float.valueOf(stringValue);
                    else 
                        return Integer.valueOf(stringValue);

                }
            }
        }
        return valor;
    }
    
    @Cacheable("PermisosMenu")
    public Map<String, Boolean> consultarAccesosMenuUsuario(Integer id_usuario){
        List<Object[]> results=repo.ListadoMenuUsuario(id_usuario);
        Map<String, Boolean> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Boolean valor = (Boolean) convertirValor(result[1]);
            convert.put(nombre, valor);
        }
        
        return convert;
    }
    
    @Cacheable("PermisosPantalla")
    public Map<String, Object> consultarAccesosPantallaUsuario(Integer id_usuario,String pantalla){
        List<Object[]> results=repo.ListadoAccesosPantallaUsuario(id_usuario,pantalla);
        Map<String, Object> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Object valor = convertirValor(result[1]);
            convert.put(nombre, valor);
        }
        
        return convert;
    }
    
    public List<Object[]> ListadoAccesosUsuarioEditar(int idUsuario){
        return repo.ListadoAccesosUsuarioEditar(idUsuario);
    }
    
      
    @Transactional  
    @CacheEvict(value ={"PermisosUsuario","PermisosPantalla","PermisosMenu"}, allEntries = true)
    public void GuardarTodosMap(Map<String,String> accesos,Usuario usuario){
        List<AccesoUsuario> listaAccesoNuevo = new ArrayList<>();
        List<AccesoUsuario> listaAccesoEdicion = new ArrayList<>();
        for (String key : accesos.keySet()) {
            //si el acceso existe crear acceso y agregar
            Optional<Dato> dato=dataRepo.findById(key);
            if(dato.isPresent()){
                Optional<Acceso> acc=accRepo.findByPantalla(dato.get());
                
                if(acc.isPresent()){
                    
                    Optional<AccesoUsuario> accUsr= repo.findAllByUsuarioAndAcceso(
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
        
        repo.saveAll(listaAccesoEdicion);
        repo.saveAll(listaAccesoNuevo);
    }
    
    public AccesoServ(
            UsuarioServ UsuarioServicio,
            ConfServ ConfServicio,
            VehiculoServ VehiculoServicio,
            ParadaServ ParadaServicio,
            RutaServ RutaServicio,
            PublicidadServ PublicidadServicio,
            ProductoServ ProductoServicio,
            AccesoUsuarioRepo repo,
            AccesoRepo accRepo,
            DatoRepo dataRepo
    ){
        this.repo=repo;
        this.dataRepo=dataRepo;
        this.accRepo=accRepo;
        initActions( 
            UsuarioServicio, 
            ConfServicio, 
            VehiculoServicio, 
            ParadaServicio, 
            RutaServicio,
            PublicidadServicio,
            ProductoServicio
        );
    }

    private void initActions(
            UsuarioServ UsuarioServicio,
            ConfServ ConfServicio,
            VehiculoServ VehiculoServicio,
            ParadaServ ParadaServicio,
            RutaServ RutaServicio,
            PublicidadServ PublicidadServicio,
            ProductoServ ProductoServicio
    ) {

        this.actions.put("dat_gen_consulta_datos", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("datos", dataRepo.customFindAll(null,true));
            } catch (Exception e) {
                log.error("Error ejecutando 'dat_gen_consulta_datos'", e);
            }
        });

        this.actions.put("dat_gen_registro_datos", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "dat_gen_registro_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("grupos", dataRepo.customFindAll(null,false));
                dataModel.addAttribute("update", false);
            } catch (Exception e) {
                log.error("Error ejecutando 'dat_gen_registro_datos'", e);
            }
        });
        
        this.actions.put("dat_gen_consulta_empresa", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_empresa");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("datos", dataRepo.customFindAll("Empresas",false));
            } catch (Exception e) {
                log.error("Error ejecutando 'dat_gen_consulta_empresa'", e);
            }
        });

        this.actions.put("dat_gen_registro_empresa", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "dat_gen_registro_empresa");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("update", false);
            } catch (Exception e) {
                log.error("Error ejecutando 'dat_gen_registro_empresa'", e);
            }
        });

        this.actions.put("usr_mgr_principal", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "usr_mgr_principal");
                dataModel.addAttribute("usuarios", UsuarioServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'usr_mgr_principal'", e);
            }
        });

        this.actions.put("usr_mgr_registro", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "usr_mgr_registro");
                dataModel.addAttribute("update", false);
                Persona p = new Persona();
                Usuario u = new Usuario();
                u.setPersona(p);
                dataModel.addAttribute("user", u);
                dataModel.addAttribute("persona", p);
                dataModel.addAttribute("sangre", dataRepo.customFindAll("Tipos Sanguineos",false));
                dataModel.addAttribute("sexo", dataRepo.customFindAll("Sexo",false));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'usr_mgr_registro'", e);
            }
        });

        this.actions.put("sys_configuracion", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "sys_configuracion");
                dataModel.addAttribute("conf", ConfServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'sys_configuracion'", e);
            }
        });

        this.actions.put("trp_vehiculo_consulta", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "trp_vehiculo_consulta");
                dataModel.addAttribute("vehiculos", VehiculoServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_vehiculo_consulta'", e);
            }
        });

        this.actions.put("trp_vehiculo_registro", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "trp_vehiculo_registro");
                Vehiculo p = new Vehiculo();
                dataModel.addAttribute("vehiculo", p);
                List<DatoDTO> marcas = dataRepo.customFindAll("Marca",false);
                dataModel.addAttribute("marca", marcas);
                dataModel.addAttribute("tipo_vehiculo", dataRepo.customFindAll("Tipo Vehiculo",false));
                dataModel.addAttribute("estado", dataRepo.customFindAll("Estados Vehiculo",false));
                dataModel.addAttribute("color", dataRepo.customFindAll("Colores",false));
                dataModel.addAttribute("modelo",dataRepo.customFindAll(marcas.get(0).dat(),false ));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_vehiculo_registro'", e);
            }
        });

        this.actions.put("trp_paradas_consulta", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "trp_paradas_consulta");
                dataModel.addAttribute("paradas", ParadaServicio.consultarTodo(null, null));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_paradas_consulta'", e);
            }
        });

        this.actions.put("trp_paradas_registro", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "trp_paradas_registro");
                Parada p = new Parada();
                dataModel.addAttribute("parada", p);
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_paradas_registro'", e);
            }
        });

        this.actions.put("trp_rutas_consulta", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "trp_rutas_consulta");
                dataModel.addAttribute("rutas", RutaServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_rutas_consulta'", e);
            }
        });

        this.actions.put("trp_rutas_registro", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "trp_rutas_registro");
                Ruta r = new Ruta();
                dataModel.addAttribute("ruta", r);
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'trp_rutas_registro'", e);
            }
        });
        
        this.actions.put("pub_publicidad_consulta", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "pub_publicidad_consulta");
                dataModel.addAttribute("publicidades", PublicidadServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'pub_publicidad_consulta'", e);
            }
        });

        this.actions.put("pub_publicidad_registro", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "pub_publicidad_registro");
                Publicidad r = new Publicidad();
                dataModel.addAttribute("publicidad", r);
                dataModel.addAttribute("empresas", dataRepo.customFindAll("Empresas",false));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'pub_publicidad_registro'", e);
            }
        });
        
        this.actions.put("inv_producto_consulta", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "inv_producto_consulta");
                dataModel.addAttribute("productos", ProductoServicio.consultar());
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'inv_producto_consulta'", e);
            }
        });

        this.actions.put("inv_producto_registro", () -> {
            try {
                Map<String, Object> acc = consultarAccesosPantallaUsuario(userId, "inv_producto_registro");
                Producto r = new Producto();
                dataModel.addAttribute("producto", r);
                dataModel.addAttribute("categorias", dataRepo.customFindAll("Categoria Producto",false));
                dataModel.addAllAttributes(acc);
            } catch (Exception e) {
                log.error("Error ejecutando 'inv_producto_registro'", e);
            }
        });
    }
    
    public void cargarPagina(String idPage,Model model,Integer idUser){
        this.dataModel=model;
        this.userId=idUser;
        
        actions.getOrDefault(idPage, () -> log.warn("Acción no encontrada: " + idPage)).run();
        
    }
    
    public Usuario getUsuarioLogueado(){
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    @Cacheable("PermisosUsuario")
    public String verificarPermisos(String permiso,Model modelo,Usuario usuario){
        Map<String,Object> m=consultarAccesosPantallaUsuario(usuario.getId(),permiso);
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
