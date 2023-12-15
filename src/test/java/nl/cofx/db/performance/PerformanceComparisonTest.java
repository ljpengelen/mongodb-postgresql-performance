package nl.cofx.db.performance;

import lombok.extern.slf4j.Slf4j;
import nl.cofx.db.performance.mongodb.MongoEvent;
import nl.cofx.db.performance.mongodb.MongoEventRepository;
import nl.cofx.db.performance.mongodb.MongoEventService;
import nl.cofx.db.performance.mongodb.MongoEventsPage;
import nl.cofx.db.performance.mongodb.MongoEventsPageRequest;
import nl.cofx.db.performance.postgresql.PostgresEvent;
import nl.cofx.db.performance.postgresql.PostgresEventRepository;
import nl.cofx.db.performance.postgresql.PostgresEventService;
import nl.cofx.db.performance.postgresql.PostgresEventsPage;
import nl.cofx.db.performance.postgresql.PostgresEventsPageRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Disabled
@Slf4j
@SpringBootTest
public class PerformanceComparisonTest {

    private static final int PAGE_SIZE = 16_000;

    @Autowired
    MongoEventRepository mongoEventRepository;

    @Autowired
    MongoEventService mongoEventService;

    @Autowired
    PostgresEventRepository postgresEventRepository;

    @Autowired
    PostgresEventService postgresEventService;

    @Test
    void generateMongoEvents() {
        for (int i = 0; i < 40; ++i) {
            var events = new ArrayList<MongoEvent>();
            for (var j = 0; j < 100_000; ++j) {
                events.add(MongoEvent.builder()
                        .contractId(String.valueOf(i % 3))
                        .severity(j % 100)
                        .build());
            }
            mongoEventRepository.saveAll(events);
        }
    }

    @Test
    void generatePostgresEvents() {
        for (int i = 0; i < 40; ++i) {
            var events = new ArrayList<PostgresEvent>();
            for (var j = 0; j < 100_000; ++j) {
                events.add(PostgresEvent.builder()
                        .contractId(String.valueOf(i % 3))
                        .severity(j % 100)
                        .build());
            }
            postgresEventRepository.saveAll(events);
        }
    }

    @Test
    void retrieveAllEventsForContractFromMongoDb() {
        var stopWatch = new StopWatch("Mongo");
        var numberOfIterations = 5;
        for (var i = 0; i < numberOfIterations; ++i) {
            stopWatch.start(String.valueOf(i));
            retrieveMongoEventsOnce();
            stopWatch.stop();
        }
        /*
            StopWatch 'Mongo': 16.90953971 seconds
            ----------------------------------------
            Seconds       %       Task name
            ----------------------------------------
            03.660394     22%     0
            03.32834896   20%     1
            03.32197554   20%     2
            03.296343     19%     3
            03.30247821   20%     4
         */
        log.info(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }

    private void retrieveMongoEventsOnce() {
        var total = 0;
        var numberOfPages = 0;
        var request = MongoEventsPageRequest.builder()
                .page(MongoEventsPage.builder()
                        .size(PAGE_SIZE)
                        .build())
                .contractId("0")
                .build();
        var response = mongoEventService.getEvents(request);
        total += response.getEvents().size();
        ++numberOfPages;
        while (response.getNextPage() != null) {
            request = request.toBuilder()
                    .page(response.getNextPage())
                    .build();
            response = mongoEventService.getEvents(request);
            total += response.getEvents().size();
            ++numberOfPages;
        }
        log.info("Retrieved {} events in {} pages", total, numberOfPages);
    }

    @Test
    void retrieveAllEventsForContractFromPostgres() {
        var stopWatch = new StopWatch("Postgres");
        var numberOfIterations = 5;
        for (var i = 0; i < numberOfIterations; ++i) {
            stopWatch.start(String.valueOf(i));
            retrievePostgresEventsOnce();
            stopWatch.stop();
        }
        /*
            StopWatch 'Postgres': 23.767512792 seconds
            ------------------------------------------
            Seconds       %       Task name
            ------------------------------------------
            04.9838815    21%     0
            04.71800063   20%     1
            04.70958454   20%     2
            04.69276042   20%     3
            04.66328571   20%     4
         */
        log.info(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }

    private void retrievePostgresEventsOnce() {
        var total = 0;
        var numberOfPages = 0;
        var request = PostgresEventsPageRequest.builder()
                .page(PostgresEventsPage.builder()
                        .size(PAGE_SIZE)
                        .build())
                .contractId("0")
                .build();
        var response = postgresEventService.getEvents(request);
        total += response.getEvents().size();
        ++numberOfPages;
        while (response.getNextPage() != null) {
            request = request.toBuilder()
                    .page(response.getNextPage())
                    .build();
            response = postgresEventService.getEvents(request);
            total += response.getEvents().size();
            ++numberOfPages;
        }
        log.info("Retrieved {} events in {} pages", total, numberOfPages);
    }
}
