/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;

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
    @Column(name = "cod")
    private String dato;

    @Column(name = "dsc")
    private String descripción;
    
    @Column(name = "val")
    private String valor;
    
    @Column(name = "cat")
    private String categoria;
}
