package cz.cvut.fel.recordManagerStatisticsServer.repository;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public interface FormTemplateRepository {
    Map<URI, String> findLabelsByUris(Set<URI> templateUris);
}