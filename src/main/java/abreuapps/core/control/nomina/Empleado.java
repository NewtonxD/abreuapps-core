package abreuapps.core.control.nomina;

import abreuapps.core.control.general.Dato;
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
 * Esta entidad representan a los empleados y sus atributos.
 *
 * @author Carlos Abreu Pérez
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emp",schema = "payroll")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@EmpleadoId")
public class Empleado {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "ced")
    private String cedula;

    @Column(name = "nam")
    private String nombre;
    
    @Column(name = "ap")
    private String apellido;
    
    @ManyToOne
    @PrimaryKeyJoinColumn 
    private Dato sexo;

    
    @ManyToOne
    @PrimaryKeyJoinColumn 
    private Dato puesto;
    
    @Column(name = "dt_ini")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_entrada;
    
    @Column(name = "dt_fin")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_salida;
    
    @Column(name = "act")
    private boolean activo;
    
}
