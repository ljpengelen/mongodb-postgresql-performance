package nl.cofx.db.performance.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoEventRepository extends MongoRepository<MongoEvent, String>, CustomMongoEventRepository<MongoEvent, String> {}
