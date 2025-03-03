package abreuapps.core.conf;

import jakarta.servlet.annotation.WebFilter;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */
@Component
@Order(1)
@WebFilter(urlPatterns = "/API/tiles/*")
public class SpecificRLFilter extends BaseRLFilter{

    public SpecificRLFilter(MessageSource messageSrc) {
        super(messageSrc);
    }

    @Override
    protected int getMaxRequests() {
        return 2500;
    }
    
}
