package abreuapps.core.control.inventario;

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
@Table(name = "sell",schema = "inventory")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@VentaId")
public class Venta {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    
    @Column(name = "cnt_art")
    private int cantidad_articulos;
    
    @Column(name = "amo_tot")
    private float monto_total;    
    
    @Column(name = "amo_imp")
    private float monto_impuestos;
    
    @Column(name = "amo_pay")
    private float monto_pagado;
    
    @Column(name = "amo_dsc")
    private float monto_descuento;
    
    @Column(name = "act")
    private boolean activo;
    
    @ManyToOne
    @PrimaryKeyJoinColumn 
    private Usuario hecho_por;
    
    @Column(name= "mde_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
}
