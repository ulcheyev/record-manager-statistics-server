package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class QueryBuilder {

    private static final DateTimeFormatter SPARQL_DT =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private QueryBuilder() {
    }

    /**
     * Builds an optional SPARQL interval filter block.
     * Returns empty string if interval is null or empty.
     */
    public static String intervalFilter(StatisticsInterval interval) {
        if (interval == null || interval.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("OPTIONAL { ?r <").append(Vocabulary.s_p_created).append("> ?created . }\n");

        if (interval.hasFrom()) {
            String from = interval.getFrom()
                    .atStartOfDay()
                    .atOffset(ZoneOffset.UTC)
                    .format(SPARQL_DT);

            sb.append("FILTER(!BOUND(?created) || ?created >= \"")
                    .append(from)
                    .append("\"^^xsd:dateTime)\n");
        }

        if (interval.hasTo()) {
            String to = interval.getTo()
                    .atTime(23, 59, 59)
                    .atOffset(ZoneOffset.UTC)
                    .format(SPARQL_DT);

            sb.append("FILTER(!BOUND(?created) || ?created <= \"")
                    .append(to)
                    .append("\"^^xsd:dateTime)\n");
        }

        return sb.toString();
    }


}