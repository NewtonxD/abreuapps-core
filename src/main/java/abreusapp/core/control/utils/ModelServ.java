/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import abreusapp.core.control.general.ConfServ;
import abreusapp.core.control.general.Dato;
import abreusapp.core.control.general.DatoServ;
import abreusapp.core.control.general.GrupoDatoServ;
import abreusapp.core.control.general.Persona;
import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoServ;
import abreusapp.core.control.usuario.AccesoServ;
import abreusapp.core.control.usuario.Usuario;
import abreusapp.core.control.usuario.UsuarioServ;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ModelServ {
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private Model dataModel=null;
    
    private Integer userId=null;
    
    private final AccesoServ AccesoServicio;
    
    //  custom constructor injection
    public ModelServ(GrupoDatoServ GrupoServicio,DatoServ DatoServicio,AccesoServ AccesoServicio,UsuarioServ UsuarioServicio,ConfServ ConfServ,VehiculoServ VehiculoServ){
        
        this.AccesoServicio=AccesoServicio;
        
        this.actions.put("dat_gen_consulta_grupos", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_grupos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("grupos", GrupoServicio.consultar());
            }
        );
        
        this.actions.put("dat_gen_consulta_datos", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("datos", DatoServicio.consultar());
            }
        );
        
        this.actions.put("dat_gen_registro_datos", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_datos");
                dataModel.addAllAttributes(acc);                
                dataModel.addAttribute("grupos", GrupoServicio.consultar());
                dataModel.addAttribute("update", false);
            }
        );
        
        this.actions.put("dat_gen_registro_grupos", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_grupos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("update", false);
            }
        );
        
        this.actions.put("dat_gen_principal", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "dat_gen_principal");
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("usr_mgr_principal", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "usr_mgr_principal");
                dataModel.addAttribute("usuarios", UsuarioServicio.consultar());
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("usr_mgr_registro", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "usr_mgr_registro");
                dataModel.addAttribute("update",false);
                Persona p=new Persona();
                Usuario u=new Usuario();
                u.setPersona(p);
                dataModel.addAttribute("user",u);
                dataModel.addAttribute("persona",p);
                dataModel.addAttribute("sangre",DatoServicio.consultarPorGrupo(GrupoServicio.obtener("Tipos Sanguineos").get() ));
                dataModel.addAttribute("sexo",DatoServicio.consultarPorGrupo(GrupoServicio.obtener("Sexo").get() ));
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("sys_configuracion", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "sys_configuracion");
                dataModel.addAttribute("conf", ConfServ.consultar());
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("trp_vehiculo_consulta", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_vehiculo_consulta");
                dataModel.addAttribute("vehiculos", VehiculoServ.consultar());
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("trp_vehiculo_registro", ()->{
                Map<String, Object> acc=AccesoServicio.consultarAccesosPantallaUsuario(userId, "trp_vehiculo_registro");
                Vehiculo p=new Vehiculo();
                dataModel.addAttribute("vehiculo",p);
                List<Dato> marcas=DatoServicio.consultarPorGrupo( GrupoServicio.obtener("Marca").get() );
                dataModel.addAttribute(
                        "marca",
                        marcas
                );
                dataModel.addAttribute(
                        "tipo_vehiculo",
                        DatoServicio.consultarPorGrupo(
                                GrupoServicio.obtener("Tipo Vehiculo").get() 
                        )
                );
                dataModel.addAttribute(
                        "estado",
                        DatoServicio.consultarPorGrupo(
                                GrupoServicio.obtener("Estados Vehiculo").get() 
                        )
                );
                dataModel.addAttribute(
                        "color",
                        DatoServicio.consultarPorGrupo(
                                GrupoServicio.obtener("Colores").get() 
                        )
                );
                dataModel.addAttribute(
                        "modelo",
                        DatoServicio.consultarPorGrupo(
                                GrupoServicio.obtener( marcas.getFirst().getDato() ).get() 
                        )
                );
                dataModel.addAllAttributes(acc);                
            }
        );
        
    }
    
    public void load(String idPage,Model model,Integer idUser){
        this.dataModel=model;
        this.userId=idUser;
        
        actions.getOrDefault(idPage,()->{}).run();
        
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
