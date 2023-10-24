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
    @Column(name = "pl")
    private String placa;

    @Column(name = "mar")
    private String marca;

    @Column(name = "mdl")
    private String modelo;

    @Column(name = "cap_pax")
    private Integer capacidad_pasajeros;

    @Column(name = "est")
    private String estado;

    @Column(name = "mde_by")
    private Integer hecho_por;

    @Column(name = "reg_dt")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
}
