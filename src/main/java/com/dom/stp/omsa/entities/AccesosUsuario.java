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
 * Esta entidad representa los accesos con los usuarios.
 *
 * @author Carlos Abreu Pérez
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr_acc")
public class AccesosUsuario {
    
    @Id
    @Column(name = "acc_id")
    public Integer idAcceso;
    
    @Column(name = "usr_id", nullable = false)
    public Integer idUsuario;
    
    @Column(name = "val", nullable = false)
    public String valor;
    
    @Column(name = "act",nullable=false)
    public boolean activo;
    
}
