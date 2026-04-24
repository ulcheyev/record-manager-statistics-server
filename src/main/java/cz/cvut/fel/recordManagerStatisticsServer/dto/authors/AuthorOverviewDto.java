package cz.cvut.fel.recordManagerStatisticsServer.dto.authors;

import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsWithMetadata;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Data
public abstract class AuthorOverviewDto extends StatisticsWithMetadata {
    private String username;
    private String fullName;
    private long totalRecords;
    private double completionRate;
    private double rejectionRate;
    private long totalAnswers;
    private long evaluableAnswers;
    private long totalCorrectAnswers;
    private double correctnessRate;
    private PhaseCountDto byPhase;
    private Instant periodFrom;
    private Instant periodTo;
}