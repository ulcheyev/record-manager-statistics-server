package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.CorrectAnswer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class AnswerCountsQueryBuilder {

    private static final String TEMPLATE = """
            SELECT ?entity
                   (COUNT(DISTINCT ?q)           AS ?totalAnswers)
                   (COUNT(DISTINCT ?evaluableQ)  AS ?evaluableAnswers)
                   (COUNT(DISTINCT ?correctQ)    AS ?correctAnswers)
            WHERE {
                GRAPH ?r {
                    ?r a <{recordType}> ;
                       <{groupingPredicate}> ?entity ;
                       <{phasePredicate}> <{completedPhase}> ;
                       <{hasQuestion}> ?root .
                    ?root <{hasRelatedQuestion}> ?q .
                    ?q <{hasQuestionOrigin}> ?qo ;
                       <{hasAnswer}> ?ans .
                    {
                        ?ans <{hasObjectValue}> ?picked .
                    } UNION {
                        ?ans <{hasDataValue}> ?picked .
                        FILTER(STR(?picked) != "")
                    }
                    OPTIONAL {
                        {valuesBlock}
                        FILTER(?vqo = ?qo)
                        BIND(?q AS ?evaluableQ)
                        OPTIONAL {
                            FILTER(
                                ?picked = ?correctUri
                                ||
                                (isLiteral(?picked) &&
                                 LCASE(REPLACE(STR(?picked),       "^\\\\s+|\\\\s+$", ""))
                               = LCASE(REPLACE(STR(?correctLabel), "^\\\\s+|\\\\s+$", "")))
                            )
                            BIND(?q AS ?correctQ)
                        }
                    }
                    {intervalFilter}
                }
            }
            GROUP BY ?entity
            """;

    private final String groupingPredicate;
    private final StatisticsInterval interval;
    private final List<CorrectAnswer> correctAnswers;

    public String build() {
        return substitute(Map.ofEntries(
                Map.entry("valuesBlock", valuesBlock()),
                Map.entry("recordType", Vocabulary.s_c_record),
                Map.entry("groupingPredicate", groupingPredicate),
                Map.entry("phasePredicate", Vocabulary.s_p_has_phase),
                Map.entry("completedPhase", Vocabulary.s_c_completed_record_phase),
                Map.entry("hasQuestion", Vocabulary.s_p_has_question),
                Map.entry("hasRelatedQuestion", Vocabulary.s_p_has_related_question),
                Map.entry("hasQuestionOrigin", Vocabulary.s_p_has_question_origin),
                Map.entry("hasAnswer", Vocabulary.s_p_has_answer),
                Map.entry("hasObjectValue", Vocabulary.s_p_has_object_value),
                Map.entry("hasDataValue", Vocabulary.s_p_has_data_value),
                Map.entry("intervalFilter", QueryBuilder.intervalFilter(interval))));
    }

    private String valuesBlock() {
        if (correctAnswers.isEmpty()) {
            return "VALUES (?vqo ?correctUri ?correctLabel) { (<urn:none> <urn:none> \"\") }";
        }

        String rows = correctAnswers.stream()
                .map(this::toValuesRow)
                .collect(Collectors.joining("\n                    "));

        return "VALUES (?vqo ?correctUri ?correctLabel) {\n                    "
                + rows + "\n                }";
    }

    private String toValuesRow(CorrectAnswer ca) {
        return "(<%s> <%s> \"%s\")".formatted(
                ca.questionOrigin(),
                ca.correctUri(),
                escape(ca.safeLabel()));
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String substitute(Map<String, String> placeholders) {
        String result = TEMPLATE;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}