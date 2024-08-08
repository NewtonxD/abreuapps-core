package abreuapps.core.control.general;

import abreuapps.core.control.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Esta entidad representa los datos generales que se utilizan en todas las partes del sistema.
 *
 * @author Carlos Abreu Pérez
 * 
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gnr_dat",schema = "public")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@DatoId")
public class Dato {
    
    @Id
    @Column(name = "dat")
    private String dato;
    
    @Column(name = "fat_dat")
    private String dato_padre;

    @Column(name = "dsc")
    private String descripcion;

    @Column(name = "act")
    private boolean activo;
    
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
