package nl.cofx.db.performance.mongodb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(MongoEventService.class)
class MongoEventServiceTest {

    private static final String CONTRACT_ID = "contractId";

    @Autowired
    MongoEventRepository mongoEventRepository;

    @Autowired
    MongoEventService mongoEventService;

    @BeforeEach
    void setUp() {
        mongoEventRepository.deleteAll();
    }

    @Test
    void returnsEventsPageByPage() {
        var eventOne = MongoEvent.builder()
                .id("1")
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventOne = mongoEventRepository.save(eventOne);
        var eventTwo = MongoEvent.builder()
                .id("2")
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventTwo = mongoEventRepository.save(eventTwo);
        var eventThree = MongoEvent.builder()
                .id("3")
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventThree = mongoEventRepository.save(eventThree);

        var response = mongoEventService.getEvents(MongoEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(MongoEventsPage.builder()
                        .size(1)
                        .build())
                .build());

        assertThat(response.getEvents()).containsExactly(eventThree);
        assertThat(response.getNextPage()).isNotNull();

        response = mongoEventService.getEvents(MongoEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(response.getNextPage())
                .build());

        assertThat(response.getEvents()).containsExactly(eventTwo);
        assertThat(response.getNextPage()).isNotNull();

        response = mongoEventService.getEvents(MongoEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(response.getNextPage())
                .build());

        assertThat(response.getEvents()).containsExactly(eventOne);
        assertThat(response.getNextPage()).isNotNull();

        response = mongoEventService.getEvents(MongoEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(response.getNextPage())
                .build());

        assertThat(response.getEvents()).isEmpty();
        assertThat(response.getNextPage()).isNull();
    }
}
