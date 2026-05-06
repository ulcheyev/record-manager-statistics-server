package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsWithMetadata;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class RecordListDto extends StatisticsWithMetadata {
    private long total;
    private List<RecordSummaryDto> records;
    private PhaseDistributionDto phaseDistribution;
    private FormTemplateUsageDto formTemplateUsage;
}