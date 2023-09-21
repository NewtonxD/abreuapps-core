/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.ruta;

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
 * Esta entidad sirve para asignar una ruta a un autobus, tomando en cuenta el chofer
 * y su compañera cobradora, las asignaciones tienen fecha, hora y manejan estado.
 * 
 * @author Carlos Abreu Pérez
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rta_asg")
public class AsignacionRuta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "pl", columnDefinition = "varchar(15)",nullable=false)
    private String placa;

    @Column(name = "rta", columnDefinition = "varchar(50)",nullable=false)
    private String ruta;
    
    @Column(name = "est", columnDefinition = "varchar(50)",nullable=false)
    private String estado;

    @Column(name = "dt_ini", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date fecha_inicio;
    
    @Column(name = "dt_fin", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date fecha_final;
    
    @Column(name = "id_drv", nullable = false)
    private Integer conductor_id;
    
    @Column(name = "id_cob", nullable = false)
    private Integer cobrador_id;
    
    @Column(name = "mde_by", nullable = false)
    private Integer hecho_por;
    
    @Column(name = "reg_dt",nullable=false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;

}
