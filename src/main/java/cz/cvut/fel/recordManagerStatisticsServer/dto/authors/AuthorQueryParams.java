package cz.cvut.fel.recordManagerStatisticsServer.dto.authors;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthorQueryParams {
    StatisticsInterval interval;
    String usernameFilter;
}