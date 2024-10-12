package abreuapps.core.control.inventario;

import abreuapps.core.control.general.Dato;
import abreuapps.core.control.usuario.Usuario;
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
 * @author cabreu
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prd",schema = "inventory")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@EmpleadoId")
public class Producto {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nom")
    private String nombre;
    
    @Column(name = "dsc")
    private String descripcion;
    
    @Column(name = "pho")
    private String foto;
    
    @Column(name = "prc_sel")
    private Float precio_venta;
    
    @ManyToOne
    @PrimaryKeyJoinColumn 
    private Dato categoria;
    
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
    
    @Column(name = "act")
    private boolean activo;
    
}
