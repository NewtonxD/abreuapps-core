/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;

import com.dom.stp.omsa.control.domain.usuario.UsuarioServ;
import com.dom.stp.omsa.control.domain.usuario.AccesoServ;
import com.dom.stp.omsa.control.domain.dato.DatoServ;
import com.dom.stp.omsa.control.domain.dato.GrupoDatoServ;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author cabreu
 */

@Transactional
@Service
public class ModelServ {
    
    @Autowired
    private GrupoDatoServ dtgrpServ;
    
    @Autowired
    private DatoServ dtgnrServ;
    
    @Autowired
    private AccesoServ AccServ;
    
    @Autowired
    private UsuarioServ usrServ;
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private Model dataModel=null;
    
    private Integer userId=null;
    
    public ModelServ(){
        this.actions.put("dat_gen_consulta_grupos", ()->{
                Map<String, Object> acc=AccServ.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_grupos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("grupos", dtgrpServ.consultar());
            }
        );
        
        this.actions.put("dat_gen_consulta_datos", ()->{
                Map<String, Object> acc=AccServ.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("datos", dtgnrServ.consultar());
            }
        );
        
        this.actions.put("dat_gen_registro_datos", ()->{
                Map<String, Object> acc=AccServ.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_datos");
                dataModel.addAllAttributes(acc);                
                dataModel.addAttribute("grupos", dtgrpServ.consultar());
                dataModel.addAttribute("update", false);
            }
        );
        
        this.actions.put("dat_gen_registro_grupos", ()->{
                Map<String, Object> acc=AccServ.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_grupos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("update", false);
            }
        );
        
        this.actions.put("dat_gen_principal", ()->{
                Map<String, Object> acc=AccServ.consultarAccesosPantallaUsuario(userId, "dat_gen_principal");
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("usr_mgr_principal", ()->{
                Map<String, Object> acc=AccServ.consultarAccesosPantallaUsuario(userId, "usr_mgr_principal");
                dataModel.addAttribute("usuarios", usrServ.consultar());
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
