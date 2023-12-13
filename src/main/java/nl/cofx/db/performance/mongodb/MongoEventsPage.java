package nl.cofx.db.performance.mongodb;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MongoEventsPage {

    int size;

    String id;
    Integer severity;
}
