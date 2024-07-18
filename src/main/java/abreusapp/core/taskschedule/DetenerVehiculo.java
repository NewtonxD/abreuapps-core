package abreusapp.core.taskschedule;

import abreusapp.core.control.transporte.VehiculoServ;
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

	@Scheduled(
            timeUnit = TimeUnit.MINUTES, 
            fixedRate = 2,
            initialDelay = 30
        ) public void detenerVehiculosSinActividad() {
            VehiculoServicio.procesarVehiculoSinActividad();
	}
}