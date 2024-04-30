/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package abreusapp.core.control.transporte;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface LocRutaRepo  extends JpaRepository<LocRuta, Long> {
    public List<LocRuta> findAllByRuta(Ruta ruta);
    public void deleteAllByRuta(Ruta ruta);
}


