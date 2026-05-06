package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsWithMetadata;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class FormTemplateUsageDto extends StatisticsWithMetadata {

    private StatisticsInterval interval;
    private long total;
    private List<TemplateSliceDto> templates;

    @Data
    @Builder
    public static class TemplateSliceDto {
        private String templateUri;
        private String templateLabel;
        private long count;
        private double percentage;
    }
}