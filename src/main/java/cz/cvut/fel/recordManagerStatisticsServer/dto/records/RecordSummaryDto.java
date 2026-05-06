package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerBreakdownDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.QuestionPoolDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.net.URI;
import java.time.Instant;

@Data
@SuperBuilder
public class RecordSummaryDto {
    private URI uri;
    private String name;
    private RecordPhase phase;
    private String formTemplateLabel;
    private Instant dateCreated;

    private QuestionPoolDto questions;
    private AnswerBreakdownDto answers;
}