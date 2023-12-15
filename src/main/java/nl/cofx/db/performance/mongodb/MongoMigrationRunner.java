package nl.cofx.db.performance.mongodb;

import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoMigrationRunner {

    private final MongoClient mongoClient;

    @EventListener
    public void runMigrations(ApplicationStartedEvent event) {
        var indexSpecification = Document.parse("""
                {
                    contractId: 1,
                    severity: -1,
                    _id: -1
                }""");
        mongoClient.getDatabase("performance").getCollection("mongoEvent").createIndex(indexSpecification);
    }
}
