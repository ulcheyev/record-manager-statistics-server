package cz.cvut.fel.recordManagerStatisticsServer.dto.institutions;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsWithMetadata;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class InstitutionsStatisticsDto extends StatisticsWithMetadata {
    private int totalInstitutions;
    private String mostRecordsInstitutionInfo;
    private String mostAnswersInstitutionInfo;
    private String bestCompletionRateInstitutionInfo;
    private String mostRejectionRateInstitutionInfo;
    private String bestAnswerCorrectnessInstitutionInfo;
    private List<InstitutionSummaryDto> institutions;
}
