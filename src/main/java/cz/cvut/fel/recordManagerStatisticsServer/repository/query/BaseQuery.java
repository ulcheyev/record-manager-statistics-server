package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

public final class BaseQuery {

    public static final String FIND_ALL = """
            SELECT ?x WHERE {
                ?x a ?type .
            }
            """;
    public static final String COUNT_ALL = """
            SELECT (COUNT(?x) AS ?count) WHERE {
                ?x a ?type .
            }
            """;

    private BaseQuery() {
    }
}