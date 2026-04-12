package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

public final class RecordQuery {

    public static final String COUNT_BY_PHASE = """
            SELECT (COUNT(?r) AS ?count) WHERE {
                GRAPH ?r {
                    ?r a <%s> ;
                       <%s> <%s> .
                    %s
                }
            }
            """;

    // -------------------------------------------------------------------------
    // Count queries
    // -------------------------------------------------------------------------
    public static final String COUNT_DISTINCT_INSTITUTIONS = """
            SELECT (COUNT(DISTINCT ?i) AS ?count) WHERE {
                GRAPH ?r {
                    ?r a <%s> ;
                       <%s> ?i .
                    %s
                }
            }
            """;
    public static final String COUNT_DISTINCT_AUTHORS = """
            SELECT (COUNT(DISTINCT ?a) AS ?count) WHERE {
                GRAPH ?r {
                    ?r a <%s> ;
                       <%s> ?a .
                    %s
                }
            }
            """;
    public static final String FIND_EARLIEST_CREATED = """
            SELECT (MIN(?created) AS ?min) WHERE {
                {
                    SELECT ?created WHERE {
                        GRAPH ?r {
                            ?r a <%s> ;
                               <%s> ?created .
                            %s
                        }
                    }
                }
            }
            """;

    // -------------------------------------------------------------------------
    // Period bound queries
    // -------------------------------------------------------------------------
    public static final String FIND_LATEST_CREATED = """
            SELECT (MAX(?created) AS ?max) WHERE {
                {
                    SELECT ?created WHERE {
                        GRAPH ?r {
                            ?r a <%s> ;
                               <%s> ?created .
                            %s
                        }
                    }
                }
            }
            """;
    /**
     * Fetches record URIs matching the given type and filter.
     */
    public static final String FIND_ALL_WITH_FILTER = """
                SELECT ?r WHERE {
                    ?r a <%s> .
                    %s
                }
            """;


    private RecordQuery() {
    }

}