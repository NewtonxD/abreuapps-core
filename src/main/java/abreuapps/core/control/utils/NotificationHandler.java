package abreuapps.core.control.utils;

import abreuapps.core.control.general.DatoDTO;
import abreuapps.core.control.transporte.LogVehiculoDTO;
import abreuapps.core.control.transporte.ParadaDTO;
import abreuapps.core.control.transporte.RutaDTO;
import abreuapps.core.control.transporte.VehiculoDTO;
import abreuapps.core.control.usuario.UsuarioDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGNotification;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */
@Component
@RequiredArgsConstructor
public class NotificationHandler implements Consumer<PGNotification> {

    private final SSEServ SSEServicio;

    private final DateUtils FechaUtils;

    private final Map<String, String> DBNOMBRE_VS_DOMINIO = Map.ofEntries(
            entry("public.gnr_dat", "dtgnr"),
            entry("public.usr", "usrmgr"),
            entry("transport.vhl", "vhl"),
            entry("transport.pda", "pda"),
            entry("transport.rta", "rta"),
            entry("transport.vhl_log", "vhl_log")
    );

    private final Map<String, Class> DOMINIO_VS_DTO = Map.ofEntries(
            entry("dtgnr", DatoDTO.class),
            entry("usrmgr", UsuarioDTO.class),
            entry("vhl", VehiculoDTO.class),
            entry("pda", ParadaDTO.class),
            entry("rta", RutaDTO.class),
            entry("vhl_log", LogVehiculoDTO.class)
    );

    @Override
    public void accept(PGNotification t) {
        String RawData = t.getParameter();

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(RawData);

            String DBNombre = jsonNode.get("schema").asText() + "." + jsonNode.get("table").asText();
            String Dominio = DBNOMBRE_VS_DOMINIO.getOrDefault(DBNombre, "");

            if (!Dominio.isEmpty()) {

                HashMap<String, Object> map = new HashMap<>();

                char DBOperacion = jsonNode.get("operation").asText().charAt(0);
                JsonNode DBData = jsonNode.get("data");

                String DBDate = FechaUtils.FromLocalDTToFormato1(jsonNode.get("timestamp").asText());
                map.put("date", DBDate);

                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                map.put(String.valueOf(DBOperacion), objectMapper.treeToValue(DBData, DOMINIO_VS_DTO.get(Dominio)));


                SSEServicio.publicar(Dominio, map);

            }

        } catch (JsonProcessingException e) {
            //log.error(e.getMessage());
        }
    }

}
