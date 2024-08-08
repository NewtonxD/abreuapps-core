package abreuapps.core.control.transporte;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */

public record RutaDTO (
    String rta,
    String loc_ini,
    String loc_fin,
    boolean act
) implements Serializable {}
