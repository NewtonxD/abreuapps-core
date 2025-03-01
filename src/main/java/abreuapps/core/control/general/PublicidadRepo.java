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
            + "p.imagen_video_direccion,"
            + "p.link_destino,"
            + "p.empresa.dato,"
            + "p.activo"
            + ") FROM Publicidad p WHERE p.activo ORDER BY coalesce(p.conteo_view,0) ASC LIMIT 1")
    PublicidadDTO customFindCurrent();
    
    @Query(value="SELECT new abreuapps.core.control.general.PublicidadDTO("
            + "p.id,"
            + "p.titulo,"
            + "p.descripcion,"
            + "p.imagen_video_direccion,"
            + "p.link_destino,"
            + "p.empresa.dato,"
            + "p.activo"
            + ") FROM Publicidad p")
    List<PublicidadDTO> customFindAll();
    
    @Modifying
    @Query(value=" UPDATE public.pub SET cnt_view=coalesce(cnt_view,0)+1 WHERE id=?1 ",nativeQuery = true)
    void customIncreaseViewsById(Long id);
    
    @Modifying
    @Query(value=" UPDATE public.pub SET cnt_clk=coalesce(cnt_clk,0)+1 WHERE id=?1 ",nativeQuery = true)
    void customIncreaseClickById(Long id);
    
    @Modifying
    @Query(value=" UPDATE public.pub SET act=false WHERE CURRENT_DATE>dt_fin AND act ",nativeQuery = true)
    void stopAllOutDated();
    
    @Modifying
    @Query(value="""
        DELETE FROM public.sta WHERE DATE(dt)<CURRENT_DATE and DATE(dt)>=CURRENT_DATE - interval '8 days';
        INSERT into public.sta(dt,cnt_viw) SELECT DATE(dt) AS date, COUNT(*) AS record_count FROM public.vis_log WHERE DATE(dt)<CURRENT_DATE and DATE(dt)>=CURRENT_DATE - interval '8 days' GROUP BY DATE(dt) ORDER BY  date;
        DELETE FROM  public.vis_log WHERE dt < CURRENT_DATE - INTERVAL '31 days';
    """,nativeQuery = true)
    void saveStatictics();
    
    @Modifying
    @Query(value="insert into vis_log (dt) values (now())", nativeQuery = true)
    void addClientVisit();
    
    @Query(value="select count(vl.id) from public.vis_log vl where vl.dt>=current_date", nativeQuery = true)
    Integer consultarTotalViewsHoy();
                                                                             

}
