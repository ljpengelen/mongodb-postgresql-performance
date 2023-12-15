package nl.cofx.db.performance.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories
public class MongoConfiguration {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }
}
