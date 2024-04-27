/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.transporte;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@Transactional
@RequiredArgsConstructor
public class LocVehiculoServ {
    
    private final LocVehiculoRepo repo;
    
    public LocVehiculo guardar(LocVehiculo Loc){
        Loc.setFecha_registro(new Date());
        return repo.save(Loc);
    }
    
    public Optional<LocVehiculo> consultarUltimaLocVehiculo(String placa){
        return repo.findLastByPlaca(placa);
    }
    
    public boolean tieneUltimaLoc(String placa){
        return consultarUltimaLocVehiculo(placa)!=null;
    }
    
}
