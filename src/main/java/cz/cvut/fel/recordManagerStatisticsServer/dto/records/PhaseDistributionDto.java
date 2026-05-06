package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhaseDistributionDto {

    private StatisticsInterval interval;
    private long total;
    private List<PhaseSliceDto> distribution;

    @Data
    @Builder
    public static class PhaseSliceDto {
        private RecordPhase phase;
        private long count;
        private double percentage;
    }
}