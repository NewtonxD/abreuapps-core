/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.general;

import java.io.Serializable;

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
    String lnk_dst
) implements Serializable {}
