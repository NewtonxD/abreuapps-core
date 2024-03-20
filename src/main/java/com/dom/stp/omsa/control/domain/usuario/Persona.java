package com.dom.stp.omsa.control.domain.usuario;

import com.dom.stp.omsa.control.domain.dato.Dato;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * Esta entidad representa los datos personales que se manejan el sistema.
 *
 * @author Carlos Abreu Pérez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ppl_inf",schema = "public")
public class Persona{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nam")
    private String nombre;

    @Column(name = "apl")
    private String apellido;
    
    @Column(name = "ced")
    private String cedula;
    
    @Column(name = "sex")
    @OneToOne()
    @JoinColumn(name = "dat")
    private Dato sexo;
    
    @Column(name = "num_cel")
    private String numero_celular;
    
    @Column(name = "emg_tel")
    private String numero_emergencia;
    
    @Column(name = "emg_nam")
    private String nombre_emergencia;
    
    @Column(name = "dir")
    private String direccion; 
    
    @Column(name = "nic")
    private String apodo;
    
    @Column(name = "bld_tpe")
    @OneToOne()
    @JoinColumn(name = "dat")
    private Dato tipo_sangre;
    
    @Column(name = "brt_at")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_nacimiento;

    @Column(name= "mde_by")
    @OneToOne()
    @JoinColumn(name = "id")
    private Usuario hecho_por;
    
    @Column(name= "mde_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
    @Column(name= "upd_by")
    @OneToOne()
    @JoinColumn(name = "id")
    private Usuario actualizado_por;
    
    @Column(name= "upd_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;
 
    
}    