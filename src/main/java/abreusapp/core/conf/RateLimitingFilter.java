package abreusapp.core.conf;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.core.annotation.Order;

@Component
@Order(1)
public class RateLimitingFilter implements Filter {

    private static final int MAX_REQUESTS = 2500; // Increased limit
    private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    private final Map<String, Long> requestTimesPerIpAddress = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIpAddress = httpRequest.getRemoteAddr();

        long currentTime = System.currentTimeMillis();
        requestCountsPerIpAddress.putIfAbsent(clientIpAddress, new AtomicInteger(0));
        requestTimesPerIpAddress.putIfAbsent(clientIpAddress, currentTime);

        long timeSinceLastRequest = currentTime - requestTimesPerIpAddress.get(clientIpAddress);
        if (timeSinceLastRequest > 60000) {
            requestCountsPerIpAddress.get(clientIpAddress).set(0);
            requestTimesPerIpAddress.put(clientIpAddress, currentTime);
        }
            
        if (requestCountsPerIpAddress.get(clientIpAddress).incrementAndGet()  > MAX_REQUESTS) {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too many requests - please try again later.");
            return;
        }

        chain.doFilter(request, response);
    }

   
}
