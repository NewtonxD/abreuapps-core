package abreusapp.core.control.general;

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
    @Query(value="SELECT new abreusapp.core.control.general.PublicidadDTO("
            + "p.id,"
            + "p.titulo,"
            + "p.descripcion,"
            + "p.lg_imagen_video_direccion,"
            + "p.sm_imagen_video_direccion,"
            + "p.link_destino,"
            + "p.fecha_inicio,"
            + "p.fecha_fin,"
            + "p.conteo_clic,"
            + "p.conteo_view,"
            + "p.activo"
            + ") FROM Publicidad p order by p.conteo_view asc limit 1")
    PublicidadDTO customFindAll();
    
    @Modifying
    @Query(value=" update public.pub set cnt_clic=cnt_clic+1 where id=?1 ",nativeQuery = true)
    void customIncreaseById(Long id);
}
