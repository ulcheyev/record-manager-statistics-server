package cz.cvut.fel.recordManagerStatisticsServer.dto.record;

import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
@Builder
public class InstitutionStatsDto {

    private StatisticsInterval interval;
    private List<InstitutionRecordStatsDto> institutions;

    @Data
    @Builder
    public static class InstitutionRecordStatsDto {
        private URI uri;
        private String name;
        private PhaseCountDto byPhase;
        private long total;
        private double completionRate;
        private double rejectionRate;
    }
}