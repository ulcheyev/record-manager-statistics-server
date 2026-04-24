package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

public final class RecordQuery {

    /**
     * Fetches record URIs matching the given type and filter.
     */
    public static final String FIND_ALL_WITH_FILTER = """
                SELECT ?r WHERE {
                    ?r a <%s> .
                    %s
                }
            """;

    /**
     * Fetches record URIs matching the given author username.
     */
    public static final String FIND_ALL_WITH_USERNAME_FILTER = """
            SELECT ?r WHERE {
                ?r a <%s> ;
                   <%s> ?author .
                ?author <%s> ?uname .
                FILTER(CONTAINS(LCASE(STR(?uname)), LCASE("%s")))
                %s
            }
            """;

    private RecordQuery() {
    }

}