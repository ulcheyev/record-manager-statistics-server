package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AnswerBreakdownDto {
    private EvaluablePoolDto evaluable;
    private InformativePoolDto informative;

    public static AnswerBreakdownDto empty() {
        return AnswerBreakdownDto.builder()
                .evaluable(EvaluablePoolDto.empty())
                .informative(InformativePoolDto.empty())
                .build();
    }

    public long totalAnswered() {
        long evaluableAnswered = evaluable == null ? 0 : evaluable.getAnswered();
        long informativeAnswered = informative == null ? 0 : informative.getAnswered();
        return evaluableAnswered + informativeAnswered;
    }

    public long correctAnswers() {
        return evaluable == null ? 0 : evaluable.getCorrect();
    }
}