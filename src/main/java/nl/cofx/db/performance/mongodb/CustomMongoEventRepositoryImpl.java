package nl.cofx.db.performance.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class CustomMongoEventRepositoryImpl implements CustomMongoEventRepository {

    private static final String ID_FIELD = "_id";
    private static final String SEVERITY_FIELD = "severity";

    private final MongoTemplate mongoTemplate;

    @Override
    public List<MongoEvent> find(String contractId, String id, Integer severity, int size) {
        var query = new Query();
        query.addCriteria(Criteria.where("contractId").is(contractId));
        if (id != null && severity != null)
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(SEVERITY_FIELD).is(severity).and(ID_FIELD).lt(id),
                    Criteria.where(SEVERITY_FIELD).lt(severity)));
        query.with(Sort.by(Sort.Order.desc(SEVERITY_FIELD), Sort.Order.desc(ID_FIELD)));
        query.limit(size);
        return mongoTemplate.find(query, MongoEvent.class);
    }
}
