package nl.cofx.db.performance.mongodb;

import java.util.List;

public interface CustomMongoEventRepository<T, ID> {

    List<MongoEvent> find(String contractId, String id, Integer severity, int size);
}
