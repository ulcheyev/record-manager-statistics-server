package cz.cvut.fel.recordManagerStatisticsServer.dto;

import java.net.URI;

public record AnswerCounts(
        URI entityUri,
        long totalAnswers,
        long evaluableAnswers,
        long correctAnswers
) {

    public static AnswerCounts empty(URI entityUri) {
        return new AnswerCounts(entityUri, 0, 0, 0);
    }

    public double correctnessRate() {
        return evaluableAnswers == 0
                ? 0.0
                : (double) correctAnswers / evaluableAnswers * 100;
    }
}