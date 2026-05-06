package cz.cvut.fel.recordManagerStatisticsServer.repository;

import cz.cvut.fel.recordManagerStatisticsServer.repository.model.CorrectAnswer;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FormTemplateRepository {
    Map<URI, String> findLabelsByUris(Set<URI> templateUris);

    List<CorrectAnswer> findCorrectAnswers();
}