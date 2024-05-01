/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    private Double longitud_apunta;
    private Double latitud_apunta;
    private String descripción_apunta;
    private boolean activo;
    private Integer hecho_por;
    private Date fecha_registro;
    private Integer actualizado_por;
    private Date fecha_actualizacion;
}
