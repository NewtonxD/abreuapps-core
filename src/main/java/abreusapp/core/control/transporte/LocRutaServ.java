/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.transporte;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Transactional
@Service
@RequiredArgsConstructor
public class LocRutaServ {
    
    private LocRutaRepo repo;
    
    public List<LocRuta> consultar(){
        return repo.findAll();
    }
    
    public List<LocRuta> consultarPorRuta(Ruta ruta){
        return repo.findAllByRuta(ruta);
    }
    
    public void borrarPorRuta(Ruta ruta){
        repo.deleteAllByRuta(ruta);
    }
    
    public void guardarTodos(List<LocRuta> gd){
        repo.saveAll(gd);
    }
    
}

