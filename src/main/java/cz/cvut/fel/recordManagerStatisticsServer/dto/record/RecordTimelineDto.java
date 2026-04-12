package cz.cvut.fel.recordManagerStatisticsServer.dto.record;

import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.TimeSeriesDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordTimelineDto {

    private StatisticsInterval interval;
    private Granularity granularity;
    private TimeSeriesDto timeSeries;
}