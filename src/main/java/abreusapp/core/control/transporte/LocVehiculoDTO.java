/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.transporte;

import java.util.Date;

/**
 *
 * @author cabreu
 */
public record LocVehiculoDTO(
    Long id,
    String pl,
    Double lat,
    Double lon,
    Date reg_dt
) {}
