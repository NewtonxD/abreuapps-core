package abreuapps.core.control.transporte;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cabreu
 */
public record LocVehiculoDTO(
    Long id,
    String pl,
    Double lat,
    Double lon,
    String ruta_rta,
    Date reg_dt
)  implements Serializable {}
