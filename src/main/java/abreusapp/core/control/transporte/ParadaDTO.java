package abreusapp.core.control.transporte;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */
public record ParadaDTO (
    Integer id,
    String dsc,
    Double lon,
    Double lat,
    boolean act
) implements Serializable {}
