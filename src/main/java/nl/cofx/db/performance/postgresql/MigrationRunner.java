package nl.cofx.db.performance.postgresql;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MigrationRunner {

    private final String url;
    private final String username;

    public MigrationRunner(@Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username) {
        this.url = url;
        this.username = username;
    }

    @EventListener
    public void runMigrations(ApplicationStartedEvent event) {
        var flyway = Flyway.configure().dataSource(url, username, null).load();
        flyway.migrate();
    }
}
