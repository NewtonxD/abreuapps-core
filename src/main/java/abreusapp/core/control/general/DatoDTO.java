package abreusapp.core.control.general;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */
public record DatoDTO(
    String dat,
    String fat_dat,
    String dsc,
    boolean act   
)  implements Serializable {}
