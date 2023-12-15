package nl.cofx.db.performance.mongodb;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    String contractId;
    int severity;
}
