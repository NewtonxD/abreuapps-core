/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface AccesoUsuarioRepo extends JpaRepository<AccesoUsuario, Integer>{

    public List<AccesoUsuario> findAllByidUsuario(Integer idUsuario);
    
    @Query( value ="""
                   SELECT 
                        a.acc_nam,u.val
                    FROM
                        usr_acc u
                            INNER JOIN
                        acc a ON u.usr_id=:idusuario and (a.scr is null or a.fat_scr is null) AND u.acc_id = a.id and a.act and u.act;
                   """,nativeQuery = true)
    public List<Object[]> ListadoMenuUsuario(@Param("idusuario") Integer idUsuario);
    
    @Query( value ="""
                   SELECT 
                       a.acc_nam,u.val
                   FROM
                       usr_acc u
                           INNER JOIN
                       acc a ON u.usr_id=:idusuario and (a.scr =:pantalla or a.fat_scr=:pantalla) AND u.acc_id = a.id and a.act and u.act;
                   """,nativeQuery = true)
    public List<Object[]> ListadoAccesosPantallaUsuario(@Param("idusuario") Integer idUsuario,@Param("pantalla") String pantalla);
    
    
}
