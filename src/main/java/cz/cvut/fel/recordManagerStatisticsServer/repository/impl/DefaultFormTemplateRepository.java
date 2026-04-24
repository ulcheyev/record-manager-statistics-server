package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.repository.FormTemplateRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.CorrectAnswer;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.FormTemplateQuery;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class DefaultFormTemplateRepository implements FormTemplateRepository {

    private final EntityManager em;

    public DefaultFormTemplateRepository(
            @Qualifier("formgenEntityManager") EntityManager em) {
        this.em = em;
    }

    @Override
    public Map<URI, String> findLabelsByUris(Set<URI> templateUris) {
        final Map<URI, String> result = new HashMap<>();
        templateUris.forEach(uri -> result.put(uri, resolveLabel(uri)));
        return result;
    }

    private String resolveLabel(URI uri) {
        final String query = FormTemplateQuery.FIND_LABEL_BY_URI.formatted(uri, uri);

        List<?> results = em.createNativeQuery(query).getResultList();

        if (results == null || results.isEmpty() || results.getFirst() == null) {
            log.warn("No label found for template: {}", uri);
            return uri.toString();
        }

        log.debug("Resolved label for template={} label={}", uri, results.getFirst());
        return results.getFirst().toString();
    }

    @Override
    public List<CorrectAnswer> findCorrectAnswers() {
        String query = FormTemplateQuery.FIND_CORRECT_ANSWERS.formatted(
                Vocabulary.s_p_has_question_origin,
                Vocabulary.s_p_has_correct_answer);

        List<?> rows = em.createNativeQuery(query).getResultList();
        log.debug("findCorrectAnswers rows={}", rows.size());

        return rows.stream()
                .map(r -> (Object[]) r)
                .map(this::toCorrectAnswer)
                .toList();
    }

    private CorrectAnswer toCorrectAnswer(Object[] row) {
        final int QUESTION_ORIGIN = 0;
        final int CORRECT_URI = 1;
        final int CORRECT_LABEL = 2;

        URI questionOrigin = (URI) row[QUESTION_ORIGIN];
        URI correctUri = (URI) row[CORRECT_URI];
        String correctLabel = row[CORRECT_LABEL] == null ? null : row[CORRECT_LABEL].toString();

        return new CorrectAnswer(questionOrigin, correctUri, correctLabel);
    }
}