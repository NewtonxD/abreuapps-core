/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
public class DateUtils {
    

    public SimpleDateFormat FechaFormato1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
    
    public SimpleDateFormat FechaFormato2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // Utility method to convert any TemporalAccessor to Date
    public Date TemporalAccToDate(TemporalAccessor temporalAccessor) {
        return Date.from(LocalDateTime.from(temporalAccessor).toInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS));
    }
    
    public Date Formato2ToDate(String str) throws ParseException {
        return FechaFormato2.parse(str);
    }
    
    public Date Formato1ToDate(String str) throws ParseException {
        return FechaFormato1.parse(str);
    }

}
