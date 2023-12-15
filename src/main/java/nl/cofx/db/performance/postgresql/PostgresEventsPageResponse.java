package nl.cofx.db.performance.postgresql;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class PostgresEventsPageResponse {

    PostgresEventsPage nextPage;
    @Builder.Default
    List<PostgresEvent> events = new ArrayList<>();
}
