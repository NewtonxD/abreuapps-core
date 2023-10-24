/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "usr_acc")
public class AccesoUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    
    @Column(name = "val")
    public String valor;
    
    @Column(name = "act")
    public boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "usr_id") // Nombre de la columna de clave foránea
    private Usuario usuario;    
    
    @ManyToOne
    @JoinColumn(name = "acc_id") // Nombre de la columna de clave foránea
    private Acceso acceso;
    
}
