package cz.cvut.fel.recordManagerStatisticsServer.dto.record;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FormTemplateUsageDto {

    private StatisticsInterval interval;
    private long total;
    private List<TemplateSliceDto> templates;

    @Data
    @Builder
    public static class TemplateSliceDto {
        private String templateUri;
        private long count;
        private double percentage;
    }
}