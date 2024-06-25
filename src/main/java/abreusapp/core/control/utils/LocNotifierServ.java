package abreusapp.core.control.utils;

import java.sql.Connection;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author cabreu
 */

@RequiredArgsConstructor
public class LocNotifierServ {

    private final JdbcTemplate tpl;
    
    private final SSEServ SSEServ;

    private static final String CHANNEL_NAME = "loc_db_event";

    private static final int DELAY = 3; // seconds

    public Runnable createNotificationHandler(){
        return (() -> {
            var executor = Executors.newVirtualThreadPerTaskExecutor();
            executor.execute(() -> {
                tpl.execute((Connection c) -> {

                    c.createStatement().execute("LISTEN " + CHANNEL_NAME);
                    PGConnection pgconn = c.unwrap(PGConnection.class);
                    
                    while (!Thread.currentThread().isInterrupted()) {

                        try {
                            
                            PGNotification[] nts = pgconn.getNotifications(1000);
                            
                            if (nts == null || nts.length == 0) {
                                continue;
                            }

                            Thread.sleep(Duration.ofSeconds(DELAY));
                            handleNotification();
                            
                        } catch (InterruptedException ex) {
                            Logger.getLogger(LocNotifierServ.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    return 0;
                });
            });
            executor.shutdown();
        });

    }

    private void handleNotification() {
        // Process the notification
        
        // sse publicar y realizar query
        System.out.println("\nHandling notification after delay\n");
    }
}
