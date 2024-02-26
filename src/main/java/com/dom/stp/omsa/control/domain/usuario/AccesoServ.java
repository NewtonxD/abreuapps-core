/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Transactional
@Service
@RequiredArgsConstructor
public class AccesoServ {
    
    private final AccesoUsuarioRepo repo;
    
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
    
    public List<AccesoUsuario> consultarAccesosUsuario(Integer id_usuario){
        
        return repo.findAllByUsuarioId(id_usuario);
    }
    
    public Map<String, Object> consultarAccesosMenuUsuario(Integer id_usuario){
        List<Object[]> results=repo.ListadoMenuUsuario(id_usuario);
        Map<String, Object> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Object valor = convertirValor(result[1]);
            convert.put(nombre, valor);
        }
        
        return convert;
    }
    
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
    
}
