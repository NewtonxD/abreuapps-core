package abreuapps.core.control.transporte;

import abreuapps.core.control.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
 * Esta entidad representa las paradas.
 *
 * @author Carlos Abreu Pérez
 * 
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pda",schema = "transport")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@ParadaId")
public class Parada {
    
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "dsc")
    private String descripción;
    
    @Column(name = "lon")
    private Double longitud;
    
    @Column(name = "lat")
    private Double latitud;
    
    @Column(name = "act")
    private boolean activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Usuario hecho_por;
    
    @Column(name= "mde_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Usuario actualizado_por;
    
    @Column(name= "upd_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;
}
