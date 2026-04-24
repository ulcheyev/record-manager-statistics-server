package cz.cvut.fel.recordManagerStatisticsServer.dto.authors;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsWithMetadata;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class AuthorsStatisticsDto extends StatisticsWithMetadata {
    private List<AuthorWithInstitutionDto> authors;
    private AuthorsOverviewDto overview;
}
