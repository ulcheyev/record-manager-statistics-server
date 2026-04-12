package cz.cvut.fel.recordManagerStatisticsServer.dto.history;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
@Builder
public class UserActivityStatsDto {

    private StatisticsInterval interval;
    private List<UserActivityDto> users;

    @Data
    @Builder
    public static class UserActivityDto {
        private URI uri;
        private String fullName;
        private String institutionName;
        private long totalActions;
        private long saveSuccess;
        private long saveErrors;
        private long loadErrors;
        private long unauthEvents;
        private double errorRate;
    }
}