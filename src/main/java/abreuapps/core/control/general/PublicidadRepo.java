package abreuapps.core.control.general;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cabreu
 */

@Repository
public interface PublicidadRepo extends JpaRepository<Publicidad, Long> {
    @Query(value="SELECT new abreuapps.core.control.general.PublicidadDTO("
            + "p.id,"
            + "p.titulo,"
            + "p.descripcion,"
            + "p.lg_imagen_video_direccion,"
            + "p.sm_imagen_video_direccion,"
            + "p.link_destino,"
            + "p.empresa.getDato()"
            + ") FROM Publicidad p WHERE p.activo ORDER BY p.conteo_view ASC LIMIT 1")
    PublicidadDTO customFindCurrent();
    
    @Query(value="SELECT new abreuapps.core.control.general.PublicidadDTO("
            + "p.id,"
            + "p.titulo,"
            + "p.descripcion,"
            + "p.lg_imagen_video_direccion,"
            + "p.sm_imagen_video_direccion,"
            + "p.link_destino,"
            + "p.empresa.getDato()"
            + ") FROM Publicidad p")
    List<PublicidadDTO> customFindAll();
    
    @Modifying
    @Query(value=" UPDATE public.pub SET cnt_view=cnt_view+1 WHERE id=?1 ",nativeQuery = true)
    void customIncreaseViewsById(Long id);
    
    @Modifying
    @Query(value=" UPDATE public.pub SET cnt_clk=cnt_clk+1 WHERE id=?1 ",nativeQuery = true)
    void customIncreaseClickById(Long id);
    
    @Modifying
    @Query(value=" UPDATE public.pub SET act=false WHERE CURRENT_DATE>dt_fin AND act ",nativeQuery = true)
    void stopAllOutDated();
}
