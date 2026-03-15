package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecordStatsDto {
    private long total;
    private List<CategoryCountDto> byCategory;
}