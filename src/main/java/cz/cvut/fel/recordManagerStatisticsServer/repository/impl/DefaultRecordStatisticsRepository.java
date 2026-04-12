package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.QueryBuilder;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQuery;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Repository
public class DefaultRecordStatisticsRepository extends AbstractRepository<RMRecord>
        implements RecordStatisticsRepository {

    public DefaultRecordStatisticsRepository(EntityManager em) {
        super(em);
    }

    @Override
    public long countByPhase(RecordPhase phase, StatisticsInterval interval) {
        log.debug("countByPhase phase={} interval={}", phase, interval);
        return em.createNativeQuery(
                        RecordQuery.COUNT_BY_PHASE.formatted(
                                Vocabulary.s_c_record,
                                Vocabulary.s_p_has_phase,
                                phase.getIri(),
                                QueryBuilder.intervalFilter(interval)
                        ), Long.class)
                .getSingleResult();
    }

    @Override
    public long countDistinctInstitutions(StatisticsInterval interval) {
        log.debug("countDistinctInstitutions interval={}", interval);
        return em.createNativeQuery(
                        RecordQuery.COUNT_DISTINCT_INSTITUTIONS.formatted(
                                Vocabulary.s_c_record,
                                Vocabulary.s_p_was_treated_at,
                                QueryBuilder.intervalFilter(interval)
                        ), Long.class)
                .getSingleResult();
    }

    @Override
    public long countDistinctAuthors(StatisticsInterval interval) {
        log.debug("countDistinctAuthors interval={}", interval);
        return em.createNativeQuery(
                        RecordQuery.COUNT_DISTINCT_AUTHORS.formatted(
                                Vocabulary.s_c_record,
                                Vocabulary.s_p_has_author,
                                QueryBuilder.intervalFilter(interval)
                        ), Long.class)
                .getSingleResult();
    }

    @Override
    public Instant findEarliestCreated(StatisticsInterval interval) {
        log.debug("findEarliestCreated interval={}", interval);
        return findInstant(RecordQuery.FIND_EARLIEST_CREATED.formatted(
                Vocabulary.s_c_record,
                Vocabulary.s_p_created,
                QueryBuilder.intervalFilter(interval)
        ));
    }

    @Override
    public Instant findLatestCreated(StatisticsInterval interval) {
        log.debug("findLatestCreated interval={}", interval);
        return findInstant(RecordQuery.FIND_LATEST_CREATED.formatted(
                Vocabulary.s_c_record,
                Vocabulary.s_p_created,
                QueryBuilder.intervalFilter(interval)
        ));
    }

    @Override
    public List<RMRecord> findAllByInterval(StatisticsInterval interval) {
        log.debug("findAllByInterval interval={}", interval);
        return em.createNativeQuery(
                        RecordQuery.FIND_ALL_WITH_FILTER.formatted(
                                Vocabulary.s_c_record,
                                QueryBuilder.intervalFilter(interval)
                        ), RMRecord.class)
                .getResultList();
    }

    private Instant findInstant(String sparql) {
        try {
            List<?> results = em.createNativeQuery(sparql, OffsetDateTime.class)
                    .getResultList();
            if (results == null || results.isEmpty() || results.get(0) == null) {
                log.debug("findInstant returned empty result");
                return null;
            }
            return ((OffsetDateTime) results.get(0)).toInstant();
        } catch (Exception e) {
            log.warn("Could not retrieve date value, returning null. Reason: {}",
                    e.getMessage());
            return null;
        }
    }
}