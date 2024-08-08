package abreuapps.core.conf;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

@Component
public class LocationFilter implements Filter {

    private final DatabaseReader dbReader;
    
    public static final String[] PERMIT_CONTINENTS = new String[] {"NA","SA"}; // NORTH AND SOUTH AMERICA

    public LocationFilter() throws IOException {
        File database = new File("src/main/resources/GeoLite2-Country.mmdb");
        this.dbReader = new DatabaseReader.Builder(database).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ipAddress = httpRequest.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);

        try {
            CountryResponse countryResponse = dbReader.country(inetAddress);
            String continentCode = countryResponse.getContinent().getCode();

            if (Arrays.stream(PERMIT_CONTINENTS).anyMatch(continentCode::equals)) {
                chain.doFilter(request, response);
            } else {
                response.getWriter().write("Accesso denegado. Si esta utilizando una VPN desactivela y vuelva a intentarlo.");
            }
        } catch (GeoIp2Exception e) {
            // permit if cant identify ip
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialize filter if needed
    }

    @Override
    public void destroy() {
        // Clean up resources if needed
    }
}
