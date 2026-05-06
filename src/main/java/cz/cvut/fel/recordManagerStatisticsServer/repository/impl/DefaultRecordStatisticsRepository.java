package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.QueryBuilder;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQuery;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQueryCriteria;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        em.clear();
        return em.createNativeQuery(
                        RecordQuery.FIND_ALL_WITH_FILTER.formatted(
                                Vocabulary.s_c_record,
                                Vocabulary.s_c_patient_record,
                                QueryBuilder.intervalFilter(interval)
                        ), RMRecord.class)
                .getResultList();
    }


    @Override
    public List<RMRecord> findAllByCriteria(RecordQueryCriteria criteria) {
        log.debug("findAllByCriteria criteria={}", criteria);
        em.clear();
        if (!criteria.hasUsernameFilter()) {
            return findAllByInterval(criteria.getInterval());
        }

        String sparql = RecordQuery.FIND_ALL_WITH_USERNAME_FILTER.formatted(
                Vocabulary.s_c_patient_record,
                Vocabulary.s_p_has_author,
                Vocabulary.s_p_accountName,
                escapeSparql(criteria.getAuthorUsernameLike()),
                QueryBuilder.intervalFilter(criteria.getInterval())
        );


        log.debug("findAllByCriteria filter={}", criteria.getAuthorUsernameLike());
        return em.createNativeQuery(sparql, RMRecord.class).getResultList();
    }

    @Override
    public Map<URI, RecordPhase> findFreshPhasesByRecordUris(Set<URI> recordUris) {
        if (recordUris.isEmpty()) {
            return Map.of();
        }

        String values = recordUris.stream()
                .map(uri -> "<" + uri + ">")
                .collect(Collectors.joining(" "));

        String sparql = """
        SELECT ?record ?phase WHERE {
            VALUES ?record { %s }

            GRAPH ?record {
                ?record <%s> ?phase .
            }
        }
        """.formatted(values, Vocabulary.s_p_has_phase);

        List<Object[]> rows = em.createNativeQuery(sparql).getResultList();

        return rows.stream()
                .collect(Collectors.toMap(
                        row -> (URI) row[0],
                        row -> RecordPhase.fromIri((URI) row[1])
                ));
    }


    private String escapeSparql(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}