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
 * Esta entidad representan a los empleados y sus atributos.
 *
 * @author Carlos Abreu Pérez
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emp")
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ced", columnDefinition = "varchar(13)",nullable=false)
    private String cedula;

    @Column(name = "nam", nullable = false)
    private String nombre;
    
    @Column(name = "ap", nullable = false)
    private String apellido;

    @Column(name = "sex", columnDefinition = "varchar(50)",nullable=false)
    private String sexo;

    @Column(name = "pst", columnDefinition = "varchar(50)",nullable=false)
    private String puesto;
    
    @Column(name = "dt_ini",nullable=false)
    @Temporal(value = TemporalType.DATE)
    private Date fecha_entrada;
    
    @Column(name = "dt_fin", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date fecha_salida;
    
    @Column(name = "act",nullable=false)
    private boolean activo;
    
}
