package nl.cofx.db.performance.postgresql;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostgresEventService {

    private final PostgresEventRepository repository;

    public PostgresEventsPageResponse getEvents(PostgresEventsPageRequest request) {
        var page = request.getPage();
        var events = repository.find(request.getContractId(), page.getId(), page.getSeverity(), page.getSize());

        return PostgresEventsPageResponse.builder()
                .events(events)
                .nextPage(nextPage(events, page))
                .build();
    }

    private PostgresEventsPage nextPage(List<PostgresEvent> events, PostgresEventsPage page) {
        if (events == null || events.size() < page.getSize()) return null;

        var lastEvent = events.getLast();

        return PostgresEventsPage.builder()
                .id(lastEvent.getId())
                .severity(lastEvent.getSeverity())
                .size(page.getSize())
                .build();
    }
}
