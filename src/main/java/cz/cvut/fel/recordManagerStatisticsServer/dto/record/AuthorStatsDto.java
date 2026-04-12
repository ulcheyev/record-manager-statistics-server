package cz.cvut.fel.recordManagerStatisticsServer.dto.record;

import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
@Builder
public class AuthorStatsDto {

    private StatisticsInterval interval;

    private List<AuthorRecordStatsDto> authors;

    @Data
    @Builder
    public static class AuthorRecordStatsDto {
        private URI uri;
        private String fullName;
        private String username;
        private String institutionName;
        private PhaseCountDto byPhase;
        private long total;
        private double completionRate;
        private double rejectionRate;
    }
}