/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package abreusapp.core.control.transporte;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface ParadaRepo extends JpaRepository<Parada,Integer> {
    
    @Query(
        value = "select * from transport.pda p where  p.id != coalesce(?1,0) and case when ?2!=null then p.act=?2 else 1=1 end order by p.id desc",
        nativeQuery = true
    )
    public List<Parada> findAllCustom(Integer excluyeId,Boolean activo);
    
    public List<Parada> findByActivo(boolean activo);
}
