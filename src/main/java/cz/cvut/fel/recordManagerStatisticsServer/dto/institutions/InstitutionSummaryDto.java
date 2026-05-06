package cz.cvut.fel.recordManagerStatisticsServer.dto.institutions;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerBreakdownDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.QuestionPoolDto;
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

    private QuestionPoolDto questions;
    private AnswerBreakdownDto answers;
}