/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.stp.control.domain.usuario;

import abreusapp.core.stp.control.domain.dato.Dato;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class Acceso {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @OneToOne
    @PrimaryKeyJoinColumn
    private Dato tipo_dato;
    
    @OneToOne
    @PrimaryKeyJoinColumn
    private Dato pantalla;
    
    @Column(name = "fat_scr")
    private String pantalla_padre;
    
    @Column(name = "act")
    private boolean activo;
}
