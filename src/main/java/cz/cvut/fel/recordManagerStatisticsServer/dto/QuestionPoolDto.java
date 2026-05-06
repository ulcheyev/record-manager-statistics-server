package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionPoolDto {
    private long evaluable;
    private long informative;

    public static QuestionPoolDto empty() {
        return QuestionPoolDto.builder()
                .evaluable(0)
                .informative(0)
                .build();
    }

    public long total() {
        return evaluable + informative;
    }
}