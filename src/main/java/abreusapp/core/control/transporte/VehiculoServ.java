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
public class VehiculoServ {
    
    private final VehiculoRepo repo;
    
    public List<Vehiculo> consultar(){
        return repo.findAll();
    }
}
