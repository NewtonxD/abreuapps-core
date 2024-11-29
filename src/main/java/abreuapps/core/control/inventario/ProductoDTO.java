package abreuapps.core.control.inventario;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */
public record ProductoDTO(
    Integer id,
    String nom,
    String dsc,
    Float prc_sel,
    String categoria_dat,
    Boolean act
) implements Serializable {}
