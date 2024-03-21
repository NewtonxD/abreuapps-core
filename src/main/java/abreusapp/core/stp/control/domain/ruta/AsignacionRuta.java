/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.stp.control.domain.ruta;

import abreusapp.core.stp.control.domain.dato.Dato;
import abreusapp.core.stp.control.domain.transporte.Transporte;
import abreusapp.core.stp.control.domain.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "rta_asg",schema = "transport")
public class AsignacionRuta {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Transporte placa;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Ruta ruta;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Dato estado;

    @Column(name = "dt_ini")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_inicio;
    
    @Column(name = "dt_fin")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_final;
    
    @Column(name = "id_drv")
    private Integer conductor_id;
    
    @Column(name = "id_cob")
    private Integer cobrador_id;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Usuario hecho_por;
    
    @Column(name= "mde_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Usuario actualizado_por;
    
    @Column(name= "upd_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;

}
