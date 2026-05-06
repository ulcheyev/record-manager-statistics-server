package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerBreakdownDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.EvaluablePoolDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.InformativePoolDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.QuestionPoolDto;

import java.net.URI;

public record AnswerCounts(
        URI entityUri,
        long evaluableQuestions,
        long informativeQuestions,
        long evaluableAnswers,
        long informativeAnswers,
        long correctAnswers
) {

    public static AnswerCounts empty(URI entityUri) {
        return new AnswerCounts(entityUri, 0, 0, 0, 0, 0);
    }

    public long totalQuestions() {
        return evaluableQuestions + informativeQuestions;
    }

    public long totalAnswers() {
        return evaluableAnswers + informativeAnswers;
    }

    public double correctnessRate() {
        return evaluableAnswers == 0
                ? 0.0
                : (double) correctAnswers / evaluableAnswers * 100;
    }

    public QuestionPoolDto toQuestionsDto() {
        return QuestionPoolDto.builder()
                .evaluable(evaluableQuestions)
                .informative(informativeQuestions)
                .build();
    }

    public AnswerBreakdownDto toAnswersDto() {
        return AnswerBreakdownDto.builder()
                .evaluable(EvaluablePoolDto.builder()
                        .answered(evaluableAnswers)
                        .correct(correctAnswers)
                        .build())
                .informative(InformativePoolDto.builder()
                        .answered(informativeAnswers)
                        .build())
                .build();
    }
}