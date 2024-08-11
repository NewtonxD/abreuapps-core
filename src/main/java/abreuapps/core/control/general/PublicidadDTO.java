package abreuapps.core.control.general;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */
public record PublicidadDTO( 
    Long id,
    String tit,
    String dsc,
    String img_vid,
    String lnk_dst,
    String empresa_dat,
    boolean act
) implements Serializable {}
