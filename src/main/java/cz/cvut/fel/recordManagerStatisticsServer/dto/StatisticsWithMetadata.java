package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class StatisticsWithMetadata {
    private StatisticsLabel label;
    private String description;
    private StatisticsInterval interval;
}
