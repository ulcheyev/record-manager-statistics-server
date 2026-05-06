package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluablePoolDto {
    private long answered;
    private long correct;

    public static EvaluablePoolDto empty() {
        return EvaluablePoolDto.builder()
                .answered(0)
                .correct(0)
                .build();
    }

    public long incorrect() {
        return Math.max(0, answered - correct);
    }
}