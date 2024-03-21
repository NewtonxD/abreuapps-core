/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package abreusapp.core.control.usuario;

import java.util.List;
import java.util.Optional;
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

    public List<AccesoUsuario> findAllByUsuario(Usuario usuario);
    public Optional<AccesoUsuario> findAllByUsuarioAndAcceso(Usuario usuario,Acceso idAcceso);
    
    @Query( value ="""
                   SELECT 
                        a.scr as acc_nam,u.val
                    FROM
                        usr_acc u
                            INNER JOIN
                        acc a ON u.usr_id=:idusuario AND u.acc_id = a.id and a.act and u.act
                            INNER JOIN
                        gnr_dat g ON g.dat=a.scr AND (g.dat_grp="Menu" or g.dat_grp="Modulo");
                   """,nativeQuery = true)
    public List<Object[]> ListadoMenuUsuario(@Param("idusuario") Integer idUsuario);
    
    @Query( value ="""
                   SELECT 
                       a.scr as acc_nam,u.val
                   FROM
                       usr_acc u
                           INNER JOIN
                       acc a ON u.usr_id=:idusuario and (a.scr =:pantalla or a.fat_scr=:pantalla) AND u.acc_id = a.id and a.act and u.act;
                   """,nativeQuery = true)
    public List<Object[]> ListadoAccesosPantallaUsuario(@Param("idusuario") Integer idUsuario,@Param("pantalla") String pantalla);
    
    @Query( value ="""
                   SELECT 
                        a.*,
                        d.dsc,
                        d.dat_grp,
                        coalesce(u.val, "false") as val 
                   FROM acc a 
                        INNER JOIN gnr_dat d ON d.dat=a.scr AND a.act 
                        LEFT JOIN usr_acc u ON a.id=u.acc_id AND u.usr_id=:idusuario  
                   ORDER BY d.dat_grp, a.fat_scr;
                   """,nativeQuery = true)
    public List<Object[]> ListadoAccesosUsuarioEditar(@Param("idusuario") int idUsuario);
    
}
