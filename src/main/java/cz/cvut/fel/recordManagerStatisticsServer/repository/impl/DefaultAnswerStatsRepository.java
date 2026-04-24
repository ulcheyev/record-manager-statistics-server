package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.AnswerStatsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.FormTemplateRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.CorrectAnswer;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.AnswerCountsQueryBuilder;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DefaultAnswerStatsRepository implements AnswerStatsRepository {

    private final EntityManager em;
    private final FormTemplateRepository formTemplateRepo;

    @Override
    public Map<URI, AnswerCounts> countByAuthor(StatisticsInterval interval) {
        return count(Vocabulary.s_p_has_author, interval);
    }

    @Override
    public Map<URI, AnswerCounts> countByInstitution(StatisticsInterval interval) {
        return count(Vocabulary.s_p_was_treated_at, interval);
    }

    private Map<URI, AnswerCounts> count(String groupingPredicate, StatisticsInterval interval) {
        List<CorrectAnswer> correctAnswers = formTemplateRepo.findCorrectAnswers();

        String sparql = new AnswerCountsQueryBuilder(groupingPredicate, interval, correctAnswers)
                .build();

        log.debug("count predicate={} correctAnswers={}",
                groupingPredicate, correctAnswers.size());

        List<?> rows = em.createNativeQuery(sparql).getResultList();
        return rows.stream()
                .map(r -> (Object[]) r)
                .map(this::toAnswerCounts)
                .collect(Collectors.toMap(AnswerCounts::entityUri, Function.identity()));
    }

    private AnswerCounts toAnswerCounts(Object[] row) {
        final int ENTITY_URI = 0;
        final int TOTAL_ANSWERS = 1;
        final int EVALUABLE_ANSWERS = 2;
        final int CORRECT_ANSWERS = 3;

        URI entityUri = (URI) row[ENTITY_URI];
        long totalAnswers = ((Number) row[TOTAL_ANSWERS]).longValue();
        long evaluableAnswers = ((Number) row[EVALUABLE_ANSWERS]).longValue();
        long correctAnswers = ((Number) row[CORRECT_ANSWERS]).longValue();

        return new AnswerCounts(entityUri, totalAnswers, evaluableAnswers, correctAnswers);
    }
}