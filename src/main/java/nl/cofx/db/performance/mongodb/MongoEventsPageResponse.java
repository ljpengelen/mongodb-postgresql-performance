package nl.cofx.db.performance.mongodb;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class MongoEventsPageResponse {

    MongoEventsPage nextPage;
    @Builder.Default
    List<MongoEvent> events = new ArrayList<>();
}
