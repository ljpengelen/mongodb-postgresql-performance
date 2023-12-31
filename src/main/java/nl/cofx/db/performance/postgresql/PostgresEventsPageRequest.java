package nl.cofx.db.performance.postgresql;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class PostgresEventsPageRequest {

    PostgresEventsPage page;
    String contractId;
}
