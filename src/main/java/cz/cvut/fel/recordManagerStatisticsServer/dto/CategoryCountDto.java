package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCountDto {
    private String category;
    private long count;
}