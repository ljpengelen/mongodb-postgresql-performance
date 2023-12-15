package nl.cofx.db.performance.postgresql;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PostgresEventsPage {

    int size;

    Long id;
    Integer severity;
}
