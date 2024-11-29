
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
public interface ProductoRepo extends JpaRepository<Producto, Integer> {
    
    @Query("SELECT new abreuapps.core.control.inventario.ProductoDTO(" +
           "p.id, " +
           "p.nombre, " +
           "p.descripcion, " +
           "p.precio_venta, " +
           "p.categoria.dato, " +
           "p.activo ) FROM Producto p")
    List<ProductoDTO> customFindAll();
}
