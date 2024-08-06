package abreusapp.core.control.general;

import abreusapp.core.control.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author cabreu
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pub",schema = "public")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@PublicidadId")
public class Publicidad {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "tit")
    private String titulo;

    @Column(name = "dsc")
    private String descripcion;
    
    @Column(name = "lg_img_vid")
    private String lg_imagen_video_direccion;
    
    @Column(name = "sm_img_vid")
    private String sm_imagen_video_direccion;
    
    @Column(name = "lnk_dst")
    private String link_destino;
    
    @Column(name= "dt_str")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_inicio;
    
    @Column(name= "dt_fin")
    @Temporal(value = TemporalType.DATE)
    private Date fecha_fin;
    
    @Column(name = "cnt_clk")
    private Integer conteo_clic;
    
    @Column(name = "cnt_view")
    private Integer conteo_view;
    
    @Column(name = "act")
    private Boolean activo;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Usuario hecho_por;
    
    @Column(name= "mde_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Usuario actualizado_por;
    
    @Column(name= "upd_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;
    
}
