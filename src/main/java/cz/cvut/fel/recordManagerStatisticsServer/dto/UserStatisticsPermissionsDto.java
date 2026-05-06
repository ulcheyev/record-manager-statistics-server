package cz.cvut.fel.recordManagerStatisticsServer.dto;

public record UserStatisticsPermissionsDto(
        boolean canReadAllRecords,
        boolean canReadOrgRecords,
        boolean canReadAllUsers,
        boolean canReadOrgUsers,
        boolean canReadAllOrganizations,
        boolean canReadOrganization,
        boolean canReadStatistics
) {
}