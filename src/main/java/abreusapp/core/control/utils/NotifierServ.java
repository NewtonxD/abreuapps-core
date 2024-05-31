package abreusapp.core.control.utils;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author cabreu
 */

@RequiredArgsConstructor
public class NotifierServ {
    
    private final JdbcTemplate tpl;
    
    private static final String CHANNEL_NAME = "core_db_event";
    
    public Runnable createNotificationHandler(Consumer<PGNotification> consumer) {
        
        return () -> {
            var executor = Executors.newVirtualThreadPerTaskExecutor();
            executor.execute(()->{
                tpl.execute((Connection c) -> {
                    c.createStatement().execute("LISTEN " + CHANNEL_NAME);

                    PGConnection pgconn = c.unwrap(PGConnection.class); 
                    while(!Thread.currentThread().isInterrupted()) {
                        PGNotification[] nts = pgconn.getNotifications(1000);
                        if ( nts == null || nts.length == 0 ) {
                            continue;
                        }

                        for( PGNotification nt : nts) {
                            consumer.accept(nt);
                        }
                    }

                    return 0;
                });
            });
            executor.shutdown();
        };
        
    }
}
