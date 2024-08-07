package abreusapp.core.taskschedule;

import abreusapp.core.control.general.PublicidadServ;
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
public class FinalizarPublicidad {
    
    private final PublicidadServ PublicidadServicio;
    
    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1) 
    public void FinalizarPublicidadFecha() {
        PublicidadServicio.procesarPublicidadFinalizada();
    }
}
