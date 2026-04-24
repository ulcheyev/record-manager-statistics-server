package cz.cvut.fel.recordManagerStatisticsServer.dto.authors;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsWithMetadata;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class AuthorsOverviewDto extends StatisticsWithMetadata {
    private long totalAuthors;
    private String mostRecordsAuthorInfo;
    private String mostAnswersAuthorInfo;
    private String bestCompletionRateInfo;
    private String mostRejectionRateInfo;
    private String bestAnswerCorrectnessInfo;

}
