package abreusapp.core.control.transporte;

import java.io.Serializable;


/**
 *
 * @author cabreu
 */
public record LocRutaDTO (
    Long id,
    String rta,
    Double lon,
    Double lat
) implements Serializable {}
