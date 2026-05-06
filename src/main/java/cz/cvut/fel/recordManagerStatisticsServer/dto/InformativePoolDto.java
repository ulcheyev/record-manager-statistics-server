package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InformativePoolDto {
    private long answered;

    public static InformativePoolDto empty() {
        return InformativePoolDto.builder()
                .answered(0)
                .build();
    }
}