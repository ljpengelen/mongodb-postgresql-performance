package nl.cofx.db.performance.postgresql;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
@Import(PostgresMigrationRunner.class)
class PostgresEventRepositoryTest {

    private static final String CONTRACT_ID = "contractId";
    private static final String OTHER_CONTRACT_ID = "otherContractId";

    @Autowired
    PostgresEventRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void returnsEventsOrderedBySeverityAndId() {
        var eventOne = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventOne = repository.save(eventOne);
        var eventTwo = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventTwo = repository.save(eventTwo);
        var eventThree = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventThree = repository.save(eventThree);

        var events = repository.find(CONTRACT_ID, null, null, 10);

        assertThat(events).containsExactly(eventThree, eventTwo, eventOne);
    }

    @Test
    void returnsEventsForContractId() {
        var eventForContract = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventForContract = repository.save(eventForContract);
        var eventForOtherContract = PostgresEvent.builder()
                .contractId(OTHER_CONTRACT_ID)
                .severity(0)
                .build();
        eventForOtherContract = repository.save(eventForOtherContract);

        assertThat(repository.find(CONTRACT_ID, null, null, 10)).containsExactly(eventForContract);
        assertThat(repository.find(OTHER_CONTRACT_ID, null, null, 10)).containsExactly(eventForOtherContract);
    }

    @Test
    void returnsEventsByIdAndSeverity() {
        var eventOne = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventOne = repository.save(eventOne);
        var eventTwo = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventTwo = repository.save(eventTwo);
        var eventThree = PostgresEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventThree = repository.save(eventThree);

        assertThat(repository.find(CONTRACT_ID, null, null, 1)).containsExactly(eventThree);
        assertThat(repository.find(CONTRACT_ID, eventThree.getId(), eventThree.getSeverity(), 2)).containsExactly(eventTwo, eventOne);
    }

    @Test
    void rejectsEventWithoutContractId() {
        var event = PostgresEvent.builder()
                .severity(1)
                .build();
        assertThatThrownBy(() -> repository.save(event))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must not be null");
    }
}
