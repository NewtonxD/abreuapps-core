package abreuapps.core.conf;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.annotation.WebListener;
import java.util.EnumSet;

/**
 *
 * @author cabreu
 */
@WebListener
public class SessionTracker implements ServletContextListener {

    @Override
    public void contextInitialized (ServletContextEvent event) {
        event.getServletContext()
             .setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }

    @Override
    public void contextDestroyed (ServletContextEvent sce) {
    }
}
