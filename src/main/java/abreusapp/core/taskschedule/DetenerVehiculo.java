package abreusapp.core.taskschedule;

import abreusapp.core.control.transporte.VehiculoServ;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DetenerVehiculo {
        
        private final VehiculoServ VehiculoServicio;

	@Scheduled(
            timeUnit = TimeUnit.MINUTES, 
            fixedRate = 1
        ) public void detenerVehiculosSinActividad() {
            VehiculoServicio.procesarVehiculoSinActividad();
	}
}