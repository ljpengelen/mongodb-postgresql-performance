package nl.cofx.db.performance.mongodb;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MongoEventsPageRequest {

    MongoEventsPage page;
    String contractId;
}
