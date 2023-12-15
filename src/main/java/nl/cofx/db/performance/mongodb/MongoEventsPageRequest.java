package nl.cofx.db.performance.mongodb;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class MongoEventsPageRequest {

    MongoEventsPage page;
    String contractId;
}
