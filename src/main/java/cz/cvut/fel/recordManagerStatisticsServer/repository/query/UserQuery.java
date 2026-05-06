package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

public final class UserQuery {

    public static final String FIND_BY_USERNAME = """
            SELECT ?x WHERE {
                ?x a <%s> ;
                   <%s> ?username .
                FILTER(STR(?username) = "%s")
            }
            """;

    private UserQuery() {
    }
}