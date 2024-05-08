package abreusapp.core.control.transporte;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author cabreu
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParadaDTO {

    private Integer id;
    private String descripción;
    private Double longitud;
    private Double latitud;
    private boolean activo;
    private Integer hecho_por;
    private Date fecha_registro;
    private Integer actualizado_por;
    private Date fecha_actualizacion;
}
