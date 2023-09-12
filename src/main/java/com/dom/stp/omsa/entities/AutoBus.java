/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.entities;

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
 * Esta entidad representa al autobus y sus atributos.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bus")
public class AutoBus {

    @Id
    @Column(name = "pl", columnDefinition = "varchar(15)")
    private String placa;

    @Column(name = "mar", columnDefinition = "varchar(50)",nullable=false)
    private String marca;

    @Column(name = "mdl", columnDefinition = "varchar(50)",nullable=false)
    private String modelo;

    @Column(name = "cap_pax", nullable = false)
    private Integer capacidad_pasajeros;

    @Column(name = "est", columnDefinition = "varchar(50)",nullable=false)
    private String estado;

    @Column(name = "mde_by", nullable = false)
    private Integer hecho_por;

    @Column(name = "reg_dt",nullable=false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
}
