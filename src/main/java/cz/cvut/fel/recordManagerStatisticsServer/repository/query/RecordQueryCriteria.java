package cz.cvut.fel.recordManagerStatisticsServer.repository.query;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RecordQueryCriteria {
    StatisticsInterval interval;
    String authorUsernameLike;

    public boolean hasUsernameFilter() {
        return authorUsernameLike != null && !authorUsernameLike.isBlank();
    }
}