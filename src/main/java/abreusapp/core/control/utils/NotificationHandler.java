/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGNotification;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationHandler implements Consumer<PGNotification> {

    private final SSEServ SSEServicio;
    
    private final DateUtils FechaUtils;
    
    private static final Map<String,String> DBNOMBRE_VS_DOMINIO= Map.ofEntries(
            entry("public.gnr_dat","dtgnr"),
            entry("public.dat_grp","dtgrp"),
            entry("public.usr","usrmgr"),
            entry("transport.vhl","vhl"),
            entry("transport.pda","pda"),
            entry("transport.rta","rta")
    );

    @Override
    public void accept(PGNotification t) {
        String RawData=t.getParameter();
        
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(RawData);
            
            String DBNombre=jsonNode.get("schema").asText()+"."+jsonNode.get("table").asText();
            
            if(!DBNOMBRE_VS_DOMINIO.getOrDefault(DBNombre,"").isEmpty()){
                char DBOperacion=jsonNode.get("operation").asText().charAt(0);
                JsonNode DBData=jsonNode.get("data");
                String DBDate=FechaUtils.FromLocalDTToFormato1(jsonNode.get("timestamp").asText() );

                HashMap<String, Object> map = new HashMap<>();
                map.put(String.valueOf(DBOperacion), DBData);
                map.put("date", DBDate);
                SSEServicio.publicar(DBNOMBRE_VS_DOMINIO.get(DBNombre), map);
            }
            
        }catch(JsonProcessingException e){
            log.error(e.getMessage());
        }
    }
    
}
