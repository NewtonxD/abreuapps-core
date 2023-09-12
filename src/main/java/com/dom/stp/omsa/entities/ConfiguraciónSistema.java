/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  
 * Esta entidad contiene las configuraciones generales del sistema.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_cnf")
public class ConfiguraciónSistema {
    
    @Id
    @Column(name = "cod",columnDefinition = "varchar(50)")
    private String dato;

    @Column(name = "dsc", nullable = true)
    private String descripción;
    
    @Column(name = "val", nullable = false)
    private String valor;
    
    @Column(name = "cat",columnDefinition = "varchar(50)",nullable=false)
    private String categoria;
}
