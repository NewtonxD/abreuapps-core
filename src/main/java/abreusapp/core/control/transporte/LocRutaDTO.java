/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.transporte;

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
public class LocRutaDTO {
    
    private Long id;
    private String ruta;
    private Double longitud;
    private Double latitud;
}
