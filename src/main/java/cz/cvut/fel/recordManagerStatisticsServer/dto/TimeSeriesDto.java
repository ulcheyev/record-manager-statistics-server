package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class TimeSeriesDto {
    private Granularity granularity;
    private List<String> labels;
    private Map<String, List<Long>> series;
    private List<Long> totals;
}