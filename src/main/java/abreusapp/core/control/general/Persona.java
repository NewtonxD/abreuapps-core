package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@PersonaId")
public class Persona{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "nam")
    private String nombre;

    @Column(name = "apl")
    private String apellido;
    
    @Column(name = "ced")
    private String cedula;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
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
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Dato tipo_sangre;
    
    @Column(name = "brt_at")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_nacimiento;

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