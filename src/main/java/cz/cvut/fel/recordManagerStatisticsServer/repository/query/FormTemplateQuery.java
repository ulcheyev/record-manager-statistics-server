package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

public final class FormTemplateQuery {

    private FormTemplateQuery() {}

    /**
     * Fetches rdfs:label from the named graph identified by the template URI.
     */
    public static final String FIND_LABEL_BY_URI =
            "SELECT (STR(?label) AS ?l) WHERE { " +
            "GRAPH <%s> { " +
            "<%s> <http://www.w3.org/2000/01/rdf-schema#label> ?label . " +
            "} } LIMIT 1";
}