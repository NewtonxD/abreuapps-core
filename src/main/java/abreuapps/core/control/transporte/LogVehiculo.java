package abreuapps.core.control.transporte;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
 * @author cabreu
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vhl_log",schema = "transport")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@LogVehiculoId")
public class LogVehiculo {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    
    @Column(name = "pl")
    private String placa;
    
    @Column(name = "rta_old")
    private String ruta_old;
    
    @Column(name = "rta_new")
    private String ruta_new;
    
    @Column(name = "est_old")
    private String estado_old;
    
    @Column(name = "est_new")
    private String estado_new;

    @Column(name = "lat")
    private Double latitud;

    @Column(name = "lon")
    private Double longitud;
    
    @Column(name = "sys")
    private boolean system_change;
    
    @Column(name = "reg_dt")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
}

