/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.transporte;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cabreu
 */
public record LogVehiculoDTO(
    Long id,
    Date reg_dt,
    String pl,
    String rta_old,
    String rta_new,
    Double lon,
    Double lat,
    String est_old,
    String est_new,
    boolean sys
) implements Serializable {}

