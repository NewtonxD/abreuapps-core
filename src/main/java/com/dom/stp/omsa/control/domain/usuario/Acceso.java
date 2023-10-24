/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Esta entidad representa los accesos de los usuarios.
 *
 * @author Carlos Abreu Pérez
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "acc")
public class Acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dsc")
    private String descripción;
    
    @Column(name = "dat_tpe")
    private String tipo_dato;
    
    @Column(name = "acc_tpe")
    private String tipo_acceso;
    
    @Column(name = "scr")
    private String pantalla;
    
    @Column(name = "act")
    private boolean activo;
}
