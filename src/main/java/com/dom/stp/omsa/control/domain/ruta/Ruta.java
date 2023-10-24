/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.ruta;

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
 * Esta entidad representa la ruta de los autobuses.
 *
 * @author Carlos Abreu Pérez
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rta")
public class Ruta {
    
    @Id
    @Column(name = "rta")
    private String ruta;

    @Column(name = "loc_ini")
    private String localizacion_inicial;
    
    @Column(name = "loc_fin")
    private String localizacion_final;
    
    @Column(name = "act")
    private boolean activo;
    
    @Column(name = "mde_by")
    private Integer hecho_por;
    
    @Column(name = "reg_dt")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
}
