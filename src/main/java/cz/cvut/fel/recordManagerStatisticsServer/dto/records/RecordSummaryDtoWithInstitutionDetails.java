package cz.cvut.fel.recordManagerStatisticsServer.dto.records;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class RecordSummaryDtoWithInstitutionDetails extends RecordSummaryDto {
    private String institutionName;

}
