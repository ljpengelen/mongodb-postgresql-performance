package nl.cofx.db.performance.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MongoEventService {

    private final MongoEventRepository repository;

    public MongoEventsPageResponse getEvents(MongoEventsPageRequest request) {
        var page = request.getPage();
        var events = repository.find(request.getContractId(), page.getId(), page.getSeverity(), page.getSize());

        return MongoEventsPageResponse.builder()
                .events(events)
                .nextPage(nextPage(events, page))
                .build();
    }

    private MongoEventsPage nextPage(List<MongoEvent> events, MongoEventsPage page) {
        if (events == null || events.size() < page.getSize()) return null;

        var lastEvent = events.getLast();

        return MongoEventsPage.builder()
                .id(lastEvent.getId())
                .severity(lastEvent.getSeverity())
                .size(page.getSize())
                .build();
    }
}
