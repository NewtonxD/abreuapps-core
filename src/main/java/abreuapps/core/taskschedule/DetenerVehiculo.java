package abreuapps.core.taskschedule;

import abreuapps.core.control.transporte.VehiculoServ;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */
@Component
@RequiredArgsConstructor
public class DetenerVehiculo {
        
    private final VehiculoServ VehiculoServicio;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1) 
    public void detenerVehiculosSinActividad() {
        VehiculoServicio.procesarVehiculoSinActividad();
    }
    
}