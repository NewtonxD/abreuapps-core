/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import abreusapp.core.control.transporte.Parada;
import abreusapp.core.control.transporte.ParadaDTO;
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
    
    public static ParadaDTO paradaToDTO(Parada parada){
        if(parada==null) return null;
        else return ParadaDTO
                .builder()
                .id(parada.getId())
                .descripción(parada.getDescripción())
                .longitud(parada.getLongitud())
                .latitud(parada.getLatitud())
                .longitud_apunta(parada.getLongitud_apunta())
                .latitud_apunta(parada.getLatitud_apunta())
                .descripción_apunta(parada.getDescripción_apunta())
                .activo(parada.isActivo())
                .hecho_por(parada.getHecho_por()==null ? null : parada.getHecho_por().getId())
                .fecha_registro(parada.getFecha_registro())
                .actualizado_por(parada.getActualizado_por() ==null ? null : parada.getActualizado_por().getId())
                .fecha_actualizacion(parada.getFecha_actualizacion())
                .build();
    }
    
}
