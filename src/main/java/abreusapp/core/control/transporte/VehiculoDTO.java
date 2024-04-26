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
public class VehiculoDTO {
    
    private String placa;
    private String marca;
    private String modelo;
    private String ruta;
    private short capacidad_pasajeros;
    private short anio_fabricacion;
    private String tipo_vehiculo;
    private String estado;
    private String color;
    private boolean activo;
    private Integer hecho_por;
    private Date fecha_registro;
    private Integer actualizado_por;
    private Date fecha_actualizacion;
    private String token;
    
}
