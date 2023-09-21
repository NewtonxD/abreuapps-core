/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.autobus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Esta entidad sirve para asignar la localización instantanea del autobus.
 * 
 * @author Carlos Abreu Pérez
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bus_loc")
public class LocalizacionBus {
    
    @Id
    @Column(name = "pl",columnDefinition = "varchar(15)")
    private String placa;

    @Column(name = "lat",columnDefinition = "decimal(12,7)",nullable=false)
    private float latitud;

    @Column(name = "lon", columnDefinition = "decimal(12,7)",nullable=false)
    private float longitud;

    @Column(name = "reg_dt",nullable=false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
}
