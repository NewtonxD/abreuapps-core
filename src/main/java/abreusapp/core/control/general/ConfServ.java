/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */
@Service
@RequiredArgsConstructor
public class ConfServ {
    
    private final ConfRepo repo;
    
    public Map<String,String> consultarConfMap(){
        List<Conf> results=repo.findAll();
        Map<String, String> convert=new HashMap<>();
        
        for (Conf result : results) convert.put(result.getCodigo(), result.getValor());
        
        return convert;
    }
    
    public List<ConfDTO> consultar(){
        return repo.customFindAll();
    }
    
    public void GuardarTodosMap(Map<String,String> configuracion,Usuario usuario){
        List<Conf> listaConf = new ArrayList<>();
        for (Map.Entry<String,String> val : configuracion.entrySet()) {
            Optional<Conf> conf=repo.findById(val.getKey());
            if(conf.isPresent()){
                if(! conf.get().getValor().equals(val.getValue())){
                    listaConf.add(
                        new Conf(
                            val.getKey(), 
                            conf.get().getDescripcion(), 
                            val.getValue(), 
                            usuario, 
                            new Date())
                    );
                }
            }
        }
        
        repo.saveAllAndFlush(listaConf);
    }
    
}
