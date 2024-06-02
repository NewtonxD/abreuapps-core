package abreusapp.core.control.usuario;

import abreusapp.core.control.general.Dato;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Esta entidad representa los accesos de los usuarios.
 *
 * @author Carlos Abreu Pérez
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "acc",schema = "public")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@AccesoId")
public class Acceso {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Dato tipo_dato;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Dato pantalla;
    
    @Column(name = "fat_scr")
    private String pantalla_padre;
    
    @Column(name = "act")
    private boolean activo;
}
