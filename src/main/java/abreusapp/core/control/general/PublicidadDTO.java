/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.general;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cabreu
 */
public record PublicidadDTO( 
    Long id,
    String tit,
    String dsc,
    String lg_img_vid,
    String sm_img_vid,
    String lnk_dst,
    Date dt_str,
    Date dt_fin,
    Integer cnt_clk,
    Integer cnt_view,
    Boolean act
) implements Serializable {}
