package cz.cvut.fel.recordManagerStatisticsServer.dto.history;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class GeneralActionStatsDto {

    private StatisticsInterval interval;
    private long totalActions;
    private long totalErrors;
    private double errorRate;

    private Map<String, Long> byType;

    private long saveRecordSuccess;
    private long saveRecordErrors;
    private long loadRecordErrors;
    private long saveInstitutionSuccess;
    private long saveInstitutionErrors;
    private long publishRecordsSuccess;
    private long unauthEvents;
}