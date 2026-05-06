package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RecordsQueryParams {
    StatisticsInterval interval;
    String formTemplateFilter;
    RecordPhase phaseFilter;
}