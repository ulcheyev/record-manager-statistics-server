package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class RecordSummaryDtoWithAuthorDetails extends RecordSummaryDtoWithInstitutionDetails {
    private String authorFullName;
    private String authorUsername;
}
