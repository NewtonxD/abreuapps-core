/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.services;

import com.dom.stp.omsa.entities.AccesosUsuario;
import com.dom.stp.omsa.repositorys.AccesosUsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Transactional
@Service
public class AccesosService {
    
    @Autowired
    private AccesosUsuarioRepository accUsrRepo;
    
    private Object convertirValor(Object valor) {
        if (valor instanceof String) {
            String stringValue = (String) valor;
            
            if(!stringValue.contains(",")){
                
                if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")) {
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
    
    public List<AccesosUsuario> consultarAccesosUsuario(Integer id_usuario){
        
        return accUsrRepo.findAllByidUsuario(id_usuario);
    }
    
    public List<Integer> consultarAccesosMenuUsuario(Integer id_usuario){
        
        return accUsrRepo.ListadoMenuUsuario(id_usuario);
    }
    
    public Map<String, Object> consultarAccesosPantallaUsuario(Integer id_usuario,String pantalla){
        List<Object[]> results=accUsrRepo.ListadoAccesosPantallaUsuario(id_usuario,pantalla);
        Map<String, Object> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Object valor = convertirValor(result[1]);
            convert.put(nombre, valor);
        }
        
        return convert;
    }
    
}
