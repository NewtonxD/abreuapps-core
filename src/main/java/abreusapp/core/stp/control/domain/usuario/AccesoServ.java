/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.stp.control.domain.usuario;

import abreusapp.core.stp.control.domain.dato.Dato;
import abreusapp.core.stp.control.domain.dato.DatoRepo;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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

@Transactional
@Service
@RequiredArgsConstructor
public class AccesoServ {
    
    private final AccesoUsuarioRepo repo;
    
    private final AccesoRepo accRepo;
    
    private final DatoRepo dataRepo;
    
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
    
    public List<AccesoUsuario> consultarAccesosUsuario(Usuario usuario){
        
        return repo.findAllByUsuario(usuario);
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
    
    public List<Object[]> ListadoAccesosUsuarioEditar(int idUsuario){
        return repo.ListadoAccesosUsuarioEditar(idUsuario);
    }
    
        
    public void GuardarTodosMap(Map<String,String> accesos,Usuario usuario){
        List<AccesoUsuario> listaAcceso = new ArrayList<>();
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
                        listaAcceso.add(AccUsr);
                    }else{
                        //nuevo
                        listaAcceso.add(
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
        
        repo.saveAllAndFlush(listaAcceso);
    }
    
}
