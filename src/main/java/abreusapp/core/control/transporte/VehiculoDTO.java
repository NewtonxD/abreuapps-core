package abreusapp.core.control.transporte;

import java.io.Serializable;

/**
 *
 * @author cabreu
 */
public record VehiculoDTO (
    String pl,
    String marca_dat,
    String modelo_dat,
    String ruta_rta,
    short cap_pax,
    short mke_at,
    String tipo_vehiculo_dat,
    String estado_dat,
    String color_dat,
    boolean act,
    String tkn    
) implements Serializable {}