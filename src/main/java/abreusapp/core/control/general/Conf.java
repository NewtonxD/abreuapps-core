package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
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
 * Esta entidad contiene las configuraciones generales del sistema.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_cnf",schema = "public")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@ConfId")
public class Conf {
    
    @Id
    @Column(name = "cod")
    private String codigo;

    @Column(name = "dsc")
    private String descripcion;
    
    @Column(name = "val")
    private String valor;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Usuario actualizado_por;
    
    @Column(name= "upd_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;
    
}
