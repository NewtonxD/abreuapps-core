package abreuapps.core.control.utils;

import abreuapps.core.control.general.DatoDTO;
import abreuapps.core.control.general.PublicidadDTO;
import abreuapps.core.control.inventario.ProductoDTO;
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

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final Map<String, String> DBNOMBRE_VS_DOMINIO = Map.ofEntries(
            entry("public.gnr_dat", "dtgnr"),
            entry("public.usr", "usrmgr"),
            entry("transport.vhl", "vhl"),
            entry("transport.pda", "pda"),
            entry("transport.rta", "rta"),
            entry("transport.vhl_log", "vhl_log"),
            entry("public.vis_log", "vis_log"),
            entry("public.pub", "pub"),
            entry("inventory.prd","prd")
    );

    private final Map<String, Class> DOMINIO_VS_DTO = Map.ofEntries(
            entry("dtgnr", DatoDTO.class),
            entry("usrmgr", UsuarioDTO.class),
            entry("vhl", VehiculoDTO.class),
            entry("pda", ParadaDTO.class),
            entry("rta", RutaDTO.class),
            entry("vhl_log", LogVehiculoDTO.class),
            entry("pub", PublicidadDTO.class),
            entry("vis_log", DateUtils.class), //No va a hacer nada porque solo trabaja con records
            entry("prd",ProductoDTO.class)
    );

    @Override
    public void accept(PGNotification notification) {
        String rawData = notification.getParameter();

        try {
            JsonNode jsonNode = objectMapper.readTree(rawData);

            String dbName = jsonNode.get("schema").asText() + "." + jsonNode.get("table").asText();
            String domain = DBNOMBRE_VS_DOMINIO.getOrDefault(dbName, "");

            if (domain.isEmpty()) {
                return;
            }

            Map<String, Object> responseMap = new HashMap<>();

            char dbOperation = jsonNode.get("operation").asText().charAt(0);
            JsonNode dbData = jsonNode.get("data");

            String formattedDate = FechaUtils.FromLocalDTToFormato1(jsonNode.get("timestamp").asText());
            responseMap.put("date", formattedDate);

            if (DOMINIO_VS_DTO.get(domain).isRecord()) {
                responseMap.put(String.valueOf(dbOperation), objectMapper.treeToValue(dbData, DOMINIO_VS_DTO.get(domain)));
            }

            SSEServicio.publicar(domain, responseMap);

        } catch (JsonProcessingException e) {
            // Log the error for troubleshooting
            log.error("Error processing JSON: " + e.getMessage(), e);
        }
    }


}
