/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;

import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.domain.dato.DatoServ;
import com.dom.stp.omsa.control.domain.dato.GrupoDatoServ;
import com.dom.stp.omsa.control.domain.usuario.Persona;
import com.dom.stp.omsa.control.domain.usuario.PersonaServ;
import com.dom.stp.omsa.control.domain.usuario.Usuario;
import com.dom.stp.omsa.control.domain.usuario.UsuarioServ;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *  
 * Servicio para manejar las pantallas usando 1 solo endpoint.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Transactional
@Service
public class ModelServ {
    
    @Autowired
    GrupoDatoServ GrupoServicio;
    
    @Autowired
    DatoServ DatoServicio;
    
    @Autowired
    AccesoServ AccesoServicio;
    
    @Autowired
    UsuarioServ UsuarioServicio;
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private Model dataModel=null;
    
    private Integer userId=null;
    
    public ModelServ(){
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
                dataModel.addAttribute("sangre",DatoServicio.consultarPorGrupo("Tipos Sanguineos"));
                dataModel.addAttribute("sexo",DatoServicio.consultarPorGrupo("sexo"));
                dataModel.addAllAttributes(acc);                
            }
        );
        
    }
    
    public void load(String idPage,Model model,Integer idUser){
        this.dataModel=model;
        this.userId=idUser;
        
        actions.getOrDefault(idPage,()->{}).run();
        
    }
}
