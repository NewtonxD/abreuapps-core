/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import abreusapp.core.control.transporte.Parada;
import abreusapp.core.control.transporte.ParadaDTO;
import abreusapp.core.control.transporte.Ruta;
import abreusapp.core.control.transporte.RutaDTO;
import abreusapp.core.control.transporte.Vehiculo;
import abreusapp.core.control.transporte.VehiculoDTO;
import java.util.ArrayList;
import java.util.List;

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
    
    public static List<VehiculoDTO> listVehiculoToDTO(List<Vehiculo> vehiculos){
            List<VehiculoDTO> listaDTO = new ArrayList<>();
            for (Vehiculo vehiculo : vehiculos) {
                listaDTO.add(vehiculoToDTO(vehiculo));
            }
            return listaDTO;
    }
    
    public static ParadaDTO paradaToDTO(Parada parada){
        if(parada==null) return null;
        else return ParadaDTO
                .builder()
                .id(parada.getId())
                .descripción(parada.getDescripción())
                .longitud(parada.getLongitud())
                .latitud(parada.getLatitud())
                .activo(parada.isActivo())
                .hecho_por(parada.getHecho_por()==null ? null : parada.getHecho_por().getId())
                .fecha_registro(parada.getFecha_registro())
                .actualizado_por(parada.getActualizado_por() ==null ? null : parada.getActualizado_por().getId())
                .fecha_actualizacion(parada.getFecha_actualizacion())
                .build();
    }
    
    public static List<ParadaDTO> listParadaToDTO(List<Parada> paradas){
            List<ParadaDTO> listaDTO = new ArrayList<>();
            for (Parada parada : paradas) {
                listaDTO.add(paradaToDTO(parada));
            }
            return listaDTO;
    }
    
    public static RutaDTO rutaToDTO(Ruta ruta){
        if(ruta==null) return null;
        else return RutaDTO
                .builder()
                .ruta(ruta.getRuta())
                .localizacion_inicial(ruta.getLocalizacion_inicial())
                .localizacion_final(ruta.getLocalizacion_final())
                .activo(ruta.isActivo())
                .hecho_por(ruta.getHecho_por()==null ? null : ruta.getHecho_por().getId())
                .fecha_registro(ruta.getFecha_registro())
                .actualizado_por(ruta.getActualizado_por() ==null ? null : ruta.getActualizado_por().getId())
                .fecha_actualizacion(ruta.getFecha_actualizacion())
                .build();
    }
    
    public static List<RutaDTO> listRutaToDTO(List<Ruta> rutas){
            List<RutaDTO> listaDTO = new ArrayList<>();
            for (Ruta ruta : rutas) {
                listaDTO.add(rutaToDTO(ruta));
            }
            return listaDTO;
    }
}
