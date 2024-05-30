package abreusapp.core.control.transporte;

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
){}
