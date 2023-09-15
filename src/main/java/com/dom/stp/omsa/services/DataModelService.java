/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.services;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
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
public class DataModelService {
    
    @Autowired
    private GrupoDatosService dtgrpServ;
    
    @Autowired
    private DatosGeneralesService dtgnrServ;
    
    @Autowired
    private AccesosService AccService;
    
    private final Map<String,Runnable> actions=new HashMap<>();
    
    private Model dataModel=null;
    
    private Integer userId=null;
    
    public DataModelService(){
        this.actions.put("dat_gen_consulta_grupos", ()->{
                Map<String, Object> acc=AccService.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_grupos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("grupos", dtgrpServ.consultar());
            }
        );
        
        this.actions.put("dat_gen_consulta_datos", ()->{
                Map<String, Object> acc=AccService.consultarAccesosPantallaUsuario(userId, "dat_gen_consulta_datos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("datos", dtgnrServ.consultar());
            }
        );
        
        this.actions.put("dat_gen_registro_datos", ()->{
                Map<String, Object> acc=AccService.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_datos");
                dataModel.addAllAttributes(acc);                
                dataModel.addAttribute("grupos", dtgrpServ.consultar());
                dataModel.addAttribute("update", false);
            }
        );
        
        this.actions.put("dat_gen_registro_grupos", ()->{
                Map<String, Object> acc=AccService.consultarAccesosPantallaUsuario(userId, "dat_gen_registro_grupos");
                dataModel.addAllAttributes(acc);
                dataModel.addAttribute("update", false);
            }
        );
        
        this.actions.put("dat_gen_principal", ()->{
                Map<String, Object> acc=AccService.consultarAccesosPantallaUsuario(userId, "dat_gen_principal");
                dataModel.addAllAttributes(acc);                
            }
        );
        
        this.actions.put("usr_mgr_principal", ()->{
                Map<String, Object> acc=AccService.consultarAccesosPantallaUsuario(userId, "usr_mgr_principal");
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
