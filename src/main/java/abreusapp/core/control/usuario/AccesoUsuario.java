/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.usuario;

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
 * Esta entidad representa los accesos con los usuarios.
 *
 * @author Carlos Abreu Pérez
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr_acc",schema = "public")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@AccesoUsuarioId")
public class AccesoUsuario {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer id;
    
    @Column(name = "val")
    public String valor;
    
    @Column(name = "act")
    public boolean activo;
    
    @ManyToOne
    @PrimaryKeyJoinColumn // Nombre de la columna de clave foránea
    private Usuario usuario;    
    
    @ManyToOne
    @PrimaryKeyJoinColumn // Nombre de la columna de clave foránea
    private Acceso acceso;
    
}
