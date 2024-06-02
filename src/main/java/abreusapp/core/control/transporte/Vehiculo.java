package abreusapp.core.control.transporte;

import abreusapp.core.control.general.Dato;
import abreusapp.core.control.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
 * Esta entidad representa al autobus y sus atributos.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vhl",schema = "transport")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@VehiculoId")
public class Vehiculo {

    @Id
    @Column(name = "pl")
    private String placa;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Dato marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Dato modelo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Ruta ruta;
    
    @Column(name = "cap_pax")
    private short capacidad_pasajeros;
    
    @Column(name = "mke_at")
    private short anio_fabricacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Dato tipo_vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Dato estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Dato color;
    
    @Column(name="act")
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
    
    @Column(name = "tkn")
    private String token;
}
