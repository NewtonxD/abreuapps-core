package abreuapps.core.control.inventario;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cabreu
 */
public record VentaDTO(
    int id,
    int cnt_art,
    float amo_tot,
    float amo_imp,
    float amo_pay,
    float amo_dsc,
    boolean act,
    Date mde_at
) implements Serializable {}
