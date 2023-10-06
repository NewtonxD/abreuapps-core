/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 *  
 * Utilidades para manejar y convertir fechas de un formato a otro.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Service
public class DateUtils {
    

    public SimpleDateFormat FechaFormato1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
    
    public SimpleDateFormat FechaFormato2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public Date Formato2ToDate(String str) throws ParseException {
        return FechaFormato2.parse(str);
    }
    
    public Date Formato1ToDate(String str) throws ParseException {
        return FechaFormato1.parse(str);
    }

}
