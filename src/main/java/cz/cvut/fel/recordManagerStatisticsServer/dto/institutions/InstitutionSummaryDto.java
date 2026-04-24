package cz.cvut.fel.recordManagerStatisticsServer.dto.institutions;

import lombok.Builder;
import lombok.Data;

import java.net.URI;

@Data
@Builder
public class InstitutionSummaryDto {
    private URI uri;
    private String name;
    private long totalRecords;
    private long authorCount;
    private double completionRate;
    private double rejectionRate;
    private long totalAnswers;
    private long evaluableAnswers;
    private long totalCorrectAnswers;
    private double correctnessRate;
}