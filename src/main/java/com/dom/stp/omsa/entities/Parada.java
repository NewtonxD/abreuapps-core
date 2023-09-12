/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
 * Esta entidad representa las paradas del autobus.
 *
 * @author Carlos Abreu Pérez
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pda")
public class Parada {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "pwd", nullable = false)
    private String contraseña;

    @Column(name = "dsc", nullable = false)
    private String descripción;
    
    @Column(name = "lon",columnDefinition = "decimal(12,7)",nullable=false)
    private float longitud;
    
    @Column(name = "lat", columnDefinition = "decimal(12,7)",nullable=false)
    private float latitud;
    
    @Column(name = "act",nullable=false)
    private boolean activo;
    
    @Column(name = "mde_by", nullable = false)
    private Integer hecho_por;
    
    @Column(name = "reg_dt",nullable=false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
}
