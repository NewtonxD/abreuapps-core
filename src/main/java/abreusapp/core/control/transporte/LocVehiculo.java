package abreusapp.core.control.transporte;

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
 * Esta entidad sirve para asignar la localización instantanea del vehiculo.
 * 
 * @author Carlos Abreu Pérez
 * 
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trp_loc",schema = "transport")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@LocVehiculoId")
public class LocVehiculo {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Vehiculo placa;

    @Column(name = "lat")
    private Double latitud;

    @Column(name = "lon")
    private Double longitud;

    @Column(name = "reg_dt")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
}
