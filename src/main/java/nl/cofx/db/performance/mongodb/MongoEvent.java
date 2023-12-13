package nl.cofx.db.performance.mongodb;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@Value
public class MongoEvent {

    @Id
    String id;
    String contractId;
    int severity;
}
