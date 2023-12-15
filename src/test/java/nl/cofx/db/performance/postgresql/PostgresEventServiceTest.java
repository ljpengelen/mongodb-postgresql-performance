package nl.cofx.db.performance.postgresql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
@Import(PostgresEventService.class)
class PostgresEventServiceTest {

    private static final String CONTRACT_ID = "contractId";

    @Autowired
    PostgresEventRepository postgresEventRepository;

    @Autowired
    PostgresEventService postgresEventService;

    @BeforeEach
    void setUp() {
        postgresEventRepository.deleteAll();
    }

    @Test
    void returnsEventsPageByPage() {
        var eventOne = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventOne = postgresEventRepository.save(eventOne);
        var eventTwo = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventTwo = postgresEventRepository.save(eventTwo);
        var eventThree = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventThree = postgresEventRepository.save(eventThree);

        var response = postgresEventService.getEvents(PostgresEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(PostgresEventsPage.builder()
                        .size(1)
                        .build())
                .build());

        assertThat(response.getEvents()).containsExactly(eventThree);
        assertThat(response.getNextPage()).isNotNull();

        response = postgresEventService.getEvents(PostgresEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(response.getNextPage())
                .build());

        assertThat(response.getEvents()).containsExactly(eventTwo);
        assertThat(response.getNextPage()).isNotNull();

        response = postgresEventService.getEvents(PostgresEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(response.getNextPage())
                .build());

        assertThat(response.getEvents()).containsExactly(eventOne);
        assertThat(response.getNextPage()).isNotNull();

        response = postgresEventService.getEvents(PostgresEventsPageRequest.builder()
                .contractId(CONTRACT_ID)
                .page(response.getNextPage())
                .build());

        assertThat(response.getEvents()).isEmpty();
        assertThat(response.getNextPage()).isNull();
    }
}
