package abreuapps.core.control.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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
    
    
    public Date Formato2ToDate(String str){
        try{
        return FechaFormato2.parse(str);
        } catch (ParseException e){
            log.error(" Error al parsear fecha : "+e.getLocalizedMessage());
            return null;
        }
    }
    
    public Date Formato1ToDate(String str) {
        try{
            return FechaFormato1.parse(str);
        } catch (ParseException e){
            log.error(" Error al parsear fecha : "+e.getLocalizedMessage());
            return null;
        }
    }
    
    public String FromLocalDTToFormato1(String timestampString){
        LocalDateTime timestamp = LocalDateTime.parse(timestampString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Date fecha = Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant());
        return FechaFormato1.format(fecha);
    }
    
    public String DateToFormato1(Date dt){
        return FechaFormato1.format(dt);
    }
    
    public String DateToFormato2(Date dt){
        return FechaFormato2.format(dt);
    }
}
