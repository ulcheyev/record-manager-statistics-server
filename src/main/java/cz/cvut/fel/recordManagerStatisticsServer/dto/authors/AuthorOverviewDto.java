package cz.cvut.fel.recordManagerStatisticsServer.dto.authors;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerBreakdownDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.QuestionPoolDto;
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

    private QuestionPoolDto questions;
    private AnswerBreakdownDto answers;

    private PhaseCountDto byPhase;
    private Instant periodFrom;
    private Instant periodTo;
}