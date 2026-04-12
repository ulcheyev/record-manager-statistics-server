package cz.cvut.fel.recordManagerStatisticsServer.config.security;

import lombok.Builder;
import lombok.Getter;

import java.net.URI;

@Getter
@Builder
public class UserContext {

    private final URI userUri;
    private final String fullName;
    private final boolean canReadStatistics;

    public boolean hasStatisticsAccess() {
        return canReadStatistics;
    }
}