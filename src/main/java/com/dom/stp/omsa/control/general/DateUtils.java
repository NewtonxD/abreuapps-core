/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.general;

/**
 *
 * @author cabreu
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class DateUtils implements Converter<String, Date> {
    
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public SimpleDateFormat FechaFormato1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
    
    public SimpleDateFormat FechaFormato2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public Date convert(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            // Handle the parse exception appropriately
            log.error(" Error al parsear fecha : "+e.getLocalizedMessage());
            return null;
        }
    }
    
    
    public Date Formato2ToDate(String str) throws ParseException {
        try{
        return FechaFormato2.parse(str);
        } catch (ParseException e){
            log.error(" Error al parsear fecha : "+e.getLocalizedMessage());
            return null;
        }
    }
    
    public Date Formato1ToDate(String str) throws ParseException {
        try{
            return FechaFormato1.parse(str);
        } catch (ParseException e){
            log.error(" Error al parsear fecha : "+e.getLocalizedMessage());
            return null;
        }
    }
}
