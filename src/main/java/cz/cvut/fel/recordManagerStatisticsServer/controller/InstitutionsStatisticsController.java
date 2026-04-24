package cz.cvut.fel.recordManagerStatisticsServer.controller;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.SecurityConstants;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionsStatisticsDto;
import cz.cvut.fel.recordManagerStatisticsServer.service.InstitutionsStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.Api.INSTITUTIONS)
@RequiredArgsConstructor
@PreAuthorize(SecurityConstants.HAS_STATISTICS)
public class InstitutionsStatisticsController {

    private final InstitutionsStatisticsService institutionsStatisticsService;

    @GetMapping
    @PreAuthorize(SecurityConstants.HAS_ALL_ORGS)
    public ResponseEntity<InstitutionsStatisticsDto> getAllInstitutions(StatisticsInterval interval) {
        return ResponseEntity.ok(institutionsStatisticsService.getAllInstitutions(interval));
    }
}