package abreuapps.core.conf;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.annotation.WebListener;
import java.util.EnumSet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


/**
 *
 * @author cabreu
 */

@WebListener
public class SessionTracker implements ServletContextListener, HttpSessionListener {

    private static final Logger logger = Logger.getLogger(SessionTracker.class.getName());

    private final Set<HttpSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        logger.info("Context initialized and session tracking set to COOKIE mode.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        for (HttpSession session : sessions) {
            try {
                session.invalidate();
                logger.info("Invalidated session: " + session.getId());
            } catch (IllegalStateException ex) {
                logger.warning("Session already invalidated: " + session.getId());
            }
        }
        sessions.clear();
        logger.info("All sessions have been invalidated and cleared.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.add(session);
        //logger.info("Session created: " + session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.remove(session);
        //logger.info("Session destroyed: " + session.getId());
    }
}

