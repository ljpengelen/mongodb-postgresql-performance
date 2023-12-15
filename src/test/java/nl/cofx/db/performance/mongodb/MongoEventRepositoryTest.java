package nl.cofx.db.performance.mongodb;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import({LocalValidatorFactoryBean.class, MongoConfiguration.class})
class MongoEventRepositoryTest {

    private static final String CONTRACT_ID = "contractId";
    private static final String OTHER_CONTRACT_ID = "otherContractId";

    @Autowired
    MongoEventRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void returnsEventsOrderedBySeverityAndId() {
        var eventOne = MongoEvent.builder()
                .id("1")
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventOne = repository.save(eventOne);
        var eventTwo = MongoEvent.builder()
                .id("2")
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventTwo = repository.save(eventTwo);
        var eventThree = MongoEvent.builder()
                .id("3")
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventThree = repository.save(eventThree);

        var events = repository.find(CONTRACT_ID, null, null, 10);

        assertThat(events).containsExactly(eventThree, eventTwo, eventOne);
    }

    @Test
    void returnsEventsForContractId() {
        var eventForContract = MongoEvent.builder()
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventForContract = repository.save(eventForContract);
        var eventForOtherContract = MongoEvent.builder()
                .contractId(OTHER_CONTRACT_ID)
                .severity(0)
                .build();
        eventForOtherContract = repository.save(eventForOtherContract);

        assertThat(repository.find(CONTRACT_ID, null, null, 10)).containsExactly(eventForContract);
        assertThat(repository.find(OTHER_CONTRACT_ID, null, null, 10)).containsExactly(eventForOtherContract);
    }

    @Test
    void returnsEventsByIdAndSeverity() {
        var eventOne = MongoEvent.builder()
                .id("1")
                .contractId(CONTRACT_ID)
                .severity(0)
                .build();
        eventOne = repository.save(eventOne);
        var eventTwo = MongoEvent.builder()
                .id("2")
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventTwo = repository.save(eventTwo);
        var eventThree = MongoEvent.builder()
                .id("3")
                .contractId(CONTRACT_ID)
                .severity(3)
                .build();
        eventThree = repository.save(eventThree);

        assertThat(repository.find(CONTRACT_ID, null, null, 1)).containsExactly(eventThree);
        assertThat(repository.find(CONTRACT_ID, eventThree.getId(), eventThree.getSeverity(), 2)).containsExactly(eventTwo, eventOne);
    }

    @Test
    void rejectsEventWithoutContractId() {
        var event = MongoEvent.builder()
                .severity(1)
                .build();
        assertThatThrownBy(() -> repository.save(event))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("contractId: must not be null");
    }
}
