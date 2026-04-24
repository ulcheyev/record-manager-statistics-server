package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.QueryBuilder;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQuery;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQueryCriteria;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DefaultRecordStatisticsRepository extends AbstractRepository<RMRecord>
        implements RecordStatisticsRepository {

    public DefaultRecordStatisticsRepository(EntityManager em) {
        super(em);
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


    @Override
    public List<RMRecord> findAllByCriteria(RecordQueryCriteria criteria) {
        if (!criteria.hasUsernameFilter()) {
            return findAllByInterval(criteria.getInterval());
        }

        String sparql = RecordQuery.FIND_ALL_WITH_USERNAME_FILTER.formatted(
                Vocabulary.s_c_record,
                Vocabulary.s_p_has_author,
                Vocabulary.s_p_accountName,
                escapeSparql(criteria.getAuthorUsernameLike()),
                QueryBuilder.intervalFilter(criteria.getInterval())
        );

        log.debug("findAllByCriteria filter={}", criteria.getAuthorUsernameLike());
        return em.createNativeQuery(sparql, RMRecord.class).getResultList();
    }

    private String escapeSparql(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}