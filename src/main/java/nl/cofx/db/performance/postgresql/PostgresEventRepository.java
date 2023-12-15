package nl.cofx.db.performance.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostgresEventRepository extends JpaRepository<PostgresEvent, Long> {

    @Query("SELECT e FROM PostgresEvent e WHERE e.contractId = ?1 AND " +
            "((e.severity = ?3 AND e.id < ?2) OR " +
            "e.severity < ?3 OR " +
            "(?2 IS NULL AND ?3 IS NULL)) " +
            "ORDER BY severity DESC, id DESC " +
            "LIMIT ?4")
    List<PostgresEvent> find(String contractId, Long id, Integer severity, int size);

    @Modifying
    @Query(value = "DELETE FROM postgres_event", nativeQuery = true)
    void deleteAll();
}
