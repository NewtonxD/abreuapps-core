package abreuapps.core.control.inventario;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */


@Repository
public interface VentaRepo extends JpaRepository<Venta, Long>  {
    
    @Query(" SELECT new abreuapps.core.control.inventario.VentaDTO("+
            "v.id,"+
            "v.cantidad_articulos,"+
            "v.monto_total,"+
            "v.monto_impuestos,"+
            "v.monto_pagado,"+
            "v.monto_descuento,"+
            "v.activo,"+
            "v.fecha_registro ) FROM Venta v")
    List<VentaDTO> customFindAll();
}
