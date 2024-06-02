package abreusapp.core.control.transporte;

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
    Date reg_dt
) {}
