package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.net.URI;
import java.time.Instant;

@Data
@SuperBuilder
public class RecordSummaryDto {
    private URI uri;
    private RecordPhase phase;
    private String formTemplateLabel;
    private long totalQuestions;
    private long totalAnswers;
    private long totalEvaluableAnswers;
    private long totalCorrectAnswers;
    private double correctnessRate;
    private Instant dateCreated;
}