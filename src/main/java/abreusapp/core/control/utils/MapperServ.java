/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoDTO;

/**
 *
 * @author cabreu
 */

public class MapperServ {
    
    public static VehiculoDTO vehiculoToDTO(Vehiculo vehiculo) {
        if(vehiculo==null) return null;
        else return VehiculoDTO
                .builder()
                .placa(vehiculo.getPlaca())
                .marca(vehiculo.getMarca() ==null ? null : vehiculo.getMarca().getDato())
                .modelo(vehiculo.getModelo() ==null ? null : vehiculo.getModelo().getDato())
                .ruta( vehiculo.getRuta()==null ? null : vehiculo.getRuta().getRuta() )
                .capacidad_pasajeros(vehiculo.getCapacidad_pasajeros())
                .anio_fabricacion(vehiculo.getAnio_fabricacion())
                .tipo_vehiculo(vehiculo.getTipo_vehiculo() ==null ? null : vehiculo.getTipo_vehiculo().getDato())
                .estado(vehiculo.getEstado() ==null ? null : vehiculo.getEstado().getDato())
                .color(vehiculo.getColor() ==null ? null : vehiculo.getColor().getDato())
                .activo(vehiculo.isActivo())
                .hecho_por(vehiculo.getHecho_por() ==null ? null : vehiculo.getHecho_por().getId())
                .fecha_registro(vehiculo.getFecha_registro())
                .actualizado_por(vehiculo.getActualizado_por() ==null ? null : vehiculo.getActualizado_por().getId())
                .fecha_actualizacion(vehiculo.getFecha_actualizacion())
                .token(vehiculo.getToken())
                .build();
    }
    
}
