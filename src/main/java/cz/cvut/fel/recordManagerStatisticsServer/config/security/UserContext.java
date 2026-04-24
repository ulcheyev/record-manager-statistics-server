package cz.cvut.fel.recordManagerStatisticsServer.config.security;

import lombok.Builder;
import lombok.Getter;

import java.net.URI;

@Getter
@Builder
public class UserContext {

    private final URI userUri;
    private final String username;
    private final String fullName;
    private final URI institutionUri;
    private final String institutionName;

    private final boolean canReadAllRecords;
    private final boolean canReadOrgRecords;
    private final boolean canReadAllUsers;
    private final boolean canReadOrgUsers;
    private final boolean canReadAllOrganizations;
    private final boolean canReadOrganization;
    private final boolean canReadStatistics;
}