package cz.cvut.fel.recordManagerStatisticsServer.controller;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.RequestUserContext;
import cz.cvut.fel.recordManagerStatisticsServer.dto.UserStatisticsPermissionsDto;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.Api.PERMISSIONS)
@RequiredArgsConstructor
public class StatisticsPermissionsController {

    private final RequestUserContext userContext;

    @GetMapping
    public ResponseEntity<UserStatisticsPermissionsDto> getStatisticsPermissions() {
        return ResponseEntity.ok(new UserStatisticsPermissionsDto(
                userContext.canReadAllRecords(),
                userContext.canReadOrgRecords(),
                userContext.canReadAllUsers(),
                userContext.canReadOrgUsers(),
                userContext.canReadAllOrganizations(),
                userContext.canReadOrganization(),
                userContext.canReadStatistics()
        ));
    }
}