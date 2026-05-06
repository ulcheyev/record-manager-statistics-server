package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.repository.model.AnswerCounts;
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

    @Override
    public Map<URI, AnswerCounts> countByRecord(StatisticsInterval interval) {
        return count(null, interval);
    }

    private Map<URI, AnswerCounts> count(String groupingPredicate, StatisticsInterval interval) {
        em.clear();
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
        final int EVALUABLE_QUESTIONS = 1;
        final int INFORMATIVE_QUESTIONS = 2;
        final int EVALUABLE_ANSWERS = 3;
        final int INFORMATIVE_ANSWERS = 4;
        final int CORRECT_ANSWERS = 5;

        URI entityUri = (URI) row[ENTITY_URI];

        long evaluableQuestions = number(row[EVALUABLE_QUESTIONS]);
        long informativeQuestions = number(row[INFORMATIVE_QUESTIONS]);
        long evaluableAnswers = number(row[EVALUABLE_ANSWERS]);
        long informativeAnswers = number(row[INFORMATIVE_ANSWERS]);
        long correctAnswers = number(row[CORRECT_ANSWERS]);

        return new AnswerCounts(
                entityUri,
                evaluableQuestions,
                informativeQuestions,
                evaluableAnswers,
                informativeAnswers,
                correctAnswers
        );
    }

    private long number(Object value) {
        return value == null ? 0 : ((Number) value).longValue();
    }
}