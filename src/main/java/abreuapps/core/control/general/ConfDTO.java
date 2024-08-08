package abreuapps.core.control.general;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */
public record ConfDTO (
    String cod,
    String dsc,
    String val
) implements Serializable {}
